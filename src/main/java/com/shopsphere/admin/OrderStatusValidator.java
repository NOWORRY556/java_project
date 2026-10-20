package com.shopsphere.admin;
import java.util.Set;
public final class OrderStatusValidator{
 private static final Set<String> ALLOWED=Set.of("PLACED","PROCESSING","CONFIRMED","SHIPPED","DELIVERED","CANCELLED");
 private OrderStatusValidator(){}
 public static boolean isAllowed(String status){return status!=null&&ALLOWED.contains(status.toUpperCase());}
 public static String normalize(String status){if(!isAllowed(status))throw new IllegalArgumentException("Unsupported order status");return status.toUpperCase();}
}
