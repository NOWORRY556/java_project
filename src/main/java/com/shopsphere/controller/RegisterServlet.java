package com.shopsphere.controller;

import com.shopsphere.security.InputValidator;
import com.shopsphere.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final AuthService authService=new AuthService();

    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req,resp);
    }

    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException {
        try {
            String name=InputValidator.maxLength(InputValidator.required(req.getParameter("name"),"Name"),"Name",100);
            String email=InputValidator.email(req.getParameter("email"));
            String password=InputValidator.required(req.getParameter("password"),"Password");
            String mobile=InputValidator.mobile(req.getParameter("mobile"));
            boolean created=authService.register(name,email,password,mobile);
            if(!created) {
                req.setAttribute("error","Registration failed. Check details or email may already exist.");
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req,resp);
                return;
            }
            com.shopsphere.service.EmailService.sendWelcomeEmailAsync(email, name);
            resp.sendRedirect(req.getContextPath()+"/login?registered=true");
        } catch(IllegalArgumentException e) {
            req.setAttribute("error",e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req,resp);
        } catch(Exception e) {
            throw new ServletException("Registration failed",e);
        }
    }
}
