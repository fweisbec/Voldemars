package com.example.test;

public class WordList {
	public enum Lang {LATVIAN, RUSSIAN};
	static Lang curr_translated;
	String french;
	String english;
	String latvian;
	String russian;

	public WordList(String french, String english, String latvian, String russian) {
		this.french = french;
		this.english = english;
		this.latvian = latvian;
		this.russian = russian;
	}

	public String translation() {
		if (WordList.curr_translated == Lang.LATVIAN)
			return this.latvian;
		else if (WordList.curr_translated == Lang.RUSSIAN)
			return this.russian;
		else return null;
	}
}