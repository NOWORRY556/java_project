package com.shopsphere.dao;

import com.shopsphere.config.DBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class AdminAnalyticsDAO {

    public Map<String, Object> summary() throws SQLException {
        Map<String, Object> m = new LinkedHashMap<>();
        int usersCount = 0, productsCount = 0, ordersCount = 0, reviewsCount = 0;
        BigDecimal revenue = BigDecimal.ZERO;

        try (Connection c = DBConnection.getConnection()) {
            try (Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT COUNT(*) FROM users")) { if (r.next()) usersCount = r.getInt(1); } catch (Exception ignored) {}
            try (Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT COUNT(*) FROM products WHERE status=TRUE")) { if (r.next()) productsCount = r.getInt(1); } catch (Exception ignored) {}
            try (Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT COUNT(*), COALESCE(SUM(total_amount),0) FROM orders WHERE order_status<>'CANCELLED'")) {
                if (r.next()) {
                    ordersCount = r.getInt(1);
                    revenue = r.getBigDecimal(2) != null ? r.getBigDecimal(2) : BigDecimal.ZERO;
                }
            } catch (Exception ignored) {}
            try (Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT COUNT(*) FROM reviews")) { if (r.next()) reviewsCount = r.getInt(1); } catch (Exception ignored) {}
        }

        m.put("users", usersCount);
        m.put("products", productsCount);
        m.put("orders", ordersCount);
        m.put("revenue", revenue);
        m.put("reviews", reviewsCount);
        return m;
    }

    public List<Map<String, Object>> recentOrders() throws SQLException {
        List<Map<String, Object>> out = new ArrayList<>();
        String q = "SELECT DATE(created_at), COUNT(*), COALESCE(SUM(total_amount),0) FROM orders WHERE order_status<>'CANCELLED' GROUP BY DATE(created_at) ORDER BY DATE(created_at) DESC LIMIT 7";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement s = c.prepareStatement(q);
             ResultSet r = s.executeQuery()) {
            while (r.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("day", r.getDate(1) != null ? r.getDate(1).toString() : "Today");
                m.put("orders", r.getInt(2));
                m.put("revenue", r.getBigDecimal(3) != null ? r.getBigDecimal(3) : BigDecimal.ZERO);
                out.add(m);
            }
        } catch (Exception ignored) {}
        return out;
    }

    public Map<String, Integer> orderStatusDistribution() throws SQLException {
        Map<String, Integer> map = new LinkedHashMap<>();
        String q = "SELECT order_status, COUNT(*) FROM orders GROUP BY order_status";
        try (Connection c = DBConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery(q)) {
            while (r.next()) {
                map.put(r.getString(1), r.getInt(2));
            }
        } catch (Exception ignored) {}
        return map;
    }

    public List<Map<String, Object>> topSellingProducts() throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        String q = "SELECT p.product_id, p.name, p.brand, SUM(oi.quantity) as total_qty, SUM(oi.quantity * oi.price) as total_sales "
                 + "FROM order_items oi JOIN products p ON p.product_id = oi.product_id "
                 + "JOIN orders o ON o.order_id = oi.order_id WHERE o.order_status <> 'CANCELLED' "
                 + "GROUP BY p.product_id, p.name, p.brand ORDER BY total_qty DESC LIMIT 5";
        try (Connection c = DBConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery(q)) {
            while (r.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", r.getInt(1));
                m.put("name", r.getString(2));
                m.put("brand", r.getString(3));
                m.put("qty", r.getInt(4));
                m.put("sales", r.getBigDecimal(5) != null ? r.getBigDecimal(5) : BigDecimal.ZERO);
                list.add(m);
            }
        } catch (Exception ignored) {}
        return list;
    }

    public List<Map<String, Object>> lowStockProducts(int threshold) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        String q = "SELECT product_id, name, brand, stock, price, image_url FROM products WHERE stock <= ? AND status = TRUE ORDER BY stock ASC LIMIT 10";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement s = c.prepareStatement(q)) {
            s.setInt(1, threshold);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", r.getInt(1));
                    m.put("name", r.getString(2));
                    m.put("brand", r.getString(3));
                    m.put("stock", r.getInt(4));
                    m.put("price", r.getDouble(5));
                    m.put("imageUrl", r.getString(6));
                    list.add(m);
                }
            }
        } catch (Exception ignored) {}
        return list;
    }
}
