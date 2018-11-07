package HTTP;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import InvertedIndex.Processing;

/*
 * Mutlithreaded HTTPServer that services incoming requests with responses.
 * @author ksonar
 */
public class HTTPServer extends HTML implements Runnable {
	public static int PORT;
	public static Config configData;
	public static Processing processed;
	private Socket connection;
	private HTTPResponse response;
	private HTTPRequest request;
	private String httpResponse;

	private HashMap<String, Handler> validPaths = new HashMap<>();
	
	public HTTPServer(Socket s, HashMap<String, Handler> validPaths) {
		connection = s;
		this.validPaths = validPaths;
	}

	public HTTPServer() {}
	
	/*
	 * Get valid paths/handlers
	 */
	public String getValidPaths() { return validPaths.toString(); }
	
	public void startup() {
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		try(ServerSocket server = new ServerSocket(PORT);) {
			LogData.log.info("SERVER STARTED\n<<<<<<<>>>>>>>");
			System.out.println("Listening on PORT : " + PORT);
			int c = 0;
			while(true) {
					HTTPServer client = new HTTPServer(server.accept(), this.validPaths);
					LogData.log.info(c + " Connection opened @ " + new Date());
					System.out.println(c + " Connection opened @ " + new Date());
					executorService.execute(client);
					c++;
			}
		} catch (IOException e) {
			LogData.log.warning("STARTUP PROBLEM while creating server/client connection");
		}
	}
	/*
	 * Will service incoming request and render appropriate data
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try(InputStream in = connection.getInputStream();
			PrintWriter out = new PrintWriter(connection.getOutputStream());
				) {
			String line = oneLine(in).trim();
			if(line.equals("")) {
				System.out.println("Returning");
				return;
			}
			request = new HTTPRequest(line, in, connection);
			
			response = new HTTPResponse(request);
			
			if(response.getResponse().equals(HTTPStatus.NOT_ALLOWED)) {
				sendResponse(out);
				return;
			}

			if (validPaths.containsKey(request.getRequestPath())) {
				Handler newHandler = validPaths.get(request.getRequestPath());
				newHandler.handle(request, response);
				LogData.log.info("HANDLING DONE");
			}
			else {
				if(request.getRequestPath().equals("/") && (!response.getResponse().equals(HTTPStatus.NOT_ALLOWED))) {
					setupHTML("welcome.html",validPaths.keySet().toString());
					response.setResponse(HTTPStatus.OK, "welcome.html");
				}
				else {
					response.setResponse(HTTPStatus.ERROR, "error.html");
				}
			}
			sendResponse(out);			
			
		} catch (IOException e) {
			System.out.println("IO Exception");
			LogData.log.warning("IO ERROR in client connection");
		}
		finally {
			System.out.println("CLOSING CLIENT CONNECTION @ " + new Date() + "\n~~~~~~~~~~\n");
			LogData.log.info("CLOSING CLIENT CONNECTION @ " + new Date() + "\n~~~~~~~~~~\n");
		}
	}	
	/*
	 * Build a static InvertedIndex, to be accessed for Find and Review Handler
	 */
	public void buildInvertedIndex() {
		try {
			processed = new Processing(configData.getFiles().get(0), configData.getFiles().get(1), configData.getReadCount());
		} catch (IOException e) {
			LogData.log.warning("FILE NOT FOUND");
		}
		LogData.log.info("INVERTED INDEX BUILT SIZE : " + processed.getSizes());
		
	}
	
	/*
	 * Add APIs and handler of specific application
	 * @params path, handler
	 */
	public void addMapping(String path, Handler handler) {
		validPaths.put(path, handler);
	}
		
	/*
	 * Read required HTML page
	 * @params file
	 */
	public String readFile(String file) {
		String line;
		String fileData = "";
		try (BufferedReader f = Files.newBufferedReader(Paths.get(file), StandardCharsets.ISO_8859_1)) {
			while((line = f.readLine()) != null) {
				fileData += line;
			}
		} catch (IOException e) {
			LogData.log.warning("INCORRECT HTML PAGE");
		}		
		return fileData;
	}
	/*
	 * Read from config file
	 * @params cFile
	 */
	public void readConfig(String cFile) {
		Gson gson = new GsonBuilder().create();
		try {
		BufferedReader f = Files.newBufferedReader(Paths.get(cFile));
		configData = gson.fromJson(f, Config.class);
		System.out.println(configData.toString() + '\n');
		LogData.log.info(configData.toString());
		HTTPServer.PORT = configData.getPort();
		}
		catch (IOException | NullPointerException i) {
			LogData.log.warning("NO SUCH FILE");
			System.out.println("NO SUCH FILE");
			System.exit(1);
		}
		catch (JsonSyntaxException i) {
			LogData.log.warning("JSON EXCEPTION");
		}	

	}
	/*
	 * Read byte-data from incoming client request 
	 * @params instream
	 */
	private static String oneLine(InputStream instream) {
		String line = null;
		byte b;
		try(ByteArrayOutputStream bout = new ByteArrayOutputStream();) {
			b = (byte) instream.read();
			while(b != '\n' && b != -1) {
				bout.write(b);
				bout.flush();
				b = (byte) instream.read();
			}
			line = new String(bout.toByteArray());
			
		} catch (IOException e) {
			System.out.println("IO ERROR!!!");
			LogData.log.warning("IO ERROR!!");
		}
		return line;
	}
	/*
	 * Send response to client's output stream
	 * @params out
	 */
	public void sendResponse(PrintWriter out) {
		LogData.log.info("RESPONSE SET");

		httpResponse = response.getResponse() + "\n\r\n" + readFile(response.getFileToRead());
		out.write(httpResponse);
	}
}
