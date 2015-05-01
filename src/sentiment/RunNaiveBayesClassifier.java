package sentiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import de.daslaboratorium.machinelearning.classifier.*;

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
public class RunNaiveBayesClassifier {

	private static ArrayList<String> mostFrequentWords;// = new ArrayList<String>();
	public static Classifier<String, String> bayes;// = new BayesClassifier<String, String>();

	public static void main(String[] args) throws IOException {
		
		mostFrequentWords=null;
		
		System.out.print("bag of words only using all terms ");
		trainOnBagOfWordsOnly();
		
		System.out.print("negated only using all terms ");
		trainOnNegatedOnly();
		
		System.out.print("without NE only using all terms ");
		trainOnWithoutNEOnly();
		
		System.out.println();
		
		//run different experiments
		for(int i=1000; i<=5000; i=i+1000){
			
					
			System.out.print("bag of words only using top "+i+" most frequent terms ");
			readDictionaryFile("small.dictionary."+i);
			trainOnBagOfWordsOnly();
			
			System.out.print("negated only using top "+i+" most frequent terms ");
			readDictionaryFile("small.dictionary.negated."+i);
			trainOnNegatedOnly();
			
			System.out.print("without NE only using top "+i+" most frequent terms ");
			readDictionaryFile("small.dictionary.withoutNE."+i);
			trainOnWithoutNEOnly();
			
			System.out.println();
		}
		
	}

	/**
	 * 
	 * @throws IOException
	 */
	public static void trainOnBagOfWordsOnly() throws IOException{
		
		bayes = new BayesClassifier<String, String>();

		BufferedReader br = new BufferedReader(new FileReader("movie.reviews"));
		String line = br.readLine();

		float correct=0;
		int counter = 0;

		while (line != null) {		
			
			String[] tokens;
			
			//if mostFrequentWords is not specified
			//train naive bayes on all encountered terms in review file
			if(mostFrequentWords==null){

				//for all terms
				tokens = line.toLowerCase().split("\\s+");
			}
			
			//train naive bayes on using specified dictionary
			else {
				//for top something words
				String[] words = line.toLowerCase().split("\\s+");
				String releventWords = words[0] + " " + words[1] + " ";
	
				for(int i=2;i<words.length;i++){
					if(mostFrequentWords.contains(words[i].toLowerCase().trim())){
						releventWords += words[i].toLowerCase()+" ";
					}
				}
	
				//System.out.println(releventWords);
				tokens = releventWords.split("\\s+");
			}
			
			if(counter<1500){

				//1 = positive
				//0 = negative
				if(tokens[1].equals("1")){

					bayes.learn("positive", Arrays.asList(tokens));

				} else {

					bayes.learn("negative", Arrays.asList(tokens));
				}
			}

			counter++;

			if(counter>=1500){

				//System.out.println();
				//System.out.println(line);

				String prediction = bayes.classify(Arrays.asList(tokens)).getCategory();
				//System.out.println("predicted: " + prediction);
				//System.out.println();

				if(prediction.equals("negative")&&tokens[1].equals("0") || prediction.equals("positive")&&tokens[1].equals("1")){
					correct++;
				}
			}


			line = br.readLine();
			((BayesClassifier<String, String>) bayes).classifyDetailed(Arrays.asList(tokens));
		}

		bayes.setMemoryCapacity(2000); 
		System.out.println("accurracy: " + (float)(correct/(float)500));
		br.close();

	}

	/**
	 * 
	 * @throws IOException
	 */
	public static void trainOnNegatedOnly() throws IOException{
		
		bayes = new BayesClassifier<String, String>();
		
		BufferedReader br = new BufferedReader(new FileReader("movie.reviews.negated"));	
		String line = br.readLine();

		float correct=0;
		int counter = 0;

		while (line != null) {		
			String[] tokens;
			
			//if mostFrequentWords is not specified
			//train naive bayes on all encountered terms in review file
			if(mostFrequentWords==null){

				//for all terms
				tokens = line.toLowerCase().split("\\s+");
			}
			//train naive bayes on using specified dictionary
			else {

				//for top something words
				String[] words = line.toLowerCase().split("\\s+");
				String releventWords = words[0] + " " + words[1] + " ";
	
				for(int i=2;i<words.length;i++){
					if(mostFrequentWords.contains(words[i].toLowerCase().trim())){
						releventWords += words[i].toLowerCase()+" ";
					}
				}
	
				//System.out.println(releventWords);
				tokens = releventWords.split("\\s+");
			}
			
			if(counter<1500){

				//1 = positive
				//0 = negative
				if(tokens[1].equals("1")){

					bayes.learn("positive", Arrays.asList(tokens));

				} else {

					bayes.learn("negative", Arrays.asList(tokens));
				}
			}

			counter++;

			if(counter>=1500){

				//System.out.println();
				//System.out.println(line);

				String prediction = bayes.classify(Arrays.asList(tokens)).getCategory();
				//System.out.println("predicted: " + prediction);
				//System.out.println();

				if(prediction.equals("negative")&&tokens[1].equals("0") || prediction.equals("positive")&&tokens[1].equals("1")){
					correct++;
				}
			}


			line = br.readLine();

			((BayesClassifier<String, String>) bayes).classifyDetailed(Arrays.asList(tokens));
		}

		bayes.setMemoryCapacity(2000); 

		System.out.println("accurracy: " + (float)(correct/(float)500));

		br.close();

	}

	/**
	 * 
	 * @throws IOException
	 */
	public static void trainOnWithoutNEOnly() throws IOException{
		
		bayes = new BayesClassifier<String, String>();
		
		BufferedReader br = new BufferedReader(new FileReader("movie.reviews.tagged"));
		String line = br.readLine();

		float correct=0;
		int counter = 0;

		while (line != null) {	

			String[] tokens;
			
			//if mostFrequentWords is not specified
			//train naive bayes on all encountered terms minus the tagged info in review file
			if(mostFrequentWords==null){
				//remove tagged info
				String strippedTags = line.toLowerCase().replaceAll("<.*?>.*?</.*?>|<.*?/>", "");
				tokens = strippedTags.split("\\s+");
			
			//train naive bayes on using specified dictionary
			}else{
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
				tokens = releventWords.split("\\s+");
			}
			
			if(counter<1500){

				//1 = positive
				//0 = negative
				if(tokens[1].equals("1")){

					bayes.learn("positive", Arrays.asList(tokens));

				} else {

					bayes.learn("negative", Arrays.asList(tokens));
				}
			}

			counter++;

			if(counter>=1500){

				//System.out.println();
				//System.out.println(line);

				String prediction = bayes.classify(Arrays.asList(tokens)).getCategory();
				//System.out.println("predicted: " + prediction);
				//System.out.println();

				if(prediction.equals("negative")&&tokens[1].equals("0") || prediction.equals("positive")&&tokens[1].equals("1")){
					correct++;
				}
			}


			line = br.readLine();

			((BayesClassifier<String, String>) bayes).classifyDetailed(Arrays.asList(tokens));
		}

		bayes.setMemoryCapacity(2000); 

		System.out.println("accurracy: " + (float)(correct/(float)500));

		br.close();

	}

	/**
	 * 
	 * @throws IOException
	 */
	public static void readDictionaryFile(String fileName) throws IOException{
		
		mostFrequentWords = new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(fileName));	

		String line = br.readLine();
		while (line != null) {	

			mostFrequentWords.add(line.trim());
			line = br.readLine();
		}
		br.close();

	}
}
