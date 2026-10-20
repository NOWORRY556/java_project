<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Orders Management — ShopSphere Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
<div class="admin-shell">
    <aside class="sidebar">
        <a class="brand" href="${pageContext.request.contextPath}/admin">Shop<span>Sphere</span></a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin">▦ Dashboard</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/products">◈ Products</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/categories">◇ Categories</a>
        <a class="side-link active" href="${pageContext.request.contextPath}/admin/orders">▤ Orders</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/coupons">% Coupons</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/users">◉ Customers</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/reviews">★ Reviews</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/analytics">📊 Analytics</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/export">⇩ Export orders</a>
        <a class="side-link" href="${pageContext.request.contextPath}/products">↗ Store</a>
    </aside>

    <main class="admin-main">
        <div class="section-head">
            <div>
                <span class="eyebrow">OPERATIONS</span>
                <h1>Orders Management</h1>
                <p class="muted">Review purchases, manage return requests, and update fulfilment statuses.</p>
            </div>
            <a href="${pageContext.request.contextPath}/admin/export" class="btn secondary">⇩ Export CSV</a>
        </div>

        <div class="toolbar" style="display:flex; justify-content:space-between; align-items:center; margin-bottom:16px;">
            <h2>Recent Orders & Returns</h2>
            <input class="search" data-search="#orders" placeholder="Search orders, customers, or emails…" style="padding:10px; border-radius:6px; background:#181818; border:1px solid #333; color:#fff; min-width:280px;">
        </div>

        <div id="orders" class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Order #</th>
                    <th>Customer</th>
                    <th>Total</th>
                    <th>Payment</th>
                    <th>Status</th>
                    <th>Created</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <% 
                    List<Map<String,Object>> orders = (List<Map<String,Object>>) request.getAttribute("orders");
                    if (orders != null && !orders.isEmpty()) {
                        for (Map<String,Object> o : orders) {
                            String ordStatus = (String) o.get("orderStatus");
                            String chipClass = "info";
                            if ("DELIVERED".equals(ordStatus)) chipClass = "success";
                            else if ("CANCELLED".equals(ordStatus) || "RETURNED".equals(ordStatus)) chipClass = "danger";
                            else if ("RETURN_REQUESTED".equals(ordStatus)) chipClass = "warning";
                %>
                <tr data-search-item>
                    <td>
                        <strong>#<%= o.get("orderId") %></strong>
                        <br>
                        <a href="${pageContext.request.contextPath}/order-detail?orderId=<%= o.get("orderId") %>" target="_blank" style="font-size:11px; color:#d8c8a8;">View items ↗</a>
                    </td>
                    <td>
                        <strong><%= o.get("name") %></strong>
                        <div class="muted" style="font-size:12px;"><%= o.get("email") %></div>
                    </td>
                    <td><strong>₹<%= o.get("total") %></strong></td>
                    <td>
                        <span class="chip"><%= o.get("paymentMethod") %></span>
                        <div class="muted" style="font-size:11px; margin-top:2px;"><%= o.get("paymentStatus") %></div>
                    </td>
                    <td><span class="chip <%= chipClass %>"><%= ordStatus %></span></td>
                    <td class="muted" style="font-size:12px;"><%= o.get("createdAt") %></td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/admin/orders" class="inline" style="display:flex; gap:6px; align-items:center;">
                            <input type="hidden" name="_csrf" value="${csrfToken}" />
                            <input type="hidden" name="orderId" value="<%= o.get("orderId") %>">
                            <select name="status" style="padding:6px; background:#181818; border:1px solid #333; color:#fff; border-radius:4px; font-size:12px;">
                                <% 
                                    String[] allStatuses = {"PLACED", "PROCESSING", "SHIPPED", "DELIVERED", "RETURN_REQUESTED", "RETURNED", "REFUNDED", "CANCELLED"};
                                    for (String s : allStatuses) { 
                                %>
                                <option value="<%= s %>" <%= s.equals(ordStatus) ? "selected" : "" %>><%= s %></option>
                                <% } %>
                            </select>
                            <button type="submit" class="btn secondary" style="padding:6px 12px; font-size:12px;">Update</button>
                        </form>
                    </td>
                </tr>
                <% 
                        } 
                    } else { 
                %>
                <tr>
                    <td colspan="7" class="empty">No customer orders found.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </main>
</div>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>