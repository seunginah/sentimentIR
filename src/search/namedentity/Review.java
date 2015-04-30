package search.namedentity;

import java.util.ArrayList;

/**
 * A representation of the documents that will be stored in our
 * data collection.
 * 
 * @author dkauchak
 */
public class Review
{
	// Approximate maximum line length when calling toString for printing document text
	private static final int LINE_LENGTH = 50;
	
	private int reviewID;
	private int sentiment;
	private ArrayList<String> text;
	
	/**
	 * Create a new document
	 * 
	 * @param docID the unique ID associated with this document
	 * @param text the words in the document
	 */
	public Review(int docID, int sentiment, ArrayList<String> text){
		this.reviewID = docID;
		this.sentiment = sentiment;
		this.text = text;
	}
	
	/**
	 * Get the document ID
	 * 
	 * @return the docID
	 */
	public int getDocID() {
		return reviewID;
	}

	/**
	 * Get the sentiment
	 * 
	 * @return the sentiment
	 */
	public int getSentiment() {
		return sentiment;
	}
	/**
	 * Set the document ID
	 * 
	 * @param docID
	 */
	public void setDocID(int docID) {
		this.reviewID = docID;
	}

	/**
	 * Get the text (i.e. words) that make up this document.
	 * 
	 * @return
	 */
	public ArrayList<String> getText() {
		return text;
	}

	/**
	 * Set the text (i.e. words) that make up this document.
	 * 
	 * @param text
	 */
	public void setText(ArrayList<String> text) {
		this.text = text;
	}
	
	public String toString(){
		StringBuffer returnMe = new StringBuffer();
		
		for( int i = 0; i < LINE_LENGTH; i++ ){
			returnMe.append("-");
		}
		
		returnMe.append("\nDocID: ");
		returnMe.append(reviewID);
		returnMe.append("\n");
		
		int line_char_count = 0;
		
		for( String s: text ){
			returnMe.append(s);
		
			line_char_count += s.length();
			
			if( line_char_count >= LINE_LENGTH ){
				returnMe.append("\n");
				line_char_count = 0;
			}else{
				returnMe.append(" ");
				line_char_count++;
			}
		}
		
		return returnMe.toString();
	}
}
