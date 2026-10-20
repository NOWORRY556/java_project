<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Coupons — ShopSphere Admin</title>
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
        <a class="side-link active" href="${pageContext.request.contextPath}/admin/coupons">% Coupons</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/users">◉ Customers</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/reviews">★ Reviews</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/analytics">📊 Analytics</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/export">⇩ Export orders</a>
        <a class="side-link" href="${pageContext.request.contextPath}/products">↗ Store</a>
    </aside>
    <main class="admin-main">
        <div class="section-head">
            <div>
                <span class="eyebrow">DISCOUNTS</span>
                <h1>Coupons & Promotions</h1>
                <p class="muted">Create controlled discounts and enable or disable promotional offers.</p>
            </div>
        </div>
        
        <div class="grid" style="grid-template-columns: 1fr 2fr; gap: 24px;">
            <section class="card">
                <h3>Create Coupon</h3>
                <form method="post" action="${pageContext.request.contextPath}/admin/coupons" style="margin-top:16px;">
    <input type="hidden" name="_csrf" value="${csrfToken}" /><div class="field"><label>Code</label><input name="code" placeholder="WELCOME10" required maxlength="50"></div>
                    <div class="field"><label>Type</label><select name="discountType"><option value="PERCENT">Percentage (%)</option><option value="FIXED">Fixed amount (₹)</option></select></div>
                    <div class="field"><label>Discount value</label><input type="number" name="discountValue" min="0.01" step="0.01" required></div>
                    <div class="field"><label>Minimum order</label><input type="number" name="minimumOrder" min="0" step="0.01" value="0" required></div>
                    <div class="field"><label>Maximum discount (optional)</label><input type="number" name="maximumDiscount" min="0.01" step="0.01"></div>
                    <div class="field"><label>Expiry date (optional)</label><input type="date" name="expiryDate"></div>
                    <button type="submit" style="width:100%; margin-top:8px;">Create coupon</button>
                </form>
            </section>
            
            <section class="card">
                <h3>Active & Expired Coupons</h3>
                <div class="table-wrap" style="margin-top:16px;">
                    <table>
                        <thead>
                        <tr>
                            <th>Code</th>
                            <th>Rule</th>
                            <th>Minimum</th>
                            <th>Expiry</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="c" items="${coupons}">
                            <tr>
                                <td><strong>${c.code}</strong></td>
                                <td>${c.discountType} ${c.discountValue}</td>
                                <td>₹${c.minimumOrder}</td>
                                <td>${empty c.expiryDate ? 'No expiry' : c.expiryDate}</td>
                                <td><span class="chip ${c.status ? 'success' : 'danger'}">${c.status ? 'Active' : 'Disabled'}</span></td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/coupons">
    <input type="hidden" name="_csrf" value="${csrfToken}" /><input type="hidden" name="action" value="toggle">
                                        <input type="hidden" name="id" value="${c.couponId}">
                                        <input type="hidden" name="status" value="${!c.status}">
                                        <button class="btn secondary" type="submit">${c.status ? 'Disable' : 'Enable'}</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </section>
        </div>
    </main>
</div>
</body>
</html>