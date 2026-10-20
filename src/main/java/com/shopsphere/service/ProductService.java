package com.shopsphere.service;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;
import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private final ProductDAO productDAO = new ProductDAO();

    public List<Product> getActiveProducts() throws SQLException { return productDAO.findAll(); }
    public Product getProduct(int productId) throws SQLException { return productDAO.findById(productId); }
}
