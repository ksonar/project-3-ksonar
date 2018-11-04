package HTTP;

import java.util.ArrayList;

public class Config {
	private ArrayList<String> files;
	private int readCount;
	private String appName;
	
	public ArrayList<String> getFiles() { return files; }
	public int getReadCount() { return readCount; }
	public String getAppName() { return appName; }
	
	public String toString() { 
		return files + "\t" + readCount + '\t' + appName;
	}

}
