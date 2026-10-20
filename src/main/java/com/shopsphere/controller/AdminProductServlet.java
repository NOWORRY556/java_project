package com.shopsphere.controller;

import com.shopsphere.model.Product;
import com.shopsphere.service.CatalogAdminService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Collections;

@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {
    private final CatalogAdminService service = new CatalogAdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("categories", service.categories());
            String action = req.getParameter("action");
            if ("delete".equals(action) || "toggle".equals(action)) {
                String idParam = req.getParameter("id");
                if (idParam != null && !idParam.isBlank()) {
                    service.deleteProduct(Integer.parseInt(idParam));
                }
                resp.sendRedirect(req.getContextPath() + "/admin/products");
                return;
            }
            req.setAttribute("editProduct", null);
            if ("edit".equals(action)) {
                String idParam = req.getParameter("id");
                if (idParam != null && !idParam.isBlank()) {
                    req.setAttribute("editProduct", service.findProduct(Integer.parseInt(idParam)));
                }
            }
            req.setAttribute("products", service.products());
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("products", Collections.emptyList());
            req.setAttribute("error", "Unable to manage products: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/admin/products.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if ("bulk_import".equals(req.getParameter("action"))) {
                String csvData = req.getParameter("csvData");
                if (csvData == null || csvData.isBlank()) {
                    throw new IllegalArgumentException("CSV data cannot be empty.");
                }
                String[] lines = csvData.split("\\r?\\n");
                int count = 0;
                for (String line : lines) {
                    line = line.trim();
                    if (line.isEmpty() || line.toLowerCase().startsWith("name,") || line.toLowerCase().startsWith("\"name\"")) {
                        continue;
                    }
                    String[] cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    if (cols.length >= 7) {
                        Product bp = new Product();
                        bp.setName(cols[0].replace("\"", "").trim());
                        bp.setBrand(cols[1].replace("\"", "").trim());
                        bp.setCategoryId(Integer.parseInt(cols[2].replace("\"", "").trim()));
                        bp.setPrice(Double.parseDouble(cols[3].replace("\"", "").trim()));
                        bp.setDiscount(Double.parseDouble(cols[4].replace("\"", "").trim()));
                        bp.setStock(Integer.parseInt(cols[5].replace("\"", "").trim()));
                        bp.setDescription(cols[6].replace("\"", "").trim());
                        if (cols.length > 7) {
                            bp.setImageUrl(cols[7].replace("\"", "").trim());
                        } else {
                            bp.setImageUrl("");
                        }
                        bp.setStatus(true);
                        service.createProduct(bp);
                        count++;
                    }
                }
                req.getSession().setAttribute("msg", count + " products imported successfully via bulk CSV upload!");
                resp.sendRedirect(req.getContextPath() + "/admin/products");
                return;
            }

            Product p = new Product();
            p.setCategoryId(Integer.parseInt(req.getParameter("categoryId")));
            p.setName(req.getParameter("name"));
            p.setBrand(req.getParameter("brand"));
            p.setDescription(req.getParameter("description"));
            p.setPrice(Double.parseDouble(req.getParameter("price")));
            p.setDiscount(Double.parseDouble(req.getParameter("discount")));
            p.setStock(Integer.parseInt(req.getParameter("stock")));
            p.setImageUrl(req.getParameter("imageUrl"));

            String statusParam = req.getParameter("status");
            boolean status = "update".equals(req.getParameter("action")) ? ("true".equalsIgnoreCase(statusParam) || "on".equalsIgnoreCase(statusParam)) : true;
            p.setStatus(status);

            if ("update".equals(req.getParameter("action"))) {
                p.setProductId(Integer.parseInt(req.getParameter("productId")));
                service.updateProduct(p);
            } else {
                service.createProduct(p);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("products", Collections.emptyList());
            req.setAttribute("error", "Product operation failed: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/admin/products.jsp").forward(req, resp);
        }
    }
}
