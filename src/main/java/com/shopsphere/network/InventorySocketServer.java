package com.shopsphere.network;

import java.io.*;
import java.net.*;
import java.util.Map;

public final class InventorySocketServer {
    private static final Map<Integer, String> PRODUCTS = Map.of(1, "Java Programming", 2, "Wireless Mouse");
    private static final Map<Integer, Integer> STOCK = Map.of(1, 25, 2, 50);

    private InventorySocketServer() {}
    public static String handle(String command) {
        if (command == null) return "ERROR: empty command";
        String[] parts = command.trim().split("\\s+", 2);
        try {
            return switch (parts[0].toUpperCase()) {
                case "GET_PRODUCT" -> parts.length == 2 && PRODUCTS.containsKey(Integer.valueOf(parts[1]))
                        ? PRODUCTS.get(Integer.valueOf(parts[1])) : "NOT_FOUND";
                case "GET_STOCK" -> parts.length == 2 && STOCK.containsKey(Integer.valueOf(parts[1]))
                        ? String.valueOf(STOCK.get(Integer.valueOf(parts[1]))) : "NOT_FOUND";
                case "SEARCH_PRODUCT" -> parts.length == 2
                        ? PRODUCTS.entrySet().stream().filter(e -> e.getValue().toLowerCase().contains(parts[1].toLowerCase()))
                        .map(e -> e.getKey()+":"+e.getValue()).findFirst().orElse("NOT_FOUND") : "ERROR";
                case "QUIT" -> "BYE";
                default -> "ERROR: supported commands GET_PRODUCT, GET_STOCK, SEARCH_PRODUCT, QUIT";
            };
        } catch (NumberFormatException e) {
            return "ERROR: product id must be numeric";
        }
    }
    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(5051)) {
            System.out.println("ShopSphere Inventory Socket Server listening on 5051");
            while (true) {
                Socket client = server.accept();
                new Thread(() -> {
                    try (Socket socket=client;
                         BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                         PrintWriter out=new PrintWriter(socket.getOutputStream(), true)) {
                        String line;
                        while ((line=in.readLine())!=null) {
                            String response=handle(line);
                            out.println(response);
                            if ("QUIT".equalsIgnoreCase(line.trim())) break;
                        }
                    } catch (IOException ignored) {}
                }, "inventory-client").start();
            }
        }
    }
}
