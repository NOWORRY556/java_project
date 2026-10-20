package com.shopsphere.dao;
import com.shopsphere.config.DBConnection; import com.shopsphere.model.User; import java.sql.*;
public class UserDAO{
 private static final String FIND="SELECT user_id,name,email,password,mobile,role,status FROM users WHERE email=?";
 private static final String INSERT="INSERT INTO users(name,email,password,mobile,role,status) VALUES(?,?,?,?, 'CUSTOMER', TRUE)";
 public boolean existsEmail(String email)throws SQLException{try(Connection c=DBConnection.getConnection();PreparedStatement s=c.prepareStatement("SELECT 1 FROM users WHERE email=?")){s.setString(1,email);try(ResultSet r=s.executeQuery()){return r.next();}}}
 public User findByEmail(String email)throws SQLException{try(Connection c=DBConnection.getConnection();PreparedStatement s=c.prepareStatement(FIND)){s.setString(1,email);try(ResultSet r=s.executeQuery()){if(!r.next())return null;User u=new User();u.setUserId(r.getInt(1));u.setName(r.getString(2));u.setEmail(r.getString(3));u.setPassword(r.getString(4));u.setMobile(r.getString(5));u.setRole(r.getString(6));u.setStatus(r.getBoolean(7));return u;}}}
 public boolean create(User u)throws SQLException{try(Connection c=DBConnection.getConnection();PreparedStatement s=c.prepareStatement(INSERT)){s.setString(1,u.getName());s.setString(2,u.getEmail());s.setString(3,u.getPassword());s.setString(4,u.getMobile());return s.executeUpdate()==1;}}
 public void updateProfile(int id,String name,String mobile)throws SQLException{try(Connection c=DBConnection.getConnection();PreparedStatement s=c.prepareStatement("UPDATE users SET name=?,mobile=? WHERE user_id=?")){s.setString(1,name);s.setString(2,mobile);s.setInt(3,id);s.executeUpdate();}}
 public void updatePassword(int id,String hash)throws SQLException{try(Connection c=DBConnection.getConnection();PreparedStatement s=c.prepareStatement("UPDATE users SET password=? WHERE user_id=?")){s.setString(1,hash);s.setInt(2,id);s.executeUpdate();}}
}