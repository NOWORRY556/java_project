package com.shopsphere.controller;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import com.shopsphere.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/google-login")
public class GoogleLoginServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(GoogleLoginServlet.class.getName());
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String credential = req.getParameter("credential");
        String googleEmail = req.getParameter("googleEmail");
        String googleName = req.getParameter("googleName");

        String email = null;
        String name = null;

        try {
            if (credential != null && !credential.isBlank() && credential.contains(".")) {
                String[] parts = credential.split("\\.");
                if (parts.length >= 2) {
                    String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
                    email = extractJsonField(payloadJson, "email");
                    name = extractJsonField(payloadJson, "name");
                }
            }

            if ((email == null || email.isBlank()) && googleEmail != null && !googleEmail.isBlank()) {
                email = googleEmail.trim().toLowerCase();
                name = (googleName != null && !googleName.isBlank()) ? googleName.trim() : email.split("@")[0];
            }

            if (email == null || email.isBlank()) {
                req.setAttribute("error", "Google Account authentication details missing.");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;
            }

            if (name == null || name.isBlank()) {
                name = email.split("@")[0];
            }


            User user = userDao.findByEmail(email);
            if (user == null) {
                // Register new user seamlessly via Google OAuth
                user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPassword(PasswordUtil.hash("G_OAUTH_" + UUID.randomUUID().toString()));
                user.setMobile("");
                user.setRole("CUSTOMER");
                user.setStatus(true);

                userDao.create(user);
                user = userDao.findByEmail(email);
            }


            // Create logged-in session
            HttpSession session = req.getSession(true);
            session.setAttribute("loggedInUser", user);
            LOGGER.info("[GOOGLE OAUTH LOGIN] User logged in: " + email);

            resp.sendRedirect(req.getContextPath() + "/");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Google authentication error", e);
            req.setAttribute("error", "Failed to sign in with Google: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }

    private String extractJsonField(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int idx = json.indexOf(searchKey);
        if (idx == -1) return null;
        int start = idx + searchKey.length();
        while (start < json.length() && (json.charAt(start) == ' ' || json.charAt(start) == '"')) {
            start++;
        }
        int end = start;
        while (end < json.length() && json.charAt(end) != '"' && json.charAt(end) != ',' && json.charAt(end) != '}') {
            end++;
        }
        return json.substring(start, end).trim();
    }
}
