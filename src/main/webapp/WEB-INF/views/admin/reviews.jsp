<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List,java.util.Map" %>
<%
    List<Map<String, Object>> reviews = (List<Map<String, Object>>) request.getAttribute("reviews");
%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Customer Reviews — ShopSphere Admin</title>
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
        <a class="side-link" href="${pageContext.request.contextPath}/admin/coupons">% Coupons</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/users">◉ Customers</a>
        <a class="side-link active" href="${pageContext.request.contextPath}/admin/reviews">★ Reviews</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/analytics">📊 Analytics</a>
        <a class="side-link" href="${pageContext.request.contextPath}/admin/export">⇩ Export orders</a>
        <a class="side-link" href="${pageContext.request.contextPath}/products">↗ Store</a>
    </aside>
    <main class="admin-main">
        <div class="section-head">
            <div>
                <span class="eyebrow">MODERATION</span>
                <h1>Customer Reviews</h1>
                <p class="muted">Review and moderate customer feedback.</p>
            </div>
        </div>
        <div class="table-wrap">
            <table>
                <tr>
                    <th>Product</th>
                    <th>Customer</th>
                    <th>Rating</th>
                    <th>Review</th>
                    <th>Date</th>
                    <th>Action</th>
                </tr>
                <% if (reviews != null && !reviews.isEmpty()) { for (Map<String, Object> r : reviews) { %>
                <tr>
                    <td><strong><%= r.get("product") %></strong></td>
                    <td><%= r.get("customer") %></td>
                    <td><span class="chip">★ <%= r.get("rating") %>/5</span></td>
                    <td class="muted"><%= r.get("reviewText") != null ? r.get("reviewText") : "" %></td>
                    <td class="muted"><%= r.get("createdAt") %></td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/admin/reviews">
    <input type="hidden" name="_csrf" value="${csrfToken}" /><input type="hidden" name="reviewId" value="<%= r.get("reviewId") %>">
                            <button type="submit" class="btn danger" onclick="return confirm('Delete this review?')">Delete</button>
                        </form>
                    </td>
                </tr>
                <% } } else { %>
                <tr><td colspan="6" class="empty">No reviews to display.</td></tr>
                <% } %>
            </table>
        </div>
    </main>
</div>
</body>
</html>
