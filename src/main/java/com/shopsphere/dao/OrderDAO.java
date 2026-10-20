package com.shopsphere.dao;

import com.shopsphere.config.DBConnection;
import com.shopsphere.model.CartItem;
import java.sql.*;
import java.util.List;

public class OrderDAO {
    public int create(int userId, int addressId, List<CartItem> items, double total, double discount, int couponId) throws SQLException {
        return create(userId, addressId, items, total, discount, couponId, "COD", "PENDING");
    }

    public int create(int userId, int addressId, List<CartItem> items, double total, double discount, int couponId, String paymentMethod, String paymentStatus) throws SQLException {
        String orderSql = "INSERT INTO orders(user_id,address_id,total_amount,discount,payment_method,payment_status,order_status) VALUES(?,?,?,?,?,?, 'PLACED')";
        String stockSql = "UPDATE products SET stock=stock-? WHERE product_id=? AND status=TRUE AND stock>=?";
        String itemSql = "INSERT INTO order_items(order_id,product_id,quantity,price) VALUES(?,?,?,?)";
        String usageSql = "INSERT INTO coupon_usage(coupon_id,user_id,order_id) VALUES(?,?,?)";

        if (paymentMethod == null || paymentMethod.isBlank()) paymentMethod = "COD";
        if (paymentStatus == null || paymentStatus.isBlank()) paymentStatus = "PENDING";

        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement stock = c.prepareStatement(stockSql)) {
                    for (CartItem x : items) {
                        stock.setInt(1, x.getQuantity()); stock.setInt(2, x.getProductId()); stock.setInt(3, x.getQuantity());
                        if (stock.executeUpdate() != 1) throw new SQLException("Insufficient stock for product ID " + x.getProductId());
                    }
                }
                int orderId;
                try (PreparedStatement order = c.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                    order.setInt(1, userId); order.setInt(2, addressId); order.setDouble(3, total); order.setDouble(4, discount);
                    order.setString(5, paymentMethod); order.setString(6, paymentStatus);
                    order.executeUpdate();
                    try (ResultSet rs = order.getGeneratedKeys()) { if (!rs.next()) throw new SQLException("Order ID was not generated"); orderId = rs.getInt(1); }
                }
                try (PreparedStatement item = c.prepareStatement(itemSql)) {
                    for (CartItem x : items) { item.setInt(1, orderId); item.setInt(2, x.getProductId()); item.setInt(3, x.getQuantity()); item.setDouble(4, x.getPrice()); item.addBatch(); }
                    item.executeBatch();
                }
                if (couponId > 0) {
                    try (PreparedStatement usage = c.prepareStatement(usageSql)) { usage.setInt(1, couponId); usage.setInt(2, userId); usage.setInt(3, orderId); usage.executeUpdate(); }
                }
                c.commit(); return orderId;
            } catch (SQLException e) { c.rollback(); throw e; }
        }
    }
}