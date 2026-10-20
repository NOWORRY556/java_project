package com.shopsphere.network;
import java.io.*; import java.net.*;
public class ShopSocketServer {
 public static void main(String[] args)throws IOException{
  try(ServerSocket server=new ServerSocket(5050)){System.out.println("ShopSphere Socket Server listening on 5050");
   while(true){Socket client=server.accept();new Thread(()->handle(client)).start();}
  }
 }
 private static void handle(Socket socket){try(Socket s=socket;BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));PrintWriter out=new PrintWriter(s.getOutputStream(),true)){String line;while((line=in.readLine())!=null){if("quit".equalsIgnoreCase(line))break;out.println("ShopSphere Server: "+line);}}catch(IOException e){e.printStackTrace();}}
}