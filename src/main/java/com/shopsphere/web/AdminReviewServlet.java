package com.shopsphere.web;

import com.shopsphere.dao.AdminReviewDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Collections;

@WebServlet("/admin/reviews")
public class AdminReviewServlet extends HttpServlet {
    private final AdminReviewDAO dao = new AdminReviewDAO();

    @Override
    protected void doGet(HttpServletRequest q, HttpServletResponse p) throws ServletException, IOException {
        try {
            q.setAttribute("reviews", dao.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            q.setAttribute("reviews", Collections.emptyList());
            q.setAttribute("error", "Failed to load reviews: " + e.getMessage());
        }
        q.getRequestDispatcher("/WEB-INF/views/admin/reviews.jsp").forward(q, p);
    }

    @Override
    protected void doPost(HttpServletRequest q, HttpServletResponse p) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(q.getParameter("reviewId"));
            dao.delete(id);
            q.getSession().setAttribute("msg", "Review deleted successfully!");
            p.sendRedirect(q.getContextPath() + "/admin/reviews");
        } catch (Exception e) {
            e.printStackTrace();
            q.getSession().setAttribute("error", "Failed to delete review: " + e.getMessage());
            p.sendRedirect(q.getContextPath() + "/admin/reviews");
        }
    }
}
