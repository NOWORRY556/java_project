package com.shopsphere.dao;

import com.shopsphere.config.DBConnection;
import com.shopsphere.model.Product;
import java.sql.*;
import java.util.*;

public class ProductAdminDAO {
    private static final String SELECT_ALL = "SELECT product_id,category_id,name,brand,description,price,discount,stock,image_url,status FROM products ORDER BY product_id DESC";
    private static final String SELECT_BY_ID = "SELECT product_id,category_id,name,brand,description,price,discount,stock,image_url,status FROM products WHERE product_id=?";

    public List<Product> findAll() throws SQLException {
        List<Product> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(SELECT_ALL); ResultSet r = s.executeQuery()) {
            while (r.next()) out.add(map(r));
        }
        return out;
    }

    public Product findById(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(SELECT_BY_ID)) {
            s.setInt(1, id);
            try (ResultSet r = s.executeQuery()) { return r.next() ? map(r) : null; }
        }
    }

    public int create(Product p) throws SQLException {
        String sql = "INSERT INTO products(category_id,name,brand,description,price,discount,stock,image_url,status) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(s, p);
            s.setBoolean(9, p.isStatus());
            s.executeUpdate();
            try (ResultSet r = s.getGeneratedKeys()) { return r.next() ? r.getInt(1) : 0; }
        }
    }

    public boolean update(Product p) throws SQLException {
        String sql = "UPDATE products SET category_id=?,name=?,brand=?,description=?,price=?,discount=?,stock=?,image_url=?,status=? WHERE product_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(sql)) {
            bind(s, p);
            s.setBoolean(9, p.isStatus());
            s.setInt(10, p.getProductId());
            return s.executeUpdate() == 1;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement("UPDATE products SET status = CASE WHEN status=TRUE THEN FALSE ELSE TRUE END WHERE product_id=?")) {
            s.setInt(1, id);
            return s.executeUpdate() == 1;
        }
    }

    private void bind(PreparedStatement s, Product p) throws SQLException {
        s.setInt(1, p.getCategoryId()); s.setString(2, p.getName()); s.setString(3, p.getBrand());
        s.setString(4, p.getDescription()); s.setDouble(5, p.getPrice()); s.setDouble(6, p.getDiscount());
        s.setInt(7, p.getStock()); s.setString(8, p.getImageUrl());
    }

    private Product map(ResultSet r) throws SQLException {
        Product p = new Product();
        p.setProductId(r.getInt("product_id")); p.setCategoryId(r.getInt("category_id"));
        p.setName(r.getString("name")); p.setBrand(r.getString("brand")); p.setDescription(r.getString("description"));
        p.setPrice(r.getDouble("price")); p.setDiscount(r.getDouble("discount")); p.setStock(r.getInt("stock"));
        p.setImageUrl(r.getString("image_url")); p.setStatus(r.getBoolean("status")); return p;
    }
}
