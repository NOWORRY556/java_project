package com.shopsphere.dao;

import com.shopsphere.config.DBConnection;
import com.shopsphere.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductDAO {
    private static final String FIND_ALL =
        "SELECT product_id, category_id, name, brand, description, price, discount, stock, image_url, status FROM products WHERE status = TRUE ORDER BY product_id DESC";
    private static final String FIND_BY_ID =
        "SELECT product_id, category_id, name, brand, description, price, discount, stock, image_url, status FROM products WHERE product_id = ? AND status = TRUE";

    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement s = c.prepareStatement(FIND_ALL);
             ResultSet rs = s.executeQuery()) {
            while (rs.next()) products.add(map(rs));
        }
        return products;
    }

    public Product findById(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement s = c.prepareStatement(FIND_BY_ID)) {
            s.setInt(1, id);
            try (ResultSet rs = s.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Product> search(String term, int categoryId, String sort, int page, int size) throws SQLException {
        return search(term, categoryId, null, null, false, sort, page, size);
    }

    public List<Product> search(String term, int categoryId, Double minPrice, Double maxPrice, boolean inStockOnly, String sort, int page, int size) throws SQLException {
        StringBuilder q = new StringBuilder("SELECT product_id, category_id, name, brand, description, price, discount, stock, image_url, status FROM products WHERE status=TRUE");
        List<Object> args = new ArrayList<>();
        if (term != null && !term.isBlank()) {
            q.append(" AND (LOWER(name) LIKE ? OR LOWER(brand) LIKE ?)");
            String x = "%" + term.trim().toLowerCase(Locale.ROOT) + "%";
            args.add(x);
            args.add(x);
        }
        if (categoryId > 0) {
            q.append(" AND category_id=?");
            args.add(categoryId);
        }
        if (minPrice != null && minPrice >= 0) {
            q.append(" AND price >= ?");
            args.add(minPrice);
        }
        if (maxPrice != null && maxPrice > 0) {
            q.append(" AND price <= ?");
            args.add(maxPrice);
        }
        if (inStockOnly) {
            q.append(" AND stock > 0");
        }
        String order = safeSort(sort);
        q.append(" ORDER BY ").append(order).append(" LIMIT ? OFFSET ?");
        int safePage = Math.max(1, page), safeSize = Math.min(50, Math.max(1, size));
        args.add(safeSize);
        args.add((safePage - 1) * safeSize);
        try (Connection c = DBConnection.getConnection();
             PreparedStatement s = c.prepareStatement(q.toString())) {
            for (int i = 0; i < args.size(); i++) {
                Object a = args.get(i);
                if (a instanceof Integer) s.setInt(i + 1, (Integer) a);
                else if (a instanceof Double) s.setDouble(i + 1, (Double) a);
                else s.setString(i + 1, a.toString());
            }
            try (ResultSet rs = s.executeQuery()) {
                List<Product> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    private String safeSort(String sort){
        return switch(sort==null?"newest":sort){
            case "price_asc" -> "price ASC, product_id DESC";
            case "price_desc" -> "price DESC, product_id DESC";
            case "stock" -> "stock DESC, product_id DESC";
            default -> "product_id DESC";
        };
    }

    private Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setDiscount(rs.getDouble("discount"));
        p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        p.setStatus(rs.getBoolean("status"));
        return p;
    }
}
