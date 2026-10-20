package com.shopsphere.model;
public class Address {
 private int addressId,userId; private String addressLine,city,state,pincode,addressType;
 public int getAddressId(){return addressId;} public void setAddressId(int v){addressId=v;}
 public int getUserId(){return userId;} public void setUserId(int v){userId=v;}
 public String getAddressLine(){return addressLine;} public void setAddressLine(String v){addressLine=v;}
 public String getCity(){return city;} public void setCity(String v){city=v;}
 public String getState(){return state;} public void setState(String v){state=v;}
 public String getPincode(){return pincode;} public void setPincode(String v){pincode=v;}
 public String getAddressType(){return addressType;} public void setAddressType(String v){addressType=v;}
}
