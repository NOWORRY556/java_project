package com.shopsphere.security;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class SecurityHeadersFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse http = (HttpServletResponse) response;
        http.setHeader("X-Content-Type-Options", "nosniff");
        if (request instanceof jakarta.servlet.http.HttpServletRequest req && req.getSession(false) != null) { http.setHeader("Cache-Control", "no-store"); }
        http.setHeader("X-Frame-Options", "DENY");
        http.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        http.setHeader("Permissions-Policy", "camera=(), microphone=(), geolocation=()");
        http.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        http.setHeader("Content-Security-Policy",
                "default-src 'self'; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
                "script-src 'self' 'unsafe-inline' https://checkout.razorpay.com https://cdn.jsdelivr.net; " +
                "img-src 'self' data: https:; font-src 'self' data: https://fonts.gstatic.com; " +
                "connect-src 'self' https://api.razorpay.com https://lux-checkout.razorpay.com; " +
                "frame-src https://api.razorpay.com; form-action 'self'; frame-ancestors 'none'; base-uri 'self'");
        chain.doFilter(request, response);
    }
}
