package InvertedIndex;
/*
 * Stores a Data object and count of a specific word. It is comparable.
 * @author ksonar
 */

public class MetaData implements Comparable {
	private Data objData;
	private int countOfWord = 0;
	
	public MetaData(Data a, int count) {
		this.objData = a;
		this.countOfWord = count;
	}
	
	//getters
	public Data getObjData() { return objData; }
	public int getCountOfWOrd() { return countOfWord; }
	
	@Override
	public String toString() {
		return objData.toString();
	}

	/*
	 * Sorts MetaData objects in descending order
	 * @param o
	 */
	@Override
	public int compareTo (Object o) {
		//Metadata m = (Metadata) o;
		int r1 = this.countOfWord;
		int r2 = ((MetaData) o).countOfWord;
		int result = (r1 < r2 ) ? 1 : (r1 == r2) ? 0 : -1;
		
		return result;
	}
}
