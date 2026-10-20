<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List,com.shopsphere.model.Product" %>
<%
    List<Product> products = (List<Product>) request.getAttribute("products");
    List<String[]> categories = (List<String[]>) request.getAttribute("categories");
    Product editProduct = (Product) request.getAttribute("editProduct");
    boolean isEdit = (editProduct != null);
%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Products Management — ShopSphere Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
<div class="admin-shell">
    <aside class="sidebar">
        <a class="brand" href="${pageContext.request.contextPath}/admin">Shop<span>Sphere</span></a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin">▦ Dashboard</a>
        <a class="side-link active" href="${pageContext.request.contextPath}/admin/products">◈ Products</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/categories">◇ Categories</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/orders">▤ Orders</a>
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
                <span class="eyebrow">CATALOG MANAGEMENT</span>
                <h1><%= isEdit ? "Edit Product #" + editProduct.getProductId() : "Products Catalog" %></h1>
                <p class="muted"><%= isEdit ? "Update product pricing, stock, details and image URL." : "Add new products or manage existing inventory." %></p>
            </div>
            <% if (isEdit) { %>
            <a class="btn ghost" href="${pageContext.request.contextPath}/admin/products">← Cancel Editing</a>
            <% } %>
        </div>

        <div class="card" style="margin-bottom:1.5rem">
            <h3><%= isEdit ? "Update Product: " + editProduct.getName() : "Add New Product" %></h3>
            <form method="post" action="${pageContext.request.contextPath}/admin/products" style="margin-top:16px;">
    <input type="hidden" name="_csrf" value="${csrfToken}" /><input type="hidden" name="action" value="<%= isEdit ? "update" : "create" %>">
                <% if (isEdit) { %>
                <input type="hidden" name="productId" value="<%= editProduct.getProductId() %>">
                <% } %>
                <div class="grid" style="grid-template-columns: repeat(3, 1fr); gap: 16px;">
                    <div class="field">
                        <label>Category</label>
                        <select name="categoryId" required>
                            <% if (categories != null) { for (String[] c : categories) { boolean isActive = c.length > 4 ? "true".equalsIgnoreCase(c[4]) : (c.length > 3 ? "true".equalsIgnoreCase(c[3]) : true); if (isActive) { %>
                            <option value="<%= c[0] %>" <%= (isEdit && Integer.toString(editProduct.getCategoryId()).equals(c[0])) ? "selected" : "" %>><%= c[1] %></option>
                            <% } } } %>
                        </select>
                    </div>
                    <div class="field">
                        <label>Product Name</label>
                        <input name="name" value="<%= isEdit ? editProduct.getName() : "" %>" required placeholder="e.g. Wireless Mouse">
                    </div>
                    <div class="field">
                        <label>Brand</label>
                        <input name="brand" value="<%= isEdit && editProduct.getBrand() != null ? editProduct.getBrand() : "" %>" placeholder="e.g. Logitech">
                    </div>
                    <div class="field">
                        <label>Price (₹)</label>
                        <input name="price" type="number" step="0.01" min="0" value="<%= isEdit ? editProduct.getPrice() : "" %>" required placeholder="999.00">
                    </div>
                    <div class="field">
                        <label>Discount %</label>
                        <input name="discount" type="number" step="0.01" min="0" max="100" value="<%= isEdit ? editProduct.getDiscount() : "0" %>">
                    </div>
                    <div class="field">
                        <label>Stock Quantity</label>
                        <input name="stock" type="number" min="0" value="<%= isEdit ? editProduct.getStock() : "10" %>" required>
                    </div>
                </div>
                <div class="field" style="margin-top:12px;">
                    <label>Image URL</label>
                    <input name="imageUrl" value="<%= isEdit && editProduct.getImageUrl() != null ? editProduct.getImageUrl() : "" %>" placeholder="https://images.unsplash.com/photo-...">
                </div>
                <div class="field" style="margin-top:12px;">
                    <label>Description</label>
                    <textarea name="description" rows="3" placeholder="Product details..."><%= isEdit && editProduct.getDescription() != null ? editProduct.getDescription() : "" %></textarea>
                </div>
                <% if (isEdit) { %>
                    <div class="field" style="margin-top:12px;">
                        <label style="display:flex; align-items:center; gap:8px; cursor:pointer;">
                            <input type="checkbox" name="status" value="true" <%= editProduct.isStatus() ? "checked" : "" %> style="accent-color:var(--accent,#d8c8a8); width:16px; height:16px;">
                            <span style="font-size:13px; color:#f3efe7;">Product Active & Visible in Store</span>
                        </label>
                    </div>
                <% } %>
                <div style="display:flex; gap:12px; margin-top:16px;">
                    <button type="submit" class="btn"><%= isEdit ? "Save Product Changes" : "Create Product" %></button>
                    <% if (isEdit) { %>
                    <a class="btn ghost" href="${pageContext.request.contextPath}/admin/products">Cancel</a>
                    <% } %>
                </div>
            </form>
        </div>

        <!-- Bulk Product Import via CSV (Feature #6) -->
        <details class="card" style="margin-bottom:1.5rem; cursor:pointer;">
            <summary style="font-weight:600; font-size:15px; color:#d8c8a8;">📦 Bulk Product CSV Import (Click to expand)</summary>
            <form method="post" action="${pageContext.request.contextPath}/admin/products" style="margin-top:16px; cursor:default;">
                <input type="hidden" name="_csrf" value="${csrfToken}" />
                <input type="hidden" name="action" value="bulk_import">
                <p class="muted" style="font-size:12px; margin-bottom:8px;">
                    Paste comma-separated rows: <code>name, brand, category_id, price, discount, stock, description, image_url</code>.
                    <a href="${pageContext.request.contextPath}/admin/export?type=template" target="_blank" style="color:#d8c8a8; margin-left:8px; text-decoration:underline;">⇩ Download Sample Template (.csv)</a>
                </p>
                <textarea name="csvData" rows="4" placeholder="Spatial Audio Pro,Sony,1,19999.00,10.0,30,Premium wireless noise cancelling headphones,https://images.unsplash.com/...&#10;Classic Leather Watch,Fossil,1,9500.00,0,15,Genuine leather minimalist watch,https://images.unsplash.com/..." style="width:100%; font-family:monospace; font-size:12px; background:#181818; border:1px solid #333; color:#fff; padding:10px; border-radius:6px;"></textarea>
                <div style="margin-top:10px;">
                    <button type="submit" class="btn secondary">Import Products from CSV</button>
                </div>
            </form>
        </details>

        <div class="toolbar" style="display:flex; justify-content:space-between; align-items:center;">
            <h2>Product Catalog (<%= products != null ? products.size() : 0 %>)</h2>
            <div style="display:flex; gap:10px;">
                <a href="${pageContext.request.contextPath}/admin/export?type=products" class="btn secondary" style="font-size:12px; padding:6px 12px;">⇩ Export Inventory CSV</a>
                <input class="search" data-search="#adminProducts" placeholder="Search products by name or brand..." style="min-width:240px;">
            </div>
        </div>
        <div id="adminProducts" class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Product</th>
                    <th>Price</th>
                    <th>Discount</th>
                    <th>Stock</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <% if (products != null && !products.isEmpty()) { for (Product p : products) { %>
                <tr data-search-item>
                    <td>#<%= p.getProductId() %></td>
                    <td>
                        <div style="display:flex; align-items:center; gap:12px;">
                            <img src="<%= p.getImageUrl() != null && !p.getImageUrl().trim().isEmpty() ? p.getImageUrl() : "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?auto=format&fit=crop&w=150&q=80" %>" alt="" style="width:40px; height:40px; object-fit:cover; border-radius:4px;">
                            <div>
                                <strong><%= p.getName() %></strong>
                                <div class="muted"><%= p.getBrand() != null ? p.getBrand() : "ShopSphere" %></div>
                            </div>
                        </div>
                    </td>
                    <td>₹<%= String.format("%.2f", p.getPrice()) %></td>
                    <td><%= p.getDiscount() > 0 ? p.getDiscount() + "%" : "-" %></td>
                    <td><span class="chip <%= p.getStock() > 0 ? "success" : "danger" %>"><%= p.getStock() %> units</span></td>
                    <td><span class="chip <%= p.isStatus() ? "success" : "danger" %>"><%= p.isStatus() ? "Active" : "Disabled" %></span></td>
                    <td>
                        <div class="product-actions" style="margin:0; display:flex; gap:6px;">
                            <a class="btn secondary" style="padding:6px 12px; font-size:11px;" href="${pageContext.request.contextPath}/admin/products?action=edit&id=<%= p.getProductId() %>">Edit</a>
                            <% if (p.isStatus()) { %>
                                <a class="btn danger" style="padding:6px 12px; font-size:11px;" onclick="return confirm('Disable this product?')" href="${pageContext.request.contextPath}/admin/products?action=toggle&id=<%= p.getProductId() %>">Disable</a>
                            <% } else { %>
                                <a class="btn secondary" style="padding:6px 12px; font-size:11px; border-color:#81c784; color:#81c784;" onclick="return confirm('Enable this product?')" href="${pageContext.request.contextPath}/admin/products?action=toggle&id=<%= p.getProductId() %>">Enable</a>
                            <% } %>
                        </div>
                    </td>
                </tr>
                <% } } else { %>
                <tr><td colspan="7" class="empty">No products found in catalog.</td></tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </main>
</div>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>