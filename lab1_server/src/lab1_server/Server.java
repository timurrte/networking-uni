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

                // Regular expression to find the pattern "filename -d"
                Pattern pattern = Pattern.compile("^(.+) +-d$");

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if ("quit".equalsIgnoreCase(inputLine)) {
                        out.println("Server is shutting down.");
                        return;
                    }

                    // Check if the command to change the file is entered
                    Matcher matcher = pattern.matcher(inputLine);
                    if (matcher.matches()) {
                        // Extract the filename from the line
                        String currentFile = matcher.group(1).trim();
                        out.println("File changed to: " + currentFile);
                        FILENAME = currentFile;
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
    }
}

