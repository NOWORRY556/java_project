package com.shopsphere.controller;

import com.shopsphere.dao.ReviewDAO;
import com.shopsphere.model.User;
import com.shopsphere.security.InputValidator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/reviews")
public class ReviewServlet extends HttpServlet {
    private final ReviewDAO dao=new ReviewDAO();

    protected void doPost(HttpServletRequest r,HttpServletResponse p)throws IOException{
        try{
            User u=(User)r.getSession().getAttribute("loggedInUser");
            int productId=InputValidator.positiveInt(r.getParameter("productId"),"Product");
            int rating=InputValidator.positiveInt(r.getParameter("rating"),"Rating");
            if(rating>5) throw new IllegalArgumentException("Rating must be between 1 and 5.");
            String text=r.getParameter("reviewText");
            if(text!=null && text.trim().length()>2000) throw new IllegalArgumentException("Review must be at most 2000 characters.");
            dao.add(u.getUserId(),productId,rating,text==null?"":text.trim());
            p.sendRedirect(r.getContextPath()+"/product?id="+productId);
        }catch(IllegalArgumentException e){
            p.sendError(HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
        }catch(Exception e){throw new IOException("Unable to submit review",e);}
    }
}
