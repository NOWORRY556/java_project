package com.shopsphere.rmi;

import java.rmi.registry.LocateRegistry;

public final class InventoryRmiClient {
    private InventoryRmiClient() {}
    public static void main(String[] args) throws Exception {
        var registry=LocateRegistry.getRegistry("localhost",1099);
        InventoryService service=(InventoryService)registry.lookup("ShopSphereInventory");
        int productId=args.length==0?1:Integer.parseInt(args[0]);
        System.out.println("Product: "+service.getProduct(productId));
        System.out.println("Stock: "+service.getStock(productId));
    }
}
