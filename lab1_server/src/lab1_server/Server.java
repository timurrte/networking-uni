package lab1_server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    private static final int PORT = 8189;
    private static String FILENAME = "server_log.txt";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                	// Регулярний вираз для пошуку шаблону "ім'я_файла -d"
                    Pattern pattern = Pattern.compile("^(.+) +-d$");
                	
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        if ("quit".equalsIgnoreCase(inputLine)) {
                            out.println("Server is shutting down.");
                            return;
                        }
                        
                        // Перевіряємо чи введено команду зміни файлу
                        Matcher matcher = pattern.matcher(inputLine);
                        if (matcher.matches()) {
                            // Витягуємо ім'я файлу з рядка
                            String currentFile = matcher.group(1).trim();
                            out.println("File changed to: " + currentFile);
                            FILENAME = currentFile;
                            continue;
                        }

                        // Запис в файл
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME, true))) {
                            writer.write(inputLine);
                            writer.newLine();
                            writer.flush();
                        }

                        out.println("Message received. Characters saved: " + inputLine.length());
                    }
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
