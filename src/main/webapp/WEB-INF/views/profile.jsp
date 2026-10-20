<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.shopsphere.model.User" %>
<% User u = (User) request.getAttribute("profile"); %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>My Account — ShopSphere</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
<header class="nav-header">
    <div class="container nav-inner">
        <nav class="nav-menu">
            <a class="nav-link" href="${pageContext.request.contextPath}/">Shop</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/products">Collections</a>
        </nav>
        <a class="brand-logo" href="${pageContext.request.contextPath}/">ShopSphere</a>
        <div>
            <a class="btn ghost" href="${pageContext.request.contextPath}/wishlist">Wishlist</a>
            <a class="btn ghost" href="${pageContext.request.contextPath}/cart">Cart</a>
            <a class="btn ghost" href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </div>
</header>
<main class="container section">
    <div class="section-head">
        <div>
            <span class="eyebrow">ACCOUNT</span>
            <h2>User Profile</h2>
            <p class="muted">Manage your personal info, addresses, and account security.</p>
        </div>
    </div>
    
    <div class="grid" style="grid-template-columns: 2fr 1fr; gap: 24px;">
        <div class="card">
            <h3>Personal Information</h3>
            <% if (request.getAttribute("error") != null) { %><div class="chip danger" style="margin-top:12px;"><%= request.getAttribute("error") %></div><% } %>
            <% if (request.getAttribute("success") != null) { %><div class="chip success" style="margin-top:12px;"><%= request.getAttribute("success") %></div><% } %>
            <form method="post" style="margin-top:16px;">
    <input type="hidden" name="_csrf" value="${csrfToken}" /><div class="field"><label>Full Name</label><input name="name" value="<%= u != null ? u.getName() : "" %>" required></div>
                <div class="field"><label>Email Address</label><input value="<%= u != null ? u.getEmail() : "" %>" disabled></div>
                <div class="field"><label>Mobile Number</label><input name="mobile" value="<%= u != null && u.getMobile() != null ? u.getMobile() : "" %>"></div>
                <button type="submit" style="width:100%; margin-top:8px;">Save changes</button>
            </form>
        </div>
        
        <div class="card" style="display:flex; flex-direction:column; gap:12px;">
            <h3>Quick Actions</h3>
            <a class="btn secondary" href="${pageContext.request.contextPath}/orders" style="width:100%; text-align:center;">My Orders</a>
            <a class="btn secondary" href="${pageContext.request.contextPath}/wishlist" style="width:100%; text-align:center;">Saved Wishlist</a>
            <a class="btn secondary" href="${pageContext.request.contextPath}/addresses" style="width:100%; text-align:center;">Shipping Addresses</a>
            <a class="btn ghost" href="${pageContext.request.contextPath}/password" style="width:100%; text-align:center;">Change Password</a>
            <hr style="border:0; border-top:1px solid var(--line); margin:4px 0;">
            <a class="btn danger" href="${pageContext.request.contextPath}/logout" style="width:100%; text-align:center;">Sign out</a>
        </div>
    </div>
</main>
<footer class="main-footer">
    <div class="container">
        <div class="footer-bottom">
            <span>© 2026 ShopSphere</span>
            <a href="${pageContext.request.contextPath}/products">Continue shopping</a>
        </div>
    </div>
</footer>
</body>
</html>