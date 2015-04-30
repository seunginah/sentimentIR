package search.evaluation.term;


/**
 * This class is used to evaluate the ranked results from a search engine
 * 
 * @author dkauchak
 *
 */
public class Evaluator {
	/** 
	 * Calculate recall at k.
	 * 
	 * @param relevantIDs the list of relevant ids for this query (may be in any order)
	 * @param returnedIDs the list of IDs returned from the system in order
	 * @param k
	 * @return Returns the recall at k
	 */
	public static double recall(int[] relevantIDs, int[] returnedIDs, int k){
		//recall = relANDret / relevant
		
		double relANDret = 0;
		
		if(k>returnedIDs.length){
			return 0.0;
		}
		
		for (int i=0; i<k; i++){
			for(int j=0;j<relevantIDs.length;j++){
				
				if(returnedIDs[i]==relevantIDs[j]){
					relANDret++;
				}
			}
		}
		
		
		
		return  relANDret / (double) relevantIDs.length;
	}
	
	/** 
	 * Calculate precision at k.
	 * 
	 * @param relevantIDs the list of relevant ids for this query (may be in any order)
	 * @param returnedIDs the list of IDs returned from the system in order
	 * @param k
	 * @return Returns the precision at k
	 */
	public static double precision(int[] relevantIDs, int[] returnedIDs, int k){
		//precision = relANDret / retrieved
		
		double relANDret = 0.0;
		
		if(k>returnedIDs.length){
			return 0.0;
		}
		
		for (int i=0; i<k; i++){
			for(int j=0;j<relevantIDs.length;j++){
				
				if(returnedIDs[i]==relevantIDs[j]){
					relANDret++;
				}
			}
		}
		
		return  ((double)relANDret/(double)k);
		//return  ((double)relANDret/(double)returnedIDs.length);
	}

	/** 
	 * Calculate R-Precision
	 * 
	 * @param relevantIDs the list of relevant ids for this query (may be in any order)
	 * @param returnedIDs the list of IDs returned from the system in order
	 * @return Returns the R-Precision
	 */
	public static double rPrecision(int[] relevantIDs, int[] returnedIDs){
		//rPrecision = sum for all queries (relevant/returned) / number of queries
		//the calculation part for all queries is done in Experiment.java main method
		return ((double)relevantIDs.length/(double)returnedIDs.length);
	}
	
	/** 
	 * Calculate MAP
	 * 
	 * @param relevantIDs the list of relevant ids for this query (may be in any order)
	 * @param returnedIDs the list of IDs returned from the system in order
	 * @return Returns the MAP
	 */
	public static double map(int[] relevantIDs, int[] returnedIDs){
		
		double averagePrecision = 0.0;
		double rel = 0;
	
		for (int i=0; i<relevantIDs.length; i++){
				for(int j=0;j<returnedIDs.length;j++){
					
					if(returnedIDs[j]==relevantIDs[i]){
						rel++;
						averagePrecision += (rel/(j+1));
					}
				}

		}
		
		return  (1/(double)relevantIDs.length)*averagePrecision;
	}
}
