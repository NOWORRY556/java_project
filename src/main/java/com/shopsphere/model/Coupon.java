package com.shopsphere.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Coupon implements Serializable {
    private int couponId;
    private String code;
    private String discountType;
    private double discountValue;
    private double minimumOrder;
    private Double maximumDiscount;
    private LocalDate expiryDate;
    private boolean status;

    public int getCouponId(){return couponId;}
    public void setCouponId(int v){couponId=v;}
    public String getCode(){return code;}
    public void setCode(String v){code=v;}
    public String getDiscountType(){return discountType;}
    public void setDiscountType(String v){discountType=v;}
    public double getDiscountValue(){return discountValue;}
    public void setDiscountValue(double v){discountValue=v;}
    public double getMinimumOrder(){return minimumOrder;}
    public void setMinimumOrder(double v){minimumOrder=v;}
    public Double getMaximumDiscount(){return maximumDiscount;}
    public void setMaximumDiscount(Double v){maximumDiscount=v;}
    public LocalDate getExpiryDate(){return expiryDate;}
    public void setExpiryDate(LocalDate v){expiryDate=v;}
    public boolean isStatus(){return status;}
    public void setStatus(boolean v){status=v;}
}