package com.shopsphere.controller;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import com.shopsphere.security.InputValidator;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private final UserDAO users = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        User u = s == null ? null : (User) s.getAttribute("loggedInUser");
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.setAttribute("profile", u);
        req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        User u = s == null ? null : (User) s.getAttribute("loggedInUser");
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            String n = InputValidator.maxLength(InputValidator.required(req.getParameter("name"), "Name"), "Name", 100);
            if (n.length() < 2) throw new IllegalArgumentException("Name must contain at least 2 characters.");
            String m = InputValidator.mobile(req.getParameter("mobile"));
            users.updateProfile(u.getUserId(), n, m);
            u.setName(n); u.setMobile(m);
            req.getSession().setAttribute("loggedInUser", u);
            req.setAttribute("success", "Profile updated successfully.");
            doGet(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        } catch (Exception e) {
            throw new ServletException("Unable to update profile", e);
        }
    }
}
