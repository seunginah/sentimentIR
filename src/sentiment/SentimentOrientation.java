package sentiment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import de.daslaboratorium.machinelearning.classifier.BayesClassifier;

/**
 * since running Gensim requires a lot of python packages
 * i have saved the LSA output as a txt file
 * and will parse it out here
 * 
 * there were 3 experiments, which had to do with using
 * different topic numbers in LSA
 * 
 * I used 2 topics, 10 topics, and then 15 topics
 * 
 * @author Seunginah
 *
 */
public class SentimentOrientation {
	
	// save the semantic orientation outputs to String[]
	private String[] two = new String[1500];
	private String[] ten = new String[1500];
	private String[] fif = new String[1500];

	public SentimentOrientation() {
		// do all three outputs
		try {
			fill("2topics.txt", two );
			fill("10topics.txt",ten);
			fill("15topics.txt",fif);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 2 number of topics
	 * @return
	 */
	public String[] getTwo(){
		return two;
	}
	/**
	 * 10 number of topics 
	 * @return
	 */
	public String[] getTen(){
		return ten;
	}
	/**
	 * 15 number of topics
	 * @return
	 */
	public String[] getFif(){
		return fif;
	}
	
	/**
	 * fill an array with specified file and array
	 * 
	 * @param file
	 * @param array
	 * @throws IOException
	 */
	public void fill(String file, String[] array) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		
		String[] vals = line.split(" ");
		for (int i =0; i<vals.length; i++){
			array[i] = vals[i].replaceAll(",", " ");
			
		}
		
	}
	
	public static void main(String[] args){
		SentimentOrientation so = new SentimentOrientation();
		
	}

}
