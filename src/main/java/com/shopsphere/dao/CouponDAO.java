package com.shopsphere.dao;

import com.shopsphere.config.DBConnection;
import com.shopsphere.model.Coupon;
import java.sql.*;
import java.util.*;

public class CouponDAO {
    public Coupon findActive(String code) throws SQLException {
        String q = "SELECT coupon_id, code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status FROM coupons WHERE code=? AND status=TRUE AND (expiry_date IS NULL OR expiry_date >= CURRENT_DATE)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(q)) {
            s.setString(1, code);
            try (ResultSet r = s.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        } catch (SQLException sqle) {
            return null;
        }
    }

    public List<Coupon> findAll() throws SQLException {
        List<Coupon> out = new ArrayList<>();
        String q = "SELECT coupon_id, code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status FROM coupons ORDER BY coupon_id DESC";
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(q); ResultSet r = s.executeQuery()) {
            while (r.next()) out.add(map(r));
        } catch (SQLException sqle) {
            // Return empty list if coupons table is missing
        }
        return out;
    }

    public void create(Coupon x) throws SQLException {
        String q = "INSERT INTO coupons(code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status) VALUES(?,?,?,?,?,?,TRUE)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(q)) {
            s.setString(1, x.getCode());
            s.setString(2, x.getDiscountType());
            s.setDouble(3, x.getDiscountValue());
            s.setDouble(4, x.getMinimumOrder());
            if (x.getMaximumDiscount() == null) s.setNull(5, Types.DECIMAL); else s.setDouble(5, x.getMaximumDiscount());
            if (x.getExpiryDate() == null) s.setNull(6, Types.DATE); else s.setDate(6, java.sql.Date.valueOf(x.getExpiryDate()));
            s.executeUpdate();
        }
    }

    public void setStatus(int id, boolean active) throws SQLException {
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement("UPDATE coupons SET status=? WHERE coupon_id=?")) {
            s.setBoolean(1, active);
            s.setInt(2, id);
            s.executeUpdate();
        } catch (SQLException sqle) {}
    }

    private Coupon map(ResultSet r) throws SQLException {
        Coupon x = new Coupon();
        x.setCouponId(r.getInt("coupon_id"));
        x.setCode(r.getString("code"));
        x.setDiscountType(r.getString("discount_type"));
        x.setDiscountValue(r.getDouble("discount_value"));
        x.setMinimumOrder(r.getDouble("minimum_order"));
        double m = r.getDouble("maximum_discount");
        x.setMaximumDiscount(r.wasNull() ? null : m);
        java.sql.Date d = r.getDate("expiry_date");
        x.setExpiryDate(d == null ? null : d.toLocalDate());
        x.setStatus(r.getBoolean("status"));
        return x;
    }
}