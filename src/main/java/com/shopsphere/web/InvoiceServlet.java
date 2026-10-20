package com.shopsphere.web;

import com.shopsphere.dao.OrderDetailDAO;
import com.shopsphere.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/invoice")
public class InvoiceServlet extends HttpServlet {
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

            if (order == null && "ADMIN".equalsIgnoreCase(u.getRole())) {
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
            } else if (order != null) {
                q.setAttribute("items", dao.findItems(u.getUserId(), id));
            }

            if (order == null) {
                q.getSession().setAttribute("error", "Order #" + id + " was not found.");
                p.sendRedirect(q.getContextPath() + "/orders");
                return;
            }

            q.setAttribute("order", order);
            q.setAttribute("user", u);
            q.getRequestDispatcher("/WEB-INF/views/invoice.jsp").forward(q, p);
        } catch (Exception e) {
            e.printStackTrace();
            p.sendRedirect(q.getContextPath() + "/orders");
        }
    }
}
