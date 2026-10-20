package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.security.InputValidator; import com.shopsphere.security.LoginRateLimiter;
import com.shopsphere.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService authService=new AuthService();

    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req,resp);
    }

    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException {
        try {
            String email=InputValidator.email(req.getParameter("email"));
            String rateKey=req.getRemoteAddr()+"|"+email;
            if(!LoginRateLimiter.allow(rateKey)){req.setAttribute("error","Too many login attempts. Please wait a minute.");req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req,resp);return;}
            String password=InputValidator.required(req.getParameter("password"),"Password");
            User user=authService.login(email,password);
            if(user==null) {
                req.setAttribute("error","Invalid email or password.");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req,resp);
                return;
            }
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) oldSession.invalidate();
            req.getSession(true).setAttribute("loggedInUser",user);
            resp.sendRedirect(req.getContextPath()+"/products");
        } catch(IllegalArgumentException e) {
            req.setAttribute("error",e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req,resp);
        } catch(Exception e) {
            throw new ServletException("Login failed",e);
        }
    }
}
