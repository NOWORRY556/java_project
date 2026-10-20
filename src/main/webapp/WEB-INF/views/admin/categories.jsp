<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<% String ctx = request.getContextPath(); %>
<% String[] editCat = (String[]) request.getAttribute("editCategory"); %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Categories — ShopSphere Admin</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/css/app.css">
    <style>
        .cat-thumb { width: 44px; height: 44px; border-radius: 6px; object-fit: cover; background: #1a1a1a; border: 1px solid rgba(255,255,255,0.1); }
        .edit-card { background: #121212; border: 1px solid rgba(255,255,255,0.16); border-radius: 10px; padding: 24px; margin-bottom: 24px; box-shadow: 0 10px 30px rgba(0,0,0,0.5); }
        .form-row { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 16px; margin-bottom: 16px; }
    </style>
</head>
<body>
<div class="admin-shell">
    <aside class="sidebar">
        <a class="brand" href="<%= ctx %>/admin">Shop<span>Sphere</span></a>
        <a class="side-link" href="<%= ctx %>/admin">▦ Dashboard</a>
        <a class="side-link" href="<%= ctx %>/admin/products">◈ Products</a>
        <a class="side-link active" href="<%= ctx %>/admin/categories">◇ Categories</a>
        <a class="side-link" href="<%= ctx %>/admin/orders">▤ Orders</a>
        <a class="side-link" href="<%= ctx %>/admin/coupons">% Coupons</a>
        <a class="side-link" href="<%= ctx %>/admin/users">◉ Customers</a>
        <a class="side-link" href="<%= ctx %>/admin/reviews">★ Reviews</a>
        <a class="side-link" href="<%= ctx %>/admin/analytics">📊 Analytics</a>
        <a class="side-link" href="<%= ctx %>/admin/export">⇩ Export orders</a>
        <a class="side-link" href="<%= ctx %>/products">↗ Store</a>
    </aside>

    <main class="admin-main">
        <div class="section-head">
            <div>
                <span class="eyebrow">CATALOG MANAGEMENT</span>
                <h1>Category Manager</h1>
                <p class="muted">Create, edit details, upload category images, and toggle status.</p>
            </div>
        </div>

        <!-- Add / Edit Category Form -->
        <div class="edit-card">
            <h3 style="font-size:14px; font-weight:600; text-transform:uppercase; letter-spacing:0.08em; color:var(--accent,#d8c8a8); margin-bottom:16px;">
                <%= editCat != null ? "Edit Category #" + editCat[0] : "Add New Category" %>
            </h3>

            <form method="post" action="<%= ctx %>/admin/categories">
    <input type="hidden" name="_csrf" value="${csrfToken}" />
                <input type="hidden" name="action" value="<%= editCat != null ? "update" : "create" %>">
                <% if (editCat != null) { %>
                    <input type="hidden" name="id" value="<%= editCat[0] %>">
                <% } %>

                <div class="form-row">
                    <div class="field">
                        <label>Category Name</label>
                        <input name="name" required placeholder="e.g. Apparel & Footwear" value="<%= editCat != null ? editCat[1] : "" %>">
                    </div>
                    <div class="field">
                        <label>Image URL (Optional)</label>
                        <input name="imageUrl" placeholder="https://images.unsplash.com/photo-..." value="<%= editCat != null ? editCat[3] : "" %>">
                    </div>
                </div>

                <div class="field">
                    <label>Description</label>
                    <input name="description" placeholder="Brief summary of category products..." value="<%= editCat != null ? editCat[2] : "" %>">
                </div>

                <% if (editCat != null) { %>
                    <div class="field" style="margin-top:12px;">
                        <label style="display:flex; align-items:center; gap:8px; cursor:pointer;">
                            <input type="checkbox" name="status" value="true" <%= "true".equalsIgnoreCase(editCat[4]) ? "checked" : "" %> style="accent-color:var(--accent,#d8c8a8); width:16px; height:16px;">
                            <span style="font-size:13px; color:#f3efe7;">Category Active & Visible in Store</span>
                        </label>
                    </div>
                <% } %>

                <div style="display:flex; gap:10px; margin-top:20px;">
                    <button type="submit" class="btn"><%= editCat != null ? "Save Category Changes" : "Add Category" %></button>
                    <% if (editCat != null) { %>
                        <a href="<%= ctx %>/admin/categories" class="btn secondary">Cancel Edit</a>
                    <% } %>
                </div>
            </form>
        </div>

        <!-- Categories Table -->
        <div class="toolbar">
            <h2>All Categories</h2>
            <input class="search" data-search="#categoriesTable" placeholder="Search categories…">
        </div>

        <div id="categoriesTable" class="table-wrap">
            <table>
                <tr>
                    <th>Image</th>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                <% 
                List<String[]> categoriesList = (List<String[]>) request.getAttribute("categories");
                if (categoriesList != null && !categoriesList.isEmpty()) {
                    for (String[] row : categoriesList) { 
                        String img = (row[3] != null && !row[3].isBlank()) ? row[3] : "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?auto=format&fit=crop&w=150&q=80";
                        boolean isActive = "true".equalsIgnoreCase(row[4]);
                %>
                        <tr data-search-item>
                            <td><img src="<%= img %>" class="cat-thumb" alt="<%= row[1] %>" onerror="this.src='https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?auto=format&fit=crop&w=150&q=80'"></td>
                            <td><strong>#<%= row[0] %></strong></td>
                            <td><strong><%= row[1] %></strong></td>
                            <td class="muted"><%= row[2] %></td>
                            <td><span class="chip <%= isActive ? "" : "danger" %>"><%= isActive ? "ACTIVE" : "DISABLED" %></span></td>
                            <td style="display:flex; gap:8px;">
                                <a href="<%= ctx %>/admin/categories?action=edit&id=<%= row[0] %>" class="btn secondary" style="padding:6px 12px; font-size:11px;">Edit</a>
                                <form method="post" action="<%= ctx %>/admin/categories" style="display:inline;">
    <input type="hidden" name="_csrf" value="${csrfToken}" />
                                    <input type="hidden" name="action" value="toggle">
                                    <input type="hidden" name="id" value="<%= row[0] %>">
                                    <button class="btn <%= isActive ? "danger" : "secondary" %>" style="padding:6px 12px; font-size:11px;" type="submit">
                                        <%= isActive ? "Disable" : "Enable" %>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    <% } 
                } else { %>
                    <tr><td colspan="6" style="text-align:center; padding:20px;" class="muted">No categories found.</td></tr>
                <% } %>
            </table>
        </div>
    </main>
</div>
<script src="<%= ctx %>/assets/js/app.js"></script>
</body>
</html>