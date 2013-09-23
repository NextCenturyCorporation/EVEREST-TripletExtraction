package com.nextcentury.TripletExtraction;

import edu.stanford.nlp.trees.Tree;

class Triplet {
	protected Tree entity1;
	protected Tree relation;
	protected Tree entity2;

	public Triplet(Tree subj, Tree pred, Tree obj) {
		entity1 = subj;
		relation = pred;
		entity2 = obj;
	};

	public Tree getEntity1() {
		return entity1;
	}

	public Tree getEntity2() {
		return entity2;
	}

	public Tree getRelation() {
		return relation;
	}

	public String getEntity1String() {
		return entity1.toString();
	}
	
	public String getEntity2String() {
		return entity2.toString();
	}
	
	public String getRelationString() {
		return relation.toString();
	}

	public String toString() {
		return "{" + entity1.toString() + " " + relation.toString() + " " + entity2.toString() + "}";
	}
}