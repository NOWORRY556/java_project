package com.shopsphere.dao;

import com.shopsphere.config.DBConnection;
import com.shopsphere.model.CartItem;
import java.sql.*;
import java.util.*;

public class CartDAO {
    public List<CartItem> findByUser(int userId) throws SQLException {
        List<CartItem> list = new ArrayList<>();
        String q = "SELECT c.product_id,c.quantity,p.name,p.price FROM cart c JOIN products p ON p.product_id=c.product_id WHERE c.user_id=? AND p.status=TRUE";
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(q)) {
            s.setInt(1, userId);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    CartItem x = new CartItem();
                    x.setProductId(r.getInt(1));
                    x.setQuantity(r.getInt(2));
                    x.setProductName(r.getString(3));
                    x.setPrice(r.getDouble(4));
                    list.add(x);
                }
            }
        }
        return list;
    }

    public void add(int userId, int productId, int quantity) throws SQLException {
        String q = "INSERT INTO cart(user_id,product_id,quantity) VALUES(?,?,?) ON DUPLICATE KEY UPDATE quantity=quantity+VALUES(quantity)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(q)) {
            s.setInt(1, userId);
            s.setInt(2, productId);
            s.setInt(3, quantity);
            s.executeUpdate();
        }
    }

    public void updateQuantity(int userId, int productId, int quantity) throws SQLException {
        String q = "UPDATE cart SET quantity=? WHERE user_id=? AND product_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(q)) {
            s.setInt(1, quantity);
            s.setInt(2, userId);
            s.setInt(3, productId);
            s.executeUpdate();
        }
    }

    public void remove(int userId, int productId) throws SQLException {
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement("DELETE FROM cart WHERE user_id=? AND product_id=?")) {
            s.setInt(1, userId);
            s.setInt(2, productId);
            s.executeUpdate();
        }
    }

    public void clear(int userId) throws SQLException {
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement("DELETE FROM cart WHERE user_id=?")) {
            s.setInt(1, userId);
            s.executeUpdate();
        }
    }
}
