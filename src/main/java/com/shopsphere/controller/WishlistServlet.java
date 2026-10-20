package com.shopsphere.controller;

import com.shopsphere.dao.WishlistDAO;
import com.shopsphere.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {
    private final WishlistDAO dao = new WishlistDAO();

    private User user(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (User) session.getAttribute("loggedInUser");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User u = user(request);
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        try {
            request.setAttribute("products", dao.findByUser(u.getUserId()));
            request.getRequestDispatcher("/WEB-INF/views/wishlist.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Unable to load wishlist", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User u = user(request);
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            if ("remove".equals(request.getParameter("action"))) {
                dao.remove(u.getUserId(), productId);
            } else {
                dao.add(u.getUserId(), productId);
            }
            response.sendRedirect(request.getContextPath() + "/wishlist");
        } catch (Exception e) {
            throw new IOException("Wishlist operation failed", e);
        }
    }
}
