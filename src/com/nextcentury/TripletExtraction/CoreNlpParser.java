package com.nextcentury.TripletExtraction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;

/**
 * @author 	Josh Williams
 * @date	10Jun2013
 */
public class CoreNlpParser {

	private static Logger log = Logger.getLogger(TestDriver.class);
	Properties props;
	StanfordCoreNLP pipeline;
	ExtractionService extractor = new ExtractionService();
	
	/**
	 * Default constructor
	 * 
	 * Creates a generic CoreNLP instance to annotate with the tokenize, split
	 * and parse options.
	 */
	public CoreNlpParser() {
		props = new Properties();
		props.put("annotators", "tokenize, ssplit, parse");
		pipeline = new StanfordCoreNLP(props);
	}
	
	/**
	 * Creates a CoreNLP instance to annotate with the specified annotators
	 * 
	 * @param annotatorsString String - comma separated list of annotators to
	 * 		perform when parsing. 
	 */
	public CoreNlpParser(String annotatorsString) {
		props = new Properties();
		props.put("annotators", annotatorsString);
		pipeline = new StanfordCoreNLP(props);
	}
    
	/**
	 * Perform the CoreNLP annotation on the entry string, returning the results
	 * as a List of {@link CoreMap} objects corresponding to each sentence.
	 *  
	 * @param entryString String - The string to be annotated
	 * @return List - List of {@link CoreMap} objects, each corresponding to
	 * 		one sentence of the entryString parameter.
	 * @see edu.stanford.nlp.util.CoreMap
	 */
	private List<CoreMap> annotate(String entryString) {
		Annotation document = new Annotation(entryString);
	    
	    long startMilli = new Date().getTime();
	    pipeline.annotate(document);
	    long diffTime = new Date().getTime() - startMilli;
	    log.debug("Parsing took " + diffTime + "ms");
	    
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    return sentences;
	}
	
	/**
	 * Return the CoreNLP annotated and parsed result of the entryString as a
	 * String
	 * 
	 * @param entryString String - The string to be processed
	 * @return String - the flattened tree returned by {@link #annotate(String) annotate}
	 */
	public String parseToString(String entryString) {
		List<CoreMap> sentences = annotate(entryString);
		
		String ret = "";
	     
	    for(CoreMap sentence: sentences) {
	    	Tree tree = sentence.get(TreeAnnotation.class);
	    	ret = ret + tree.toString() + " ";
	    }
	    
	    return ret;
	}
	
	/**
	 * Return the CoreNLP annotated and parsed result of the entryString as a
	 * ArrayList of {@link Tree} objects
	 * 
	 * @param entryString String - The string to be processed
	 * @return ArrayList - ArrayList containing {@link Tree} objects
	 * 		representing each string
	 */
	public ArrayList<Tree> parse(String entryString) {
		List<CoreMap> sentences = annotate(entryString);
	    
	    ArrayList<Tree> ret = new ArrayList<Tree>();
	     
	    for(CoreMap sentence: sentences) {
	    	Tree tree = sentence.get(TreeAnnotation.class);
	    	ret.add(tree);
	    }
	    
	    return ret;
	}

	/**
	* Return the CoreNLP annotated and parsed result of the entryString as a
	* Array of {@link Tree} objects formated to remove periods.
	 * @param entryString String - The string to be processed
	 * @return Array - Array containing {@link Tree} objects
	 * 		representing each string
	**/
	public Tree[] parseAndRemovePeriods(String entryString) {
		List<CoreMap> sentences = annotate(entryString);
	    
	    ArrayList<Tree> ret = new ArrayList<Tree>();
	     
	    for(CoreMap sentence: sentences) {
	    	Tree tree = sentence.get(TreeAnnotation.class);
	    	int numKids = tree.lastChild().numChildren();
	    	String last = tree.lastChild().lastChild().toString();
	    	if(last.indexOf("(. ") == 0) {
	    		tree.lastChild().removeChild(numKids -1);
	    	}
	    	ret.add(tree);
	    }
	    Tree[] arr = ret.toArray(new Tree[ret.size()]);
	    
	    return arr;
	}

	/**
	 * 
	 * @param textBlock String - The block of text to parse and extract
	 * @return ArrayList<{@link Triplet}> - The extracted tuples
	**/
	public ArrayList<Triplet> parseText(String textBlock) {
		Tree[] sentences = parseAndRemovePeriods(textBlock);
		ArrayList<Triplet> returnArray = new ArrayList<Triplet>();

		Triplet result;
		for(Tree sentence : sentences) {
			result = extractor.extractTriplet(sentence);
			if(null != result) {
				returnArray.add(result);
			} else {
				log.info("No triplet found during extraction for " + sentence);
			}
		}

		return returnArray;
	}
	
	/*
	public void checkParserFeedback() {
		Tree tree = parser.parse("My dog has fleas").get(0);
		log.info(tree.label());
		log.info(tree.children()[0].label());
		log.info(tree.children()[0].children()[0].label());
	}
	
	public static void main(String[] args) {
		ExtractionService extractor = new ExtractionService();
		Tree root = extractor.parser.parse("A rare black squirrel has become a regular visitor to a suburban garden").get(0);

		Triplet output = extractor.extractTriplet(root);

		if(output == null)
			System.err.println("ERROR: Could not find triplet");
		else
			output.toString();
	}
	 */
	
	/**
	 * Return the CoreNLP semantic graph result of the entryString as a
	 * ArrayList of {@link SemanticGraph} objects
	 * 
	 * @param entryString String - The string to be processed
	 * @return ArrayList - ArrayList containing {@link SemanticGraph} objects
	 * 		representing each string
	 */
	public ArrayList<SemanticGraph> parseSemanticGraph(String entryString) {
		
		List<CoreMap> sentences = annotate(entryString);
	    
	    ArrayList<SemanticGraph> ret = new ArrayList<SemanticGraph>();
	     
	    for(CoreMap sentence: sentences) {
	    	SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
	    	ret.add(dependencies);
	    }
	    
	    return ret;
	}

}
