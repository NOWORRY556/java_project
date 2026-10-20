package com.shopsphere.rmi;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class InventoryServiceImpl extends UnicastRemoteObject implements InventoryService {
    private final ProductDAO productDAO = new ProductDAO();
    public InventoryServiceImpl() throws RemoteException { super(); }
    @Override public Product getProduct(int id) throws RemoteException {
        try { return productDAO.findById(id); } catch (SQLException e) { throw new RemoteException("Product lookup failed", e); }
    }
    @Override public int getStock(int productId) throws RemoteException {
        Product product=getProduct(productId);
        return product == null ? -1 : product.getStock();
    }
    @Override public boolean updateStock(int productId, int quantity) throws RemoteException {
        if (quantity < 0) return false;
        try (var c=com.shopsphere.config.DBConnection.getConnection();
             var s=c.prepareStatement("UPDATE products SET stock=? WHERE product_id=? AND status=TRUE")) {
            s.setInt(1, quantity); s.setInt(2, productId);
            return s.executeUpdate()==1;
        } catch (SQLException e) { throw new RemoteException("Stock update failed", e); }
    }
}
