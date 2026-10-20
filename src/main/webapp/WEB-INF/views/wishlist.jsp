<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.shopsphere.model.Product,java.util.List,com.shopsphere.model.User" %>
<% 
    User u = (User) session.getAttribute("loggedInUser");
    List<Product> products = (List<Product>) request.getAttribute("products");
    String ctx = request.getContextPath();
%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>My Wishlist — ShopSphere</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/css/app.css">
</head>
<body>
<div class="announcement-bar"><span>YOUR PERSONAL LUXURY COLLECTION</span></div>
<header class="nav-header">
    <div class="container nav-inner">
        <nav class="nav-menu">
            <a class="nav-link" href="<%= ctx %>/">Shop</a>
            <a class="nav-link" href="<%= ctx %>/products">Collections</a>
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

<main class="container products-section" style="min-height: 60vh;">
    <div class="section-head">
        <div>
            <span class="eyebrow">SAVED FAVORITES</span>
            <h1 class="section-title">My Wishlist</h1>
            <p class="muted">Items you've bookmarked for later purchase.</p>
        </div>
        <a class="btn ghost" href="<%= ctx %>/products">← Continue Shopping</a>
    </div>

    <% if (products == null || products.isEmpty()) { %>
        <div class="card empty" style="text-align: center; padding: 60px 20px;">
            <div style="font-size: 48px; margin-bottom: 16px;">🤍</div>
            <h3>Your wishlist is empty</h3>
            <p class="muted">Explore our curated collections and save items you love.</p>
            <a class="btn" href="<%= ctx %>/products" style="margin-top: 20px;">Discover Products</a>
        </div>
    <% } else { %>
        <div class="grid">
        <% 
            for (Product p : products) {
                String image = (p.getImageUrl() != null && !p.getImageUrl().trim().isEmpty()) 
                    ? p.getImageUrl() 
                    : "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?auto=format&fit=crop&w=900&q=85";
        %>
            <article class="product-card">
                <a class="product-image" href="<%= ctx %>/product?id=<%= p.getProductId() %>">
                    <img src="<%= image %>" alt="<%= p.getName() %>">
                    <span class="product-tag"><%= p.getStock() > 0 ? "IN STOCK" : "OUT OF STOCK" %></span>
                </a>
                <div class="product-body">
                    <span class="chip"><%= p.getBrand() != null ? p.getBrand() : "ShopSphere" %></span>
                    <a class="product-title" href="<%= ctx %>/product?id=<%= p.getProductId() %>"><%= p.getName() %></a>
                    <p class="muted"><%= p.getDescription() != null && p.getDescription().length() > 80 ? p.getDescription().substring(0, 80) + "…" : (p.getDescription() == null ? "" : p.getDescription()) %></p>
                    
                    <div style="display:flex;justify-content:space-between;align-items:center; margin: 8px 0;">
                        <span class="price">₹<%= String.format("%.2f", p.getPrice()) %></span>
                        <% if (p.getStock() <= 5 && p.getStock() > 0) { %>
                            <span class="chip danger">Only <%= p.getStock() %> left!</span>
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
                            <button type="submit" style="width:100%;">Add to Bag</button>
                        </form>
                        <% } %>
                        <form method="post" action="<%= ctx %>/wishlist">
                            <input type="hidden" name="_csrf" value="${csrfToken}" />
                            <input type="hidden" name="action" value="remove">
                            <input type="hidden" name="productId" value="<%= p.getProductId() %>">
                            <button class="btn danger" type="submit" title="Remove from wishlist">✕</button>
                        </form>
                    </div>
                </div>
            </article>
        <% } %>
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