package com.shopsphere.rmi;
import java.rmi.Remote; import java.rmi.RemoteException;
public interface ShopRemote extends Remote { String getServerStatus() throws RemoteException; }