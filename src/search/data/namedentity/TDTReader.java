package search.data.namedentity;

import java.io.*;

//import search.data.namedentity.BasicDocumentReader.TextIDPair;

public class TDTReader extends BasicDocumentReader{	
	public TDTReader(String documentFile){
		super(documentFile);
	}
	
	/**
	 * Read through the file and extract the text between the next <DOC> and </DOC> tags
	 * @return The text between the next DOC tags or null if no more documents exist
	 * @throws IOException
	 */
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