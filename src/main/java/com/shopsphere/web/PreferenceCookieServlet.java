package com.shopsphere.web;
import jakarta.servlet.annotation.WebServlet; import jakarta.servlet.http.*; import java.io.*;
@WebServlet("/preferences") public class PreferenceCookieServlet extends HttpServlet{
 protected void doGet(HttpServletRequest r,HttpServletResponse p)throws IOException{String theme=r.getParameter("theme");if(theme!=null){Cookie c=new Cookie("shopsphereTheme",theme);c.setMaxAge(60*60*24*30);p.addCookie(c);}p.sendRedirect(r.getContextPath()+"/");}
}