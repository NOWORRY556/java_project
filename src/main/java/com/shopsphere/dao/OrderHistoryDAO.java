package com.shopsphere.dao;

import com.shopsphere.config.DBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class OrderHistoryDAO {
    public List<Map<String, Object>> findByUser(int id) throws SQLException {
        List<Map<String, Object>> out = new ArrayList<>();
        String q = "SELECT order_id, total_amount, payment_method, payment_status, order_status, created_at FROM orders WHERE user_id=? ORDER BY order_id DESC";
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(q)) {
            s.setInt(1, id);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("orderId", r.getInt(1));
                    m.put("total", r.getBigDecimal(2) != null ? r.getBigDecimal(2) : BigDecimal.ZERO);
                    m.put("paymentMethod", r.getString(3) != null ? r.getString(3) : "COD");
                    m.put("paymentStatus", r.getString(4) != null ? r.getString(4) : "PENDING");
                    m.put("orderStatus", r.getString(5) != null ? r.getString(5) : "PLACED");
                    m.put("createdAt", r.getTimestamp(6) != null ? r.getTimestamp(6).toString() : "");
                    out.add(m);
                }
            }
        } catch (SQLException sqle) {
            // Return empty list if orders table issue occurs
        }
        return out;
    }
}