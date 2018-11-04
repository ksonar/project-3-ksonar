package HTTP;

import InvertedIndex.*;

public class ReviewSearchHandler extends HTML implements Handler {
	@Override
	public void handle(HTTPRequest request, HTTPResponse response, Processing data) {
		String output;
		if(request.getRequestFullQuery() != null && request.getRequestType().equals("POST")) {
			String search = getInput(request.getRequestFullQuery());
			output = data.getData("search", search);
			setupHTML("output.html", output, "reviewsearch");
			response.setResponse(HTTPStatus.OK, "output.html");
		}
		else
			System.out.println("NOTHING TO QUERY");
		
	}
	
	public String getInput(String text) {
		String query = text.split("\\=")[1].replaceAll("\\+", " ");
		return query;
	}
	
}
