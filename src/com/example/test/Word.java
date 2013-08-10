package com.example.test;

public class Word {
	public enum Lang {LATVIAN, RUSSIAN};
	static Lang curr_translated;
	String french;
	String english;
	String latvian;
	String russian;
	WordRate rate;

	public Word(String french, String english, String latvian, String russian) {
		this.french = french;
		this.english = english;
		this.latvian = latvian;
		this.russian = russian;
	}

	public String translation() {
		if (Word.curr_translated == Lang.LATVIAN)
			return this.latvian;
		else if (Word.curr_translated == Lang.RUSSIAN)
			return this.russian;
		else return null;
	}
}