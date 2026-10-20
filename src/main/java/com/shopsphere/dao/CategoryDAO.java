package com.shopsphere.dao;

import com.shopsphere.config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public CategoryDAO() {
        ensureSchema();
    }

    private void ensureSchema() {
        try (Connection c = DBConnection.getConnection(); Statement stmt = c.createStatement()) {
            try {
                stmt.executeUpdate("ALTER TABLE categories ADD COLUMN image_url VARCHAR(500) DEFAULT ''");
            } catch (Exception ignored) {}

            try {
                stmt.executeUpdate("ALTER TABLE categories ADD COLUMN status BOOLEAN DEFAULT TRUE");
            } catch (Exception ignored) {}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String[]> findAll() throws SQLException {
        List<String[]> rows = new ArrayList<>();
        ensureSchema();

        String sql1 = "SELECT category_id, name, description, image_url, status FROM categories ORDER BY category_id DESC";
        String sql2 = "SELECT category_id, name, description, image_url, TRUE AS status FROM categories ORDER BY category_id DESC";
        String sql3 = "SELECT category_id, name, description, '' AS image_url, status FROM categories ORDER BY category_id DESC";
        String sql4 = "SELECT category_id, name, description, '' AS image_url, TRUE AS status FROM categories ORDER BY category_id DESC";

        try (Connection c = DBConnection.getConnection()) {
            Statement s = c.createStatement();
            ResultSet rs = null;

            try {
                rs = s.executeQuery(sql1);
            } catch (SQLException e1) {
                try {
                    rs = s.executeQuery(sql2);
                } catch (SQLException e2) {
                    try {
                        rs = s.executeQuery(sql3);
                    } catch (SQLException e3) {
                        rs = s.executeQuery(sql4);
                    }
                }
            }

            if (rs != null) {
                while (rs.next()) {
                    rows.add(new String[]{
                        String.valueOf(rs.getInt("category_id")),
                        rs.getString("name") != null ? rs.getString("name") : "",
                        rs.getString("description") != null ? rs.getString("description") : "",
                        rs.getString("image_url") != null ? rs.getString("image_url") : "",
                        String.valueOf(rs.getBoolean("status"))
                    });
                }
                rs.close();
            }
            if (s != null) s.close();
        }
        return rows;
    }

    public String[] findById(int id) throws SQLException {
        ensureSchema();
        String sql1 = "SELECT category_id, name, description, image_url, status FROM categories WHERE category_id = ?";
        String sql2 = "SELECT category_id, name, description, image_url, TRUE AS status FROM categories WHERE category_id = ?";
        String sql3 = "SELECT category_id, name, description, '' AS image_url, status FROM categories WHERE category_id = ?";
        String sql4 = "SELECT category_id, name, description, '' AS image_url, TRUE AS status FROM categories WHERE category_id = ?";

        try (Connection c = DBConnection.getConnection()) {
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                ps = c.prepareStatement(sql1);
                ps.setInt(1, id);
                rs = ps.executeQuery();
            } catch (SQLException e1) {
                try {
                    if (ps != null) ps.close();
                    ps = c.prepareStatement(sql2);
                    ps.setInt(1, id);
                    rs = ps.executeQuery();
                } catch (SQLException e2) {
                    try {
                        if (ps != null) ps.close();
                        ps = c.prepareStatement(sql3);
                        ps.setInt(1, id);
                        rs = ps.executeQuery();
                    } catch (SQLException e3) {
                        if (ps != null) ps.close();
                        ps = c.prepareStatement(sql4);
                        ps.setInt(1, id);
                        rs = ps.executeQuery();
                    }
                }
            }

            if (rs != null && rs.next()) {
                String[] res = new String[]{
                    String.valueOf(rs.getInt("category_id")),
                    rs.getString("name") != null ? rs.getString("name") : "",
                    rs.getString("description") != null ? rs.getString("description") : "",
                    rs.getString("image_url") != null ? rs.getString("image_url") : "",
                    String.valueOf(rs.getBoolean("status"))
                };
                rs.close();
                if (ps != null) ps.close();
                return res;
            }
            if (ps != null) ps.close();
        }
        return null;
    }

    public int create(String name, String description, String imageUrl) throws SQLException {
        ensureSchema();
        String sql1 = "INSERT INTO categories(name, description, image_url, status) VALUES(?,?,?,TRUE)";
        String sql2 = "INSERT INTO categories(name, description, image_url) VALUES(?,?,?)";
        String sql3 = "INSERT INTO categories(name, description, status) VALUES(?,?,TRUE)";
        String sql4 = "INSERT INTO categories(name, description) VALUES(?,?)";

        try (Connection c = DBConnection.getConnection()) {
            PreparedStatement ps = null;
            try {
                ps = c.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                ps.setString(2, description);
                ps.setString(3, imageUrl != null ? imageUrl.trim() : "");
                ps.executeUpdate();
            } catch (SQLException e1) {
                try {
                    if (ps != null) ps.close();
                    ps = c.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, name);
                    ps.setString(2, description);
                    ps.setString(3, imageUrl != null ? imageUrl.trim() : "");
                    ps.executeUpdate();
                } catch (SQLException e2) {
                    try {
                        if (ps != null) ps.close();
                        ps = c.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, name);
                        ps.setString(2, description);
                        ps.executeUpdate();
                    } catch (SQLException e3) {
                        if (ps != null) ps.close();
                        ps = c.prepareStatement(sql4, Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, name);
                        ps.setString(2, description);
                        ps.executeUpdate();
                    }
                }
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                int key = rs.next() ? rs.getInt(1) : 0;
                ps.close();
                return key;
            }
        }
    }

    public boolean update(int id, String name, String description, String imageUrl, boolean status) throws SQLException {
        ensureSchema();
        String sql1 = "UPDATE categories SET name=?, description=?, image_url=?, status=? WHERE category_id=?";
        String sql2 = "UPDATE categories SET name=?, description=?, image_url=? WHERE category_id=?";
        String sql3 = "UPDATE categories SET name=?, description=?, status=? WHERE category_id=?";
        String sql4 = "UPDATE categories SET name=?, description=? WHERE category_id=?";

        try (Connection c = DBConnection.getConnection()) {
            PreparedStatement ps = null;
            try {
                ps = c.prepareStatement(sql1);
                ps.setString(1, name);
                ps.setString(2, description);
                ps.setString(3, imageUrl != null ? imageUrl.trim() : "");
                ps.setBoolean(4, status);
                ps.setInt(5, id);
                int r = ps.executeUpdate();
                ps.close();
                return r == 1;
            } catch (SQLException e1) {
                try {
                    if (ps != null) ps.close();
                    ps = c.prepareStatement(sql2);
                    ps.setString(1, name);
                    ps.setString(2, description);
                    ps.setString(3, imageUrl != null ? imageUrl.trim() : "");
                    ps.setInt(4, id);
                    int r = ps.executeUpdate();
                    ps.close();
                    return r == 1;
                } catch (SQLException e2) {
                    try {
                        if (ps != null) ps.close();
                        ps = c.prepareStatement(sql3);
                        ps.setString(1, name);
                        ps.setString(2, description);
                        ps.setBoolean(3, status);
                        ps.setInt(4, id);
                        int r = ps.executeUpdate();
                        ps.close();
                        return r == 1;
                    } catch (SQLException e3) {
                        if (ps != null) ps.close();
                        ps = c.prepareStatement(sql4);
                        ps.setString(1, name);
                        ps.setString(2, description);
                        ps.setInt(3, id);
                        int r = ps.executeUpdate();
                        ps.close();
                        return r == 1;
                    }
                }
            }
        }
    }

    public boolean delete(int id) throws SQLException {
        ensureSchema();
        try (Connection c = DBConnection.getConnection()) {
            try (PreparedStatement s = c.prepareStatement("UPDATE categories SET status = CASE WHEN status=TRUE THEN FALSE ELSE TRUE END WHERE category_id=?")) {
                s.setInt(1, id);
                return s.executeUpdate() == 1;
            } catch (SQLException sqle) {
                try (PreparedStatement s = c.prepareStatement("DELETE FROM categories WHERE category_id=?")) {
                    s.setInt(1, id);
                    return s.executeUpdate() == 1;
                }
            }
        }
    }
}
