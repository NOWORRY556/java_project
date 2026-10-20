package com.shopsphere.controller;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import com.shopsphere.service.EmailService;
import com.shopsphere.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ForgotPasswordServlet.class.getName());
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession(true);

        if ("send-otp".equals(action)) {
            String email = req.getParameter("email");
            // Rate limit OTP requests (reuse LoginRateLimiter — 5 per minute per IP)
            String otpRateKey = "otp|" + req.getRemoteAddr();
            if (!com.shopsphere.security.LoginRateLimiter.allow(otpRateKey)) {
                req.setAttribute("error", "Too many OTP requests. Please wait a minute before trying again.");
                req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
                return;
            }
            if (email == null || email.isBlank()) {
                req.setAttribute("error", "Please enter a valid email address.");
                req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
                return;
            }

            try {
                email = email.trim().toLowerCase();
                User user = userDao.findByEmail(email);
                if (user == null) {
                    req.setAttribute("error", "No user found with email: " + email);
                    req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
                    return;
                }

                String otp = String.format("%06d", new java.security.SecureRandom().nextInt(900000) + 100000);
                session.setAttribute("resetEmail", email);
                session.setAttribute("resetOtp", otp);
                session.setAttribute("resetOtpTime", System.currentTimeMillis());

                EmailService.sendPasswordResetOtpAsync(email, otp);
                LOGGER.info("[FORGOT PASSWORD] OTP sent to: " + email);

                req.setAttribute("step", "verify");
                req.setAttribute("message", "A 6-digit verification OTP has been sent to " + email);
                req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
            } catch (Exception e) {
                LOGGER.warning("Forgot password error: " + e.getMessage());
                req.setAttribute("error", "Failed to send OTP. Please try again.");
                req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
            }
            return;
        }

        if ("reset-password".equals(action)) {
            String otpInput = req.getParameter("otp");
            String newPassword = req.getParameter("newPassword");
            String confirmPassword = req.getParameter("confirmPassword");

            String sessionEmail = (String) session.getAttribute("resetEmail");
            String sessionOtp = (String) session.getAttribute("resetOtp");
            Long sessionOtpTime = (Long) session.getAttribute("resetOtpTime");

            if (sessionEmail == null || sessionOtp == null || sessionOtpTime == null) {
                req.setAttribute("error", "Session expired. Please request a new OTP code.");
                req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
                return;
            }

            // OTP expiration check (10 minutes)
            if (System.currentTimeMillis() - sessionOtpTime > 10 * 60 * 1000) {
                session.removeAttribute("resetEmail");
                session.removeAttribute("resetOtp");
                session.removeAttribute("resetOtpTime");
                req.setAttribute("error", "OTP has expired. Please request a new one.");
                req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
                return;
            }

            if (otpInput == null || !sessionOtp.equalsIgnoreCase(otpInput.trim())) {
                req.setAttribute("step", "verify");
                req.setAttribute("error", "Invalid OTP code. Please check your email and try again.");
                req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
                return;
            }

            if (newPassword == null || newPassword.length() < 8) {
                req.setAttribute("step", "verify");
                req.setAttribute("error", "Password must be at least 8 characters long.");
                req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                req.setAttribute("step", "verify");
                req.setAttribute("error", "Passwords do not match.");
                req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
                return;
            }

            try {
                User user = userDao.findByEmail(sessionEmail);
                if (user != null) {
                    userDao.updatePassword(user.getUserId(), PasswordUtil.hash(newPassword));
                    LOGGER.info("[FORGOT PASSWORD SUCCESS] Password updated for: " + sessionEmail);

                    session.removeAttribute("resetEmail");
                    session.removeAttribute("resetOtp");
                    session.removeAttribute("resetOtpTime");

                    resp.sendRedirect(req.getContextPath() + "/login?reset=true");
                    return;
                }
            } catch (Exception e) {
                LOGGER.warning("Password update error: " + e.getMessage());
                req.setAttribute("step", "verify");
                req.setAttribute("error", "Database error while updating password.");
                req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
            }
        }
    }
}
