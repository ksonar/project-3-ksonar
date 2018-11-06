package HTTP;

/*
 * Handles request with find API
 * @author ksonar
 */
public class FindHandler extends HTML implements Handler  {
	private String output;
	private String match = "asin";
	private String text;
	/*
	 * Renders appropriate page with data
	 * @see HTTP.Handler#handle(HTTP.HTTPRequest, HTTP.HTTPResponse)
	 */
	@Override
	public void handle(HTTPRequest request, HTTPResponse response) {

		if(request.getRequestFullQuery() != null && request.getRequestType().equals("POST")) {
			text = getOutput(request.getRequestFullQuery(), match).toUpperCase();
			LogData.log.info(match + " : " + text);
			output = HTTPServer.processed.getData(match, text);
			setup(response);
		}
		else if(request.getRequestFullQuery() == null && request.getRequestType().equals("POST")) {
			output = "POST but no query entered! No data to return...";
			LogData.log.info(output);
			setup(response);
		}
		else {
			response.setResponse(HTTPStatus.OK, "find.html");
		}
	}
	
	/*
	 * Setup HTML and set response
	 * @params response
	 */
	public void setup(HTTPResponse response) {
		setupHTML("output.html", output);
		response.setResponse(HTTPStatus.OK, "output.html");
	}

}
