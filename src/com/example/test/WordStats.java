package com.example.test;

import java.io.Serializable;

public class WordStats implements Serializable{
	String native_word;
	private Integer nr_asked;
	private Integer nr_success;
	
	WordStats(String native_word) {
		this.native_word = native_word;
		nr_asked = new Integer(0);
		nr_success = new Integer(0);
	}
	
	public Float weight() {
		if (nr_success == 0)
			return (float)-1;
		return ((float)nr_asked) / ((float)nr_success);
	}
	
	public void add_good_answer() {
		nr_asked++;
		nr_success++;
	}
	
	public void add_bad_answer() {
		nr_asked++;
	}

	/*
	@Override
	public int compareTo(WordStats another) {
		int cmp;
		
		cmp = rate().compareTo(another.rate());
		if (cmp == 0)
			cmp = nr_asked.compareTo(another.nr_asked);
		
		return cmp;
	}*/
	
	public String toString() {		
		return String.format("%s asked: %d success: %d", native_word, nr_asked, nr_success);
	}
}
