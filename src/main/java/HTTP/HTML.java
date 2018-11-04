package HTTP;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class HTML  {

	
	public void setupHTML(String fileToWrite, String output, String api) {
	String[] lineByLine = output.split("\n\n");
	String start = "<!doctype html><html><body><p>";
	String end = "</p></body></html>";
	try(FileWriter f = new FileWriter(fileToWrite);
		BufferedWriter b = new BufferedWriter(f);) {
			b.write(start);
			if (api.equals("reviewsearch")) {
				b.write("COUNT OF RECORDS : " + lineByLine.length + "<br />");
			}
			for(String line : lineByLine) {
				String[] objData = line.split("\n ");
				for(String obj : objData ) {
					b.write(obj + "<br />");
				}
				b.write("<br />");
			}
			//b.write(output);
			b.write(end);
				
	} catch (IOException e) {
		e.printStackTrace();
	}
	}

}
