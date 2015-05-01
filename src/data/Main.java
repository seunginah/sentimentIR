package data;

import java.io.IOException;

/**
 * This part of the application deals with reading the original review files
 * and formatting them in to text files for later use. You do not need to run this because
 * all needed review files and dictionaries files have been already created.
 * 
 * @author Qudsia and Grace
 *
 */
public class Main {

	public static void main(String[] args) throws IOException {

		//ReviewsReader rr = new ReviewsReader();


		//to create just a text file using bag of words model from movie reviews
		//rr.createFileFromReviews(false);//negation=false

		//to create just a text file using negation model from movie reviews
		//rr.createFileFromReviews(true);//negation=true


		//to create a dictionary
		ReviewsReader.createDictionary(1000);
		//ReviewsReader.printTopTerms(200);

		//to create a dictionary without named entities
		//rr.createDictionaryWithoutNE(3000);

		//to create a dictionary with negation model
		//rr.createDictionaryNegated(5000);	

	}

}
