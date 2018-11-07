package HTTP;

import java.io.IOException;
/*
 * Driver class, sets up server with data from config file and correct mappings
 * @author ksonar
 */
public class SomeApplication {
	
	public static void main(String[] args) throws SecurityException, IOException {
		LogData.createLogger();
		HTTPServer server = new HTTPServer();
		if(args.length != 1) {
			System.out.println("INVALID ARGUMENT LENGTH, MUST CONTAIN 1 CONIFG FILE");
			LogData.log.warning("INVALID ARGUMENT LENGTH, MUST CONTAIN 1 CONIFG FILE");
			System.exit(1);
		}
		String cFile = args[0];
		
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
		
		server.startup();
	}

}
