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
		String src, dst;
		String[] values;

		values = line.split(";");
		/* Malformed line */
		if (values.length != 2)
			return false;

		src = values[0].trim();
		dst = values[1].trim();

		if (dst.equals("*"))
			return true;

		wl = new Word(src, dst, "r");
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
