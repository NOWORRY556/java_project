package com.shopsphere.model;
import java.io.Serializable;
public class CartItem implements Serializable {
    private int productId, quantity;
    private String productName;
    private double price;
    public int getProductId(){return productId;} public void setProductId(int v){productId=v;}
    public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;}
    public String getProductName(){return productName;} public void setProductName(String v){productName=v;}
    public double getPrice(){return price;} public void setPrice(double v){price=v;}
    public double getSubtotal(){return price*quantity;}
}