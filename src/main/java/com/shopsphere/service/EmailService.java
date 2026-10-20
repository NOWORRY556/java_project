package com.shopsphere.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());
    private static final ExecutorService ASYNC_EXECUTOR = Executors.newFixedThreadPool(3);

    private static final String SMTP_HOST = System.getenv().getOrDefault("SMTP_HOST", "smtp.gmail.com");
    private static final String SMTP_PORT = System.getenv().getOrDefault("SMTP_PORT", "587");
    private static final String SMTP_USER = System.getenv().getOrDefault("SMTP_USER", "");
    private static final String SMTP_PASS = System.getenv().getOrDefault("SMTP_PASS", "");
    private static final String FROM_EMAIL = System.getenv().getOrDefault("FROM_EMAIL", "noreply@shopsphere.com");

    public static void sendOrderReceiptAsync(String recipientEmail, String userName, int orderId, double totalAmount) {
        sendOrderReceiptAsync(recipientEmail, userName, orderId, totalAmount, "COD", "Saved Address", null);
    }

    public static void sendOrderReceiptAsync(String recipientEmail, String userName, int orderId, double totalAmount, String paymentMethod, String shippingAddress, List<Map<String, Object>> items) {
        ASYNC_EXECUTOR.submit(() -> {
            try {
                String subject = "Order Invoice #" + orderId + " — ShopSphere";
                String htmlContent = buildOrderInvoiceHtml(userName, orderId, totalAmount, paymentMethod, shippingAddress, items);
                sendEmail(recipientEmail, subject, htmlContent);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to dispatch order receipt to " + recipientEmail, e);
            }
        });
    }

    public static void sendOrderStatusUpdateAsync(String recipientEmail, String userName, int orderId, String status, double totalAmount) {
        ASYNC_EXECUTOR.submit(() -> {
            try {
                String subject = "Order #" + orderId + " Status Update: " + status + " — ShopSphere";
                String htmlContent = buildOrderStatusUpdateHtml(userName, orderId, status, totalAmount);
                sendEmail(recipientEmail, subject, htmlContent);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to dispatch order status update to " + recipientEmail, e);
            }
        });
    }

    public static void sendWelcomeEmailAsync(String recipientEmail, String userName) {
        ASYNC_EXECUTOR.submit(() -> {
            try {
                String subject = "Welcome to ShopSphere — Your Premier Shopping Destination";
                String htmlContent = buildWelcomeHtml(userName);
                sendEmail(recipientEmail, subject, htmlContent);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to dispatch welcome email to " + recipientEmail, e);
            }
        });
    }

    public static void sendPasswordResetOtpAsync(String recipientEmail, String otp) {
        ASYNC_EXECUTOR.submit(() -> {
            try {
                String subject = "Password Reset Security Code — ShopSphere";
                String htmlContent = buildOtpHtml(recipientEmail, otp);
                sendEmail(recipientEmail, subject, htmlContent);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to dispatch OTP email to " + recipientEmail, e);
            }
        });
    }

    private static void sendEmail(String recipientEmail, String subject, String htmlContent) throws Exception {
        if (recipientEmail == null || recipientEmail.isBlank()) {
            return;
        }

        if (SMTP_USER.isBlank() || SMTP_PASS.isBlank()) {
            LOGGER.info(String.format(
                "[SMTP SIMULATED EMAIL] To: %s | Subject: %s | App URL: https://shopsphere-online.azurewebsites.net",
                recipientEmail, subject
            ));
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL, "ShopSphere Store"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setContent(htmlContent, "text/html; charset=utf-8");

        Transport.send(message);
        LOGGER.info("[LIVE EMAIL SENT] To: " + recipientEmail + " | Subject: " + subject);
    }

    private static String buildOrderInvoiceHtml(String userName, int orderId, double totalAmount, String paymentMethod, String shippingAddress, List<Map<String, Object>> items) {
        StringBuilder itemsTable = new StringBuilder();
        if (items != null && !items.isEmpty()) {
            for (Map<String, Object> item : items) {
                String pName = String.valueOf(item.get("name"));
                int qty = Integer.parseInt(String.valueOf(item.get("quantity")));
                double price = Double.parseDouble(String.valueOf(item.get("price")));
                double sub = price * qty;
                itemsTable.append("<tr style='border-bottom: 1px solid #222;'>")
                          .append("<td style='padding: 10px 0; color: #f3efe7;'>").append(pName).append("</td>")
                          .append("<td style='padding: 10px 0; color: #a6a19a; text-align: center;'>").append(qty).append("</td>")
                          .append("<td style='padding: 10px 0; color: #a6a19a; text-align: right;'>₹").append(String.format("%.2f", price)).append("</td>")
                          .append("<td style='padding: 10px 0; color: #d8c8a8; text-align: right; font-weight: bold;'>₹").append(String.format("%.2f", sub)).append("</td>")
                          .append("</tr>");
            }
        }

        return "<html><body style='font-family: Arial, sans-serif; background: #080808; color: #f3efe7; padding: 20px; margin: 0;'>"
             + "<div style='max-width: 600px; margin: 0 auto; background: #121212; border: 1px solid #222; border-radius: 12px; padding: 30px; box-shadow: 0 10px 30px rgba(0,0,0,0.5);'>"
             
             // Header Branding
             + "<div style='border-bottom: 1px solid rgba(216,200,168,0.2); padding-bottom: 18px; margin-bottom: 24px; display: flex; justify-content: space-between; align-items: center;'>"
             + "  <div>"
             + "    <h1 style='font-family: serif; letter-spacing: 3px; font-size: 24px; color: #d8c8a8; margin: 0; text-transform: uppercase;'>SHOPSPHERE</h1>"
             + "    <span style='font-size: 11px; color: #888; text-transform: uppercase; letter-spacing: 1px;'>Official Tax Invoice & Receipt</span>"
             + "  </div>"
             + "</div>"

             // Greeting & Status
             + "<p style='font-size: 15px; color: #f3efe7; margin-bottom: 8px;'>Hello <strong>" + (userName != null ? userName : "Valued Customer") + "</strong>,</p>"
             + "<p style='color: #a6a19a; font-size: 13px; line-height: 1.5; margin-top: 0;'>Thank you for shopping with ShopSphere! Your order <strong>#" + orderId + "</strong> has been confirmed and is being processed for dispatch.</p>"

             // Order Meta Card
             + "<div style='background: #181818; border: 1px solid #282828; border-radius: 8px; padding: 18px; margin: 20px 0; font-size: 13px;'>"
             + "  <div style='margin-bottom: 8px; color: #a6a19a;'><strong style='color: #f3efe7;'>Order ID:</strong> #" + orderId + "</div>"
             + "  <div style='margin-bottom: 8px; color: #a6a19a;'><strong style='color: #f3efe7;'>Payment Method:</strong> " + (paymentMethod != null ? paymentMethod : "COD") + "</div>"
             + "  <div style='color: #a6a19a;'><strong style='color: #f3efe7;'>Delivery Location:</strong> " + (shippingAddress != null ? shippingAddress : "Shipping Address") + "</div>"
             + "</div>"

             // Items Breakdown Table (If Items exist)
             + (itemsTable.length() > 0 ?
               "<table style='width: 100%; border-collapse: collapse; margin: 20px 0; font-size: 13px;'>"
             + "<thead style='border-bottom: 1px dashed #333; color: #d8c8a8; text-transform: uppercase; font-size: 11px; letter-spacing: 1px;'>"
             + "<tr><th style='text-align: left; padding-bottom: 8px;'>Product</th><th style='text-align: center; padding-bottom: 8px;'>Qty</th><th style='text-align: right; padding-bottom: 8px;'>Unit Price</th><th style='text-align: right; padding-bottom: 8px;'>Subtotal</th></tr>"
             + "</thead>"
             + "<tbody>" + itemsTable.toString() + "</tbody>"
             + "</table>" : "")

             // Total Summary Box
             + "<div style='background: #171717; border: 1px solid #d8c8a8; border-radius: 8px; padding: 16px; margin: 20px 0; text-align: right;'>"
             + "  <span style='font-size: 13px; color: #a6a19a; text-transform: uppercase; letter-spacing: 1px;'>Total Billed Amount:</span>"
             + "  <div style='font-size: 26px; font-weight: bold; color: #d8c8a8; margin-top: 4px;'>₹" + String.format("%.2f", totalAmount) + "</div>"
             + "</div>"

             // Track Order Button
             + "<div style='text-align: center; margin-top: 25px;'>"
             + "  <a href='https://shopsphere-online.azurewebsites.net/order-detail?orderId=" + orderId + "' style='display: inline-block; background: #d8c8a8; color: #080808; text-decoration: none; padding: 13px 28px; font-size: 12px; font-weight: bold; letter-spacing: 1.5px; text-transform: uppercase; border-radius: 6px;'>Track Order Details →</a>"
             + "</div>"

             // Footer
             + "<div style='border-top: 1px solid #222; margin-top: 30px; padding-top: 15px; text-align: center; font-size: 11px; color: #666;'>"
             + "  <p style='margin: 0;'>© 2026 ShopSphere E-Commerce Ltd. All rights reserved.</p>"
             + "  <p style='margin: 4px 0 0;'>Need help? Contact support at support@shopsphere-online.azurewebsites.net</p>"
             + "</div>"

             + "</div></body></html>";
    }

    private static String buildOrderStatusUpdateHtml(String userName, int orderId, String status, double totalAmount) {
        String badgeColor = "#d8c8a8";
        String statusIcon = "📦";
        String statusDesc = "Your order status has been updated.";

        if ("SHIPPED".equalsIgnoreCase(status)) {
            badgeColor = "#4fc3f7";
            statusIcon = "🚚";
            statusDesc = "Good news! Your order has been shipped and is on its way to your address.";
        } else if ("DELIVERED".equalsIgnoreCase(status)) {
            badgeColor = "#81c784";
            statusIcon = "🎉";
            statusDesc = "Your package has been successfully delivered! Thank you for shopping with ShopSphere.";
        } else if ("PROCESSING".equalsIgnoreCase(status)) {
            badgeColor = "#ffb74d";
            statusIcon = "⚙️";
            statusDesc = "Our team is actively preparing your order items for dispatch.";
        } else if ("CANCELLED".equalsIgnoreCase(status)) {
            badgeColor = "#e57373";
            statusIcon = "❌";
            statusDesc = "Your order status has been updated to CANCELLED. If you have questions, please contact support.";
        }

        return "<html><body style='font-family: Arial, sans-serif; background: #080808; color: #f3efe7; padding: 20px; margin: 0;'>"
             + "<div style='max-width: 550px; margin: 0 auto; background: #121212; border: 1px solid #222; border-radius: 12px; padding: 30px; box-shadow: 0 10px 30px rgba(0,0,0,0.5);'>"
             + "<h1 style='font-family: serif; letter-spacing: 3px; font-size: 22px; color: #d8c8a8; margin: 0 0 15px; text-transform: uppercase;'>SHOPSPHERE</h1>"
             + "<div style='background: #181818; border: 1px solid " + badgeColor + "; border-radius: 8px; padding: 18px; margin-bottom: 20px; text-align: center;'>"
             + "  <div style='font-size: 36px; margin-bottom: 6px;'>" + statusIcon + "</div>"
             + "  <span style='font-size: 11px; text-transform: uppercase; letter-spacing: 2px; color: #a6a19a;'>Order Delivery Status Update</span>"
             + "  <h2 style='color: " + badgeColor + "; margin: 6px 0 0; text-transform: uppercase; font-size: 20px;'>ORDER #" + orderId + " IS " + status + "</h2>"
             + "</div>"
             + "<p style='font-size: 14px; color: #f3efe7;'>Hi <strong>" + (userName != null ? userName : "Customer") + "</strong>,</p>"
             + "<p style='color: #a6a19a; font-size: 13px; line-height: 1.6;'>" + statusDesc + "</p>"
             + "<div style='background: #151515; border: 1px solid #282828; padding: 14px; border-radius: 6px; margin: 20px 0; font-size: 13px; color: #a6a19a; display: flex; justify-content: space-between;'>"
             + "  <span>Order Billed Amount:</span>"
             + "  <strong style='color: #d8c8a8;'>₹" + String.format("%.2f", totalAmount) + "</strong>"
             + "</div>"
             + "<div style='text-align: center; margin-top: 25px;'>"
             + "  <a href='https://shopsphere-online.azurewebsites.net/order-detail?orderId=" + orderId + "' style='display: inline-block; background: #d8c8a8; color: #080808; text-decoration: none; padding: 12px 24px; font-size: 11px; font-weight: bold; letter-spacing: 1px; text-transform: uppercase; border-radius: 4px;'>View Live Order Status →</a>"
             + "</div>"
             + "<p style='color: #666; font-size: 11px; text-align: center; margin-top: 30px;'>© 2026 ShopSphere E-Commerce. All rights reserved.</p>"
             + "</div></body></html>";
    }

    private static String buildOtpHtml(String recipientEmail, String otp) {
        return "<html><body style='font-family: Arial, sans-serif; background: #080808; color: #f3efe7; padding: 30px; margin: 0;'>"
             + "<div style='max-width: 500px; margin: 0 auto; background: #121212; border: 1px solid #222; padding: 30px; border-radius: 12px; text-align: center;'>"
             + "<h2 style='font-family: serif; letter-spacing: 2px; text-transform: uppercase; color: #d8c8a8; margin-top: 0;'>SHOPSPHERE</h2>"
             + "<h3>Password Reset Code</h3>"
             + "<p style='color: #a6a19a;'>Use the verification code below to reset your ShopSphere account password:</p>"
             + "<div style='font-size: 32px; font-weight: bold; letter-spacing: 6px; color: #d8c8a8; background: #171717; padding: 15px; margin: 20px 0; border: 1px dashed #444; border-radius: 8px;'>" + otp + "</div>"
             + "<p style='color: #706b64; font-size: 11px;'>If you did not request a password reset, please ignore this email.</p>"
             + "</div></body></html>";
    }

    private static String buildWelcomeHtml(String userName) {
        return "<html><body style='font-family: Arial, sans-serif; background: #080808; color: #f3efe7; padding: 30px; margin: 0;'>"
             + "<div style='max-width: 550px; margin: 0 auto; background: #121212; border: 1px solid #222; border-radius: 12px; padding: 30px; box-shadow: 0 10px 30px rgba(0,0,0,0.5); text-align: center;'>"
             + "<h1 style='font-family: serif; letter-spacing: 3px; font-size: 24px; color: #d8c8a8; margin: 0 0 15px; text-transform: uppercase;'>SHOPSPHERE</h1>"
             + "<div style='font-size: 40px; margin: 15px 0;'>✨</div>"
             + "<h2 style='color: #f3efe7; margin: 0 0 10px;'>Welcome to the World of Luxury Shopping</h2>"
             + "<p style='font-size: 14px; color: #a6a19a; line-height: 1.6; margin-bottom: 25px;'>Hello <strong>" + (userName != null && !userName.isBlank() ? userName : "Valued Member") + "</strong>, thank you for joining ShopSphere. Explore our curated collections of premium audio, footwear, watches, and designer lifestyle products.</p>"
             + "<a href='https://shopsphere-online.azurewebsites.net/products' style='display: inline-block; background: #d8c8a8; color: #080808; text-decoration: none; padding: 14px 28px; font-size: 12px; font-weight: bold; letter-spacing: 1px; text-transform: uppercase; border-radius: 4px;'>Start Exploring →</a>"
             + "<p style='color: #666; font-size: 11px; text-align: center; margin-top: 30px;'>© 2026 ShopSphere E-Commerce. All rights reserved.</p>"
             + "</div></body></html>";
    }
}
