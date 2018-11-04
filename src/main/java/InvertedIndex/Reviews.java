package InvertedIndex;

/*
 * Stores reviewText, reviewerID, overall score. Extends Data
 */

public class Reviews extends Data {
	private String reviewText;
	private String reviewerID;
	private float overall;
	//getters
	public String getReviewText() { return reviewText; }
	public String getReviewerID() { return reviewerID; }
	public float getOverall() { return overall; }
	

	@Override
	public String toString() {
		return "ASIN : " + getAsin() + "\n REVIEWER ID : " + reviewerID + "\n TEXT : " + reviewText + "\n SCORE : " + overall; 
	}

}
