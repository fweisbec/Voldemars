package com.example.test;
import java.util.ArrayList;
import java.util.Collections;

public class WordListIterator {
	private int idx;
	private WordList curr_word;
	private ArrayList<WordList> list;
	private TranslationOutput out;

	public WordListIterator(TranslationOutput out) {
		this.out = out;
	}

	public void iter() {
		if (idx == list.size()) {
			Collections.shuffle(list);
			Debug.out("idx " + Integer.valueOf(idx));
			idx = 0;
		}
		curr_word = list.get(idx++);
		out.set_next(curr_word.french);
	}

	void fail() {
		out.reset_translation();
	}

	public void init(ArrayList<WordList> list) {
		this.list = list;
		Collections.shuffle(list);
		Debug.out("idx " + Integer.valueOf(idx));
		iter();
	}
	
	public String curr_translation() {
		return curr_word.translation();
	}
}