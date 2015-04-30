package search.namedentity;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import search.data.namedentity.*;
import search.data.namedentity.MovieReviewReader.*;
import search.evaluation.namedentity.BasicQueryReader;
import search.evaluation.namedentity.Evaluator;
import sentiment.RunNaiveBayesClassifier;

public class Experiment {

	public static void main(String[] args) throws IOException {
		
		MovieReviewReader mr = new MovieReviewReader("movie.reviews.tagged");
		
		ImprovedTokenizer tr = new ImprovedTokenizer();
		TokenProcessor pr = new TokenProcessor();				
		pr.setLowercase(true);
		pr.setFoldNumbers(true);
		//pr.setStem(true);
	
		mr.setTokenizer(tr);
		mr.setTokenProcessor(pr);
		
//		while(cr.hasNext()){
//			Review d = cr.next();
//			System.out.println(d.toString());
//		}
		
		//Search s = new Search(cr, tr, pr, Index.TERM_MODIFIER.n, Index.DOC_MODIFIER.n, Index.LENGTH_MODIFIER.n);
		
		//s.run();
		
		BasicQueryReader bqr = new BasicQueryReader("movie.reviews.queries");
		bqr.setTokenizer(tr);
		bqr.setTokenProcessor(pr);
		
//		while(bqr.hasNext()){
//			Query q = bqr.next();
//			System.out.println(q.toString());
//		}
		
		//Search s = new Search(cr, tr, pr, Index.TERM_MODIFIER.n, Index.DOC_MODIFIER.n, Index.LENGTH_MODIFIER.n);
		
		//s.run();
		
		RunQueries r = new RunQueries(mr, tr, pr, Index.TERM_MODIFIER.n, Index.DOC_MODIFIER.n, Index.LENGTH_MODIFIER.n);
		
		RunNaiveBayesClassifier.readReviewsFile();
		
//		while(bqr.hasNext()){
//			
//			Query q = bqr.next();
//			
//			System.out.println();
//			System.out.println(q.toString());		
//			System.out.println("predicted: " + RunNaiveBayesClassifier.bayes.classify(Arrays.asList(q.toString().split(" "))).getCategory());
//			System.out.println();
//
//			
//			r.run(q.toString());
//		}
		
		ArrayList<ArrayList<Integer>> relIDArrays = readRelevanceData();
//		System.out.println("\nRelevant IDs:");
//		for (int i =0; i<relIDs.size();i++){
//			System.out.println(relIDs.get(i).toString());
//		}
		
		int queryLineNumber = 0;
		int count =0;
		double rprecision = 0.0; //rPrecision = sum for all queries (relevant/returned) / number of queries
		double recall = 0.0;
		double precision =0.0;
		double map = 0.0;
		while(bqr.hasNext() && queryLineNumber<relIDArrays.size()){
			
			Query q = bqr.next();
			System.out.println(q);
			int[] returnedIDs = r.run(q.toString());
			int[] relevantIDs = intArray(relIDArrays.get(queryLineNumber));
			System.out.println("returnedIDs: " + Arrays.toString(returnedIDs));
			//System.out.println("relevantIDs: " + Arrays.toString(relevantIDs));
			
			//evaluating queries with relevant and returned IDs only
			if(returnedIDs!=null && relevantIDs!=null){
				count++;
				
				int k = returnedIDs.length/20;
//				System.out.println();
//				
//				System.out.println("query at line number: " + (queryLineNumber+1));
//				
				System.out.println("returnedIDs: " + Arrays.toString(returnedIDs));
				System.out.println("relevantIDs: " + Arrays.toString(relevantIDs));
				
//				System.out.println("k: "+k);
				recall+=Evaluator.recall(relevantIDs, returnedIDs, k);
				precision+=Evaluator.precision(relevantIDs, returnedIDs, k);
				map+=Evaluator.map(relevantIDs, returnedIDs);
				
//				System.out.println("recall: "+ Evaluator.recall(relevantIDs, returnedIDs, k));
//				System.out.println("precision: "+ Evaluator.precision(relevantIDs, returnedIDs, k));
//				System.out.println("map: "+ Evaluator.map(relevantIDs, returnedIDs));
		
				rprecision += Evaluator.rPrecision(relevantIDs, returnedIDs);
//				System.out.println();
			}
			
			queryLineNumber++;
		}
		
		System.out.println("number of relevant queries: "+count);
		System.out.println("r-precision: " + ((double) rprecision/count));
		System.out.println("map: " + ((double) map/count));
		System.out.println("recall: " + ((double) recall/count));
		System.out.println("precision: " + ((double) precision/count));
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<ArrayList<Integer>> readRelevanceData() throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader("movie.reviews.query.relevance"));
	
		String line = br.readLine();
		ArrayList<ArrayList<Integer>> relevanceDataArrays = new ArrayList<ArrayList<Integer>>();
		String qID = "";
		ArrayList<Integer> relIDs = new ArrayList<Integer>();
		
		while(line!=null){			
			//System.out.println(line);
			String[] s = line.split(" ");
			
			if(!qID.equals(s[0])){		
				if(!relIDs.isEmpty()){
					relevanceDataArrays.add(relIDs);	
				}
				else if(!qID.equals("")){
					relevanceDataArrays.add(null);
				}
				
				qID = s[0];
				relIDs = new ArrayList<Integer>();
			}
			
			if(s[2].equals("1") || s[2].equals("2")){
				relIDs.add(Integer.parseInt(s[1]));
			}
			
			line = br.readLine();
		}
		
		br.close();
		return relevanceDataArrays;
	}
	
	/**
	 * 
	 * @param IntegerArrayList
	 * @return
	 */
	public static int[] intArray(ArrayList<Integer> IntegerArrayList) {
		
		if(IntegerArrayList==null){
			return null;
		}
		 
		int[] array = new int[IntegerArrayList.size()];
		
		for (int i=0; i<IntegerArrayList.size(); i++) {
			array[i] = IntegerArrayList.get(i);
		}

		return array;
	}

}
