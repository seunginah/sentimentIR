package search.namedentity;

import java.util.Hashtable;

import search.data.namedentity.DocumentReader;
/**
 * A modified version of Search.java to run all the queries
 * @author Qudsia
 *
 */
public class RunQueries {

	private Index index;
	private Hashtable<Integer,Review> id2doc = null;
	private Tokenizer tokenizer;
	private TokenProcessor tokenProcessor;
	
	public RunQueries(DocumentReader reader, Tokenizer tokenizer, TokenProcessor tokenProcessor,
			Index.TERM_MODIFIER termModifier, Index.DOC_MODIFIER docModifier, Index.LENGTH_MODIFIER lengthModifier){
		this.tokenizer = tokenizer;
		this.tokenProcessor = tokenProcessor;
		
		index = new Index(reader, termModifier, docModifier, lengthModifier);
	}
	
	public RunQueries(DocumentReader reader, DocumentReader reader2,
			Tokenizer tokenizer, TokenProcessor tokenProcessor,
			Index.TERM_MODIFIER termModifier, Index.DOC_MODIFIER docModifier, Index.LENGTH_MODIFIER lengthModifier){
		this.tokenizer = tokenizer;
		this.tokenProcessor = tokenProcessor;
		
		index = new Index(reader, termModifier, docModifier, lengthModifier);
		
		id2doc = new Hashtable<Integer,Review>();
		
		while( reader2.hasNext() ){
			Review next = reader2.next();
			id2doc.put(next.getDocID(), next);
		}
	}
	
	public int[] run(String textQuery){
		
		//BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		//try{
			//String textQuery = in.readLine();
					
			//while( !textQuery.equals("exit") ){
				QueryResult result;
				
				if( textQuery.contains("!") ||
					textQuery.contains("AND") ||
					textQuery.contains("OR") ){
					result = index.booleanQuery(textQuery);
				}else{
					result = index.rankedQuery(new Query(tokenProcessor.process(tokenizer.tokenize(textQuery))));
				}
				
				if( result.size() == 0 ){
					return null;
				}else{
					return result.getIDs();
				}
//				if( result.size() == 0 ){
//					System.out.println("No results");
//				}else{
//					int[] ids = result.getIDs();
//					double[] scores = result.getScores();
//					
//					for( int i = 0; i < scores.length; i++ ){
//						if( id2doc == null ){
//							System.out.print(ids[i] + ":" + scores[i] + " ");
//						}else{
//							System.out.println(id2doc.get(ids[i]));
//						}
//						
//					}
//				
//						System.out.println();
//				}
				
				
				//textQuery = in.readLine();
			//}
		//}catch(IOException e){
			//throw new RuntimeException(e.toString());
		//}
	}
}
