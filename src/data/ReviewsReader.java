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

public class ReviewsReader {

	private static final int NUMBER_OF_REVIEWS=2000; 
	private static final String OUTPUT_FILE_ALL="reviews_" + (NUMBER_OF_REVIEWS*2) + ".txt";

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
					readFile(f, reviewID, 0);
					reviewID++;
				}
			}

			for (File f : filesPos) {
				if(f.getName().startsWith(i+"_"))
				{   
					readFile(f,reviewID, 1);
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
			writeFile(reviewID + " " + sentiment + " " + line);
		}
		
		br.close();

	}

	/**
	 * 
	 * @param line
	 * @throws IOException
	 */
	private static void writeFile(String line) throws IOException {
		
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE_ALL, true)));
		out.println(line);
		out.close();
		
	}

}
