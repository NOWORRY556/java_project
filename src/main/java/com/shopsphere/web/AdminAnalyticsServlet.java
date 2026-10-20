package com.shopsphere.web;

import com.shopsphere.dao.AdminAnalyticsDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Collections;

@WebServlet("/admin/analytics")
public class AdminAnalyticsServlet extends HttpServlet {
    private final AdminAnalyticsDAO dao = new AdminAnalyticsDAO();

    @Override
    protected void doGet(HttpServletRequest q, HttpServletResponse p) throws ServletException, IOException {
        try {
            q.setAttribute("summary", dao.summary());
            q.setAttribute("daily", dao.recentOrders());
            q.setAttribute("statusDistribution", dao.orderStatusDistribution());
            q.setAttribute("topProducts", dao.topSellingProducts());
            q.setAttribute("lowStock", dao.lowStockProducts(5));
        } catch (Exception e) {
            e.printStackTrace();
            q.setAttribute("summary", Collections.emptyMap());
            q.setAttribute("daily", Collections.emptyList());
            q.setAttribute("statusDistribution", Collections.emptyMap());
            q.setAttribute("topProducts", Collections.emptyList());
            q.setAttribute("lowStock", Collections.emptyList());
            q.setAttribute("error", "Failed to load analytics: " + e.getMessage());
        }
        q.getRequestDispatcher("/WEB-INF/views/admin/analytics.jsp").forward(q, p);
    }
}
