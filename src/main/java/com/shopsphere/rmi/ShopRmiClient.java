package com.shopsphere.rmi;
import java.rmi.registry.LocateRegistry;
public class ShopRmiClient {
 public static void main(String[] args)throws Exception{ShopRemote service=(ShopRemote)LocateRegistry.getRegistry("localhost",1099).lookup("ShopSphereService");System.out.println(service.getServerStatus());}
}