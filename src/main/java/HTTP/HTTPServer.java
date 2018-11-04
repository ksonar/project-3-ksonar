package HTTP;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import InvertedIndex.Processing;

public class HTTPServer implements Runnable {
	public static final int PORT = 8080;
	private Socket connection;
	public static Config configData;
	private static Processing processed;

	private HashMap<String, Handler> validPaths = new HashMap<>();
	
	public HTTPServer(Socket s, HashMap<String, Handler> validPaths) {
		connection = s;
		this.validPaths = validPaths;
	}
	public HTTPServer() {
		
	}
	
	public void startup() throws IOException {
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		try(ServerSocket server = new ServerSocket(PORT);) {
			System.out.println("Listening on PORT : " + PORT);
			int c = 0;
			while(true) {
					HTTPServer client = new HTTPServer(server.accept(), this.validPaths);
					System.out.println(c + " Connection opened @ " + new Date());
					executorService.execute(client);
					c++;
			}
		}
	}

	@Override
	public void run() {
		//InputStream in = connection.getInputStream();
		
		try(InputStream in = connection.getInputStream();
				//BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				PrintWriter out = new PrintWriter(connection.getOutputStream());
				) {
			String line;

			if((line = oneLine(in)) == null) {
			//if((line = in.readLine()) == null) {
				return;
			}
			HTTPRequest request = new HTTPRequest(line, in, connection);

			System.out.println("\n!!!!! REQUEST !!!!!");
			request.getRequest();
			request.getRequestBody();
			System.out.println("!!!!!!");
			
			HTTPResponse response = new HTTPResponse(request);
			System.out.println("----->" + request.getRequestPath() + "<-----");
			System.out.println(validPaths.keySet());
			if (validPaths.containsKey(request.getRequestPath())) {
				System.out.println("INSIDE");
				Handler newHandler = validPaths.get(request.getRequestPath());
				processed.getSizes();
				newHandler.handle(request, response, processed);
			}
			else {
				if(request.getRequestPath().equals("/")) {
					response.setResponse(HTTPStatus.OK, "welcome.html");
				}
				else {
					response.setResponse(HTTPStatus.ERROR, "error.html");
				}
			}

			String httpResponse = response.getResponse() + "\n\r\n" + readFile(response.getFileToRead());
			out.println(httpResponse);
			
			System.out.println("@@@@@ RESPONSE @@@@@\n" + httpResponse);
			
		} catch (IOException e) {
			System.out.println("IO Exception");
		}
		finally {
			System.out.println("CLOSING CLIENT CONNECTION @ " + new Date() + "\n~~~~~~~~~~\n");
		}
	}
	
	public void buildInvertedIndex() {
		try {
			processed = new Processing(configData.getFiles().get(0), configData.getFiles().get(1), configData.getReadCount());
		} catch (IOException e) {
			e.printStackTrace();
		}
		processed.getSizes();
		
	}
	
	public void addMapping(String path, Handler handler) {
		validPaths.put(path, handler);
	}
	
	public void displayMap() {
		for(Map.Entry<String, Handler> item : validPaths.entrySet()) {
			System.out.println(item.getKey() + '\t' + item.getValue());
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
	
	public void readConfig(String cFile) {
		Gson gson = new GsonBuilder().create();
		try {
		BufferedReader f = Files.newBufferedReader(Paths.get(cFile));
		configData = gson.fromJson(f, Config.class);
		System.out.println(configData.toString() + '\n');
		//LogData.log.info(configData.toString());
		}
		catch (IOException | NullPointerException i) {
			//LogData.log.warning("NO SUCH FILE");
			System.out.println("NO SUCH FILE");
			System.exit(1);
		}
		catch (JsonSyntaxException i) {
			//LogData.log.warning("NO SUCH FILE");
		}		
	}
	
	private static String oneLine(InputStream instream) {
		//ByteArrayOutputStream bout;
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
			// TODO Auto-generated catch block
			System.out.println("SOME ERROR!!!");
		}
	

		//System.out.println(new String(bout.toByteArray()));
		return line;
	}
}
