<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.shopsphere.model.Product,java.util.List,com.shopsphere.model.User" %>
<% 
    User u = (User) session.getAttribute("loggedInUser"); 
    List<Product> products = (List<Product>) request.getAttribute("products"); 
    String query = (String) request.getAttribute("query"); if (query == null) query = ""; 
    String sort = (String) request.getAttribute("sort"); if (sort == null) sort = "newest"; 
    Integer pageObj = (Integer) request.getAttribute("page"); int currentPage = pageObj == null ? 1 : pageObj; 
    Integer currentCat = (Integer) request.getAttribute("category"); if (currentCat == null) currentCat = 0;
    String ctx = request.getContextPath();
%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Shop — ShopSphere</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/css/app.css">
</head>
<body>
<div class="announcement-bar"><span>CURATED PRODUCTS FOR MODERN LIVING</span></div>
<header class="nav-header">
    <div class="container nav-inner">
        <nav class="nav-menu">
            <a class="nav-link" href="<%= ctx %>/">Shop</a>
            <a class="nav-link active" href="<%= ctx %>/products">Collections</a>
            <a class="nav-link" href="<%= ctx %>/products?category=3">Fashion</a>
            <a class="nav-link" href="<%= ctx %>/products?category=2">Books</a>
        </nav>
        <a class="brand-logo" href="<%= ctx %>/">ShopSphere</a>
        <div>
            <a class="btn ghost" href="<%= ctx %>/products">Search</a>
            <a class="btn ghost" href="<%= u == null ? ctx + "/login" : ("ADMIN".equalsIgnoreCase(u.getRole()) ? ctx + "/admin/dashboard" : ctx + "/profile") %>">Account</a>
            <a class="btn ghost" href="<%= ctx %>/cart">Cart</a>
        </div>
    </div>
</header>
<main class="container products-section">
    <div class="section-head">
        <div>
            <span class="eyebrow">SHOP ALL</span>
            <h1 class="section-title">The collection.</h1>
            <p class="muted">Explore the current ShopSphere catalog.</p>
        </div>
    </div>

    <!-- Search & Sort Bar -->
    <div class="filter-bar">
        <form method="get" action="<%= ctx %>/products" class="filter-form">
            <input name="q" value="<%= query %>" placeholder="Search products or brands…">
            <input type="hidden" name="category" value="<%= currentCat %>">
            <select name="sort">
                <option value="newest" <%= "newest".equals(sort) ? "selected" : "" %>>Newest</option>
                <option value="price_asc" <%= "price_asc".equals(sort) ? "selected" : "" %>>Price: Low to High</option>
                <option value="price_desc" <%= "price_desc".equals(sort) ? "selected" : "" %>>Price: High to Low</option>
                <option value="stock" <%= "stock".equals(sort) ? "selected" : "" %>>Availability</option>
            </select>
            <button type="submit">Apply</button>
            <% if (!query.isEmpty() || currentCat > 0) { %>
                <a class="btn ghost" href="<%= ctx %>/products">Clear</a>
            <% } %>
        </form>
    </div>

    <!-- Category Pill Bar -->
    <div class="category-pill-bar">
        <a class="cat-pill <%= currentCat == 0 ? "active" : "" %>" href="<%= ctx %>/products?q=<%= query %>&sort=<%= sort %>">All</a>
        <a class="cat-pill <%= currentCat == 1 ? "active" : "" %>" href="<%= ctx %>/products?category=1&q=<%= query %>&sort=<%= sort %>">Electronics</a>
        <a class="cat-pill <%= currentCat == 2 ? "active" : "" %>" href="<%= ctx %>/products?category=2&q=<%= query %>&sort=<%= sort %>">Books</a>
        <a class="cat-pill <%= currentCat == 3 ? "active" : "" %>" href="<%= ctx %>/products?category=3&q=<%= query %>&sort=<%= sort %>">Fashion</a>
    </div>

    <% if (products == null || products.isEmpty()) { %>
        <div class="card empty">
            <h3>No products found.</h3>
            <p class="muted">Try another search or browse the complete collection.</p>
            <a class="btn" href="<%= ctx %>/products" style="margin-top:18px">Reset filters</a>
        </div>
    <% } else { %>
        <div class="grid">
        <% 
            java.util.Set<Integer> wishIds = (java.util.Set<Integer>) request.getAttribute("wishlistIds");
            String[] images = {
                "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?auto=format&fit=crop&w=900&q=85",
                "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=900&q=85",
                "https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=900&q=85",
                "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=900&q=85"
            };
            int i = 0;
            for (Product p : products) {
                String image = (p.getImageUrl() != null && !p.getImageUrl().trim().isEmpty()) ? p.getImageUrl() : images[i++ % images.length];
                boolean isItemWishlisted = (wishIds != null && wishIds.contains(p.getProductId()));
        %>
            <article class="product-card">
                <a class="product-image" href="<%= ctx %>/product?id=<%= p.getProductId() %>">
                    <img src="<%= image %>" alt="<%= p.getName() %>">
                    <span class="product-tag"><%= p.getStock() > 0 ? "AVAILABLE" : "SOLD OUT" %></span>
                </a>
                <div class="product-body">
                    <span class="chip"><%= p.getBrand() != null ? p.getBrand() : "ShopSphere" %></span>
                    <a class="product-title" href="<%= ctx %>/product?id=<%= p.getProductId() %>"><%= p.getName() %></a>
                    <p class="muted"><%= p.getDescription() != null && p.getDescription().length() > 85 ? p.getDescription().substring(0, 85) + "…" : (p.getDescription() == null ? "" : p.getDescription()) %></p>
                    <div style="display:flex;justify-content:space-between;align-items:center; margin: 6px 0;">
                        <span class="price">₹<%= String.format("%.2f", p.getPrice()) %></span>
                        <% if (p.getStock() <= 5 && p.getStock() > 0) { %>
                            <span class="chip danger" style="font-weight:600;">Only <%= p.getStock() %> left!</span>
                        <% } else if (p.getStock() > 0) { %>
                            <span class="chip success">In stock</span>
                        <% } else { %>
                            <span class="chip danger">Out of stock</span>
                        <% } %>
                    </div>
                    <div class="product-actions" style="display:flex; gap:8px;">
                        <% if (p.getStock() > 0) { %>
                        <form method="post" action="<%= ctx %>/cart" style="flex:1;">
                            <input type="hidden" name="_csrf" value="${csrfToken}" />
                            <input type="hidden" name="productId" value="<%= p.getProductId() %>">
                            <input type="hidden" name="quantity" value="1">
                            <button type="submit" style="width:100%;">Add to bag</button>
                        </form>
                        <% } %>
                        <form method="post" action="<%= ctx %>/wishlist">
                            <input type="hidden" name="_csrf" value="${csrfToken}" />
                            <input type="hidden" name="productId" value="<%= p.getProductId() %>">
                            <% if (isItemWishlisted) { %>
                                <input type="hidden" name="action" value="remove">
                                <button class="btn secondary" type="submit" title="Remove from wishlist">❤️</button>
                            <% } else { %>
                                <button class="btn ghost" type="submit" title="Save to wishlist">🤍</button>
                            <% } %>
                        </form>
                    </div>
                </div>
            </article>
        <% } %>
        </div>

        <div class="filter-bar" style="margin-top:40px">
            <span class="muted">Page <%= currentPage %></span>
            <div style="display:flex;gap:8px">
                <% if (currentPage > 1) { %>
                <a class="btn ghost" href="?q=<%= query %>&category=<%= currentCat %>&sort=<%= sort %>&page=<%= currentPage - 1 %>">Previous</a>
                <% } %>
                <a class="btn ghost" href="?q=<%= query %>&category=<%= currentCat %>&sort=<%= sort %>&page=<%= currentPage + 1 %>">Next</a>
            </div>
        </div>
    <% } %>
</main>
<footer class="main-footer">
    <div class="container">
        <div class="footer-bottom">
            <span>© 2026 ShopSphere</span>
            <span>Modern shopping, refined for everyday.</span>
        </div>
    </div>
</footer>
<script src="<%= ctx %>/assets/js/app.js"></script>
</body>
</html>

