package HTTP;

import java.util.ArrayList;

/*
 * Store data from config file
 * @author ksonar
 */
public class Config {
	private ArrayList<String> files;
	private int readCount;
	private String appName;
	private int port;
	//getters
	public ArrayList<String> getFiles() { return files; }
	public int getReadCount() { return readCount; }
	public String getAppName() { return appName; }
	public int getPort() { return port; }

	
	public String toString() { 
		return files + "\t" + readCount + '\t' + appName + '\t' + port ;
	}

}
