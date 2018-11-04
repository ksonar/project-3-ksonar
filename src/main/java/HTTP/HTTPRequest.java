package HTTP;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

public class HTTPRequest {
	private String line;
	private String requestType;
	private String[] fullPath;
	private String fullQuery;
	private String path;
	private String HTTP;
	String headers = "";
	// BufferedReader in
	public HTTPRequest(String line, InputStream in, Socket connection) throws IOException {
		this.line = line;
		System.out.println("LINE FROM WITHIN REQUEST : " + line);

		setRequest(line);
		setRequestHeaders(line, in, connection);
	}
	
	public String getRequestType() { return requestType; }
	public String getRequestPath() { return path; }
	public String[] getRequestFullPath() { return fullPath; }
	public String getRequestFullQuery() { return fullQuery; }
	public void getRequest() { 
		if(fullPath.length > 1)
			System.out.println(requestType + " " + path + " " + fullQuery.toString() + " " + HTTP);
		else
			System.out.println(requestType + " " + path + " " + HTTP);
		}
	public void getRequestBody() { System.out.println(headers);}
	
	public void setRequest(String line) {
		System.out.println("---->" + line);
		String[] split = line.split(" ");
		if (split.length != 3) {
			System.out.println("Invalid request length, follow protocol");
			System.exit(1);
		}
		requestType = split[0];
		fullPath = split[1].split("\\?");
		path = fullPath[0];
		if(fullPath.length > 1) {
			fullQuery = fullPath[1];
		}
		HTTP = split[2];
	}
	//BufferedReader in
	public void setRequestHeaders(String line, InputStream in, Socket connection) throws IOException {
		int length = 0;
		while (!line.trim().isEmpty()) { 
			try {
				//line = in.readLine();
				line = oneLine(in);
				if(line.startsWith("Content-Length:")) {
					length = Integer.parseInt(line.split(":")[1].trim());
				}
				headers += line + '\n';

			} catch (IOException e) {
				System.out.println("IO Exception");
			}
		}
		byte[] bytes = new byte[length];
		int read = connection.getInputStream().read(bytes);
		
		while(read < length) {
			read += connection.getInputStream().read(bytes, read, (bytes.length-read));
		}
		
		System.out.println("Bytes expected: " + length + " Bytes read: " + read);			
		
		
	}
	
	private static String oneLine(InputStream instream) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte b = (byte) instream.read();
		while(b != '\n') {
			bout.write(b);
			b = (byte) instream.read();
		}
		return new String(bout.toByteArray());
	}

}
