package com.shopsphere.network;

import java.io.*;
import java.net.Socket;

public final class InventorySocketClient {
    private InventorySocketClient() {}
    public static void main(String[] args) throws IOException {
        try (Socket socket=new Socket("localhost",5051);
             BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out=new PrintWriter(socket.getOutputStream(),true)) {
            for (String command : args.length == 0
                    ? new String[]{"GET_PRODUCT 1","GET_STOCK 1","SEARCH_PRODUCT Java","QUIT"} : args) {
                out.println(command);
                System.out.println(command+" -> "+in.readLine());
                if ("QUIT".equalsIgnoreCase(command.trim())) break;
            }
        }
    }
}
