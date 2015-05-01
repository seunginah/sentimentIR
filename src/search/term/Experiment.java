package search.term;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import de.daslaboratorium.machinelearning.classifier.BayesClassifier;
import de.daslaboratorium.machinelearning.classifier.Classifier;
import search.data.term.MovieReviewReader;
//import search.evaluation.term.BasicQueryReader;
import search.evaluation.term.Evaluator;
//import sentiment.RunNaiveBayesClassifier;

/*
The MIT License (MIT)
------------------

Copyright (c) 2012-2014 Philipp Nolte

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

/**
 * We are using the Naive Bayes Classifier written by Philipp Nolte 
 * https://github.com/ptnplanet/Java-Naive-Bayes-Classifier
 * 
 * We have written this class to run different experiments with
 * Naive Bayes Classifier. 
 * 
 * @author Qudsia and Grace
 *
 */
public class Experiment {
	
	private static ArrayList<String> mostFrequentWords = new ArrayList<String>();
	public static Classifier<String, String> bayes = new BayesClassifier<String, String>();
	private static ArrayList<String> queries = new ArrayList<String>();
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		evaluateLowercasedOnly();
		
	}
	
	public static void evaluateLowercasedOnly() throws IOException{
		
		//create an index by terms
		MovieReviewReader cr = new MovieReviewReader("movie.reviews");		
		
		//get a number of most frequent terms for sentiment analysis
		mostFrequentWords = new ArrayList<String>();
		readDictionaryFile("smallerdictionary1000.txt");
		
		//train on the reviews using a number of most frequent terms
		bayes = new BayesClassifier<String, String>();
		trainNaiveBayesOnLowerCasedReviewsOnly();
		
		ImprovedTokenizer tr = new ImprovedTokenizer();
		TokenProcessor pr = new TokenProcessor();				
		pr.setLowercase(true);
		pr.setFoldNumbers(true);
		//pr.setStem(true);
	
		cr.setTokenizer(tr);
		cr.setTokenProcessor(pr);
		
//		code to print reviews		
//		while(cr.hasNext()){
//			Review d = cr.next();
//			System.out.println(d.toString());
//		}
		
		//read queries and save in array list 		
		readQueriesFile();
		
	
		RunQueries r = new RunQueries(cr, tr, pr, Index.TERM_MODIFIER.b, Index.DOC_MODIFIER.n, Index.LENGTH_MODIFIER.n);
		
		int count =0;
		double rprecision = 0.0; //rPrecision = sum for all queries (relevant/returned) / number of queries
		double recall = 0.0;
		double precision =0.0;
		double map = 0.0;
		float correct=0;
		int k = 3;
		
		for(String q : queries){
					
			q = q.replaceAll("!", "");
			
			System.out.println();
			System.out.println("QUERY: " + q );
			String[] tokens = q.toString().split(" ");
			int[] relevantIDs =new int[1];
			relevantIDs[0] = Integer.parseInt(tokens[0]);
			int sentiment = Integer.parseInt(tokens[1]);
//			System.out.println(Arrays.toString(tokens));
			String prediction = bayes.classify(Arrays.asList(tokens)).getCategory();
			System.out.println("prediction: " + prediction + " Actual: " + sentiment);
			
			if(prediction.equals("negative")&&tokens[1].equals("0") || prediction.equals("positive")&&tokens[1].equals("1")){
				correct++;
			}
			
			int[] returnedIDs = r.run(q.toString());
		
			//evaluating queries with relevant and returned IDs only
			if(returnedIDs!=null){
				
				count++;
				
				
//				System.out.println();
				System.out.println("returnedIDs: " + Arrays.toString(returnedIDs));
				System.out.println("relevantIDs: " + Arrays.toString(relevantIDs));
				System.out.println();
//				System.out.println("k: " + k);
				recall+=Evaluator.recall(relevantIDs, returnedIDs, k);
				precision+=Evaluator.precision(relevantIDs, returnedIDs, k);
				map+=Evaluator.map(relevantIDs, returnedIDs);
				
//				System.out.println("recall: "+ Evaluator.recall(relevantIDs, returnedIDs, k));
//				System.out.println("precision: "+ Evaluator.precision(relevantIDs, returnedIDs, k));
//				System.out.println("map: "+ Evaluator.map(relevantIDs, returnedIDs));
		
				rprecision += Evaluator.rPrecision(relevantIDs, returnedIDs);
//				System.out.println();
			}

		}
		
		System.out.println();
		System.out.println("number of queries: "+ count);
		System.out.println("k: " + k);
		System.out.println("r-precision: " + ((double) rprecision/count));
		System.out.println("map: " + ((double) map/count));
		System.out.println("recall: " + ((double) recall/count));
		System.out.println("precision: " + ((double) precision/count));
		System.out.println("sentiment prediction accuracy: " + (float)(correct/(float) queries.size()));
		System.out.println();
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
	

	/**
	 * 
	 * @throws IOException
	 */
	public static void trainNaiveBayesOnLowerCasedReviewsOnly() throws IOException{

		BufferedReader br = new BufferedReader(new FileReader("movie.reviews"));
		String line = br.readLine();
		
		int counter = 0;
		while (line != null) {		

			//for top something words
			String[] words = line.toLowerCase().split("\\s+");
			String releventWords = words[0] + " " + words[1] + " ";

			for(int i=2;i<words.length;i++){
				if(mostFrequentWords.contains(words[i].toLowerCase().trim())){
					releventWords += words[i].toLowerCase()+" ";
				}
			}

			//System.out.println(releventWords);
			String[] tokens = releventWords.split("\\s+");

			if(counter<2000){
				//1 = positive
				//0 = negative
				if(tokens[1].equals("1")){
					bayes.learn("positive", Arrays.asList(tokens));
				} else {
					bayes.learn("negative", Arrays.asList(tokens));
				}
			}

			line = br.readLine();
			((BayesClassifier<String, String>) bayes).classifyDetailed(Arrays.asList(tokens));
		}

		bayes.setMemoryCapacity(2000); 
		br.close();
	}

	/**
	 * 
	 * @throws IOException
	 */
	public static void trainNaiveBayesOnNegatedReviewsOnly() throws IOException{

		BufferedReader br = new BufferedReader(new FileReader("movie.reviews.negated"));	
		String line = br.readLine();
		int counter = 0;

		while (line != null) {		

			//for top something words
			String[] words = line.toLowerCase().split("\\s+");
			String releventWords = words[0] + " " + words[1] + " ";

			for(int i=2;i<words.length;i++){
				if(mostFrequentWords.contains(words[i].toLowerCase().trim())){
					releventWords += words[i].toLowerCase()+" ";
				}
			}

			//System.out.println(releventWords);
			String[] tokens = releventWords.split("\\s+");

			if(counter<2000){

				//1 = positive
				//0 = negative
				if(tokens[1].equals("1")){
					bayes.learn("positive", Arrays.asList(tokens));
				} else {
					bayes.learn("negative", Arrays.asList(tokens));
				}
			}

			line = br.readLine();
			((BayesClassifier<String, String>) bayes).classifyDetailed(Arrays.asList(tokens));
		}

		bayes.setMemoryCapacity(2000); 
		br.close();

	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public static void trainNaiveBayesOnTaggedReviewsOnly() throws IOException{

		BufferedReader br = new BufferedReader(new FileReader("movie.reviews.tagged"));
		String line = br.readLine();
		
		int counter = 0;
		while (line != null) {	

			//for top words without named entities
			//remove tagged info
			String strippedTags = line.toLowerCase().replaceAll("<.*?>.*?</.*?>|<.*?/>", "");
			String[] words = strippedTags.toLowerCase().split("\\s+");
			String releventWords = words[0] + " " + words[1] + " ";

			for(int i=2;i<words.length;i++){
				if(mostFrequentWords.contains(words[i].toLowerCase().trim())){
					releventWords += words[i].toLowerCase()+" ";
				}
			}
			
			//System.out.println(releventWords);
			String[] tokens = releventWords.split("\\s+");

			if(counter<2000){

				//1 = positive
				//0 = negative
				if(tokens[1].equals("1")){
					bayes.learn("positive", Arrays.asList(tokens));
				} else {
					bayes.learn("negative", Arrays.asList(tokens));
				}
			}

			line = br.readLine();
			((BayesClassifier<String, String>) bayes).classifyDetailed(Arrays.asList(tokens));
		}

		bayes.setMemoryCapacity(2000); 
		br.close();
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public static void readDictionaryFile(String filename) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(filename));	

		String line = br.readLine();
		while (line != null) {	
			mostFrequentWords.add(line.trim());
			line = br.readLine();
		}
		
		br.close();

	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public static void readQueriesFile() throws IOException{

		BufferedReader br = new BufferedReader(new FileReader("movie.reviews.queries.term"));	

		String line = br.readLine();
		while (line != null) {	

			queries.add(line.trim());
			line = br.readLine();
		}
		br.close();

	}

}
