package lab1_server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
	private BufferedWriter writer;
	public Writer(String fileName) throws IOException {
		this.writer = new BufferedWriter(new FileWriter(fileName));
	}
	
	public void writeLine(String line) throws IOException {
		this.writer.append(line);
		this.writer.newLine();
		this.writer.flush();
	}
	
	public void close() throws IOException {
		this.writer.close();
	}
}
