package com.example.voldemars.translation;

public class Word {
	public enum Lang {LATVIAN, RUSSIAN};
	public static Lang curr_translated;
	String french;
	String english;
	String latvian;
	String russian;
	WordStats stats;

	public Word(String french, String english, String latvian, String russian) {
		this.french = french;
		this.english = english;
		this.latvian = latvian;
		this.russian = russian;
		this.stats = new WordStats(this.french);
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