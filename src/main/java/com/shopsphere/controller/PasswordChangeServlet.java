package com.shopsphere.controller;
import com.shopsphere.dao.UserDAO; import com.shopsphere.model.User; import com.shopsphere.security.PasswordPolicy; import com.shopsphere.util.PasswordUtil;
import jakarta.servlet.*; import jakarta.servlet.annotation.WebServlet; import jakarta.servlet.http.*; import java.io.IOException;
@WebServlet("/password")
public class PasswordChangeServlet extends HttpServlet {
 private final UserDAO users=new UserDAO();
 protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{req.getRequestDispatcher("/WEB-INF/views/password.jsp").forward(req,resp);}
 protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
  User u=(User)req.getSession().getAttribute("loggedInUser");
  try{String current=req.getParameter("currentPassword"),next=req.getParameter("newPassword"); User stored=users.findByEmail(u.getEmail());
   if(stored==null||current==null||!PasswordUtil.verify(current,stored.getPassword()))throw new IllegalArgumentException("Current password is incorrect.");
   PasswordPolicy.validate(next); users.updatePassword(u.getUserId(),PasswordUtil.hash(next)); req.setAttribute("success","Password changed successfully.");
  }catch(IllegalArgumentException e){req.setAttribute("error",e.getMessage());}catch(Exception e){throw new ServletException("Unable to change password",e);}
  req.getRequestDispatcher("/WEB-INF/views/password.jsp").forward(req,resp);
 }
}