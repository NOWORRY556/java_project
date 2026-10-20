package com.shopsphere.controller;

import com.shopsphere.model.Address;
import com.shopsphere.dao.AddressDAO;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.User;
import com.shopsphere.security.InputValidator;
import com.shopsphere.service.OrderService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private final OrderService service = new OrderService();
    private final AddressDAO addresses = new AddressDAO();

    private String getRazorpayKeyId() {
        String key = System.getenv("RAZORPAY_KEY_ID");
        if (key != null && !key.isBlank()) return key.trim();
        return ""; // Must be configured via environment variable
    }

    private User user(HttpServletRequest r) {
        HttpSession s = r.getSession(false);
        return s == null ? null : (User) s.getAttribute("loggedInUser");
    }

    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {
        User u = user(r);
        if (u == null) {
            p.sendRedirect(r.getContextPath() + "/login");
            return;
        }
        try {
            double total = service.cartTotal(u.getUserId());
            if (total <= 0) {
                r.getSession().setAttribute("cartMessage", "Your bag is empty. Please add items before checking out.");
                p.sendRedirect(r.getContextPath() + "/cart");
                return;
            }
            Coupon coupon = (Coupon) r.getSession().getAttribute("checkoutCoupon");
            double discount = service.couponDiscount(coupon, total);
            r.setAttribute("cartTotal", total);
            r.setAttribute("couponDiscount", discount);
            r.setAttribute("payableTotal", total - discount);
            r.setAttribute("checkoutCoupon", coupon);
            r.setAttribute("savedAddresses", addresses.findByUser(u.getUserId()));
            r.setAttribute("razorpayKeyId", getRazorpayKeyId());
            r.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(r, p);
        } catch (Exception e) {
            throw new ServletException("Unable to load checkout", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {
        User u = user(r);
        if (u == null) {
            p.sendRedirect(r.getContextPath() + "/login");
            return;
        }
        HttpSession session = r.getSession();
        try {
            if ("applyCoupon".equals(r.getParameter("action"))) {
                double total = service.cartTotal(u.getUserId());
                Coupon c = service.validateCoupon(r.getParameter("couponCode"), total);
                session.setAttribute("checkoutCoupon", c);
                session.setAttribute("checkoutMessage", "Coupon " + c.getCode() + " applied successfully.");
                p.sendRedirect(r.getContextPath() + "/checkout");
                return;
            }
            if ("removeCoupon".equals(r.getParameter("action"))) {
                session.removeAttribute("checkoutCoupon");
                session.setAttribute("checkoutMessage", "Coupon removed.");
                p.sendRedirect(r.getContextPath() + "/checkout");
                return;
            }

            String address; String city; String state; String pincode;
            String addressIdParam = r.getParameter("addressId");
            if (addressIdParam != null && !addressIdParam.isBlank()) {
                int addressId = InputValidator.positiveInt(addressIdParam, "Address");
                Address saved = addresses.findOwned(u.getUserId(), addressId);
                if (saved == null) throw new IllegalArgumentException("Selected address was not found.");
                address = saved.getAddressLine(); city = saved.getCity(); state = saved.getState(); pincode = saved.getPincode();
            } else {
                address = InputValidator.maxLength(InputValidator.required(r.getParameter("addressLine"), "Address"), "Address", 255);
                city = InputValidator.maxLength(InputValidator.required(r.getParameter("city"), "City"), "City", 100);
                state = InputValidator.maxLength(InputValidator.required(r.getParameter("state"), "State"), "State", 100);
                pincode = InputValidator.pincode(r.getParameter("pincode"));
            }

            String paymentMethod = r.getParameter("paymentMethod");
            if (paymentMethod == null || paymentMethod.isBlank()) paymentMethod = "COD";

            String razorpayPaymentId = r.getParameter("razorpay_payment_id");

            String paymentStatus = "RAZORPAY".equalsIgnoreCase(paymentMethod) || "ONLINE".equalsIgnoreCase(paymentMethod) ? "SUCCESS" : "PENDING";

            Coupon c = (Coupon) session.getAttribute("checkoutCoupon");
            String code = c == null ? null : c.getCode();

            int id = service.placeOrder(u.getUserId(), address, city, state, pincode, code, paymentMethod, paymentStatus);

            // Fetch created order details & items for exact invoice email generation
            try {
                com.shopsphere.dao.OrderDetailDAO detailDAO = new com.shopsphere.dao.OrderDetailDAO();
                java.util.Map<String, Object> orderMap = detailDAO.findOrder(u.getUserId(), id);
                java.util.List<java.util.Map<String, Object>> itemsList = detailDAO.findItems(u.getUserId(), id);

                double billedTotal = 0.0;
                String fullAddr = address + ", " + city + ", " + state + " - " + pincode;
                String payMethod = paymentMethod;

                if (orderMap != null) {
                    Object totObj = orderMap.get("totalAmount");
                    if (totObj != null) billedTotal = Double.parseDouble(String.valueOf(totObj));
                    if (orderMap.get("shippingAddress") != null) fullAddr = String.valueOf(orderMap.get("shippingAddress"));
                    if (orderMap.get("paymentMethod") != null) payMethod = String.valueOf(orderMap.get("paymentMethod"));
                }

                com.shopsphere.service.EmailService.sendOrderReceiptAsync(u.getEmail(), u.getName(), id, billedTotal, payMethod, fullAddr, itemsList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            session.removeAttribute("checkoutCoupon");
            session.setAttribute("checkoutMessage", null);
            r.setAttribute("orderId", id);
            r.getRequestDispatcher("/WEB-INF/views/order-success.jsp").forward(r, p);

        } catch (IllegalStateException e) {
            session.setAttribute("cartMessage", "Your bag is empty. Please add items before checking out.");
            p.sendRedirect(r.getContextPath() + "/cart");
        } catch (IllegalArgumentException e) {
            session.setAttribute("checkoutMessage", e.getMessage());
            p.sendRedirect(r.getContextPath() + "/checkout");
        } catch (Exception e) {
            throw new ServletException("Checkout failed", e);
        }
    }
}
