package com.shopsphere.web;
import jakarta.servlet.*; import jakarta.servlet.annotation.WebFilter; import java.io.IOException;
@WebFilter("/*") public class RequestLogFilter implements Filter{
 public void doFilter(ServletRequest r,ServletResponse s,FilterChain c)throws IOException,ServletException{long t=System.currentTimeMillis();try{c.doFilter(r,s);}finally{System.out.println("ShopSphere request completed in "+(System.currentTimeMillis()-t)+" ms");}}
}