package search.data.term;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * A reader for reading the text documents from the test corpus.
 * 
 * 
 * @author dkauchak
 */
public class MovieReviewReader extends BasicDocumentReader {
	
	public MovieReviewReader(String filename){
		super(filename);
	}
	
	protected TextIDPair readNextDocText(BufferedReader docIn) throws IOException{
		String line = docIn.readLine();
		
		if( line == null ){
			return null;
		}else{

			String[] parts = line.split("\\s+");			
		
			int docID = Integer.parseInt(parts[0]);
			int sentiment = Integer.parseInt(parts[1]);
			
			StringBuffer buffer = new StringBuffer();
					
			for(int i=2;i<parts.length;i++){
				buffer.append(" " + parts[i]);
			}
	
			return new TextIDPair(buffer.toString(), docID, sentiment);
		}
	}
}
