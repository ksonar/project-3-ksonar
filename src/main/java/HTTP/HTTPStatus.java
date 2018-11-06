package HTTP;

/*
 * COnstants for HTTP status code
 * @author ksonar
 */
public class HTTPStatus {
	
	public final static String OK = "HTTP/1.1 200 OK";
	public final static String ERROR = "HTTP/1.1 404 Not Found";
	public final static String NOT_ALLOWED = "HTTP/1.1 405 METHOD NOT ALLOWED\nAllow: GET & POST";

}
