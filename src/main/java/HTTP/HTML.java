package HTTP;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
/*
 * Dynamically create an HTML page.
 * @author ksonar
 */
public class HTML  {
	private String output;

	/*
	 * Setup required HTML page dynamically
	 * @params fileToWrite, output
	 */
	public void setupHTML(String fileToWrite, String output) {
	String[] lineByLine = output.split("\n\n");
	String start = "<!doctype html><html><body><p>";
	String end = "</p></body></html>";
	try(FileWriter f = new FileWriter(fileToWrite);
		BufferedWriter b = new BufferedWriter(f);) {
			b.write(start);
			for(String line : lineByLine) {
				String[] objData = line.split("\n ");
				for(String obj : objData ) {
					b.write(obj + "<br />");
				}
				b.write("<br />");
			}
			b.write(end);	
	} catch (IOException e) {
		e.printStackTrace();
		}
	}
	
	/*
	 * Get value of specific key for the respective handler
	 * @params text, match
	 */
	public String getOutput(String text, String match) {
		String output;
		System.out.println(text);
		try {
			text = text.split(match+"\\=")[1].replaceAll("\\+", " ");
			LogData.log.info(match + " : " + text);
		}
		catch (ArrayIndexOutOfBoundsException i) {
			LogData.log.warning("INVALID QUERY, OR INDEXING PROBLEM");
			text = "INVALID QUERY REQUEST";
		}
		finally {
			output = text;
		}
		return output;
	}
	
	/*
	 * Setup HTML and set response
	 * @params response
	 */
	public void setup(HTTPResponse response) {
		setupHTML("output.html", output);
		response.setResponse(HTTPStatus.OK, "output.html");
	}
	/*
	 * Check if output string is valid to post to the slack API
	 * @params output, response
	 */
	public boolean check(String output, HTTPResponse response) {
		this.output = output;
		if(output.equals("INVALID QUERY REQUEST")) {
			setup(response);
			return false;
		}
		else {
			return true;
		}
	}

}
