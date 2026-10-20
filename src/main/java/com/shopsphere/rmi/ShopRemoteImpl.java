package com.shopsphere.rmi;
import java.rmi.RemoteException; import java.rmi.server.UnicastRemoteObject;
public class ShopRemoteImpl extends UnicastRemoteObject implements ShopRemote {
 public ShopRemoteImpl()throws RemoteException{}
 public String getServerStatus(){return "ShopSphere RMI service is running";}
}