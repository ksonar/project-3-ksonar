package InvertedIndex;

/*
 * Stores question and answer. Extends class Data
 */

public class QA extends Data {
	private String question;
	private String answer;

	
	//getters
	public String getQuestion() { return question; }
	public String getAnswer() { return answer; }
	
	@Override
	public String toString() {
		return "ASIN: " + getAsin() + "\n QUESTION : " + question + "\n ANSWER : " + answer; 
	}

}
