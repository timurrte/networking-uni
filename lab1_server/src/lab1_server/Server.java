package lab1_server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    private static final int PORT = 8189;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    // Create a new thread for each client connection
                    new Thread(new ClientHandler(socket)).start();
                } catch (IOException e) {
                    System.out.println("Error accepting client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {

        private final Socket socket;
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
        	String FILENAME = "server_log.txt";
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                // Regular expressions
                Pattern pattern_dir = Pattern.compile("^(.+) +-d$");
                Pattern pattern_q = Pattern.compile("(^|\\s)-q($|\\s)");
                Pattern pattern_h = Pattern.compile("(^|\\s)-h($|\\s)");
                
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if ("quit".equalsIgnoreCase(inputLine)) {
                        out.println("Server is shutting down.");
                        return;
                    }

                    // Check if the command to change the file is entered
                    Matcher matcher_dir = pattern_dir.matcher(inputLine);
                    if (matcher_dir.matches()) {
                        // Extract the filename from the line
                        String currentFile = matcher_dir.group(1).trim();
                        out.println("Log file changed to: " + currentFile);
                        FILENAME = currentFile;
                        continue;
                    }
                    
                    Matcher matcher_q = pattern_q.matcher(inputLine);
                    if (matcher_q.matches()) {
                    	
                        System.out.println("Exiting...");
                        break;
                    }
                    
                    Matcher matcher_h = pattern_h.matcher(inputLine);
                    if (matcher_h.matches()) {
                        printHelp(out);
                        continue;
                    }

                    // Write to the file
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME, true))) {
                        writer.write(inputLine);
                        writer.newLine();
                        writer.flush();
                    }

                    out.println("Message received. Characters saved: " + inputLine.length());
                }
            } catch (IOException e) {
                System.out.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing socket: " + e.getMessage());
                }
            }
        }
        
        private static void printHelp(PrintWriter out) {
            out.println("Available commands:\n" 
        + "-h            Show this help message\n" 
        + "<filename> -d Select file to save text\n" 
        + "-q            Exit\n");
        }
    }
}

