package com.shopsphere.jndi;
import javax.naming.InitialContext;
public class JndiExample { public static Object lookupDataSource() throws Exception{return new InitialContext().lookup("java:comp/env/jdbc/ShopSphereDB");} }