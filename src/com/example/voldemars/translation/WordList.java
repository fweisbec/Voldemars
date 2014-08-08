package com.example.voldemars.translation;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.example.voldemars.settings.Settings;

public class WordList extends ArrayList<Word> {
	private int read_idx;
	private ArrayList<WordStats> orphaned_stats;

	WordList() {
		orphaned_stats = new ArrayList<WordStats>();
	}

	WordList(WordList w) {
		orphaned_stats = w.orphaned_stats;
	}

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

			/* Save stats for wordlist */
			for (Iterator<Word> it = this.iterator(); it.hasNext(); ) {
				Word w;

				w = it.next();
				fo.writeObject(w.stats);
			}

			/* Save stats that weren't used by wordlist */
			for (Iterator<WordStats> it = orphaned_stats.iterator(); it.hasNext(); ) {
				WordStats stats = it.next();
				fo.writeObject(stats);
			}

			fo.close();

		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private boolean assign_stat(WordStats stats) {
		//Debug.out(stats);
		for (Iterator<Word> it = this.iterator(); it.hasNext(); ) {
			Word w;

			w = it.next();
			if (w.src.equals(stats.native_word)) {
				w.stats = stats;
				return true;
			}
		}
		return false;
	}

	static private String get_stats_path() {
		String path = Settings.local_path;
		if (path == null)
			return null;

		path += "/word_stats";

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
					stats = (WordStats) fo.readObject();
					if (!assign_stat(stats))
						orphaned_stats.add(stats);
				} catch (EOFException e) {
					break;
				}
			}

			fo.close();
			// Collections.sort(this);

		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		for (Iterator<Word> it = this.iterator(); it.hasNext();) {
			Word w;

			w = it.next();
			s.append(w.stats.toString());
			s.append("\n");
		}

		return s.toString();
	}

	private Word pop_rand(float rand, float default_weight) {
		Word w = null;
		float total = 0;

		for (Iterator<Word> it = this.iterator(); it.hasNext();) {
			float weight;
			w = it.next();

			weight = w.stats.weight();
			if (weight < 1)
				weight = default_weight;

			total += weight;
			if (total >= rand) {
				/*
				 * Debug.out("--\n"); Debug.out("weight: "); Debug.out(weight);
				 * Debug.out("total: "); Debug.out(total); Debug.out("rand: ");
				 * Debug.out(rand); Debug.out(w.russian); Debug.out(w.stats);
				 * Debug.out("\n");
				 */
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
		float default_weight;
		int never_asked = 0;

		for (Iterator<Word> it = this.iterator(); it.hasNext();) {
			Float weight;

			Word w = it.next();
			weight = w.stats.weight();
			if (weight >= 1)
				total += weight;
			else
				never_asked++;
		}

		default_weight = total;
		total += default_weight * never_asked;

		sorted = new WordList(this);

		while (!this.isEmpty()) {
			float weight;
			float rand = new Random().nextFloat();
			rand *= total;

			Word w = pop_rand(rand, default_weight);
			sorted.add(w);

			weight = w.stats.weight();
			if (weight < 1)
				weight = default_weight;

			total -= weight;
		}

		return sorted;
	}
}
