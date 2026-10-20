package com.shopsphere.web;

import com.shopsphere.dao.OrderDetailDAO;
import com.shopsphere.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/order-detail")
public class OrderDetailServlet extends HttpServlet {
    private final OrderDetailDAO dao = new OrderDetailDAO();

    @Override
    protected void doGet(HttpServletRequest q, HttpServletResponse p) throws ServletException, IOException {
        HttpSession s = q.getSession(false);
        User u = s == null ? null : (User) s.getAttribute("loggedInUser");
        if (u == null) {
            p.sendRedirect(q.getContextPath() + "/login");
            return;
        }
        try {
            String idParam = q.getParameter("orderId");
            if (idParam == null || idParam.isBlank()) {
                p.sendRedirect(q.getContextPath() + "/orders");
                return;
            }
            int id = Integer.parseInt(idParam);
            var order = dao.findOrder(u.getUserId(), id);
            if (order == null) {
                // If not found by user, check if admin is viewing or redirect
                if ("ADMIN".equalsIgnoreCase(u.getRole())) {
                    // Fetch for admin
                    try (java.sql.Connection c = com.shopsphere.config.DBConnection.getConnection();
                         java.sql.PreparedStatement st = c.prepareStatement("SELECT user_id FROM orders WHERE order_id=?")) {
                        st.setInt(1, id);
                        try (java.sql.ResultSet r = st.executeQuery()) {
                            if (r.next()) {
                                int ownerId = r.getInt(1);
                                order = dao.findOrder(ownerId, id);
                                q.setAttribute("items", dao.findItems(ownerId, id));
                            }
                        }
                    }
                }
            } else {
                q.setAttribute("items", dao.findItems(u.getUserId(), id));
            }

            if (order == null) {
                q.getSession().setAttribute("error", "Order #" + id + " was not found.");
                p.sendRedirect(q.getContextPath() + "/orders");
                return;
            }

            q.setAttribute("order", order);
            q.getRequestDispatcher("/WEB-INF/views/order-detail.jsp").forward(q, p);
        } catch (Exception e) {
            e.printStackTrace();
            q.getSession().setAttribute("error", "Unable to load order details.");
            p.sendRedirect(q.getContextPath() + "/orders");
        }
    }

    @Override
    protected void doPost(HttpServletRequest q, HttpServletResponse p) throws ServletException, IOException {
        HttpSession s = q.getSession(false);
        User u = s == null ? null : (User) s.getAttribute("loggedInUser");
        if (u == null) {
            p.sendRedirect(q.getContextPath() + "/login");
            return;
        }
        try {
            String action = q.getParameter("action");
            int orderId = Integer.parseInt(q.getParameter("orderId"));
            String reason = q.getParameter("reason");
            if ("return_request".equals(action)) {
                boolean success = dao.requestReturn(u.getUserId(), orderId, reason);
                if (success) {
                    q.getSession().setAttribute("msg", "Return request submitted successfully for Order #" + orderId + ". Our team will review and update you soon.");
                } else {
                    q.getSession().setAttribute("error", "Return request could not be processed. Orders must be in DELIVERED state.");
                }
            }
            p.sendRedirect(q.getContextPath() + "/order-detail?orderId=" + orderId);
        } catch (Exception e) {
            q.getSession().setAttribute("error", "Failed to submit return request: " + e.getMessage());
            p.sendRedirect(q.getContextPath() + "/orders");
        }
    }
}
