<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.shopsphere.model.User" %>
<%@ page import="com.shopsphere.model.Product" %>
<%@ page import="com.shopsphere.dao.ProductDAO" %>
<%@ page import="java.util.List" %>
<%
    User u = (User) session.getAttribute("loggedInUser");
    List<Product> featuredProducts;
    try {
        featuredProducts = new ProductDAO().search(null, 0, "newest", 1, 6);
    } catch (Exception e) {
        featuredProducts = java.util.Collections.emptyList();
    }
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ShopSphere — Modern Shopping, Refined</title>
    <meta name="description" content="ShopSphere — a modern Java e-commerce storefront for curated products.">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
<div class="announcement-bar"><span>COMPLIMENTARY SHIPPING ON ORDERS OVER ₹999</span></div>
<header class="nav-header">
    <div class="container nav-inner">
        <nav class="nav-menu">
            <a class="nav-link active" href="${pageContext.request.contextPath}/">Shop</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/products">Collections</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/products?category=3">Fashion</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/products?category=2">Books</a>
        </nav>
        <a class="brand-logo" href="${pageContext.request.contextPath}/">ShopSphere</a>
        <div>
            <a class="btn ghost" href="${pageContext.request.contextPath}/products">Search</a>
            <% if (u == null) { %>
                <a class="btn ghost" href="${pageContext.request.contextPath}/login">Account</a>
            <% } else if ("ADMIN".equalsIgnoreCase(u.getRole())) { %>
                <a class="btn ghost" href="${pageContext.request.contextPath}/admin/dashboard">Account</a>
            <% } else { %>
                <a class="btn ghost" href="${pageContext.request.contextPath}/profile">Account</a>
            <% } %>
            <a class="btn ghost" href="${pageContext.request.contextPath}/cart">Cart</a>
        </div>
    </div>
</header>
<section class="hero-wrapper">
    <div class="hero-grid">
        <div>
            <span class="badge-tag">THE NEW SHOPSPHERE</span>
            <h1 class="hero-title">ShopSphere</h1>
            <p class="hero-subtitle">Modern shopping. Refined for everyday. Discover a curated collection of products across technology, books and fashion.</p>
            <div class="hero-buttons">
                <a class="btn" href="${pageContext.request.contextPath}/products">Explore Collection</a>
                <a class="btn secondary" href="#new-arrivals">View New Arrivals</a>
            </div>
        </div>
    </div>
</section>
<main>
<section class="section container">
    <div class="section-head">
        <div><span class="eyebrow">CURATED COLLECTIONS</span><h2>Find your next favorite.</h2></div>
        <span class="eyebrow">03 CATEGORIES</span>
    </div>
    <div class="curated-grid">
        <a class="curated-card" href="${pageContext.request.contextPath}/products?category=1">
            <img src="https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=1200&q=85" alt="Electronics collection">
            <div class="curated-copy"><h3>Electronics</h3><span>Explore collection</span></div>
        </a>
        <a class="curated-card" href="${pageContext.request.contextPath}/products?category=3">
            <img src="https://images.unsplash.com/photo-1483985988355-763728e1935b?auto=format&fit=crop&w=1200&q=85" alt="Fashion collection">
            <div class="curated-copy"><h3>Fashion</h3><span>Explore collection</span></div>
        </a>
        <a class="curated-card" href="${pageContext.request.contextPath}/products?category=2">
            <img src="https://images.unsplash.com/photo-1495446815901-a7297e633e8d?auto=format&fit=crop&w=1200&q=85" alt="Books collection">
            <div class="curated-copy"><h3>Books</h3><span>Explore collection</span></div>
        </a>
    </div>
</section>
<section id="new-arrivals" class="products-section container">
    <div class="section-head">
        <div><span class="eyebrow">NEW ARRIVALS</span><h2>Curated products for modern living.</h2></div>
        <a class="btn ghost" href="${pageContext.request.contextPath}/products">View all</a>
    </div>
    <div class="grid">
        <% if (featuredProducts != null && !featuredProducts.isEmpty()) {
            String[] productImages = {
                "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?auto=format&fit=crop&w=900&q=85",
                "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=900&q=85",
                "https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=900&q=85",
                "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=900&q=85",
                "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=900&q=85",
                "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?auto=format&fit=crop&w=900&q=85"
            };
            int i = 0;
            for (Product p : featuredProducts) {
                String image = (p.getImageUrl() != null && !p.getImageUrl().trim().isEmpty()) ? p.getImageUrl() : productImages[i % productImages.length];
        %>
        <article class="product-card">
            <a class="product-image" href="${pageContext.request.contextPath}/product?id=<%= p.getProductId() %>">
                <img src="<%= image %>" alt="<%= p.getName() %>">
                <span class="product-tag"><%= p.getStock() > 0 ? "NEW" : "SOLD OUT" %></span>
            </a>
            <div class="product-body">
                <span class="chip"><%= p.getBrand() != null ? p.getBrand() : "ShopSphere" %></span>
                <a class="product-title" href="${pageContext.request.contextPath}/product?id=<%= p.getProductId() %>"><%= p.getName() %></a>
                <p class="muted"><%= p.getDescription() != null && p.getDescription().length() > 72 ? p.getDescription().substring(0,72) + "…" : (p.getDescription() == null ? "" : p.getDescription()) %></p>
                <div style="display:flex;justify-content:space-between;align-items:center;margin-top:5px;">
                    <span class="price">₹<%= String.format("%.2f", p.getPrice()) %></span>
                    <% if (p.getStock() > 0) { %><span class="chip success">In stock</span><% } else { %><span class="chip danger">Out of stock</span><% } %>
                </div>
                <div class="product-actions">
                    <% if (p.getStock() > 0) { %>
                    <form method="post" action="${pageContext.request.contextPath}/cart">
    <input type="hidden" name="_csrf" value="${csrfToken}" /><input type="hidden" name="productId" value="<%= p.getProductId() %>"><input type="hidden" name="quantity" value="1"><button type="submit">Add to bag</button></form>
                    <% } %>
                    <form method="post" action="${pageContext.request.contextPath}/wishlist">
    <input type="hidden" name="_csrf" value="${csrfToken}" /><input type="hidden" name="productId" value="<%= p.getProductId() %>"><button class="btn ghost" type="submit" title="Add to wishlist">♡</button></form>
                </div>
            </div>
        </article>
        <% i++; } } else { %>
            <div class="card" style="grid-column:1/-1;text-align:center;"><h3>Discover our catalog</h3><p class="muted">Explore the full ShopSphere collection.</p><a class="btn" href="${pageContext.request.contextPath}/products" style="margin-top:18px;">Browse products</a></div>
        <% } %>
    </div>
</section>
<section class="editorial-feature"></section>
    <div class="feature-copy">
        <span class="eyebrow">THE HOME EDIT</span>
        <h2>Designed for everyday living.</h2>
        <p>Explore products selected for modern routines, thoughtful workspaces and everyday moments.</p>
        <a class="btn secondary" href="${pageContext.request.contextPath}/products">Discover collection</a>
    </div>
</section>
</main>
<footer class="main-footer">
    <div class="container">
        <div class="footer-grid">
            <div><a class="brand-logo" href="${pageContext.request.contextPath}/">ShopSphere</a><p class="muted" style="max-width:360px;margin-top:14px;">A refined e-commerce experience built with Java, JSP, Servlets and MySQL.</p></div>
            <div><h4 class="footer-title">Shop</h4><ul class="footer-links"><li><a href="${pageContext.request.contextPath}/products">All products</a></li><li><a href="${pageContext.request.contextPath}/products?category=1">Electronics</a></li><li><a href="${pageContext.request.contextPath}/products?category=2">Books</a></li><li><a href="${pageContext.request.contextPath}/products?category=3">Fashion</a></li></ul></div>
            <div><h4 class="footer-title">Account</h4><ul class="footer-links"><li><a href="${pageContext.request.contextPath}/profile">Profile</a></li><li><a href="${pageContext.request.contextPath}/orders">Orders</a></li><li><a href="${pageContext.request.contextPath}/wishlist">Wishlist</a></li><li><a href="${pageContext.request.contextPath}/cart">Cart</a></li></ul></div>
            <div><h4 class="footer-title">ShopSphere</h4><ul class="footer-links"><li><a href="${pageContext.request.contextPath}/login">Sign in</a></li><li><a href="${pageContext.request.contextPath}/register">Create account</a></li><li><a href="${pageContext.request.contextPath}/syllabus">Advanced Java overview</a></li></ul></div>
        </div>
        <div class="footer-bottom"><span>© 2026 ShopSphere</span><span>Modern shopping, refined for everyday.</span></div>
    </div>
</footer>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>