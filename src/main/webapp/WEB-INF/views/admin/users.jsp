<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List,com.shopsphere.model.User" %>
<% List<User> users = (List<User>) request.getAttribute("users"); %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Customer Directory — ShopSphere Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
<div class="admin-shell">
    <aside class="sidebar">
        <a class="brand" href="${pageContext.request.contextPath}/admin">Shop<span>Sphere</span></a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin">▦ Dashboard</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/products">◈ Products</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/categories">◇ Categories</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/orders">▤ Orders</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/coupons">% Coupons</a>
        <a class="side-link active" href="${pageContext.request.contextPath}/admin/users">◉ Customers</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/reviews">★ Reviews</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/analytics">📊 Analytics</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/export">⇩ Export orders</a>
        <a class="side-link" href="${pageContext.request.contextPath}/products">↗ Store</a>
    </aside>
    <main class="admin-main">
        <div class="section-head">
            <div>
                <span class="eyebrow">ACCOUNTS</span>
                <h1>Customer Directory</h1>
                <p class="muted">Manage user roles, accounts, and access status.</p>
            </div>
        </div>
        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Mobile</th>
                    <th>Role</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <% if (users != null && !users.isEmpty()) { for (User u : users) { %>
                <tr>
                    <td><%= u.getUserId() %></td>
                    <td><strong><%= u.getName() %></strong></td>
                    <td><%= u.getEmail() %></td>
                    <td class="muted"><%= u.getMobile() != null ? u.getMobile() : "-" %></td>
                    <td><span class="chip"><%= u.getRole() %></span></td>
                    <td><span class="chip <%= u.isStatus() ? "success" : "danger" %>"><%= u.isStatus() ? "Active" : "Disabled" %></span></td>
                    <td>
                        <div class="product-actions">
                            <form method="post" action="${pageContext.request.contextPath}/admin/users" class="inline">
    <input type="hidden" name="_csrf" value="${csrfToken}" /><input type="hidden" name="action" value="toggle">
                                <input type="hidden" name="userId" value="<%= u.getUserId() %>">
                                <input type="hidden" name="status" value="<%= !u.isStatus() %>">
                                <button class="btn secondary" type="submit"><%= u.isStatus() ? "Disable" : "Enable" %></button>
                            </form>
                            <% if ("CUSTOMER".equals(u.getRole())) { %>
                            <form method="post" action="${pageContext.request.contextPath}/admin/users" class="inline">
    <input type="hidden" name="_csrf" value="${csrfToken}" /><input type="hidden" name="action" value="role">
                                <input type="hidden" name="userId" value="<%= u.getUserId() %>">
                                <input type="hidden" name="role" value="ADMIN">
                                <button class="btn ghost" type="submit">Promote to Admin</button>
                            </form>
                            <% } %>
                        </div>
                    </td>
                </tr>
                <% } } else { %>
                <tr><td colspan="7" class="empty">No registered users found.</td></tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </main>
</div>
</body>
</html>
