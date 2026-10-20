package com.shopsphere.config;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

public final class DBConnection {
 private static final String DEFAULT_URL="jdbc:mysql://localhost:3306/shopsphere?useSSL=false&serverTimezone=UTC";
 private static volatile DataSource dataSource;
 private DBConnection(){}
 static{try{Class.forName("com.mysql.cj.jdbc.Driver");}catch(ClassNotFoundException e){throw new ExceptionInInitializerError(e);}}
 public static Connection getConnection()throws SQLException{
  String envUrl=System.getenv("SHOPSPHERE_DB_URL");
  if(envUrl!=null&&!envUrl.isBlank()){
   String user=required("SHOPSPHERE_DB_USER","root"),password=required("SHOPSPHERE_DB_PASSWORD","Krishna");
   return DriverManager.getConnection(envUrl,user,password);
  }
  DataSource ds=lookupDataSource();
  if(ds!=null){
   try{ return ds.getConnection(); }catch(SQLException ignored){}
  }
  String url=required("SHOPSPHERE_DB_URL",DEFAULT_URL),user=required("SHOPSPHERE_DB_USER","root"),password=required("SHOPSPHERE_DB_PASSWORD","Krishna");
  return DriverManager.getConnection(url,user,password);
 }
 private static DataSource lookupDataSource(){if(dataSource!=null)return dataSource;try{dataSource=(DataSource)new InitialContext().lookup("java:comp/env/jdbc/ShopSphereDB");return dataSource;}catch(NamingException ignored){return null;}}
 private static String required(String key,String fallback){String v=System.getenv(key);if(v==null||v.isBlank()){if(fallback!=null)return fallback;throw new IllegalStateException(key+" environment variable is required.");}return v;}
}
