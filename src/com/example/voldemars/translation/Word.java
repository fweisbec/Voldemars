package com.example.voldemars.translation;

public class Word {
	public enum Lang {LATVIAN, RUSSIAN};
	public static Lang curr_translated;
	String french;
	String english;
	String latvian;
	String russian;
	String type;
	String hint;
	WordStats stats;

	public Word(String french, String english, String latvian, String russian, String type, String hint) {
		this.french = french;
		this.english = english;
		this.latvian = latvian;
		this.russian = russian;
		this.type = type;
	    // Default hint to source is what we want most of the time with hint type
	    if (hint.equals("") && (type.equals("hs") || type.equals("he")))
    		hint = this.french;
		this.hint = hint;
		this.stats = new WordStats(this.french);
	}
	
	public Word(String french, String english, String latvian, String russian, String type) {
		this(french, english, latvian, russian, type, "");
	}

	public String translation() {
		if (Word.curr_translated == Lang.LATVIAN)
			return this.latvian;
		else if (Word.curr_translated == Lang.RUSSIAN)
			return this.russian;
		else return null;
	}

	public static String translation_language() {
		if (Word.curr_translated == Lang.LATVIAN)
			return "latvian";
		else if (Word.curr_translated == Lang.RUSSIAN)
			return "russian";
		else
			return null;
	}

	/*
	@Override
	public int compareTo(Word another) {
		return stats.compareTo(another.stats);
	}
	 */
}