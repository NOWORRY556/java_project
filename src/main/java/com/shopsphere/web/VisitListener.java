package com.shopsphere.web;
import jakarta.servlet.ServletContextEvent; import jakarta.servlet.ServletContextListener; import jakarta.servlet.annotation.WebListener;
@WebListener public class VisitListener implements ServletContextListener{
 public void contextInitialized(ServletContextEvent e){e.getServletContext().setAttribute("applicationStartedAt",System.currentTimeMillis());}
}