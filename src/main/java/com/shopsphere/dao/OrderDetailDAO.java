package com.shopsphere.dao;

import com.shopsphere.config.DBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class OrderDetailDAO {
    public Map<String, Object> findOrder(int userId, int orderId) throws SQLException {
        String q = "SELECT o.order_id, o.total_amount, o.discount, o.payment_method, o.payment_status, "
                 + "o.order_status, o.created_at, a.address_line, a.city, a.state, a.pincode "
                 + "FROM orders o LEFT JOIN addresses a ON a.address_id = o.address_id "
                 + "WHERE o.user_id = ? AND o.order_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement s = c.prepareStatement(q)) {
            s.setInt(1, userId);
            s.setInt(2, orderId);
            try (ResultSet r = s.executeQuery()) {
                if (!r.next()) return null;
                Map<String, Object> m = new LinkedHashMap<>();
                int id = r.getInt(1);
                BigDecimal total = r.getBigDecimal(2);
                BigDecimal discount = r.getBigDecimal(3);
                String payMethod = r.getString(4);
                String payStatus = r.getString(5);
                String ordStatus = r.getString(6);
                Timestamp createdAt = r.getTimestamp(7);
                String line = r.getString(8);
                String city = r.getString(9);
                String state = r.getString(10);
                String pincode = r.getString(11);

                String fullAddress = "Standard Delivery Address";
                if (line != null && !line.trim().isEmpty()) {
                    fullAddress = line + (city != null ? ", " + city : "") + (state != null ? ", " + state : "") + (pincode != null ? " - " + pincode : "");
                }

                m.put("orderId", id);
                m.put("total", total != null ? total : BigDecimal.ZERO);
                m.put("totalAmount", total != null ? total : BigDecimal.ZERO);
                m.put("discount", discount != null ? discount : BigDecimal.ZERO);
                m.put("paymentMethod", payMethod != null ? payMethod : "COD");
                m.put("paymentStatus", payStatus != null ? payStatus : "PENDING");
                m.put("orderStatus", ordStatus != null ? ordStatus : "PLACED");
                m.put("createdAt", createdAt != null ? createdAt.toString() : "");
                m.put("shippingAddress", fullAddress);
                m.put("addressLine", line);
                m.put("city", city);
                m.put("state", state);
                m.put("pincode", pincode);
                return m;
            }
        }
    }

    public List<Map<String, Object>> findItems(int userId, int orderId) throws SQLException {
        List<Map<String, Object>> out = new ArrayList<>();
        String q = "SELECT oi.product_id, p.name, oi.quantity, oi.price "
                 + "FROM order_items oi "
                 + "JOIN orders o ON o.order_id = oi.order_id "
                 + "JOIN products p ON p.product_id = oi.product_id "
                 + "WHERE o.user_id = ? AND oi.order_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement s = c.prepareStatement(q)) {
            s.setInt(1, userId);
            s.setInt(2, orderId);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    int prodId = r.getInt(1);
                    String pName = r.getString(2);
                    int qty = r.getInt(3);
                    BigDecimal unitPrice = r.getBigDecimal(4);
                    if (unitPrice == null) unitPrice = BigDecimal.ZERO;
                    BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(qty));

                    m.put("productId", prodId);
                    m.put("name", pName != null ? pName : "Product #" + prodId);
                    m.put("productName", pName != null ? pName : "Product #" + prodId);
                    m.put("quantity", qty);
                    m.put("unitPrice", unitPrice);
                    m.put("price", unitPrice);
                    m.put("totalPrice", subtotal);
                    out.add(m);
                }
            }
        }
        return out;
    }

    public boolean requestReturn(int userId, int orderId, String reason) throws SQLException {
        String q = "UPDATE orders SET order_status='RETURN_REQUESTED' WHERE user_id=? AND order_id=? AND order_status='DELIVERED'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement s = c.prepareStatement(q)) {
            s.setInt(1, userId);
            s.setInt(2, orderId);
            return s.executeUpdate() > 0;
        }
    }
}

