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

	private static ArrayList<String> MostFrequentWords = new ArrayList<String>();
	public final static Classifier<String, String> bayes = new BayesClassifier<String, String>();

	//private static final String INPUT_FILE_NAME = "movie.reviews";
	//private static final String INPUT_FILE_NAME = "movie.reviews.tagged";
	//private static final String INPUT_FILE_NAME = "movie.reviews.negated";

	//private static final String DICTIONARY_FILE_NAME = "smallerdictionary1000.txt";
	//private static final String DICTIONARY_FILE_NAME = "smallerdictionary2000.txt";
	//private static final String DICTIONARY_FILE_NAME = "smallerdictionary3000.txt";
	//private static final String DICTIONARY_FILE_NAME = "smallerdictionary4000.txt";
	private static final String DICTIONARY_FILE_NAME = "smallerdictionary5000.txt";

	//private static final String DICTIONARY_FILE_NAME = "smallerdictionarywithoutNE1000.txt";
	//private static final String DICTIONARY_FILE_NAME = "smallerdictionarywithoutNE2000.txt";
	//private static final String DICTIONARY_FILE_NAME = "smallerdictionarywithoutNE3000.txt";
	//private static final String DICTIONARY_FILE_NAME = "smallerdictionarywithoutNE4000.txt";
	//private static final String DICTIONARY_FILE_NAME = "smallerdictionarywithoutNE5000.txt";

	//private static final String DICTIONARY_FILE_NAME = "smallerdictionarynegated1000.txt";
	//private static final String DICTIONARY_FILE_NAME = "smallerdictionarynegated2000.txt";
	//private static final String DICTIONARY_FILE_NAME = "smallerdictionarynegated3000.txt";
	//private static final String DICTIONARY_FILE_NAME = "smallerdictionarynegated4000.txt";
	//private static final String DICTIONARY_FILE_NAME = "smallerdictionarynegated5000.txt";

	public static void main(String[] args) throws IOException {
		
		readDictionaryFile();
		RunNaiveBayesClassifier.readReviewsFile();
		
	}

	/**
	 * 
	 * @throws IOException
	 */
	public static void readReviewsFile() throws IOException{

		BufferedReader br = new BufferedReader(new FileReader("movie.reviews"));
		String line = br.readLine();

		float correct=0;
		int counter = 0;

		while (line != null) {		

			//for lower case only and negated files
			//String[] tokens = line.toLowerCase().split("\\s+");

			//for top something words
			String[] words = line.toLowerCase().split("\\s+");
			String releventWords = words[0] + " " + words[1] + " ";

			for(int i=2;i<words.length;i++){
				if(MostFrequentWords.contains(words[i].toLowerCase().trim())){
					releventWords += words[i].toLowerCase()+" ";
				}
			}

			//System.out.println(releventWords);
			String[] tokens = releventWords.split("\\s+");

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
	public static void readNegatedReviewsFile() throws IOException{

		BufferedReader br = new BufferedReader(new FileReader("movie.reviews.negated"));	
		String line = br.readLine();

		float correct=0;
		int counter = 0;

		while (line != null) {		

			//for lower case only and negated files
			//String[] tokens = line.toLowerCase().split("\\s+");

			//for top something words
			String[] words = line.toLowerCase().split("\\s+");
			String releventWords = words[0] + " " + words[1] + " ";

			for(int i=2;i<words.length;i++){
				if(MostFrequentWords.contains(words[i].toLowerCase().trim())){
					releventWords += words[i].toLowerCase()+" ";
				}
			}

			//System.out.println(releventWords);
			String[] tokens = releventWords.split("\\s+");

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
	public static void readTaggedReviewsFile() throws IOException{

		BufferedReader br = new BufferedReader(new FileReader("movie.reviews.tagged"));
		String line = br.readLine();

		float correct=0;
		int counter = 0;

		while (line != null) {	

			//remove tagged info
			//String strippedTags = line.toLowerCase().replaceAll("<.*?>.*?</.*?>|<.*?/>", "");
			//String[] tokens = strippedTags.split("\\s+");

			//for top words without named entities
			//remove tagged info
			String strippedTags = line.toLowerCase().replaceAll("<.*?>.*?</.*?>|<.*?/>", "");
			String[] words = strippedTags.toLowerCase().split("\\s+");
			String releventWords = words[0] + " " + words[1] + " ";

			for(int i=2;i<words.length;i++){
				if(MostFrequentWords.contains(words[i].toLowerCase().trim())){
					releventWords += words[i].toLowerCase()+" ";
				}
			}
			//System.out.println(releventWords);
			String[] tokens = releventWords.split("\\s+");

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
	public static void readDictionaryFile() throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(DICTIONARY_FILE_NAME));	

		String line = br.readLine();
		while (line != null) {	

			MostFrequentWords.add(line.trim());
			line = br.readLine();
		}
		br.close();

	}
}
