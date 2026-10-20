<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List,java.util.Map,com.shopsphere.model.User" %>
<%
    Map<String, Object> order = (Map<String, Object>) request.getAttribute("order");
    List<Map<String, Object>> items = (List<Map<String, Object>>) request.getAttribute("items");
    User user = (User) request.getAttribute("user");
    String ctx = request.getContextPath();
%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Tax Invoice — Order #<%= order != null ? order.get("orderId") : "" %></title>
    <style>
        :root {
            --bg: #080808;
            --card: #121212;
            --accent: #d8c8a8;
            --text: #f3efe7;
            --muted: #a6a19a;
            --border: rgba(255,255,255,0.12);
        }
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { background: var(--bg); color: var(--text); font-family: system-ui, -apple-system, sans-serif; padding: 40px 20px; min-height: 100vh; }
        .invoice-box { max-width: 800px; margin: 0 auto; background: var(--card); border: 1px solid var(--border); border-radius: 12px; padding: 40px; box-shadow: 0 12px 40px rgba(0,0,0,0.6); position: relative; }
        
        .header { display: flex; justify-content: space-between; align-items: flex-start; border-bottom: 2px solid var(--accent); padding-bottom: 20px; margin-bottom: 30px; }
        .brand { font-family: serif; font-size: 28px; font-weight: bold; color: var(--accent); letter-spacing: 3px; text-transform: uppercase; text-decoration: none; }
        .invoice-title { text-align: right; }
        .invoice-title h1 { font-size: 22px; color: var(--text); text-transform: uppercase; letter-spacing: 2px; }
        .invoice-title p { font-size: 13px; color: var(--muted); margin-top: 4px; }

        .meta-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-bottom: 30px; font-size: 13px; }
        .meta-card { background: #171717; border: 1px solid rgba(255,255,255,0.08); border-radius: 8px; padding: 18px; }
        .meta-card h3 { font-size: 11px; text-transform: uppercase; letter-spacing: 1.5px; color: var(--accent); margin-bottom: 10px; }

        table { width: 100%; border-collapse: collapse; margin-bottom: 30px; font-size: 13px; }
        th { background: #181818; border-bottom: 1px solid var(--accent); color: var(--accent); text-align: left; padding: 12px 14px; text-transform: uppercase; font-size: 11px; letter-spacing: 1px; }
        td { border-bottom: 1px solid rgba(255,255,255,0.08); padding: 14px; color: var(--text); }

        .total-box { display: flex; justify-content: flex-end; margin-bottom: 30px; }
        .total-table { width: 280px; font-size: 13px; }
        .total-table div { display: flex; justify-content: space-between; padding: 6px 0; color: var(--muted); }
        .total-table div.grand-total { border-top: 2px solid var(--accent); padding-top: 10px; margin-top: 6px; font-size: 18px; font-weight: bold; color: var(--accent); }

        .stamp-seal { border: 2px dashed var(--accent); color: var(--accent); padding: 10px 18px; border-radius: 6px; display: inline-block; font-size: 11px; font-weight: bold; letter-spacing: 2px; text-transform: uppercase; opacity: 0.85; margin-top: 10px; }

        .print-actions { text-align: center; margin-bottom: 24px; }
        .btn-print { background: var(--accent); color: #080808; border: none; padding: 12px 28px; font-size: 13px; font-weight: bold; letter-spacing: 1px; text-transform: uppercase; border-radius: 6px; cursor: pointer; transition: transform 0.2s; }
        .btn-print:hover { transform: translateY(-2px); }

        @media print {
            body { background: #ffffff !important; color: #000000 !important; padding: 0 !important; }
            .invoice-box { background: #ffffff !important; color: #000000 !important; border: none !important; box-shadow: none !important; padding: 0 !important; width: 100% !important; max-width: 100% !important; }
            .print-actions { display: none !important; }
            .brand, .invoice-title h1, .meta-card h3, th, .total-table div.grand-total, .stamp-seal { color: #000000 !important; }
            .header, th, .total-table div.grand-total { border-color: #000000 !important; }
            .meta-card { background: #f8f9fa !important; border-color: #ddd !important; }
            td { border-color: #eee !important; color: #000000 !important; }
            .meta-card p, .invoice-title p, .total-table div { color: #444444 !important; }
        }
    </style>
</head>
<body>

<div class="print-actions">
    <button onclick="window.print()" class="btn-print">🖨 Print / Save as PDF Invoice</button>
</div>

<div class="invoice-box">
    <div class="header">
        <div>
            <a class="brand" href="<%= ctx %>/">ShopSphere</a>
            <p style="font-size:12px; color:var(--muted); margin-top:4px;">Official Tax Invoice & Sales Receipt</p>
        </div>
        <div class="invoice-title">
            <h1>TAX INVOICE</h1>
            <p>Invoice #: <strong>INV-2026-<%= order != null ? order.get("orderId") : "" %></strong></p>
            <p>Date: <%= order != null ? order.get("createdAt") : "" %></p>
        </div>
    </div>

    <div class="meta-grid">
        <div class="meta-card">
            <h3>Billed To (Customer)</h3>
            <p style="font-weight:bold; color:var(--text);"><%= user != null ? user.getName() : "Customer" %></p>
            <p><%= user != null ? user.getEmail() : "" %></p>
            <p><%= user != null && user.getMobile() != null ? user.getMobile() : "" %></p>
        </div>
        <div class="meta-card">
            <h3>Shipping Location & Payment</h3>
            <p style="font-weight:bold; color:var(--text);"><%= order != null ? order.get("shippingAddress") : "Saved Address" %></p>
            <p style="margin-top:6px;">Method: <strong><%= order != null ? order.get("paymentMethod") : "COD" %></strong></p>
            <p>Payment Status: <strong><%= order != null ? order.get("paymentStatus") : "PAID" %></strong></p>
        </div>
    </div>

    <table>
        <thead>
            <tr>
                <th>Item Description</th>
                <th style="text-align:center;">Quantity</th>
                <th style="text-align:right;">Unit Price</th>
                <th style="text-align:right;">Subtotal</th>
            </tr>
        </thead>
        <tbody>
            <% 
                double grandTotal = 0.0;
                if (items != null && !items.isEmpty()) { 
                    for (Map<String, Object> item : items) { 
                        double price = Double.parseDouble(String.valueOf(item.get("price")));
                        int qty = Integer.parseInt(String.valueOf(item.get("quantity")));
                        double sub = price * qty;
                        grandTotal += sub;
            %>
            <tr>
                <td><strong><%= item.get("productName") %></strong></td>
                <td style="text-align:center;"><%= qty %></td>
                <td style="text-align:right;">₹<%= String.format("%.2f", price) %></td>
                <td style="text-align:right; font-weight:bold;">₹<%= String.format("%.2f", sub) %></td>
            </tr>
            <% 
                    } 
                } else { 
            %>
            <tr><td colspan="4" style="text-align:center;" class="muted">No items in invoice.</td></tr>
            <% } %>
        </tbody>
    </table>

    <div class="total-box">
        <div class="total-table">
            <div><span>Items Subtotal:</span><span>₹<%= String.format("%.2f", grandTotal) %></span></div>
            <div><span>Shipping & Delivery:</span><span style="color:#81c784;">FREE</span></div>
            <div><span>GST / Taxes (Included):</span><span>₹0.00</span></div>
            <div class="grand-total"><span>Total Amount:</span><span>₹<%= String.format("%.2f", grandTotal) %></span></div>
        </div>
    </div>

    <div style="display:flex; justify-content:space-between; align-items:flex-end; border-top:1px solid var(--border); padding-top:20px;">
        <div class="stamp-seal">
            ✓ OFFICIALLY VERIFIED TAX RECEIPT
        </div>
        <div style="text-align:right; font-size:11px; color:var(--muted);">
            <p>ShopSphere E-Commerce Ltd.</p>
            <p>Authorized Digital Invoice Signature</p>
        </div>
    </div>
</div>

</body>
</html>
