
package sentiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Set;

import de.daslaboratorium.machinelearning.classifier.*;

/**
 * 
 * @author Qudsia & Grace
 *
 */
public class RunNaiveBayesEvaluator {
	
	private static final String INPUT_FILE_NAME = "reviews_2000.txt";
	private static String predicted="";
	
	private static double numReviews = 0.0;
	
	private static double truePos = 0.0; // count the correct predictions
	private static double falsePos = 0.0;
	private static double trueNeg = 0.0;
	private static double falseNeg =0.0;
	
	public static void main(String[] args) throws IOException {

		RunNaiveBayesClassifier.readReviewsFile();
		
		double tPPercent = truePos/numReviews;
		double fPPercent = falsePos/numReviews;
		double tNPercent = trueNeg/numReviews;
		double fNPercent = falseNeg/numReviews;
		
		double correctPercent = (truePos+trueNeg)/numReviews;
		double incorrectPercent = (falsePos+falseNeg)/numReviews;
		
		
		System.out.println("doubleval "+correctPercent);
		
		System.out.println("\ntruePos: "+truePos+" falsePos: "+falsePos+"\ntrueNeg: "
		+trueNeg+" falseNeg: "+falseNeg+"\ntotal correct: "+correctPercent+" incorrect: "+incorrectPercent);
	}

	/**
	 * 
	 * @throws IOException
	 */
	private static void readReviewsFile() throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME));		
		
		final Classifier<String, String> bayes = new BayesClassifier<String, String>();
		
		String line = br.readLine();

		int counter = 0;
		int trainon = 1900;
		
		while (line != null) {	
			
			String[] tokens = line.split(" ");
			String label = tokens[1];
			
			// learn the first 1900
			if(counter<trainon){
				
				//1 = positive
				//0 = negative
				if(label.equals("1")){
					
					bayes.learn("positive", Arrays.asList(tokens));
					
				} else {
	
					bayes.learn("negative", Arrays.asList(tokens));
				}
			}
	        		
			counter++;
			
			// test on the rest
			if(counter>=trainon){
				
				System.out.println();
				System.out.println(line);
				// this returns a Set<K> of categories bayes knows about
				predicted = bayes.classify(Arrays.asList(tokens)).getCategory();
				System.out.println("predicted: "+predicted);
				
				// validate this example by passing prediction + label
				validate(predicted, label);
				
			}
			
			
			line = br.readLine();
		
			((BayesClassifier<String, String>) bayes).classifyDetailed(Arrays.asList(tokens));
		}
		
		// the number of reviews we've tested
		numReviews = counter-trainon;
	    bayes.setMemoryCapacity(2000); 

		br.close();

	}
	
	/**
	 * 
	 * @param guess
	 * @param correct
	 * @return
	 */
	public static void validate(String guess, String correct){
		// a correct guess can be positive with label 1
		if (guess.trim().matches("positive")){
			if (correct.trim().matches("1")){
				truePos ++;
			}
			else{
				falsePos ++;
			}
		}
		// a correct guess can be negative with label 0
		else{
			if(correct.trim().matches("0")){
				trueNeg ++;
			}
			else{
				falseNeg++;
			}
		}
		
	}
}
