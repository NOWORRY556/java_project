<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List,com.shopsphere.model.Address" %>
<% 
    List<Address> addresses = (List<Address>) request.getAttribute("addresses"); 
    String msg = (String) session.getAttribute("msg");
    if (msg != null) session.removeAttribute("msg");
    String error = (String) session.getAttribute("error");
    if (error != null) session.removeAttribute("error");
    String ctx = request.getContextPath();
%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Saved Addresses — ShopSphere</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/css/app.css">
    <style>
        .addr-card { background:#121212; border:1px solid rgba(255,255,255,0.12); border-radius:10px; padding:18px; display:flex; justify-content:space-between; align-items:center; transition: all 0.2s ease; }
        .addr-card:hover { border-color:var(--accent,#d8c8a8); box-shadow:0 6px 20px rgba(0,0,0,0.4); }
    </style>
</head>
<body>
<header class="nav-header">
    <div class="container nav-inner">
        <nav class="nav-menu">
            <a class="nav-link" href="<%= ctx %>/">Shop</a>
            <a class="nav-link" href="<%= ctx %>/products">Collections</a>
        </nav>
        <a class="brand-logo" href="<%= ctx %>/">ShopSphere</a>
        <div>
            <a class="btn ghost" href="<%= ctx %>/profile">Profile</a>
            <a class="btn ghost" href="<%= ctx %>/orders">Orders</a>
        </div>
    </div>
</header>
<main class="container section">
    <div class="section-head">
        <div>
            <span class="eyebrow">ACCOUNT</span>
            <h2>Saved addresses</h2>
            <p class="muted">Manage your shipping and billing locations.</p>
        </div>
        <a class="btn ghost" href="<%= ctx %>/profile">← Back to profile</a>
    </div>

    <% if (msg != null) { %>
        <div style="background: rgba(46, 125, 50, 0.15); border: 1px solid #2e7d32; color: #81c784; padding: 14px 18px; border-radius: 8px; margin-bottom: 24px; font-size: 14px; display: flex; align-items: center; gap: 10px;">
            <span style="font-weight: bold;">✓</span> <%= msg %>
        </div>
    <% } %>

    <% if (error != null) { %>
        <div style="background: rgba(211, 47, 47, 0.15); border: 1px solid #d32f2f; color: #ef5350; padding: 14px 18px; border-radius: 8px; margin-bottom: 24px; font-size: 14px; display: flex; align-items: center; gap: 10px;">
            <span style="font-weight: bold;">⚠</span> <%= error %>
        </div>
    <% } %>
    
    <div class="grid" style="grid-template-columns: 1fr 2fr; gap: 24px;">
        <section class="card">
            <h3 style="font-size:15px; font-weight:600; text-transform:uppercase; letter-spacing:0.06em; color:var(--accent,#d8c8a8);">Add shipping address</h3>
            <form method="post" action="<%= ctx %>/addresses" style="margin-top:16px;">
    <input type="hidden" name="_csrf" value="${csrfToken}" />
                <div class="field"><label>Address Line</label><input name="addressLine" maxlength="255" required placeholder="Street address, apartment, suite"></div>
                <div class="field"><label>City</label><input name="city" maxlength="100" required placeholder="City"></div>
                <div class="field"><label>State</label><input name="state" maxlength="100" required placeholder="State"></div>
                <div class="field"><label>Pincode</label><input name="pincode" inputmode="numeric" pattern="[0-9]{6}" maxlength="6" required placeholder="6-digit PIN code"></div>
                <button type="submit" class="btn" style="width:100%; margin-top:12px;">Save new address</button>
            </form>
        </section>
        
        <section class="card">
            <h3 style="font-size:15px; font-weight:600; text-transform:uppercase; letter-spacing:0.06em; color:var(--accent,#d8c8a8);">Your saved locations</h3>
            <div style="margin-top:16px; display:flex; flex-direction:column; gap:12px;">
                <% if (addresses == null || addresses.isEmpty()) { %>
                <div class="card empty"><p class="muted">No saved addresses yet. Add a location to speed up checkout.</p></div>
                <% } else { for (Address a : addresses) { %>
                <div class="addr-card">
                    <div>
                        <strong style="font-size:15px; color:#ffffff;"><%= a.getAddressLine() %></strong>
                        <div class="muted" style="margin-top:4px; font-size:13px;"><%= a.getCity() %>, <%= a.getState() %> — <strong><%= a.getPincode() %></strong></div>
                    </div>
                    <form method="post" action="<%= ctx %>/addresses" onsubmit="return confirm('Are you sure you want to delete this saved address?');">
    <input type="hidden" name="_csrf" value="${csrfToken}" />
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="addressId" value="<%= a.getAddressId() %>">
                        <button class="btn danger" type="submit" style="padding:6px 14px; font-size:12px; cursor:pointer;">Delete</button>
                    </form>
                </div>
                <% } } %>
            </div>
        </section>
    </div>
</main>
<footer class="main-footer">
    <div class="container">
        <div class="footer-bottom">
            <span>© 2026 ShopSphere</span>
            <a href="<%= ctx %>/products">Continue shopping</a>
        </div>
    </div>
</footer>
</body>
</html>
