<%@ page contentType="text/html;charset=UTF-8" %>
<% String ctx = request.getContextPath(); %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Order Placed — ShopSphere</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/css/app.css">
    <style>
        .success-wrapper {
            max-width: 600px;
            margin: 40px auto 80px;
            text-align: center;
        }
        .success-icon-badge {
            width: 64px;
            height: 64px;
            margin: 0 auto 20px;
            border-radius: 50%;
            background: rgba(155, 184, 159, 0.12);
            border: 1px solid rgba(155, 184, 159, 0.35);
            color: var(--success, #9bb89f);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 28px;
        }
        .receipt-card {
            background: var(--surface, #121212);
            border: 1px solid var(--line, rgba(255,255,255,.12));
            padding: 28px;
            margin: 28px 0;
            text-align: left;
            display: flex;
            flex-direction: column;
            gap: 16px;
        }
        .receipt-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 13px;
        }
    </style>
</head>
<body>
<div class="announcement-bar"><span>CURATED PRODUCTS FOR MODERN LIVING</span></div>
<header class="nav-header">
    <div class="container nav-inner">
        <nav class="nav-menu">
            <a class="nav-link" href="<%= ctx %>/">Shop</a>
            <a class="nav-link" href="<%= ctx %>/products">Collections</a>
        </nav>
        <a class="brand-logo" href="<%= ctx %>/">ShopSphere</a>
        <div>
            <a class="btn ghost" href="<%= ctx %>/orders">My Orders</a>
        </div>
    </div>
</header>

<main class="container section">
    <div class="success-wrapper">
        <div class="success-icon-badge">✓</div>
        <span class="eyebrow" style="color:var(--accent);">THANK YOU FOR YOUR ORDER</span>
        <h1 class="section-title" style="margin-top:8px;">Order #${orderId} Confirmed.</h1>
        <p class="muted" style="margin-top:12px; font-size:14px;">Your purchase has been recorded. An order confirmation receipt has been dispatched to your email.</p>

        <div class="receipt-card">
            <div class="receipt-row">
                <span class="muted">Order Reference</span>
                <strong style="color:var(--text); font-family:var(--sans);">#${orderId}</strong>
            </div>
            <div class="receipt-row">
                <span class="muted">Order Status</span>
                <span class="chip success">CONFIRMED</span>
            </div>
            <div class="receipt-row">
                <span class="muted">Payment Method</span>
                <span style="color:var(--text); font-weight:600;">Cash on Delivery (COD)</span>
            </div>
            <div class="receipt-row" style="border-top:1px solid var(--line); padding-top:14px; margin-top:4px;">
                <span class="muted">Delivery Status</span>
                <span class="chip">PREPARING FOR DISPATCH</span>
            </div>
        </div>

        <div style="display:flex; gap:12px; justify-content:center; flex-wrap:wrap;">
            <a class="btn" href="<%= ctx %>/order-detail?orderId=${orderId}">View Order Details</a>
            <a class="btn ghost" href="<%= ctx %>/products">Continue Shopping</a>
        </div>
    </div>
</main>

<footer class="main-footer">
    <div class="container">
        <div class="footer-bottom">
            <span>© 2026 ShopSphere</span>
            <span>Refined shopping for everyday life.</span>
        </div>
    </div>
</footer>
</body>
</html>