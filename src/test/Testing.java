package src.test;

import HTTP.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.junit.*;


public class Testing {
	private static int PORT;
	private static HTTPServer server;
	
	@BeforeClass
	public static void set() throws SecurityException, IOException {
		LogData.createLogger();
		HTTPServer server = new HTTPServer();
		String cFile = "config.json";
		build(cFile);
		PORT = HTTPServer.configData.getPort();
		System.out.println("STARTING");
		//server.startup();
	}
	
	@Test
	public void checkSetRequest() {
		//HTTPRequest request = new HTTPRequest();
	}
	
	@Test
	public void sendSlackMessage() {
		ChatHandler slackAPI = new ChatHandler();
		
	}
	
	
	@Test
	public void testLandingPage() {

		try {
			URL url = new URL("http://localhost:" + PORT);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			System.out.println("HERE");
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer message = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				message.append(inputLine);
			}
			in.close();
			System.out.println(message.toString());
			System.out.println("\n\n" + "URL: " + url + " CODE : " + con.getResponseCode() + " MSG : " + con.getResponseMessage());
			
		} catch (IOException e) {
			System.out.println("MALFORMED");
		}
		
	}
	
	public static void build(String cFile) {
		try {
			LogData.createLogger();
		} catch (SecurityException | IOException e1) {}
		server = new HTTPServer();
;
		
		System.out.println(cFile);
		server.readConfig(cFile);

		if(HTTPServer.configData.getAppName().equals("InvertedIndex")) {
			server.addMapping("/reviewsearch", new ReviewSearchHandler());
			server.addMapping("/find", new FindHandler());
			server.buildInvertedIndex();
		}
		else {
			server.addMapping("/slackbot", new ChatHandler());
		}
		
		System.out.println(server.getValidPaths());
		LogData.log.info(server.getValidPaths());
	}

}
