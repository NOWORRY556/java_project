<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Admin Console — ShopSphere</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
<div class="admin-shell">
    <aside class="sidebar">
        <a class="brand" href="${pageContext.request.contextPath}/">Shop<span>Sphere</span></a>
        <div style="color:#94a3b8;margin-bottom:1rem">ADMIN CONSOLE</div>
        <a class="side-link active" href="${pageContext.request.contextPath}/admin">▦ Dashboard</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/products">◈ Products</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/categories">◇ Categories</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/orders">▤ Orders</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/coupons">% Coupons</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/users">◉ Customers</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/reviews">★ Reviews</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/analytics">📊 Analytics</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/export">⇩ Export orders</a>
        <a class="side-link" href="${pageContext.request.contextPath}/products">↗ View store</a>
        <a class="side-link" href="${pageContext.request.contextPath}/logout">⇥ Logout</a>
    </aside>
    <main class="admin-main">
        <div class="section-head">
            <div>
                <span class="eyebrow">ADMIN</span>
                <h1>Store Overview</h1>
                <p class="muted">Manage catalog, orders, customers and store analytics.</p>
            </div>
        </div>
        <div class="stats">
            <div class="stat"><div class="value">Store</div><div class="label">Customer Storefront</div></div>
            <div class="stat"><div class="value">Catalog</div><div class="label">Products & Categories</div></div>
            <div class="stat"><div class="value">Orders</div><div class="label">Fulfillment & Status</div></div>
            <div class="stat"><div class="value">Analytics</div><div class="label">Revenue Metrics</div></div>
        </div>
        <div class="grid" style="grid-template-columns: repeat(3, 1fr); gap: 16px;">
            <a class="card" href="${pageContext.request.contextPath}/admin/products">
                <h3>Product Management</h3>
                <p class="muted">Create, edit, search and disable catalog products.</p>
                <span class="btn" style="margin-top:12px;">Open products →</span>
            </a>
            <a class="card" href="${pageContext.request.contextPath}/admin/orders">
                <h3>Order Fulfillment</h3>
                <p class="muted">Review purchases and update order delivery status.</p>
                <span class="btn secondary" style="margin-top:12px;">Open orders →</span>
            </a>
            <a class="card" href="${pageContext.request.contextPath}/admin/categories">
                <h3>Category Management</h3>
                <p class="muted">Organize catalog items into custom categories.</p>
                <span class="btn secondary" style="margin-top:12px;">Open categories →</span>
            </a>
            <a class="card" href="${pageContext.request.contextPath}/admin/coupons">
                <h3>Coupon Management</h3>
                <p class="muted">Create, activate and disable discount codes.</p>
                <span class="btn ghost" style="margin-top:12px;">Open coupons →</span>
            </a>
            <a class="card" href="${pageContext.request.contextPath}/admin/users">
                <h3>Customer Directory</h3>
                <p class="muted">View customer profiles and manage user accounts.</p>
                <span class="btn ghost" style="margin-top:12px;">Open customers →</span>
            </a>
            <a class="card" href="${pageContext.request.contextPath}/admin/analytics">
                <h3>Store Analytics</h3>
                <p class="muted">Track daily revenue trends and order totals.</p>
                <span class="btn ghost" style="margin-top:12px;">View analytics →</span>
            </a>
        </div>
    </main>
</div>
</body>
</html>