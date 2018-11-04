package HTTP;

import InvertedIndex.Processing;

public class FindHandler extends HTML implements Handler  {
	@Override
	public void handle(HTTPRequest request, HTTPResponse response, Processing data) {
		String output;
		if(request.getRequestFullQuery() != null && request.getRequestType().equals("POST")) {
			String search = getInput(request.getRequestFullQuery()).toUpperCase();
			output = data.getData("find", search);
			setupHTML("output.html", output, "find");
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
