package HTTP;

/*
 * Process response and set desired fields
 * @author ksonar
 */
public class HTTPResponse {
	private HTTPRequest request;
	private String response;
	private String fileToRead;
	
	public HTTPResponse(HTTPRequest request) {
		this.request = request;
		setResponse();
	}
	//getters
	public String getResponse() { return response; }
	public String getFileToRead() { return fileToRead; }
	/*
	 * Check if valid method and set response
	 */
	public void setResponse() {
		if (!(request.getRequestType().equals("GET") || request.getRequestType().equals("POST"))) {
			response = HTTPStatus.NOT_ALLOWED;
			fileToRead = "not_allowed.html";
		}
		else {
			response = HTTPStatus.OK;
			fileToRead = "index.html";			
		}
	}
	
	/*
	 * Set response of dynamically loaded pages
	 * @params response, fileToRead
	 */
	public void setResponse(String response, String fileToRead) {
		this.response = response;
		this.fileToRead = fileToRead;
	}
}
