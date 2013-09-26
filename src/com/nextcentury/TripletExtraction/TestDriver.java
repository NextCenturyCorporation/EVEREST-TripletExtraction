package com.nextcentury.TripletExtraction;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class TestDriver {

	private static Logger log = Logger.getLogger(TestDriver.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//testParser();
		//testExtraction();
		testGetTaggedText();
		testParseAndRemovePeriods();
		testDependencyTree();
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

	private static void testGetTaggedText() {
		String testSentence = "Now is the time for all good men to come to the aid of their country.";
		
		CoreNlpPOSTagger tagger = new CoreNlpPOSTagger();
		
		System.out.println(tagger.getTaggedText(testSentence));
		System.out.println("\n");
	}
	
	private static void testParseAndRemovePeriods() {
		String testSentence = "Now is the time for all good men to come to the aid of their country.";
		
		CoreNlpParser parser = new CoreNlpParser();
		List<Tree> results = parser.getTextAnnotatedTree(testSentence);
		for(Tree tree : results) {
			tree.pennPrint();
		}
		
		System.out.println("\n");
	}
	
	private static void testDependencyTree() {
		String testSentence = "Now is the time for all good men to come to the aid of their country.";
		
		CoreNlpParser parser = new CoreNlpParser();
		List<SemanticGraph> result = parser.getTextDependencyTree(testSentence);
		for(SemanticGraph graph : result) {
			graph.prettyPrint();
		}
	}
}
