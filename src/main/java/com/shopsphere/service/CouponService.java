package com.shopsphere.service;

import com.shopsphere.dao.CouponDAO;
import com.shopsphere.model.Coupon;
import java.sql.SQLException;
import java.util.List;

public class CouponService {
    private final CouponDAO dao=new CouponDAO();

    public Coupon validate(String raw,double subtotal)throws SQLException{
        if(raw==null||raw.isBlank())return null;
        Coupon c=dao.findActive(raw.trim().toUpperCase());
        if(c==null)throw new IllegalArgumentException("Coupon is invalid or expired.");
        if(subtotal<c.getMinimumOrder())throw new IllegalArgumentException("Minimum order for "+c.getCode()+" is ₹"+String.format("%.2f",c.getMinimumOrder()));
        return c;
    }
    public double calculateDiscount(Coupon c,double subtotal){
        if(c==null)return 0;
        double d="PERCENT".equalsIgnoreCase(c.getDiscountType())?subtotal*c.getDiscountValue()/100.0:c.getDiscountValue();
        if(c.getMaximumDiscount()!=null)d=Math.min(d,c.getMaximumDiscount());
        return Math.max(0,Math.min(d,subtotal));
    }
    public List<Coupon> all()throws SQLException{return dao.findAll();}
    public void create(Coupon c)throws SQLException{dao.create(c);}
    public void setStatus(int id,boolean active)throws SQLException{dao.setStatus(id,active);}
}