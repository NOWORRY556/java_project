package com.shopsphere.web;

import com.shopsphere.dao.AdminOrderDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Collections;

@WebServlet("/admin/orders")
public class AdminOrderServlet extends HttpServlet {
    private final AdminOrderDAO dao = new AdminOrderDAO();

    @Override
    protected void doGet(HttpServletRequest q, HttpServletResponse p) throws ServletException, IOException {
        try {
            q.setAttribute("orders", dao.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            q.setAttribute("orders", Collections.emptyList());
            q.setAttribute("error", "Failed to load orders: " + e.getMessage());
        }
        q.getRequestDispatcher("/WEB-INF/views/admin/orders.jsp").forward(q, p);
    }

    @Override
    protected void doPost(HttpServletRequest q, HttpServletResponse p) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(q.getParameter("orderId"));
            String s = q.getParameter("status");
            if (s == null) throw new IllegalArgumentException("Order status cannot be empty.");
            dao.updateStatus(id, s.toUpperCase());
            q.getSession().setAttribute("msg", "Order #" + id + " status updated to " + s.toUpperCase() + " and email sent!");
            p.sendRedirect(q.getContextPath() + "/admin/orders");
        } catch (Exception e) {
            e.printStackTrace();
            q.getSession().setAttribute("error", "Failed to update order status: " + e.getMessage());
            p.sendRedirect(q.getContextPath() + "/admin/orders");
        }
    }
}
