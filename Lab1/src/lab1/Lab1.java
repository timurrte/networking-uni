/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
/**
 *
 * @author Пользователь
 */
public class Lab1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            ServerSocket serv = new ServerSocket(8189);
            Socket income = serv.accept();
            InputStream instream = income.getInputStream();
            OutputStream outstream = income.getOutputStream();
            Scanner scan = new Scanner(instream);
            PrintWriter pw = new PrintWriter(outstream, true);
            
            while(scan.hasNextLine()){String line = scan.nextLine();
            System.out.println(line);
            }
            }
        catch(Exception e){
        System.out.println(e.getMessage());
        }
    }
}
