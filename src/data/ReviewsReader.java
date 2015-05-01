package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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

		//remove previously created output file
		path = FileSystems.getDefault().getPath(Paths.get("").toAbsolutePath().toString(), OUTPUT_FILE_NEGATED);
		Files.deleteIfExists(path);

	}

	/**
	 * 
	 * @throws IOException
	 */
	public void createFileFromReviews(boolean negated) throws IOException{

		int reviewID = 0;
		for(int i = 0; i < (NUMBER_OF_REVIEWS/2); i++){

			File folderNeg = new File("aclImdb\\train\\neg");
			File folderPos = new File("aclImdb\\train\\pos");
			File filesNeg[] = folderNeg.listFiles();
			File filesPos[] = folderPos.listFiles();

			for (File f : filesNeg) {
				if(f.getName().startsWith(i+"_"))
				{   
					//
					if(negated){
						readFileNegation(f, reviewID, 0);
					}
					else{
						readFile(f, reviewID, 0);
					}
					reviewID++;
				}
			}

			for (File f : filesPos) {
				if(f.getName().startsWith(i+"_"))
				{   
					if(negated){
						readFileNegation(f, reviewID, 1);
					}
					else{
						readFile(f, reviewID, 1);
					}
					reviewID++;
				}
			}

		}

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

	public static void printTopTerms(int number){
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

	public static void makeSmallerDictionary(int numWords) throws IOException{

		for (Entry<String, Integer> entry : bigDictionary.entrySet())
		{
			termList.add(new Term(entry.getKey(), entry.getValue()));

		}

		termList.sort(frequencyComparator);

		for(int i =termList.size()-1;i>=termList.size()-numWords;i--){
			//System.out.println(termList.get(i).getDescription() + " " +termList.get(i).getFrequency() );
			smallDictionary.add(termList.get(i)) ;
		}

		for (Term entry : smallDictionary)
		{
			writeFile("smallerdictionary"+numWords +".txt", entry.getDescription());
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

			String[] words = cleanedLine.split(" ");

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

			writeFile(OUTPUT_FILE_NEGATED, reviewID + " " + sentiment + " " + cleanedLine);
		}

		br.close();

	}

	public void createDictionaryWithoutNE(int numWords) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader("movie.reviews.tagged"));

		String line = "";

		while ((line = br.readLine()) != null) {			
			String strippedTags = line.toLowerCase().replaceAll("<.*?>.*?</.*?>|<.*?/>", "");
			String[] tokens = strippedTags.split("\\s+");

			for(int i = 2 ; i < tokens.length ; i++){					

				String lowerCaseWord = tokens[i].toLowerCase();
				Integer count = bigDictionary.get(lowerCaseWord);

				if(count == null){				 		 
					bigDictionary.put(lowerCaseWord, 1);
				}else{
					bigDictionary.put(lowerCaseWord, count + 1);
				}
			}
		}

		makeSmallerDictionary(numWords);
		
		//remove dictionary file if already exits
		Path path = FileSystems.getDefault().getPath(Paths.get("").toAbsolutePath().toString(), "smallerdictionarywithoutNE" + numWords + ".txt");
		Files.deleteIfExists(path);

		//write dictionary to text file
		for (Term entry : smallDictionary)
		{
			writeFile("smallerdictionarywithoutNE" + numWords + ".txt", entry.getDescription());
		}

		br.close();
	}

	public void createDictionaryNegated(int numWords) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader("movie.reviews.negated"));

		String line = "";

		while ((line = br.readLine()) != null) {	

			String[] tokens = line.split("\\s+");

			for(int i = 2 ; i < tokens.length ; i++){				

				String lowerCaseWord = tokens[i].toLowerCase();
				Integer count = bigDictionary.get(lowerCaseWord);

				if(count == null){				 		 
					bigDictionary.put(lowerCaseWord, 1);
				}else{
					bigDictionary.put(lowerCaseWord, count + 1);
				}
			}
		}

		makeSmallerDictionary(numWords);
		
		//remove dictionary file if already exits
		Path path = FileSystems.getDefault().getPath(Paths.get("").toAbsolutePath().toString(), "smallerdictionarynegated" + numWords + ".txt");
		Files.deleteIfExists(path);
		
		//write dictionary to text file
		for (Term entry : smallDictionary)
		{
			writeFile("smallerdictionarynegated" + numWords + ".txt", entry.getDescription());
		}

		br.close();
	}

	/**
	 * Creates a dictionary of words
	 * 
	 * @param numWords
	 * @throws IOException
	 */
	public static void createDictionary(int numWords) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader("movie.reviews"));

		String line = "";

		while ((line = br.readLine()) != null) {	

			String[] tokens = line.split("\\s+");

			for(int i = 2 ; i < tokens.length ; i++){				

				String lowerCaseWord = tokens[i].toLowerCase();
				Integer count = bigDictionary.get(lowerCaseWord);

				if(count == null){				 		 
					bigDictionary.put(lowerCaseWord, 1);
				}else{
					bigDictionary.put(lowerCaseWord, count + 1);
				}
			}
		}

		makeSmallerDictionary(numWords);

		//remove dictionary file if already exits
		Path path = FileSystems.getDefault().getPath(Paths.get("").toAbsolutePath().toString(), "smallerdictionary" + numWords + ".txt");
		Files.deleteIfExists(path);
		
		//write dictionary to text file
		for (Term entry : smallDictionary)
		{
			writeFile("smallerdictionary" + numWords + ".txt", entry.getDescription());
		}

		br.close();
	}



}
