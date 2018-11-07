package InvertedIndex;

import java.io.BufferedReader;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/*
 * Creates two instances of InvertedIndex and builds the data structure.
 * @author ksonar
 */

public class Processing {
	private int count;
	private Data inst;
	private InvertedIndex i1 = new InvertedIndex();
	private InvertedIndex i2 = new InvertedIndex();
	
	public int getSizes() { return i1.getSize()+i2.getSize(); }

	
	/*
	 * Constructor to read file, map  and sort data in appropriate manner.
	 * @param reviewFile, qaFile
	 */

	public Processing (String reviewFile, String qaFile, int count) throws IOException {
		Reviews r = new Reviews();
		QA qa = new QA();
		
		this.count = count;
		
		readAndMap(reviewFile, r);
		i1.sortWordIndex();
		System.out.println("i1 size : " + i1.getSize());
		readAndMap(qaFile, qa);
		i2.sortWordIndex();
		System.out.println("i2 size : " + i2.getSize());

	}
	
	/*
	 * Read a single file with object type. Store data from GSON object in desired instance of InvertedIndex.
	 * @params file
	 * @params s
	 */
	
	public void readAndMap (String file, Object s) throws IOException {
		BufferedReader f = null;
		int counter = 0;
		try {
			f = Files.newBufferedReader(Paths.get(file), StandardCharsets.ISO_8859_1);
		}
		catch (NoSuchFileException i) {
			System.out.printf("MESSAGE : NO SUCH FILE : %s\n",file);
			System.exit(1);
		}
		
		
		Gson gson = new GsonBuilder().create();
		String line;

		if (s instanceof Reviews) {
			inst = (Reviews) s; //Reference of Data points to type(Reviews) object 
		}
		else {
			inst = (QA) s; //Reference of Data points to type(QA) object
		}
		
		while((line = f.readLine()) != null && counter < count) {
			try {
				inst = gson.fromJson(line, inst.getClass());
				counter++;
			}
			catch (JsonSyntaxException i) {
				System.out.printf("MESSAGE : JsonSyntaxException");
			}
			if ( s instanceof Reviews) {
				i1.addObjectData(inst);
			}
			else {
				i2.addObjectData(inst);
			}
		}

	}
	
	
	public String getData(String operation, String term) {
		term = term.replaceAll("[^A-Za-z0-9 ]", "").toLowerCase();
		String output = "";
		switch(operation) {
			case "query":
				output = i1.search(term);
				break;
			case "asin":
				output = i1.findAsin(term.toUpperCase()) ;
				output += i2.findAsin(term.toUpperCase() + "\n\n");
		}
		output = output.replaceAll("\n\n", "<<>>");
			
		return output;
	}

}
