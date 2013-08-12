package com.example.test;

import java.io.Serializable;

public class WordStats implements Serializable, Comparable<WordStats> {
	String translated;
	private Integer nr_asked;
	private Integer nr_success;
	
	WordStats(String translated) {
		this.translated = translated;
		nr_asked = new Integer(0);
		nr_success = new Integer(0);
	}
	
	public Float rate() {
		if (nr_asked == 0)
			return (float) 0;
		return ((float)nr_success) / ((float)nr_asked);
	}
	
	public void add_good_answer() {
		nr_asked++;
		nr_success++;
	}
	
	public void add_bad_answer() {
		nr_asked++;
	}
	
	@Override
	public int compareTo(WordStats another) {
		int cmp;
		
		cmp = rate().compareTo(another.rate());
		if (cmp == 0)
			cmp = nr_asked.compareTo(another.nr_asked);
		
		return cmp;
	}
	
	public String toString() {		
		return String.format("%s asked: %d success: %d", translated, nr_asked, nr_success);
	}
}
