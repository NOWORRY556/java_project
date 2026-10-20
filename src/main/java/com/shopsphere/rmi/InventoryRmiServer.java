package com.shopsphere.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public final class InventoryRmiServer {
    private InventoryRmiServer() {}
    public static void main(String[] args) throws Exception {
        Registry registry=LocateRegistry.createRegistry(1099);
        registry.rebind("ShopSphereInventory", new InventoryServiceImpl());
        System.out.println("ShopSphere RMI Inventory service ready on registry 1099");
    }
}
