package com.shopsphere.rmi;

import com.shopsphere.model.Product;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InventoryService extends Remote {
    Product getProduct(int id) throws RemoteException;
    int getStock(int productId) throws RemoteException;
    boolean updateStock(int productId, int quantity) throws RemoteException;
}
