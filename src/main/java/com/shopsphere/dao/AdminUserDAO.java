package com.shopsphere.dao;

import com.shopsphere.config.DBConnection;
import com.shopsphere.model.User;
import java.sql.*;
import java.util.*;

public class AdminUserDAO {

    public List<User> findAll() throws SQLException {
        List<User> out = new ArrayList<>();
        String sql1 = "SELECT user_id, name, email, password, mobile, role, status FROM users ORDER BY user_id DESC";
        String sql2 = "SELECT user_id, name, email, password, '' AS mobile, 'CUSTOMER' AS role, TRUE AS status FROM users ORDER BY user_id DESC";

        try (Connection c = DBConnection.getConnection()) {
            Statement s = c.createStatement();
            ResultSet r = null;
            try {
                r = s.executeQuery(sql1);
            } catch (SQLException sqle) {
                r = s.executeQuery(sql2);
            }

            if (r != null) {
                while (r.next()) {
                    User u = new User();
                    u.setUserId(r.getInt(1));
                    u.setName(r.getString(2) != null ? r.getString(2) : "");
                    u.setEmail(r.getString(3) != null ? r.getString(3) : "");
                    u.setMobile(r.getString(5) != null ? r.getString(5) : "");
                    u.setRole(r.getString(6) != null ? r.getString(6) : "CUSTOMER");
                    u.setStatus(r.getBoolean(7));
                    out.add(u);
                }
                r.close();
            }
            if (s != null) s.close();
        }
        return out;
    }

    public void setStatus(int actorId, int id, boolean status) throws SQLException {
        if (actorId == id && !status) throw new IllegalArgumentException("You cannot disable your own admin account.");
        if (!status && isLastActiveAdmin(id)) throw new IllegalArgumentException("The last active admin cannot be disabled.");
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement("UPDATE users SET status=? WHERE user_id=?")) {
            s.setBoolean(1, status);
            s.setInt(2, id);
            s.executeUpdate();
        } catch (SQLException sqle) {
            // Ignored if status column is missing
        }
    }

    public void setRole(int actorId, int id, String role) throws SQLException {
        if (!"CUSTOMER".equals(role) && !"ADMIN".equals(role)) throw new IllegalArgumentException("Invalid role");
        if (actorId == id && "CUSTOMER".equals(role)) throw new IllegalArgumentException("You cannot remove your own admin role.");
        if ("CUSTOMER".equals(role) && isLastActiveAdmin(id)) throw new IllegalArgumentException("The last active admin cannot be demoted.");
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement("UPDATE users SET role=? WHERE user_id=?")) {
            s.setString(1, role);
            s.setInt(2, id);
            s.executeUpdate();
        }
    }

    private boolean isLastActiveAdmin(int id) throws SQLException {
        String q = "SELECT COUNT(*) FROM users WHERE role='ADMIN' AND status=TRUE AND user_id <> ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(q)) {
            s.setInt(1, id);
            try (ResultSet r = s.executeQuery()) {
                if (r.next()) return r.getInt(1) == 0;
            }
        } catch (Exception ignored) {}
        return false;
    }
}