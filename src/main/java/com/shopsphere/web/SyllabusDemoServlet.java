package com.shopsphere.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet("/syllabus/demo/*")
public class SyllabusDemoServlet extends HttpServlet {
    private static final Map<String,String> PAGES=Map.of(
        "jsp", "/WEB-INF/views/jsp-syntax-demo.jsp",
        "components", "/WEB-INF/views/jsp-components-demo.jsp",
        "xml", "/WEB-INF/views/jstl-xml-demo.jsp",
        "sql", "/WEB-INF/views/jstl-sql-demo.jsp"
    );
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        String path=req.getPathInfo();
        String page=path==null?null:PAGES.get(path.replaceFirst("^/",""));
        if(page==null){resp.sendError(404,"Unknown syllabus demo");return;}
        req.getRequestDispatcher(page).forward(req,resp);
    }
}
