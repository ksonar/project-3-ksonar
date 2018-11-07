package HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ChatHandler extends HTML implements Handler {
	private String match = "message";
	String output;
	/*
	 * Renders appropriate page with data
	 * @see HTTP.Handler#handle(HTTP.HTTPRequest, HTTP.HTTPResponse)
	 */
	@Override
	public void handle(HTTPRequest request, HTTPResponse response) {
		if((request.getRequestFullQuery() != null) && request.getRequestType().equals("POST")) {
			output = getOutput(request.getRequestFullQuery(), match);
			boolean check = check(output, response);
			if(check) {
				String url = "https://slack.com/api/chat.postMessage";
				URL obj;
				try {
					obj = new URL(url);
					HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
					setMethodRequest(con);
					String json = "{\"channel\":\"project3\",\"text\":\"" + output + "\"}";
					con.setDoOutput(true);
					con.getOutputStream().write(json.getBytes());
					
					LogData.log.info("URL: " + url + " CODE : " + con.getResponseCode() + " MSG : " + con.getResponseMessage());

					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer message = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						message.append(inputLine);
					}
					in.close();
					output = "<H1>RESPONSE FROM SLACK</H1>" + message.toString();
					LogData.log.info(message.toString());
					setupHTML("slackOutput.html", output);
					response.setResponse(HTTPStatus.OK, "slackOutput.html");
					
				} catch (MalformedURLException e) {
					LogData.log.warning("MALFORMED URL");
				} catch (IOException e) {
					LogData.log.warning("IO ERROR");
				}
				
			}
		}
		else if(request.getRequestFullQuery() == null && request.getRequestType().equals("POST")) {
			output = "POST but no query entered! No data to return...";
			LogData.log.info(output);
			setupHTML("slackOutput.html", output);
			response.setResponse(HTTPStatus.OK, "slackOutput.html");
		}
		else {
			response.setResponse(HTTPStatus.OK, "slackbot.html");
		}
	}
	
	/*
	 * Set required headers and method
	 * @param con
	 */
	public void setMethodRequest(HttpsURLConnection con) {
		try {
			con.setRequestMethod("POST");
		} catch (ProtocolException e) {
			LogData.log.warning("PROTOCOL EXCEPTION");
		}
		con.setRequestProperty("Content-type", "application/json");
		con.setRequestProperty("Authorization", "Bearer xoxp-378520430422-382632453845-472912255942-8fd46479401dffa0a61daafa93f5ec59");
		con.setRequestProperty("Connection", "close");
		con.setRequestProperty("Host", "localhost:" + HTTPServer.PORT);
	}
	
}
