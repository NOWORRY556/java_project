package com.shopsphere.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet("/language")
public class I18nServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException, ServletException{
        String language=req.getParameter("lang");
        Locale locale="hi".equalsIgnoreCase(language)?Locale.forLanguageTag("hi-IN"):Locale.ENGLISH;
        ResourceBundle bundle=ResourceBundle.getBundle("messages",locale);
        NumberFormat money=NumberFormat.getCurrencyInstance(locale);
        req.setAttribute("greeting",bundle.getString("greeting"));
        req.setAttribute("price",money.format(799));
        req.setAttribute("today",LocalDate.now().toString());
        req.getRequestDispatcher("/WEB-INF/views/i18n-demo.jsp").forward(req,resp);
    }
}
