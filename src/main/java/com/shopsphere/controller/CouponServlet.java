package com.shopsphere.controller;

import com.shopsphere.model.Coupon;
import com.shopsphere.service.CouponService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;

@WebServlet("/admin/coupons")
public class CouponServlet extends HttpServlet {
    private final CouponService service = new CouponService();

    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {
        try {
            r.setAttribute("coupons", service.all());
        } catch (Exception e) {
            e.printStackTrace();
            r.setAttribute("coupons", Collections.emptyList());
            r.setAttribute("error", "Failed to load coupons: " + e.getMessage());
        }
        r.getRequestDispatcher("/WEB-INF/views/admin/coupons.jsp").forward(r, p);
    }

    @Override
    protected void doPost(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {
        try {
            if ("toggle".equals(r.getParameter("action"))) {
                service.setStatus(Integer.parseInt(r.getParameter("id")), Boolean.parseBoolean(r.getParameter("status")));
                r.getSession().setAttribute("msg", "Coupon status updated!");
            } else {
                Coupon c = new Coupon();
                c.setCode(r.getParameter("code").trim().toUpperCase());
                c.setDiscountType(r.getParameter("discountType"));
                c.setDiscountValue(Double.parseDouble(r.getParameter("discountValue")));
                c.setMinimumOrder(Double.parseDouble(r.getParameter("minimumOrder")));
                String max = r.getParameter("maximumDiscount");
                c.setMaximumDiscount(max == null || max.isBlank() ? null : Double.parseDouble(max));
                String expiry = r.getParameter("expiryDate");
                c.setExpiryDate(expiry == null || expiry.isBlank() ? null : LocalDate.parse(expiry));

                if (c.getCode().isBlank() || c.getDiscountValue() <= 0) throw new IllegalArgumentException("Enter valid coupon details.");
                if (!"PERCENT".equals(c.getDiscountType()) && !"FIXED".equals(c.getDiscountType())) throw new IllegalArgumentException("Invalid discount type.");
                if ("PERCENT".equals(c.getDiscountType()) && c.getDiscountValue() > 100) throw new IllegalArgumentException("Percentage discount cannot exceed 100.");

                service.create(c);
                r.getSession().setAttribute("msg", "Coupon " + c.getCode() + " created successfully!");
            }
            p.sendRedirect(r.getContextPath() + "/admin/coupons");
        } catch (IllegalArgumentException e) {
            r.getSession().setAttribute("error", e.getMessage());
            p.sendRedirect(r.getContextPath() + "/admin/coupons");
        } catch (Exception e) {
            e.printStackTrace();
            r.getSession().setAttribute("error", "Coupon operation failed: " + e.getMessage());
            p.sendRedirect(r.getContextPath() + "/admin/coupons");
        }
    }
}