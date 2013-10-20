package com.example.test;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import android.os.Environment;

public class WordList extends ArrayList<Word> {
	private int read_idx;
	
	Word curr() {
		return get(read_idx);
	}
	
	Word next() {
		if (++read_idx == size())
			read_idx = 0;
		
		return curr();
	}
	
	boolean is_last() {
		if (read_idx == size() - 1)
			return true;
		else
			return false;
	}
	
	boolean save_stats() {
		String path = get_stats_path();
		try {
			FileOutputStream fp;
			ObjectOutputStream fo;
			
			fp = new FileOutputStream(path);
			fo = new ObjectOutputStream(fp);
			
			for (Iterator<Word> it = this.iterator(); it.hasNext(); ) {
				Word w;
				
				w = it.next();
				fo.writeObject(w.stats);
			}
			fo.close();
			
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	private void assign_stat(WordStats stats) {
		for (Iterator<Word> it = this.iterator(); it.hasNext(); ) {
			Word w;
			
			w = it.next();
			if (w.translation().equals(stats.translated)) {
				w.stats = stats;
				//Debug.out(stats);
				break;
			}				
		}
	}
	
	static private String get_stats_path() {
		String path = Settings.local_path;
		if (path == null)
			return null;
		
		String lang = Word.translation_language();
		if (lang == null)
			return null;
		
		path += "/word_stats_" + lang;
		
		return path;
	}
	
	boolean load_rates() {		
		String path = get_stats_path();
		try {
			FileInputStream fp;
			ObjectInputStream fo;
			WordStats stats;
			
			fp = new FileInputStream(path);
			fo = new ObjectInputStream(fp);

			for (;;) {
				try {
					stats = (WordStats)fo.readObject();
					assign_stat(stats);
				} catch (EOFException e) {
					break;
				}
			}

			fo.close();
			//Collections.sort(this);
			
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		
		for (Iterator<Word> it = this.iterator(); it.hasNext(); ) {
			Word w;
			
			w = it.next();
			s.append(w.stats.toString());
			s.append("\n");
		}
		
		return s.toString();
	}
	
	private Word pop_rand(float rand) {
		Word w = null;
		float total = 0;
		
		for (Iterator<Word> it = this.iterator(); it.hasNext(); ) {
			w = it.next();
			
			float weight = 1 / w.stats.rate();
			total += weight; 
			if (total >= rand) {
				/*Debug.out("--\n");
				Debug.out("rate: ");
				Debug.out(weight);
				Debug.out("total: ");
				Debug.out(total);
				Debug.out("rand: ");
				Debug.out(rand);
				Debug.out(w.russian);
				Debug.out(w.stats);
				Debug.out("\n");*/
				this.remove(w);
				return w;
			}
			
		}
		Debug.out("Random pop out of range");
		return null;
	}
	
	
	// http://stackoverflow.com/questions/7209465/java-random-number-with-high-probability/7209490#7209490
	public WordList sort() {
		WordList sorted;
		float total = 0;

		for (Iterator<Word> it = this.iterator(); it.hasNext(); ) {
			Word w = it.next();
			
			total += 1 / w.stats.rate();				
		}
		
		sorted = new WordList();
		
		while (!this.isEmpty()) {
			float rand = new Random().nextFloat();
			rand *= total;
			
			Word w = pop_rand(rand);
			sorted.add(w);
			total -= 1 / w.stats.rate();
		}
		
		return sorted;
	}
}
