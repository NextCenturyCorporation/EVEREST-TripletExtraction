package com.nextcentury.TripletExtraction;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class TestDriver {

	private static Logger log = Logger.getLogger(TestDriver.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testParser();
		//testExtraction();
	}
	
	private static void testExtraction() {
		ExtractionService extractor = new ExtractionService();
		//extractor.checkParserFeedback();
	}
	
	private static void testParser() {
		log.debug("Running test driver!");

		CoreNlpParser parser = new CoreNlpParser();

		String sentence = "My dog has fleas";
		String sentence2 = "My dog has fleas. Fleas bite the dog. Fleas make me sad.";
		String sentence3 = "The man does the things he does, with genius, power and magic, as he knows nothing else will suffice.";

		ArrayList<Triplet> results = parser.parseText(sentence);
		System.out.println(results.size());
		for(Triplet res : results) {
			System.out.println(res.toString());
		}

		results = parser.parseText(sentence2);
		System.out.println(results.size());
		for(Triplet res : results) {
			System.out.println(res.toString());
		}
		
		results = parser.parseText(sentence3);
		System.out.println(results.size());
		for(Triplet res : results) {
			System.out.println(res.toString());
		}
	}

}
