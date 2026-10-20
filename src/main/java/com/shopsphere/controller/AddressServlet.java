package com.shopsphere.controller;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.model.User;
import com.shopsphere.security.InputValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/addresses")
public class AddressServlet extends HttpServlet {
    private final AddressDAO dao = new AddressDAO();

    private User user(HttpServletRequest r) {
        return (User) r.getSession().getAttribute("loggedInUser");
    }

    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {
        User u = user(r);
        if (u == null) {
            p.sendRedirect(r.getContextPath() + "/login");
            return;
        }
        try {
            r.setAttribute("addresses", dao.findByUser(u.getUserId()));
            r.getRequestDispatcher("/WEB-INF/views/addresses.jsp").forward(r, p);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Unable to load addresses", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {
        User u = user(r);
        if (u == null) {
            p.sendRedirect(r.getContextPath() + "/login");
            return;
        }

        try {
            String action = r.getParameter("action");
            if ("delete".equals(action)) {
                int id = InputValidator.positiveInt(r.getParameter("addressId"), "Address ID");
                boolean deleted = dao.delete(u.getUserId(), id);
                if (deleted) {
                    r.getSession().setAttribute("msg", "Address deleted successfully!");
                } else {
                    r.getSession().setAttribute("error", "Failed to delete address.");
                }
            } else {
                String line = InputValidator.maxLength(InputValidator.required(r.getParameter("addressLine"), "Address Line"), "Address", 255);
                String city = InputValidator.maxLength(InputValidator.required(r.getParameter("city"), "City"), "City", 100);
                String state = InputValidator.maxLength(InputValidator.required(r.getParameter("state"), "State"), "State", 100);
                String pin = InputValidator.pincode(r.getParameter("pincode"));
                dao.create(u.getUserId(), line, city, state, pin);
                r.getSession().setAttribute("msg", "Address saved successfully!");
            }
            p.sendRedirect(r.getContextPath() + "/addresses");
        } catch (IllegalArgumentException e) {
            r.getSession().setAttribute("error", e.getMessage());
            p.sendRedirect(r.getContextPath() + "/addresses");
        } catch (Exception e) {
            e.printStackTrace();
            r.getSession().setAttribute("error", "Unable to process address: " + e.getMessage());
            p.sendRedirect(r.getContextPath() + "/addresses");
        }
    }
}
