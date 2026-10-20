package com.shopsphere.controller;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import com.shopsphere.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet({"/google-oauth", "/google-callback"})
public class GoogleOAuthServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(GoogleOAuthServlet.class.getName());
    private final UserDAO userDao = new UserDAO();

    // Read credentials strictly from Environment Variables / Azure App Settings
    private String getClientId() {
        String cid = System.getenv("GOOGLE_CLIENT_ID");
        if (cid != null && !cid.isBlank()) return cid.trim();
        return System.getProperty("GOOGLE_CLIENT_ID", "");
    }

    private String getClientSecret() {
        String cs = System.getenv("GOOGLE_CLIENT_SECRET");
        if (cs != null && !cs.isBlank()) return cs.trim();
        return System.getProperty("GOOGLE_CLIENT_SECRET", "");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        String clientId = getClientId();
        String clientSecret = getClientSecret();

        if ("/google-oauth".equals(servletPath)) {
            if (clientId.isBlank()) {
                LOGGER.warning("[GOOGLE OAUTH ERROR] GOOGLE_CLIENT_ID environment variable is missing.");
                req.setAttribute("error", "Google OAuth credentials are not configured on server.");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;
            }

            String redirectUri = getRedirectUri(req);
            String googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&response_type=code"
                + "&scope=" + URLEncoder.encode("openid email profile", StandardCharsets.UTF_8)
                + "&prompt=select_account";

            LOGGER.info("[GOOGLE OAUTH REDIRECT] Redirecting user to Google Auth: " + googleAuthUrl);
            resp.sendRedirect(googleAuthUrl);
            return;
        }

        if ("/google-callback".equals(servletPath)) {
            String code = req.getParameter("code");
            String error = req.getParameter("error");

            if (error != null || code == null || code.isBlank()) {
                LOGGER.warning("[GOOGLE OAUTH CALLBACK ERROR] Code missing or user cancelled: " + error);
                req.setAttribute("error", "Google authentication was cancelled or failed.");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;
            }

            String redirectUri = getRedirectUri(req);
            String accessToken = exchangeCodeForAccessToken(code, redirectUri, clientId, clientSecret);

            if (accessToken == null || accessToken.isBlank()) {
                LOGGER.warning("[GOOGLE OAUTH CALLBACK ERROR] Failed to exchange auth code for access token.");
                req.setAttribute("error", "Google authentication failed (Token exchange error).");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;
            }

            String[] googleProfile = fetchGoogleUserInfo(accessToken);
            if (googleProfile == null || googleProfile[0] == null || googleProfile[0].isBlank()) {
                LOGGER.warning("[GOOGLE OAUTH CALLBACK ERROR] Failed to fetch profile from Google.");
                req.setAttribute("error", "Google authentication failed (Profile fetch error).");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;
            }

            String email = googleProfile[0];
            String name = googleProfile[1];

            processGoogleLogin(email, name, req, resp);
        }
    }

    private String getRedirectUri(HttpServletRequest req) {
        String serverName = req.getServerName();
        String contextPath = req.getContextPath();
        if (contextPath == null) contextPath = "";

        if ("localhost".equalsIgnoreCase(serverName) || "127.0.0.1".equals(serverName)) {
            int port = req.getServerPort();
            return "http://" + serverName + (port != 80 && port != 443 ? ":" + port : "") + contextPath + "/google-callback";
        } else {
            return "https://" + serverName + contextPath + "/google-callback";
        }
    }

    private String exchangeCodeForAccessToken(String code, String redirectUri, String clientId, String clientSecret) {
        try {
            URL url = new URL("https://oauth2.googleapis.com/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String postParams = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)
                + "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                + "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8)
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&grant_type=authorization_code";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = postParams.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder json = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) json.append(line);
                    return extractJsonField(json.toString(), "access_token");
                }
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder json = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) json.append(line);
                    LOGGER.warning("[GOOGLE TOKEN EXCHANGE FAIL] Response: " + responseCode + " - " + json);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Exception during Google token exchange", e);
        }
        return null;
    }

    private String[] fetchGoogleUserInfo(String accessToken) {
        try {
            URL url = new URL("https://www.googleapis.com/oauth2/v3/userinfo");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            if (conn.getResponseCode() == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder json = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) json.append(line);
                    String body = json.toString();
                    String email = extractJsonField(body, "email");
                    String name = extractJsonField(body, "name");
                    return new String[]{email, name};
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to fetch Google userinfo", e);
        }
        return null;
    }

    private String extractJsonField(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int idx = json.indexOf(searchKey);
        if (idx == -1) return null;
        int start = idx + searchKey.length();
        while (start < json.length() && (json.charAt(start) == ' ' || json.charAt(start) == '"')) {
            start++;
        }
        int end = start;
        while (end < json.length() && json.charAt(end) != '"' && json.charAt(end) != ',' && json.charAt(end) != '}') {
            end++;
        }
        return json.substring(start, end).trim();
    }

    private void processGoogleLogin(String email, String name, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            email = email.trim().toLowerCase();
            if (name == null || name.isBlank()) {
                name = email.split("@")[0];
            }

            User user = userDao.findByEmail(email);
            if (user == null) {
                user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPassword(PasswordUtil.hash("G_OAUTH_" + UUID.randomUUID().toString()));
                user.setMobile("");
                user.setRole("CUSTOMER");
                user.setStatus(true);

                userDao.create(user);
                user = userDao.findByEmail(email);

                com.shopsphere.service.EmailService.sendWelcomeEmailAsync(email, name);
            } else if (!user.isStatus()) {
                req.setAttribute("error", "Your account has been deactivated by an administrator. Please contact support.");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;
            }

            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) oldSession.invalidate();
            HttpSession session = req.getSession(true);
            session.setAttribute("loggedInUser", user);
            LOGGER.info("[GOOGLE OAUTH VERIFIED SUCCESS] User logged in: " + email);

            resp.sendRedirect(req.getContextPath() + "/");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Google login error", e);
            req.setAttribute("error", "Google authentication failed.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}
