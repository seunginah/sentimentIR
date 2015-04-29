package sentiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import de.daslaboratorium.machinelearning.classifier.*;

/**
 * 
 * @author Qudsia
 *
 */
public class RunNaiveBayesClassifier {
	
	//private static final String INPUT_FILE_NAME = "reviews_5000.txt";
	//private static final String INPUT_FILE_NAME = "movie.reviews";
	private static final String INPUT_FILE_NAME = "movie.reviews.tagged";
	//private static final String INPUT_FILE_NAME = "reviews_2000_negated.txt";
	
	public final static Classifier<String, String> bayes = new BayesClassifier<String, String>();
	
	public static void main(String[] args) throws IOException {

		RunNaiveBayesClassifier.readReviewsFile();
	}

	/**
	 * 
	 * @throws IOException
	 */
	public static void readReviewsFile() throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME));			

		
		String line = br.readLine();
		
		float correct=0;
		int counter = 0;
		
		while (line != null) {	
			
			//remove tagged info
			String strippedTags = line.toLowerCase().replaceAll("<.*?>.*?</.*?>|<.*?/>", "");
			String[] tokens = strippedTags.split("\\s+");
			
			//String[] tokens = line.toLowerCase().split("\\s+");

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
				
//				System.out.println();
//				System.out.println(line);
				
				String prediction = bayes.classify(Arrays.asList(tokens)).getCategory();
//				System.out.println("predicted: " + prediction);
//				System.out.println();
				
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
}
