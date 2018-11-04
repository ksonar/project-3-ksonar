package HTTP;

import java.io.IOException;

public class SomeApplication {

	public static void main(String[] args) {
		HTTPServer server = new HTTPServer();
		if(args.length != 1) {
			System.out.println("INVALID ARGUMENT LENGTH< MUST CONTAIN CONIFG FILE");
			System.exit(1);
		}
		String cFile = args[0];
		
		System.out.println(cFile);
		
		server.readConfig(cFile);
		
		if(HTTPServer.configData.getAppName().equals("InvertedIndex")) {
			server.addMapping("/reviewsearch", new ReviewSearchHandler());
			server.addMapping("/find", new FindHandler());
		}
		else {
			
		}

		server.displayMap();
		
		server.buildInvertedIndex();
		try {
			server.startup();
		} catch (IOException e) {
			System.out.println("PROBLEM IN STARTUP!!");
		}
	}

}
