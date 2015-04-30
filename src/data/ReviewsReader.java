package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.Map.Entry;

public class ReviewsReader {

	private static final int NUMBER_OF_REVIEWS=2000; 
	private static final String OUTPUT_FILE_ALL="reviews_" + (NUMBER_OF_REVIEWS) + ".txt";
	private static final String OUTPUT_FILE_NEGATED="reviews_" + (NUMBER_OF_REVIEWS) + "_negated.txt";
	private static TreeMap<String,Integer> bigDictionary = new TreeMap<String,Integer>() ; //all terms
	private static LinkedList<Term> smallDictionary = new LinkedList<Term>();//top something terms
	private static ArrayList<String> stopWords = new ArrayList<String>();
	private static LinkedList<Term> termList = new LinkedList<Term>() ;

	/**
	 * 
	 * @throws IOException
	 */
	public ReviewsReader() throws IOException{
		
		//remove previously created output file
		Path path = FileSystems.getDefault().getPath(Paths.get("").toAbsolutePath().toString(), OUTPUT_FILE_ALL);
		Files.deleteIfExists(path);

	}

	/**
	 * 
	 * @throws IOException
	 */
	public void readReviewDataFiles() throws IOException{
		
		int reviewID = 0;
		for(int i = 0; i < (NUMBER_OF_REVIEWS/2); i++){

			File folderNeg = new File("aclImdb\\train\\neg");
			File folderPos = new File("aclImdb\\train\\pos");
			File filesNeg[] = folderNeg.listFiles();
			File filesPos[] = folderPos.listFiles();

			for (File f : filesNeg) {
				if(f.getName().startsWith(i+"_"))
				{   
					//readFile(f, reviewID, 0);
					readFileNegation(f, reviewID, 0);
					reviewID++;
				}
			}

			for (File f : filesPos) {
				if(f.getName().startsWith(i+"_"))
				{   
					//readFile(f,reviewID, 1);
					readFileNegation(f, reviewID, 1);
					reviewID++;
				}
			}

		}
		
		System.out.println("total number terms: " + bigDictionary.size());
		//makeSmallerDictionary(2500);
		//printTopTerms(200);
	}

	/**
	 * 
	 * @param f
	 * @param sentiment
	 * @throws IOException
	 */
	private static void readFile(File f, int reviewID, int sentiment) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;

		while ((line = br.readLine()) != null) {
			//words in a line
			String[] words = line.split(" ") ;

			//put each new word in the big dictionary and keep track of frequency of words
			for(int i = 1 ; i < words.length ; i++){					

				String lowerCaseWord = words[i].toLowerCase();
				Integer count = bigDictionary.get(lowerCaseWord);

				if(count == null){				 		 
					bigDictionary.put(lowerCaseWord, 1);
				}else{
					bigDictionary.put(lowerCaseWord, count + 1);
				}
			}
			
			writeFile(OUTPUT_FILE_ALL, reviewID + " " + sentiment + " " + line);
		}
		
		br.close();

	}

	/**
	 * 
	 * @param line
	 * @throws IOException
	 */
	private static void writeFile(String fileName, String line) throws IOException {
		
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		out.println(line);
		out.close();
		
	}
	
	private static void printTopTerms(int number){
		int count = 0;
		for (Term entry : smallDictionary)
		{
			System.out.println(entry.getDescription() + " [" + entry.getFrequency()+"]");
			count++;
			if(count==number){
				break;
			}
		}
	}

	private static void makeSmallerDictionary(int numWords){


		for (Entry<String, Integer> entry : bigDictionary.entrySet())
		{
			//	    System.out.println(entry.getKey() + "/" + entry.getValue());
			//if(!stopWords.contains(entry.getKey())){
				termList.add(new Term(entry.getKey(), entry.getValue()));
			//}
		}

		termList.sort(frequencyComparator);

		for(int i =termList.size()-1;i>=termList.size()-numWords;i--){
			//System.out.println(termList.get(i).getDescription() + " " +termList.get(i).getFrequency() );
			smallDictionary.add(termList.get(i)) ;
		}


	}
	
	/**
	 * Comparator 
	 */
	public static Comparator<Term> frequencyComparator = new Comparator<Term>(){

		public int compare(Term t1, Term t2) {
			return (int) (t1.getFrequency() - t2.getFrequency());
		}
	};
	
	private static void readFileNegation(File f, int reviewID, int sentiment) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(f));

		String line = "";
		boolean negationDetected=false;
		
		while ((line = br.readLine()) != null) {

			line = line.replaceAll("[,]", " , ");
			line = line.replaceAll("[.]", " . ");
			line = line.replaceAll("[!]", " ! ");
			line = line.replaceAll("[?]", " ? ");
			
			String[] terms = line.split(" ");

			String cleanedLine="";

			for(int i=0;i<terms.length;i++){		
				
				String term = terms[i].toLowerCase().trim();

				//if a negation is detected prefix NOT_ to words until 
				if(negationDetected==true && !term.equals(",") && !term.equals(".") && !term.equals("!") && !term.equals("?") && !stopWords.contains(term)){
					
					term = "NOT_" + term;

				}

				if(term.equals(",") || term.equals(".")){
					negationDetected=false;	
				}

				cleanedLine+= term;
				cleanedLine+=" ";

				if(term.equals("don't") || term.equals("didn't") ||term.equals("isn't") || 
						term.equals("aren't") ||term.equals("weren't") ||term.equals("wouldn't") ||
						term.equals("not")|| term.equals("no") || term.equals("won't") ||
						term.equals("wasn't") || term.equals("doesn't") || term.equals("can't")||
						term.equals("never") ||term.equals("ain't") ){
					
					negationDetected=true;
				}


			}

			writeFile(OUTPUT_FILE_NEGATED, reviewID + " " + sentiment + " " + cleanedLine);
		}

		br.close();

	}


}
