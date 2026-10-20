package com.shopsphere.service;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.dao.ProductAdminDAO;
import com.shopsphere.model.Product;
import java.sql.SQLException;
import java.util.List;

public class CatalogAdminService {
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ProductAdminDAO productDAO = new ProductAdminDAO();

    public List<String[]> categories() throws SQLException { return categoryDAO.findAll(); }
    public String[] findCategory(int id) throws SQLException { return categoryDAO.findById(id); }
    public int createCategory(String name, String description, String imageUrl) throws SQLException { return categoryDAO.create(name, description, imageUrl); }
    public boolean updateCategory(int id, String name, String description, String imageUrl, boolean status) throws SQLException { return categoryDAO.update(id, name, description, imageUrl, status); }
    public boolean deleteCategory(int id) throws SQLException { return categoryDAO.delete(id); }

    public List<Product> products() throws SQLException { return productDAO.findAll(); }
    public Product findProduct(int id) throws SQLException { return productDAO.findById(id); }
    public int createProduct(Product p) throws SQLException { return productDAO.create(p); }
    public boolean updateProduct(Product p) throws SQLException { return productDAO.update(p); }
    public boolean deleteProduct(int id) throws SQLException { return productDAO.delete(id); }
}
