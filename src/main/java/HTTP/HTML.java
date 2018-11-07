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
	String[] lineByLine;
	String start = "<!doctype html><html><body><p>";
	String end = "</p></body></html>";

	try(FileWriter f = new FileWriter(fileToWrite);
		BufferedWriter b = new BufferedWriter(f);) {
			b.write(start);
			if(fileToWrite.equals("output.html")) {
				if(output.equals("-1") || output.equals("-1-1")) {
					b.write("Data for query does not exist!");
					return;
				}
				
				lineByLine = output.split("<<>>");
				b.write("<p>" + "#Records : " + lineByLine.length + "</p>");
				String tableHead = "<h2>Results</h2><style>table, th, td {border: 2px solid black;}</style><table style=\"width:100%\"><tr><th>#</th><th>ASIN</th><th>REVIEWER ID</th><th>TEXT</th><th>SCORE</th></tr>";
				
				b.write(tableHead);
				for(String line : lineByLine) {
					if(line.equals("-1")) {
						return;
					}
					String[] objData = line.split("\t");
					b.write("<tr>");
					for(String obj : objData) {
							String data = obj.split(":")[1].trim();
							b.write("<td>" + data + "</td>");
					}
					b.write("</tr>");
				}
				b.write("</table>");
				
			}
			else {
				b.write(output);
			}
			b.write(end);	
	} catch (IOException e) {
		LogData.log.warning("ISSUE WITH OUTPUT DATA");
		}
	}
	
	/*
	 * Get value of specific key for the respective handler
	 * @params text, match
	 */
	public String getOutput(String text, String match) {
		String output;
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
	 * Setup HTML and set response
	 * @params response
	 */
	public void setup(HTTPResponse response, String file) {
		setupHTML(file, output);
		response.setResponse(HTTPStatus.OK, file);
	}
	/*
	 * Check if output string is valid to post to the slack API
	 * @params output, response
	 */
	public boolean check(String output, HTTPResponse response) {
		this.output = output;
		if(output.equals("INVALID QUERY REQUEST")) {
			setup(response, "slackOutput.html");
			return false;
		}
		else {
			return true;
		}
	}
}
