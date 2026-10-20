package com.shopsphere.serialization;
import java.io.*;
public class SerializationDemo {
 public static void main(String[] args)throws Exception{ShopMessage m=new ShopMessage("INFO","ShopSphere serialization demo");try(ObjectOutputStream o=new ObjectOutputStream(new FileOutputStream("shop-message.ser"))){o.writeObject(m);}try(ObjectInputStream i=new ObjectInputStream(new FileInputStream("shop-message.ser"))){ShopMessage x=(ShopMessage)i.readObject();System.out.println(x.getType()+": "+x.getMessage());}}
}