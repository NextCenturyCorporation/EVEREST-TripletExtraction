package com.nextcentury.TripletExtraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.Tag;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class ExtractionService {

	private static Logger log = Logger.getLogger(ExtractionService.class);
	
	public ExtractionService() {
	
	}

	public Triplet extractTriplet(Tree root) {
		/*
		 * The trees that the Stanford parser generates always have a root node
		 * called ROOT and exactly one child node called S which represents the sentence.
		 * Thus, to get the NP and VP subtrees, we must get both grand children of ROOT.
		 */
		Tree subjectTree = extractSubject(root.firstChild().firstChild());
		Tree predicateTree = extractPredicate(root.firstChild().lastChild());
		Tree objectTree = extractObject(root.firstChild().lastChild());

		if(subjectTree == null || predicateTree == null || objectTree == null)
			return null;

		Triplet triplet = new Triplet(subjectTree, predicateTree, objectTree);

		return triplet;
	}

	private Tree extractSubject(Tree npSubtree) {

		return findFirstNoun(npSubtree);
	}

	private Tree extractPredicate(Tree vpSubtree) {

		return findDeepestVerb(vpSubtree);
	}

	private Tree extractObject(Tree vpSubtree) {
		String wordType;
		Tree object = null;
		List<Tree> siblings = new ArrayList<Tree>();
		int currentSibling = 0;

		findDecendents(vpSubtree, siblings);

		while(object == null && currentSibling < siblings.size()) {
			wordType = siblings.get(currentSibling).value();

			if(wordType.equalsIgnoreCase("NP"))
				object = findFirstNoun(siblings.get(currentSibling));
			else if(wordType.equalsIgnoreCase("PP"))
				/*
				 * Here we make an assumption that the last child of a PP will always
				 * be a NP. For now, this works, but it may have to change later.
				 */
				object = findFirstNoun(siblings.get(currentSibling).lastChild());
			else
				object = findFirstAdjective(siblings.get(currentSibling));

			currentSibling++;
		}

		return object;
	}

	private Tree findFirstNoun(Tree npSubtree) {
		Tree noun = null;
		int currentChild = 0;

		while(noun == null && currentChild < npSubtree.numChildren()) {

			if(isNoun(npSubtree.getChild(currentChild).value()))
				noun = npSubtree.getChild(currentChild);

			currentChild++;
		}

		if(noun == null)
			return noun;
		
		return noun.firstChild();
	}

	private Tree findDeepestVerb(Tree vpSubtree) {
		Tree verb = null;

		for(int i = 0; i < vpSubtree.numChildren(); i++) {

			if(vpSubtree.getChild(i).value().equalsIgnoreCase("VP"))
				return findDeepestVerb(vpSubtree.getChild(i));
			else if(isVerb(vpSubtree.getChild(i).value()))
				verb = vpSubtree.getChild(i);
		}

		if(verb == null)
			return verb;

		return verb.firstChild();
	}

	private Tree findFirstAdjective(Tree tree) {
		Tree adjective = null;
		int currentChild = 0;

		while(adjective == null && currentChild < tree.numChildren()) {

			if(isAdjective(tree.getChild(currentChild).value()))
				adjective = tree.getChild(currentChild);

			currentChild++;
		}

		if(adjective == null)
			return adjective;
		
		return adjective.firstChild();
	}

	private void findDecendents(Tree tree, List<Tree> siblings) {
		String wordType = tree.value();

		if(wordType.equalsIgnoreCase("NP") || wordType.equalsIgnoreCase("PP") ||
				wordType.equalsIgnoreCase("ADJP"))
			siblings.add(tree);		
		else {

			for(int i = 0; i < tree.numChildren(); i++) {

				if(tree.getChild(i) != null)
					findDecendents(tree.getChild(i), siblings);
			}
		}
	}

	private boolean isNoun(String wordType) {
		return wordType.equalsIgnoreCase("NN") || wordType.equalsIgnoreCase("NNP") ||
				wordType.equalsIgnoreCase("NNPS") || wordType.equalsIgnoreCase("NNS");
	}

	private boolean isVerb(String wordType) {
		return wordType.equalsIgnoreCase("VB") || wordType.equalsIgnoreCase("VBD") ||
				wordType.equalsIgnoreCase("VBG") || wordType.equalsIgnoreCase("VBN") ||
				wordType.equalsIgnoreCase("VBP") || wordType.equalsIgnoreCase("VBZ");
	}

	private boolean isAdjective(String wordType) {
		return wordType.equalsIgnoreCase("JJ") || wordType.equalsIgnoreCase("JJR") ||
				wordType.equalsIgnoreCase("JJS");
	}
/*
	public static void main(String[] args) {
		ExtractionService extractor = new ExtractionService();
		
		Tree root = extractor.parser.parse("A rare black squirrel has become a regular visitor to a suburban garden").get(0);

		Triplet output = extractor.extractTriplet(root);

		if(output == null)
			System.err.println("ERROR: Could not find triplet");
		else {
			System.out.println( output.toString() );
		}

		String testString = "Now is the time for all good men to come to the aid of their country.  Sam has a fork. A rare black squirrel has become a regular visitor to a suburban garden.";
		
		ArrayList<SemanticGraph> dependencies = extractor.parser.parseSemanticGraph(testString);
		
		if(dependencies == null)
			System.err.println("ERROR: Could not find dependencies");
		else {
			
			for (int k=0; k <dependencies.size(); k++ ) {

				System.out.println( dependencies.get(k).toString() );

				Collection<IndexedWord> roots = dependencies.get(k).getRoots();
				
				for (IndexedWord r : roots) {
					System.out.println( r.toString() );					
					System.out.println( dependencies.get(k).childPairs(r).toString() );					
				}
				
				System.out.println();
				
/*
				System.out.println( dependencies.get(k).toString() );
				System.out.println( dependencies.get(k).toDotFormat() );
				System.out.println( dependencies.get(k).getRoots().toString());
				System.out.println( dependencies.get(k).childRelns(dependencies.get(k).getFirstRoot()).toString() );
				System.out.println( dependencies.get(k).childPairs(dependencies.get(k).getFirstRoot()).toString() );
				System.out.println( dependencies.get(k).getEdgeSet().toString());
				System.out.println( dependencies.get(k).vertexSet().toString());
*/
/*			}
		}

	}
*/
}
