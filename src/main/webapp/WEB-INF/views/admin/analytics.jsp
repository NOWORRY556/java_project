<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%
    Map<String, Object> summary = (Map<String, Object>) request.getAttribute("summary");
    List<Map<String, Object>> daily = (List<Map<String, Object>>) request.getAttribute("daily");
    Map<String, Integer> statusDist = (Map<String, Integer>) request.getAttribute("statusDistribution");
    List<Map<String, Object>> topProducts = (List<Map<String, Object>>) request.getAttribute("topProducts");
    List<Map<String, Object>> lowStock = (List<Map<String, Object>>) request.getAttribute("lowStock");
    String ctx = request.getContextPath();

    // Prepare JSON arrays for Chart.js
    StringBuilder daysLabels = new StringBuilder("[");
    StringBuilder revenueData = new StringBuilder("[");
    StringBuilder ordersData = new StringBuilder("[");
    if (daily != null) {
        for (int i = daily.size() - 1; i >= 0; i--) {
            Map<String, Object> d = daily.get(i);
            daysLabels.append("'").append(d.get("day")).append("',");
            revenueData.append(d.get("revenue")).append(",");
            ordersData.append(d.get("orders")).append(",");
        }
    }
    daysLabels.append("]");
    revenueData.append("]");
    ordersData.append("]");

    StringBuilder statusLabels = new StringBuilder("[");
    StringBuilder statusCounts = new StringBuilder("[");
    if (statusDist != null && !statusDist.isEmpty()) {
        for (Map.Entry<String, Integer> entry : statusDist.entrySet()) {
            statusLabels.append("'").append(entry.getKey()).append("',");
            statusCounts.append(entry.getValue()).append(",");
        }
    } else {
        statusLabels.append("'PLACED', 'PROCESSING', 'DELIVERED'");
        statusCounts.append("1, 1, 1");
    }
    statusLabels.append("]");
    statusCounts.append("]");
%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Store Analytics & Intelligence — ShopSphere Admin</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/css/app.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        .charts-grid { display: grid; grid-template-columns: 2fr 1fr; gap: 20px; margin-top: 24px; }
        .chart-box { background: #111; border: 1px solid #222; border-radius: 12px; padding: 22px; }
        .chart-title { font-size: 14px; font-weight: 600; text-transform: uppercase; letter-spacing: 1px; color: #d8c8a8; margin-bottom: 16px; }
        @media (max-width: 900px) { .charts-grid { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
<div class="admin-shell">
    <aside class="sidebar">
        <a class="brand" href="<%= ctx %>/admin">Shop<span>Sphere</span></a>
        <a class="side-link" href="<%= ctx %>/admin">▦ Dashboard</a>
        <a class="side-link" href="<%= ctx %>/admin/products">◈ Products</a>
        <a class="side-link" href="<%= ctx %>/admin/categories">◇ Categories</a>
        <a class="side-link" href="<%= ctx %>/admin/orders">▤ Orders</a>
        <a class="side-link" href="<%= ctx %>/admin/coupons">% Coupons</a>
        <a class="side-link" href="<%= ctx %>/admin/users">◉ Customers</a>
        <a class="side-link" href="<%= ctx %>/admin/reviews">★ Reviews</a>
        <a class="side-link active" href="<%= ctx %>/admin/analytics">📊 Analytics</a>
        <a class="side-link" href="<%= ctx %>/admin/export">⇩ Export orders</a>
        <a class="side-link" href="<%= ctx %>/products">↗ Store</a>
    </aside>

    <main class="admin-main">
        <div class="section-head">
            <div>
                <span class="eyebrow">INTELLIGENCE</span>
                <h1>Store Analytics & Performance</h1>
                <p class="muted">Live revenue trends, customer growth metrics, and stock inventory health.</p>
            </div>
            <div style="display:flex; gap:10px;">
                <a href="<%= ctx %>/admin/export?type=sales" class="btn secondary">⇩ Export Sales Report</a>
            </div>
        </div>
        
        <!-- Summary KPI Cards -->
        <div class="stats" style="grid-template-columns: repeat(4, 1fr); margin-bottom: 24px;">
            <div class="stat">
                <div class="value" style="color:#d8c8a8;">₹<%= summary != null ? summary.get("revenue") : "0" %></div>
                <div class="label">Gross Sales Revenue</div>
            </div>
            <div class="stat">
                <div class="value"><%= summary != null ? summary.get("orders") : "0" %></div>
                <div class="label">Completed Orders</div>
            </div>
            <div class="stat">
                <div class="value"><%= summary != null ? summary.get("users") : "0" %></div>
                <div class="label">Active Customer Accounts</div>
            </div>
            <div class="stat">
                <div class="value"><%= summary != null ? summary.get("products") : "0" %></div>
                <div class="label">Active Catalog Products</div>
            </div>
        </div>

        <!-- Chart.js Interactive Charts (Feature #4) -->
        <div class="charts-grid">
            <div class="chart-box">
                <div class="chart-title">📈 Daily Revenue & Sales Volume</div>
                <div style="position: relative; height: 260px;">
                    <canvas id="revenueChart"></canvas>
                </div>
            </div>
            <div class="chart-box">
                <div class="chart-title">🍩 Order Status Breakdown</div>
                <div style="position: relative; height: 260px;">
                    <canvas id="statusChart"></canvas>
                </div>
            </div>
        </div>

        <!-- Two Column Section: Top Selling & Low Stock Alerts -->
        <div class="charts-grid" style="margin-top: 24px;">
            <!-- Top Selling Products -->
            <div class="chart-box">
                <div class="chart-title">🏆 Top Selling Products</div>
                <div class="table-wrap">
                    <table>
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Brand</th>
                                <th>Units Sold</th>
                                <th>Total Revenue</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (topProducts != null && !topProducts.isEmpty()) { 
                                for (Map<String, Object> tp : topProducts) { %>
                            <tr>
                                <td><strong><%= tp.get("name") %></strong></td>
                                <td><span class="chip"><%= tp.get("brand") %></span></td>
                                <td><%= tp.get("qty") %> units</td>
                                <td><strong style="color:#d8c8a8;">₹<%= tp.get("sales") %></strong></td>
                            </tr>
                            <%  } 
                               } else { %>
                            <tr><td colspan="4" class="empty">No sales recorded yet.</td></tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Low Stock Inventory Alerts (Feature #7) -->
            <div class="chart-box">
                <div class="chart-title" style="color: #ff6b6b;">⚠️ Low Stock Inventory Alerts</div>
                <div class="table-wrap">
                    <table>
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Remaining Stock</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (lowStock != null && !lowStock.isEmpty()) { 
                                for (Map<String, Object> ls : lowStock) { 
                                    int stockVal = (Integer) ls.get("stock");
                            %>
                            <tr>
                                <td><strong><%= ls.get("name") %></strong></td>
                                <td><span class="chip danger"><%= stockVal %> units</span></td>
                                <td>
                                    <a href="<%= ctx %>/admin/products?action=edit&id=<%= ls.get("id") %>" class="btn ghost" style="padding:4px 8px; font-size:11px;">Restock</a>
                                </td>
                            </tr>
                            <%  } 
                               } else { %>
                            <tr><td colspan="3" class="empty" style="color:#28a745;">✓ All inventory healthy (>5 units in stock).</td></tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>
</div>

<script>
    // Revenue Line Chart
    const ctxRev = document.getElementById('revenueChart').getContext('2d');
    new Chart(ctxRev, {
        type: 'line',
        data: {
            labels: <%= daysLabels.toString() %>,
            datasets: [{
                label: 'Revenue (₹)',
                data: <%= revenueData.toString() %>,
                borderColor: '#d8c8a8',
                backgroundColor: 'rgba(216, 200, 168, 0.15)',
                tension: 0.35,
                fill: true,
                pointBackgroundColor: '#d8c8a8',
                pointRadius: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { labels: { color: '#aaa', font: { size: 12 } } }
            },
            scales: {
                x: { ticks: { color: '#888' }, grid: { color: 'rgba(255,255,255,0.05)' } },
                y: { ticks: { color: '#888' }, grid: { color: 'rgba(255,255,255,0.05)' } }
            }
        }
    });

    // Order Status Doughnut Chart
    const ctxStatus = document.getElementById('statusChart').getContext('2d');
    new Chart(ctxStatus, {
        type: 'doughnut',
        data: {
            labels: <%= statusLabels.toString() %>,
            datasets: [{
                data: <%= statusCounts.toString() %>,
                backgroundColor: ['#d8c8a8', '#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6'],
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: 'bottom', labels: { color: '#aaa', boxWidth: 12 } }
            }
        }
    });
</script>
</body>
</html>
