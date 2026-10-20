package com.shopsphere.rmi;
import java.rmi.registry.LocateRegistry; import java.rmi.registry.Registry;
public class ShopRmiServer {
 public static void main(String[] args)throws Exception{Registry registry=LocateRegistry.createRegistry(1099);registry.rebind("ShopSphereService",new ShopRemoteImpl());System.out.println("ShopSphere RMI server running on 1099");}
}