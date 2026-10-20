package com.shopsphere.controller;

import com.shopsphere.dao.AdminUserDAO;
import com.shopsphere.security.InputValidator;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Collections;

@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {
    private final AdminUserDAO dao = new AdminUserDAO();

    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {
        try {
            r.setAttribute("users", dao.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            r.setAttribute("users", Collections.emptyList());
            r.setAttribute("error", "Failed to load customers: " + e.getMessage());
        }
        r.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(r, p);
    }

    @Override
    protected void doPost(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {
        try {
            int id = InputValidator.positiveInt(r.getParameter("userId"), "User");
            String action = r.getParameter("action");
            com.shopsphere.model.User actor = (com.shopsphere.model.User) r.getSession().getAttribute("loggedInUser");
            int actorId = actor != null ? actor.getUserId() : 0;

            if ("toggle".equals(action)) {
                dao.setStatus(actorId, id, "true".equalsIgnoreCase(r.getParameter("status")));
            } else if ("role".equals(action)) {
                dao.setRole(actorId, id, r.getParameter("role"));
            } else {
                throw new IllegalArgumentException("Invalid user action");
            }
            r.getSession().setAttribute("msg", "Customer #" + id + " updated successfully!");
            p.sendRedirect(r.getContextPath() + "/admin/users");
        } catch (IllegalArgumentException e) {
            r.getSession().setAttribute("error", e.getMessage());
            p.sendRedirect(r.getContextPath() + "/admin/users");
        } catch (Exception e) {
            e.printStackTrace();
            r.getSession().setAttribute("error", "Failed to update customer: " + e.getMessage());
            p.sendRedirect(r.getContextPath() + "/admin/users");
        }
    }
}
