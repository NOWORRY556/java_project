package com.shopsphere.controller;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.dao.ReviewDAO;
import com.shopsphere.model.Product;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.Collections;

@WebServlet(urlPatterns={"/product", "/product-detail"})
public class ProductDetailServlet extends HttpServlet {
    private final ProductDAO products = new ProductDAO();
    private final ReviewDAO reviews = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest q, HttpServletResponse p) throws ServletException, IOException {
        String idParam = q.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            p.sendRedirect(q.getContextPath() + "/products");
            return;
        }
        try {
            int id = Integer.parseInt(idParam);
            Product x = products.findById(id);
            if (x == null) {
                p.sendRedirect(q.getContextPath() + "/products");
                return;
            }
            q.setAttribute("product", x);
            try {
                q.setAttribute("reviews", reviews.findByProduct(id));
            } catch (Exception ex) {
                q.setAttribute("reviews", Collections.emptyList());
            }
            try {
                com.shopsphere.model.User currentUser = (com.shopsphere.model.User) q.getSession().getAttribute("loggedInUser");
                if (currentUser != null) {
                    q.setAttribute("isWishlisted", new com.shopsphere.dao.WishlistDAO().isWishlisted(currentUser.getUserId(), id));
                }
            } catch (Exception ignored) {}
            q.getRequestDispatcher("/WEB-INF/views/product-detail.jsp").forward(q, p);
        } catch (NumberFormatException e) {
            p.sendRedirect(q.getContextPath() + "/products");
        } catch (Exception e) {
            e.printStackTrace();
            p.sendRedirect(q.getContextPath() + "/products");
        }
    }
}