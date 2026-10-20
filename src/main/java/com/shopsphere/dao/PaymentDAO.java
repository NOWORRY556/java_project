package com.shopsphere.dao;
import com.shopsphere.config.DBConnection; import java.sql.*;
public class PaymentDAO{
 public int create(int orderId,String method,String ref,double amount,String status)throws SQLException{
  String q="INSERT INTO payments(order_id,payment_method,transaction_reference,amount,payment_status) VALUES(?,?,?,?,?)";
  try(Connection c=DBConnection.getConnection()){c.setAutoCommit(false);try(PreparedStatement s=c.prepareStatement(q,Statement.RETURN_GENERATED_KEYS)){s.setInt(1,orderId);s.setString(2,method);s.setString(3,ref);s.setDouble(4,amount);s.setString(5,status);s.executeUpdate();int id;try(ResultSet r=s.getGeneratedKeys()){r.next();id=r.getInt(1);}try(PreparedStatement o=c.prepareStatement("UPDATE orders SET payment_method=?,payment_status=? WHERE order_id=?")){o.setString(1,method);o.setString(2,status);o.setInt(3,orderId);o.executeUpdate();}c.commit();return id;}catch(SQLException e){c.rollback();throw e;}}
 }
}