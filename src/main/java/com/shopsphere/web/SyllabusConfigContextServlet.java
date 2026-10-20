package com.shopsphere.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value={"/syllabus", "/syllabus/config-context"}, initParams={
        @WebInitParam(name="moduleName", value="ServletConfig and ServletContext")
})
public class SyllabusConfigContextServlet extends HttpServlet {
    @Override 
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        config.getServletContext().setAttribute("shopSphereAppName", "ShopSphere");
    }

    @Override 
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletConfig config = getServletConfig();
        ServletContext context = getServletContext();
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().printf(
            "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><title>Syllabus Overview — ShopSphere</title><link rel='stylesheet' href='%s/assets/css/app.css'></head><body><main class='container section'><div class='card' style='margin-top:3rem;'><h1>%s</h1><p class='muted'>Init parameter: %s</p><p class='muted'>Application attribute: %s</p><a class='btn' style='margin-top:1.5rem;' href='%s/'>← Back to Storefront</a></div></main></body></html>",
            req.getContextPath(),
            "ServletConfig / ServletContext Syllabus Module",
            config.getInitParameter("moduleName"),
            context.getAttribute("shopSphereAppName"),
            req.getContextPath()
        );
    }
}
