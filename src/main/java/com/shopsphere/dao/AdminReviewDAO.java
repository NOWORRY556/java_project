package com.shopsphere.dao;

import com.shopsphere.config.DBConnection;
import java.sql.*;
import java.util.*;

public class AdminReviewDAO {

    public List<Map<String, Object>> findAll() throws SQLException {
        List<Map<String, Object>> out = new ArrayList<>();
        String q = "SELECT r.review_id, r.rating, r.review_text, r.created_at, u.name AS customer, p.name AS product "
                 + "FROM reviews r JOIN users u ON u.user_id=r.user_id JOIN products p ON p.product_id=r.product_id ORDER BY r.created_at DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement s = c.prepareStatement(q);
             ResultSet r = s.executeQuery()) {
            while (r.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("reviewId", r.getInt(1));
                m.put("rating", r.getInt(2));
                m.put("reviewText", r.getString(3));
                m.put("createdAt", r.getTimestamp(4));
                m.put("customer", r.getString(5));
                m.put("product", r.getString(6));
                out.add(m);
            }
        } catch (SQLException sqle) {
            // Return empty list if reviews table is missing or empty
        }
        return out;
    }

    public void delete(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement s = c.prepareStatement("DELETE FROM reviews WHERE review_id=?")) {
            s.setInt(1, id);
            s.executeUpdate();
        } catch (SQLException sqle) {
            // Ignored if review table doesn't exist
        }
    }
}
