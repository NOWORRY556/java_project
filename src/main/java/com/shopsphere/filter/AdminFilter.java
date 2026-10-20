package com.shopsphere.filter;

import com.shopsphere.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns={"/admin", "/admin/*"})
public class AdminFilter implements Filter {
    @Override 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("loggedInUser");
        if (user == null) { 
            resp.sendRedirect(req.getContextPath() + "/login"); 
            return; 
        }
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) { 
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin access required"); 
            return; 
        }
        chain.doFilter(request, response);
    }
}
