package com.shopsphere.controller;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Collections;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private final ProductDAO dao = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String q = req.getParameter("q");
        int category = parse(req.getParameter("category"), 0);
        int page = parse(req.getParameter("page"), 1);
        String sort = req.getParameter("sort");
        Double minPrice = parseDouble(req.getParameter("minPrice"));
        Double maxPrice = parseDouble(req.getParameter("maxPrice"));
        boolean inStock = "1".equals(req.getParameter("inStock")) || "true".equalsIgnoreCase(req.getParameter("inStock"));

        List<Product> products = null;
        try {
            products = dao.search(q, category, minPrice, maxPrice, inStock, sort, page, 12);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                products = dao.findAll();
            } catch (Exception ex) {
                ex.printStackTrace();
                products = Collections.emptyList();
            }
        }

        if (products == null) {
            products = Collections.emptyList();
        }

        List<String[]> categoriesList = Collections.emptyList();
        try {
            categoriesList = new com.shopsphere.dao.CategoryDAO().findAll();
        } catch (Exception ignored) {}

        req.setAttribute("products", products);
        req.setAttribute("categories", categoriesList);
        req.setAttribute("query", q == null ? "" : q);
        req.setAttribute("category", category);
        req.setAttribute("minPrice", minPrice == null ? "" : minPrice);
        req.setAttribute("maxPrice", maxPrice == null ? "" : maxPrice);
        req.setAttribute("inStock", inStock);
        req.setAttribute("sort", sort == null ? "newest" : sort);
        req.setAttribute("page", page);
        try {
            com.shopsphere.model.User currentUser = (com.shopsphere.model.User) req.getSession().getAttribute("loggedInUser");
            if (currentUser != null) {
                req.setAttribute("wishlistIds", new com.shopsphere.dao.WishlistDAO().getWishlistProductIds(currentUser.getUserId()));
            }
        } catch (Exception ignored) {}
        req.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(req, resp);
    }

    private Double parseDouble(String v) {
        try {
            return (v != null && !v.isBlank()) ? Double.parseDouble(v) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }


    private int parse(String v, int fallback) {
        try {
            return v == null ? fallback : Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}