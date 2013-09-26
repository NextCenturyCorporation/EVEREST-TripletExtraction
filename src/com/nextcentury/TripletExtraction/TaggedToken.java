package com.nextcentury.TripletExtraction;

public class TaggedToken {
	String tag;
	String token;

	public TaggedToken(String newTag, String newToken) {
		tag = newTag;
		token = newToken;
	}
	
	public String toString() {
		return "[" + tag + "]" + token;
	}
}
