package lab1_client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Sender {
	BufferedWriter writer;
	Socket socket;
	public Sender(int port) throws IOException {
		this.socket = new Socket("localhost", port);
		this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	
	public void send(String s) throws IOException {
		this.writer.write(s);
		this.writer.newLine();
		this.writer.flush();
	}
	
	public void close() throws IOException {
		this.writer.close();
		this.socket.close();
	}
}
