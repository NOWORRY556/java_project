package com.shopsphere.i18n;
import java.text.NumberFormat; import java.util.*;
public class I18nDemo {
 public static void main(String[] args){double price=12999.50;for(Locale l:new Locale[]{Locale.ENGLISH,new Locale("hi","IN")})System.out.println(l+" "+NumberFormat.getCurrencyInstance(l).format(price));}
}