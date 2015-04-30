package search.data.namedentity;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			line = line.replaceAll(">", "> ");
			line = line.replaceAll("<", " <");
			
//			Pattern p = Pattern.compile("<.*?>.*?</.*?>|<.*?/>");
//			Matcher m = p.matcher(line);
//
//			if (m.find()) {
//			    System.out.println(m.group());
//			}

					
			String[] parts = line.split("\\s+");			
		
			int docID = Integer.parseInt(parts[0]);
			int sentiment = Integer.parseInt(parts[1]);
			
			//System.out.println(docID);
			
			StringBuffer buffer = new StringBuffer();
			
			boolean namedentity = false;
			for(int i=2;i<parts.length;i++){
				
				if (parts[i].equals("</PERSON>") || parts[i].equals("</LOCATION>") || parts[i].equals("</ORGANIZATION>")){
					namedentity = false;
				}
				
				if(namedentity){
					buffer.append(" " + parts[i]);
					//System.out.println("*** "+parts[i]);
				}
				
				if(parts[i].equals("<PERSON>") || parts[i].equals("<LOCATION>") || parts[i].equals("<ORGANIZATION>")){
					namedentity = true;
				}
				
				//buffer.append(" " + parts[i]);
			}
	
			return new TextIDPair(buffer.toString(), docID, sentiment);
		}
	}
}
