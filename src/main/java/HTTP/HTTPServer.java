package HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class HTTPServer implements Runnable {
	public static final int PORT = 8080;
	private Socket connection;
	private String header;
	private String fileToRead;
	private String method;
	private String fileRequested;
	
	public HTTPServer(Socket s) {
		connection = s;
	}
	
	public static void main(String[] args) throws IOException {
		try(ServerSocket server = new ServerSocket(PORT);) {
			System.out.println("Listening on POR : " + PORT);
			int c = 0;
			while(true) {
					HTTPServer client = new HTTPServer(server.accept());
					System.out.println(c + " Connection opened @ " + new Date());
					Thread t = new Thread(client);
					t.start();
					c++;
			}
		}
	}

	@Override
	public void run() {
		try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				PrintWriter out = new PrintWriter(connection.getOutputStream());
				) {

			String line = in.readLine(); 
			getRequest(line);
			System.out.println("METHOD : " + method + "\t FILEREQUESTED : " + fileRequested);
			System.out.println("!!!!! REQUEST !!!!!");
			
			readRequest(line, in);
			setResponse();
			String httpResponse = header + '\t' + new Date().toString() + "\r\n\r\n" + readFile(fileToRead);
			
			out.println(httpResponse);
			
			System.out.println("@@@@@ RESPONSE @@@@@\n" + httpResponse);
			
		} catch (IOException e) {
			System.out.println("IO Exception");
		}
		finally {
			System.out.println("CLOSING CLIENT CONNECTION @ " + new Date() + "\n~~~~~~~~~~\n");
		}
		
	}
	
	public void getRequest(String line) {
		String[] split = line.split(" ");
		method = split[0];
		fileRequested = split[1];
	}
	
	public void setResponse() {
		if (!(method.equals("GET") || method.equals("POST"))) {
			header = "HTTP/1.1 405 METHOD NOT ALLOWED";
			fileToRead = "error.html";
		}
		else {
			header = "HTTP/1.1 200 OK";
			fileToRead = "index.html";
		}
	}
	
	public void readRequest(String line, BufferedReader in) {
		while (!line.isEmpty()) { 
			System.out.println(line); 
			try {
				line = in.readLine();
			} catch (IOException e) {
				System.out.println("IO Exception");
			}
		}
	}
	
	public String readFile(String file) {
		String line;
		String fileData = "";
		try (BufferedReader f = Files.newBufferedReader(Paths.get(file), StandardCharsets.ISO_8859_1)) {
			while((line = f.readLine()) != null) {
				fileData += line;
			}
		} catch (IOException e) {
			System.out.println("IO Exception!");
		}		
		return fileData;
	}
}
