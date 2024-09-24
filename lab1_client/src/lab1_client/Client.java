package lab1_client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8189;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the server.");

            while (true) {
                String input = scanner.nextLine();
                
                if ("-q".matches(input)) {
                	in.close();
                	out.close();
                	scanner.close();
                	socket.close();
                	System.out.println("Exiting program.");
                	break;
                }
                
                if ("-h".matches(input)) {
                	
                	printHelp(out);
                	continue;
                }
                
                out.println(input);
            	
                String response = in.readLine();
                System.out.println(response);
            }
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
    
    private static void printHelp(PrintWriter out) {
        System.out.println("Available commands:\n" 
    + "-h            Show this help message\n" 
    + "<filename> -d Select file to save text\n" 
    + "-q            Exit\n");
    }
}
