package com.shopsphere.security;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * CSRF Protection Filter.
 * Generates a per-session CSRF token and validates it on every state-changing POST request.
 * Safe methods (GET, HEAD, OPTIONS) and public endpoints are exempted.
 */
@WebFilter("/*")
public class CsrfFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(CsrfFilter.class.getName());
    private static final String TOKEN_ATTR = "csrfToken";
    private static final String TOKEN_PARAM = "_csrf";
    private static final SecureRandom RANDOM = new SecureRandom();

    /** Endpoints that accept POST without CSRF (e.g. OAuth callbacks, payment webhooks) */
    private static final String[] EXEMPT_PATHS = {
        "/google-callback", "/razorpay-webhook"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String method = req.getMethod().toUpperCase();

        // Ensure a CSRF token exists in the session
        HttpSession session = req.getSession(true);
        String token = (String) session.getAttribute(TOKEN_ATTR);
        if (token == null || token.isBlank()) {
            token = generateToken();
            session.setAttribute(TOKEN_ATTR, token);
        }

        // Make token available to JSP views via request attribute
        req.setAttribute(TOKEN_ATTR, token);

        // Safe methods — pass through
        if ("GET".equals(method) || "HEAD".equals(method) || "OPTIONS".equals(method)) {
            chain.doFilter(request, response);
            return;
        }

        // Check exempt paths
        String path = req.getServletPath();
        for (String exempt : EXEMPT_PATHS) {
            if (path.equals(exempt)) {
                chain.doFilter(request, response);
                return;
            }
        }

        // Validate CSRF token on POST
        String submitted = req.getParameter(TOKEN_PARAM);
        if (submitted == null || !submitted.equals(token)) {
            LOGGER.warning("[CSRF BLOCKED] Invalid or missing CSRF token on " + method + " " + path
                    + " from " + req.getRemoteAddr());
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid security token. Please refresh the page and try again.");
            return;
        }

        // Rotate token after successful validation (one-time use)
        session.setAttribute(TOKEN_ATTR, generateToken());
        req.setAttribute(TOKEN_ATTR, session.getAttribute(TOKEN_ATTR));

        chain.doFilter(request, response);
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
