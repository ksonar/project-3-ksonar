package HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/*
 * Store data from config file
 * @author ksonar
 */
public class Config {
	private ArrayList<String> files;
	private int readCount;
	private String appName;
	private int port;
	public static Config configData;
	//getters
	public ArrayList<String> getFiles() { return files; }
	public int getReadCount() { return readCount; }
	public String getAppName() { return appName; }
	public int getPort() { return port; }

	private Config() {}
	public String toString() { 
		return files + "\t" + readCount + '\t' + appName + '\t' + port ;
	}
	
	public static Config getConfig(String cFile) {
		if (configData == null) {
			readConfig(cFile);
		}
		return configData;
		
	}
	
	/*
	 * Read from config file
	 * @params cFile
	 */
	public static void readConfig(String cFile) {
		Gson gson = new GsonBuilder().create();
		try {
		BufferedReader f = Files.newBufferedReader(Paths.get(cFile));
		configData = gson.fromJson(f, Config.class);
		System.out.println(configData.toString() + '\n');
		LogData.log.info(configData.toString());
		}
		catch (IOException | NullPointerException i) {
			LogData.log.warning("NO SUCH FILE");
			System.out.println("NO SUCH FILE");
			System.exit(1);
		}
		catch (JsonSyntaxException i) {
			LogData.log.warning("JSON EXCEPTION");
		}		
	}

}
