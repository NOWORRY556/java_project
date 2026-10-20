package com.shopsphere.web;

import com.shopsphere.config.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/admin/export")
public class AdminExportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String type = req.getParameter("type");
        if (type == null || type.isBlank()) type = "orders";

        resp.setContentType("text/csv;charset=UTF-8");

        try (var out = resp.getWriter(); Connection c = DBConnection.getConnection()) {
            if ("template".equalsIgnoreCase(type)) {
                resp.setHeader("Content-Disposition", "attachment; filename=shopsphere-products-template.csv");
                out.println("name,brand,category_id,price,discount,stock,description,image_url");
                out.println("Spatial Audio Headphones,Bowers & Wilkins,1,24999.00,10.0,25,Studio quality wireless headphones with active noise cancellation,https://images.unsplash.com/photo-1505740420928-5e560c06d30e");
                out.println("Minimalist Ceramic Chronograph,Nomos Glashütte,1,18500.00,5.0,12,Handcrafted mechanical watch with sapphire glass,https://images.unsplash.com/photo-1523275335684-37898b6baf30");
                return;
            }

            if ("products".equalsIgnoreCase(type)) {
                resp.setHeader("Content-Disposition", "attachment; filename=shopsphere-catalog-inventory.csv");
                out.println("product_id,name,brand,category_id,price,discount,stock,status");
                String q = "SELECT product_id, name, brand, category_id, price, discount, stock, status FROM products ORDER BY product_id ASC";
                try (PreparedStatement s = c.prepareStatement(q); ResultSet r = s.executeQuery()) {
                    while (r.next()) {
                        out.printf("%d,%s,%s,%d,%.2f,%.2f,%d,%s%n",
                            r.getInt(1),
                            csv(r.getString(2)),
                            csv(r.getString(3)),
                            r.getInt(4),
                            r.getDouble(5),
                            r.getDouble(6),
                            r.getInt(7),
                            r.getBoolean(8) ? "ACTIVE" : "INACTIVE"
                        );
                    }
                }
                return;
            }

            if ("sales".equalsIgnoreCase(type)) {
                resp.setHeader("Content-Disposition", "attachment; filename=shopsphere-sales-report.csv");
                out.println("order_id,customer_name,customer_email,total_amount,discount,payment_method,payment_status,order_status,created_at");
                String q = "SELECT o.order_id, u.name, u.email, o.total_amount, o.discount, o.payment_method, o.payment_status, o.order_status, o.created_at "
                         + "FROM orders o JOIN users u ON u.user_id=o.user_id ORDER BY o.created_at DESC";
                try (PreparedStatement s = c.prepareStatement(q); ResultSet r = s.executeQuery()) {
                    while (r.next()) {
                        out.printf("%d,%s,%s,%.2f,%.2f,%s,%s,%s,%s%n",
                            r.getInt(1),
                            csv(r.getString(2)),
                            csv(r.getString(3)),
                            r.getDouble(4),
                            r.getDouble(5),
                            csv(r.getString(6)),
                            csv(r.getString(7)),
                            csv(r.getString(8)),
                            r.getTimestamp(9)
                        );
                    }
                }
                return;
            }

            // Default: orders export
            resp.setHeader("Content-Disposition", "attachment; filename=shopsphere-orders.csv");
            out.println("order_id,email,total_amount,order_status,created_at");
            String q = "SELECT o.order_id, u.email, o.total_amount, o.order_status, o.created_at "
                     + "FROM orders o JOIN users u ON u.user_id=o.user_id ORDER BY o.created_at DESC";
            try (PreparedStatement s = c.prepareStatement(q); ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    out.printf("%d,%s,%.2f,%s,%s%n",
                        r.getInt(1),
                        csv(r.getString(2)),
                        r.getDouble(3),
                        csv(r.getString(4)),
                        r.getTimestamp(5)
                    );
                }
            }
        } catch (Exception e) {
            throw new IOException("Unable to export data", e);
        }
    }

    private String csv(String v) {
        String value = v == null ? "" : v;
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}