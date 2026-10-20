<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%
    Integer status = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
    if (status == null && response.getStatus() != 200) status = response.getStatus();
    String message = (String) request.getAttribute("jakarta.servlet.error.message");
    if (message == null || message.isBlank()) message = "An unexpected error occurred while processing your request. Please try again or return to the main store.";
    String ctx = request.getContextPath();
%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title><%= status != null ? status + " — ShopSphere" : "Notice — ShopSphere" %></title>
    <link rel="stylesheet" href="<%= ctx %>/assets/css/app.css">
    <style>
        body { background: #080808; color: #f3efe7; display: flex; align-items: center; justify-content: center; min-height: 100vh; margin: 0; font-family: system-ui, -apple-system, sans-serif; }
        .error-card { background: #121212; border: 1px solid rgba(216,200,168,0.25); border-radius: 16px; padding: 40px; max-width: 480px; width: 90%; text-align: center; box-shadow: 0 12px 40px rgba(0,0,0,0.6); }
        .error-code { font-family: serif; font-size: 56px; font-weight: bold; color: var(--accent, #d8c8a8); margin: 10px 0; letter-spacing: 2px; }
        .error-msg { color: #a6a19a; font-size: 14px; line-height: 1.6; margin-bottom: 28px; }
    </style>
</head>
<body>
<div class="error-card">
    <span class="eyebrow" style="color:var(--accent,#d8c8a8); letter-spacing:2px; font-size:11px; text-transform:uppercase;">SHOPSPHERE SYSTEM</span>
    <h1 class="error-code"><%= status == null ? "Notice" : status %></h1>
    <p class="error-msg"><%= message %></p>
    <div style="display:flex; gap:12px; justify-content:center;">
        <a class="btn" href="<%= ctx %>/products">Browse Collections</a>
        <a class="btn secondary" href="<%= ctx %>/">Home Page</a>
    </div>
</div>
</body>
</html>
