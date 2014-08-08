package com.example.voldemars.translation;

public class Word {
	String src;
	String dst;
	String type;
	String hint;
	WordStats stats;

	public Word(String src, String dst, String type, String hint) {
		this.src = src;
		this.dst = dst;
		this.type = type;
	    // Default hint to source is what we want most of the time with hint type
	    if (hint.equals("") && (type.equals("hs") || type.equals("he")))
    		hint = this.src;
		this.hint = hint;
		this.stats = new WordStats(this.src);
	}
	
	public Word(String src, String dst, String type) {
		this(src, dst, type, "");
	}

	public String translation() {
		return this.dst;
	}

	/*
	@Override
	public int compareTo(Word another) {
		return stats.compareTo(another.stats);
	}
	 */
}