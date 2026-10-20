package com.shopsphere.controller;

import com.shopsphere.dao.OrderHistoryDAO;
import com.shopsphere.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Collections;

@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {
    private final OrderHistoryDAO dao = new OrderHistoryDAO();

    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {
        HttpSession s = r.getSession(false);
        User u = s == null ? null : (User) s.getAttribute("loggedInUser");
        if (u == null) {
            p.sendRedirect(r.getContextPath() + "/login");
            return;
        }
        try {
            r.setAttribute("orders", dao.findByUser(u.getUserId()));
        } catch (Exception e) {
            e.printStackTrace();
            r.setAttribute("orders", Collections.emptyList());
            r.setAttribute("error", "Unable to load order history: " + e.getMessage());
        }
        r.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(r, p);
    }
}