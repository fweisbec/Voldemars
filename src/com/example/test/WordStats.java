package com.example.test;

import java.io.Serializable;

public class WordStats implements Serializable {
	String translated;
	int nr_asked;
	int nr_success;
	
	WordStats(String translated) {
		this.translated = translated;
	}
	
	public Float rate() {
		if (nr_asked == 0)
			return (float) 0;
		return (float) (nr_success / nr_asked);
	}
	
	public void add_good_answer() {
		nr_asked++;
		nr_success++;
	}
	
	public void add_bad_answer() {
		nr_asked++;
	}
	
	public String toString() {		
		return String.format("%s asked: %d success: %d", translated, nr_asked, nr_success);
		
	}
}
