package src.test;

import HTTP.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class Testing {
	public static final int PORT = 1024;
	private static HTTPServer server;
	
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("INVALID ARGUMENT LENGTH, MUST CONTAIN 1 CONIFG FILE");
			LogData.log.warning("INVALID ARGUMENT LENGTH, MUST CONTAIN 1 CONIFG FILE");
			System.exit(1);
		}
		String cFile = args[0];
		build(cFile);
		server.startup();
		try {
			URL url = new URL("http://localhost:" + PORT);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		
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
