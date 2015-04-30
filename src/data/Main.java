package data;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		ReviewsReader rr = new ReviewsReader();
		rr.readReviewDataFiles();

	}

}
