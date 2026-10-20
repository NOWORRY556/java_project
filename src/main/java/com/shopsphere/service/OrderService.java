package com.shopsphere.service;

import com.shopsphere.dao.*;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Coupon;
import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private final CartDAO cart = new CartDAO();
    private final AddressDAO address = new AddressDAO();
    private final OrderDAO orders = new OrderDAO();
    private final CouponService coupons = new CouponService();

    public double cartTotal(int userId) throws SQLException {
        return cart.findByUser(userId).stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public Coupon validateCoupon(String code, double subtotal) throws SQLException { return coupons.validate(code, subtotal); }
    public double couponDiscount(Coupon coupon, double subtotal) { return coupons.calculateDiscount(coupon, subtotal); }

    public int placeOrder(int userId, String line, String city, String state, String pincode, String couponCode) throws SQLException {
        return placeOrder(userId, line, city, state, pincode, couponCode, "COD", "PENDING");
    }

    public int placeOrder(int userId, String line, String city, String state, String pincode, String couponCode, String paymentMethod, String paymentStatus) throws SQLException {
        List<CartItem> items = cart.findByUser(userId);
        if (items.isEmpty()) throw new IllegalStateException("Cart is empty");
        double subtotal = items.stream().mapToDouble(CartItem::getSubtotal).sum();
        Coupon coupon = coupons.validate(couponCode, subtotal);
        double discount = coupons.calculateDiscount(coupon, subtotal);
        int addressId = address.create(userId, line.trim(), city.trim(), state.trim(), pincode.trim());
        int orderId = orders.create(userId, addressId, items, subtotal - discount, discount, coupon == null ? 0 : coupon.getCouponId(), paymentMethod, paymentStatus);
        cart.clear(userId);
        return orderId;
    }
}