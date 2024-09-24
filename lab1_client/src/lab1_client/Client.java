package lab1_client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8189;
    private static String currentFile = "default_client_log.txt";

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the server.");
            printHelp();

            while (true) {
                String input = scanner.nextLine();

                if ("-q".equalsIgnoreCase(input)) {
                    System.out.println("Exiting...");
                    break;
                }

                if ("-h".equalsIgnoreCase(input)) {
                    printHelp();
                    continue;
                }

                // Відправка тексту на сервер
                out.println(input);

                // Отримання відповіді від сервера
                String response = in.readLine();
                System.out.println(response);

                // Запис у вибраний файл
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile, true))) {
                    writer.write(input);
                    writer.newLine();
                    writer.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("-h            Show this help message");
        System.out.println("<filename> -d Select file to save text");
        System.out.println("-q            Exit");
    }
}
