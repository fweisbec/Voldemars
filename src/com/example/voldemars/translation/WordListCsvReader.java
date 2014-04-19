package com.example.voldemars.translation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WordListCsvReader extends WordListReader {
	private boolean parseLine(String line, ArrayList<Word> list) {
		Word wl;
		String type, french, english, latvian, russian;
		String[] valid_types = {"n", "a", "p", "c", "r", "v", "ad", "he", "hs"};
		String[] values;

		values = line.split(";");
		/* Malformed line */
		if (values.length != 5)
			return false;

		type = values[0].trim();
		french = values[1].trim();
		english = values[2].trim();
		latvian = values[3].trim();
		russian = values[4].trim();

		boolean type_is_valid = false;

		for (String valid_type : valid_types) {
			if (valid_type.equals(type)) {
				type_is_valid = true;
				break;
			}
		}

		if (!type_is_valid)
			return false;

		if (Word.curr_translated == Word.Lang.LATVIAN)
			if (latvian.equals("*"))
				return true;

		if (Word.curr_translated == Word.Lang.RUSSIAN)
			if (russian.equals("*"))
				return true;

		wl = new Word(french, english, latvian, russian, type);
		list.add(wl);

		return true;
	}

	/*
	 * Inspired by http://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
	 */
	@Override
	public void getWordList(WordList list, File file) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;

			while ((line = br.readLine()) != null) {
				parseLine(line, list);
			}
			fr.close();
		} catch (FileNotFoundException E){}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
