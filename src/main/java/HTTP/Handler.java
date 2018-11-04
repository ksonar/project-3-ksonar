package HTTP;
import InvertedIndex.Processing;

public interface Handler {
	
	public void handle(HTTPRequest request, HTTPResponse response, Processing data);

}
