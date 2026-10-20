package com.shopsphere.dao;

import com.shopsphere.config.DBConnection;
import com.shopsphere.model.Address;
import java.sql.*;
import java.util.*;

public class AddressDAO {

    public int create(int userId, String line, String city, String state, String pincode) throws SQLException {
        String q = "INSERT INTO addresses(user_id, address_line, city, state, pincode, address_type) VALUES(?,?,?,?,?,'SHIPPING')";
        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, userId);
            s.setString(2, line);
            s.setString(3, city);
            s.setString(4, state);
            s.setString(5, pincode);
            s.executeUpdate();
            try (ResultSet r = s.getGeneratedKeys()) {
                if (!r.next()) throw new SQLException("Address was not created");
                return r.getInt(1);
            }
        }
    }

    public List<Address> findByUser(int userId) throws SQLException {
        List<Address> out = new ArrayList<>();
        String q = "SELECT address_id, user_id, address_line, city, state, pincode, address_type FROM addresses WHERE user_id=? AND (address_type IS NULL OR address_type != 'DELETED') ORDER BY address_id DESC";
        String fallbackQ = "SELECT address_id, user_id, address_line, city, state, pincode, address_type FROM addresses WHERE user_id=? ORDER BY address_id DESC";

        try (Connection c = DBConnection.getConnection()) {
            try (PreparedStatement s = c.prepareStatement(q)) {
                s.setInt(1, userId);
                try (ResultSet r = s.executeQuery()) {
                    while (r.next()) {
                        Address a = new Address();
                        a.setAddressId(r.getInt(1));
                        a.setUserId(r.getInt(2));
                        a.setAddressLine(r.getString(3));
                        a.setCity(r.getString(4));
                        a.setState(r.getString(5));
                        a.setPincode(r.getString(6));
                        a.setAddressType(r.getString(7));
                        out.add(a);
                    }
                    return out;
                }
            } catch (SQLException sqle) {
                try (PreparedStatement s = c.prepareStatement(fallbackQ)) {
                    s.setInt(1, userId);
                    try (ResultSet r = s.executeQuery()) {
                        while (r.next()) {
                            Address a = new Address();
                            a.setAddressId(r.getInt(1));
                            a.setUserId(r.getInt(2));
                            a.setAddressLine(r.getString(3));
                            a.setCity(r.getString(4));
                            a.setState(r.getString(5));
                            a.setPincode(r.getString(6));
                            a.setAddressType(r.getString(7));
                            out.add(a);
                        }
                    }
                }
            }
        }
        return out;
    }

    public Address findOwned(int userId, int addressId) throws SQLException {
        String q = "SELECT address_id, user_id, address_line, city, state, pincode, address_type FROM addresses WHERE address_id=? AND user_id=? AND (address_type IS NULL OR address_type != 'DELETED')";
        String fallbackQ = "SELECT address_id, user_id, address_line, city, state, pincode, address_type FROM addresses WHERE address_id=? AND user_id=?";

        try (Connection c = DBConnection.getConnection()) {
            try (PreparedStatement s = c.prepareStatement(q)) {
                s.setInt(1, addressId);
                s.setInt(2, userId);
                try (ResultSet r = s.executeQuery()) {
                    if (r.next()) {
                        Address a = new Address();
                        a.setAddressId(r.getInt(1));
                        a.setUserId(r.getInt(2));
                        a.setAddressLine(r.getString(3));
                        a.setCity(r.getString(4));
                        a.setState(r.getString(5));
                        a.setPincode(r.getString(6));
                        a.setAddressType(r.getString(7));
                        return a;
                    }
                }
            } catch (SQLException sqle) {
                try (PreparedStatement s = c.prepareStatement(fallbackQ)) {
                    s.setInt(1, addressId);
                    s.setInt(2, userId);
                    try (ResultSet r = s.executeQuery()) {
                        if (r.next()) {
                            Address a = new Address();
                            a.setAddressId(r.getInt(1));
                            a.setUserId(r.getInt(2));
                            a.setAddressLine(r.getString(3));
                            a.setCity(r.getString(4));
                            a.setState(r.getString(5));
                            a.setPincode(r.getString(6));
                            a.setAddressType(r.getString(7));
                            return a;
                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean delete(int userId, int addressId) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            // 1. Try hard delete first
            try (PreparedStatement s = c.prepareStatement("DELETE FROM addresses WHERE address_id=? AND user_id=?")) {
                s.setInt(1, addressId);
                s.setInt(2, userId);
                int count = s.executeUpdate();
                if (count > 0) return true;
            } catch (SQLException sqle) {
                // 2. If Foreign Key constraint fails (referenced in orders table), soft delete via address_type='DELETED'
                try (PreparedStatement s = c.prepareStatement("UPDATE addresses SET address_type='DELETED' WHERE address_id=? AND user_id=?")) {
                    s.setInt(1, addressId);
                    s.setInt(2, userId);
                    return s.executeUpdate() > 0;
                }
            }
        }
        return false;
    }
}
