package com.shopsphere.network;
import java.io.*; import java.net.*;
public class ShopSocketClient {
 public static void main(String[] args)throws IOException{try(Socket s=new Socket("localhost",5050);BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));PrintWriter out=new PrintWriter(s.getOutputStream(),true)){out.println("Hello ShopSphere");System.out.println(in.readLine());}}
}