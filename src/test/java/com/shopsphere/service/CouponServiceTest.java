package com.shopsphere.service;

import com.shopsphere.model.Coupon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CouponServiceTest {
    @Test
    void percentageDiscountRespectsMaximum() {
        Coupon c=new Coupon();
        c.setDiscountType("PERCENT"); c.setDiscountValue(20); c.setMaximumDiscount(100.0);
        assertEquals(100.0,new CouponService().calculateDiscount(c,1000.0),0.001);
    }

    @Test
    void fixedDiscountNeverExceedsSubtotal() {
        Coupon c=new Coupon();
        c.setDiscountType("FIXED"); c.setDiscountValue(900);
        assertEquals(500.0,new CouponService().calculateDiscount(c,500.0),0.001);
    }

    @Test
    void nullCouponHasNoDiscount() {
        assertEquals(0.0,new CouponService().calculateDiscount(null,500.0),0.001);
    }
}