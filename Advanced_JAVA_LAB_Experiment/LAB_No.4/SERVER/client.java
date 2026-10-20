import java.io.*;
import java.net.*;

public class client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("Starting client...");

        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
             
            System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + PORT);
            System.out.print("Enter message to send to server: ");
            String userInput = stdIn.readLine();
            
            if (userInput != null) {
                out.println(userInput);
                System.out.println("Message sent.");
                
                String response = in.readLine();
                System.out.println("Server replied: " + response);
            }
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + SERVER_ADDRESS);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + SERVER_ADDRESS);
            e.printStackTrace();
        }
        
        System.out.println("Client execution completed.");
    }
}