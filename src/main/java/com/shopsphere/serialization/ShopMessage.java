package com.shopsphere.serialization;
import java.io.Serializable;
public class ShopMessage implements Serializable {private static final long serialVersionUID=1L;private String type,message;public ShopMessage(String type,String message){this.type=type;this.message=message;}public String getType(){return type;}public String getMessage(){return message;}}