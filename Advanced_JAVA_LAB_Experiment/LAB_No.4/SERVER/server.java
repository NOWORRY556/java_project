import java.io.*;
import java.net.*;

public class server {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("Starting server on port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running and waiting for client requests...");

            // Keep the server running to accept multiple clients consecutively
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    
                    System.out.println("\nClient connected: " + clientSocket.getInetAddress().getHostAddress());

                    String message = in.readLine();
                    if (message != null) {
                        System.out.println("Received from client: " + message);
                        out.println("Message received by server");
                    } else {
                        System.out.println("Client disconnected without sending a message.");
                    }
                    
                    System.out.println("Connection closed for client: " + clientSocket.getInetAddress().getHostAddress());
                } catch (IOException e) {
                    System.err.println("Error handling client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: Could not listen on port " + PORT);
            e.printStackTrace();
        }
    }
}