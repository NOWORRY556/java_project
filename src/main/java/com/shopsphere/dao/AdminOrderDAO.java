package com.shopsphere.dao;

import com.shopsphere.config.DBConnection;
import com.shopsphere.service.EmailService;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class AdminOrderDAO {

    public List<Map<String, Object>> findAll() throws SQLException {
        List<Map<String, Object>> out = new ArrayList<>();
        String q = "SELECT o.order_id, o.total_amount, o.payment_method, o.payment_status, o.order_status, o.created_at, u.name, u.email "
                 + "FROM orders o JOIN users u ON u.user_id=o.user_id ORDER BY o.created_at DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement s = c.prepareStatement(q);
             ResultSet r = s.executeQuery()) {
            while (r.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("orderId", r.getInt(1));
                m.put("total", r.getBigDecimal(2));
                m.put("paymentMethod", r.getString(3));
                m.put("paymentStatus", r.getString(4));
                m.put("orderStatus", r.getString(5));
                m.put("createdAt", r.getTimestamp(6));
                m.put("name", r.getString(7));
                m.put("email", r.getString(8));
                out.add(m);
            }
        }
        return out;
    }

    public void updateStatus(int id, String status) throws SQLException {
        if (!Set.of("PLACED", "PROCESSING", "SHIPPED", "DELIVERED", "RETURN_REQUESTED", "RETURNED", "CANCELLED", "REFUNDED").contains(status)) {
            throw new IllegalArgumentException("Invalid order status");
        }

        // 1. Update order status in database
        try (Connection c = DBConnection.getConnection();
             PreparedStatement s = c.prepareStatement("UPDATE orders SET order_status=? WHERE order_id=?")) {
            s.setString(1, status);
            s.setInt(2, id);
            s.executeUpdate();
        }

        // 2. Fetch customer details to push status update email
        try (Connection c = DBConnection.getConnection();
             PreparedStatement s = c.prepareStatement("SELECT u.email, u.name, o.total_amount FROM orders o JOIN users u ON u.user_id=o.user_id WHERE o.order_id=?")) {
            s.setInt(1, id);
            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    String email = r.getString(1);
                    String name = r.getString(2);
                    BigDecimal total = r.getBigDecimal(3);
                    double totVal = total != null ? total.doubleValue() : 0.0;
                    EmailService.sendOrderStatusUpdateAsync(email, name, id, status, totVal);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}