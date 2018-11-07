package HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/*
 * Process request and set desired fields
 * @author ksonar
 */
public class HTTPRequest {
	private String requestType;
	private String[] fullPath;
	private String fullQuery;
	private String path;
	private String HTTP;
	private int length = 0;
	private String headers = "";
	private String msg;

	public HTTPRequest(String line, InputStream in, Socket connection) throws IOException {
		setRequest(line);
		setRequestHeaders(line, in, connection);
	}
	//getters
	public int getLength() { return length; }
	public String getRequestType() { return requestType; }
	public String getRequestPath() { return path; }
	public String[] getRequestFullPath() { return fullPath; }
	public String getRequestFullQuery() { return fullQuery; }
	public void getRequest() { 
		System.out.println(requestType + " " + path + " " + msg + " " + HTTP + '\n' + headers);
	}

	/*
	 * Set the first header line (METHOD,API,HTTP)
	 * @params line
	 */
	public void setRequest(String line) {
		String[] split = line.split(" ");
		if (split.length != 3) {
			LogData.log.warning("Invalid request length, follow protocol");
			System.exit(1);
		}
		requestType = split[0];
		fullPath = split[1].split("\\?");
		path = fullPath[0];
		HTTP = split[2];
		if(requestType.equals("POST")) {
			fullQuery = msg;
		}
		
		LogData.log.info("METHOD : " + requestType + "\tAPI : " + path + "\tQUERY : " + fullQuery + "\tHTTP : " + HTTP);
	}
	
	/*
	 * Set all remaining headers
	 * @params line, in, connection
	 */
	public void setRequestHeaders(String line, InputStream in, Socket connection) throws IOException {
		while (line != null && !line.trim().isEmpty()) { 
			try {
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
		LogData.log.info("REQUEST BODY READ, Bytes expected: " + length + " Bytes read: " + read);

		msg = new String(bytes);
		headers+= msg;
		if(requestType.equals("POST")) {
			fullQuery = msg;
		}
		LogData.log.info("METHOD : " + requestType + "\tAPI : " + path + "\tQUERY : " + fullQuery + "\tHTTP : " + HTTP);
	}
	
	/*
	 * Read data from incoming client request
	 * @instream
	 */
	private static String oneLine(InputStream instream) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte b = (byte) instream.read();
		while(b != -1 && b != '\n') {
			bout.write(b);
			b = (byte) instream.read();
		}
		return new String(bout.toByteArray());
	}

}
