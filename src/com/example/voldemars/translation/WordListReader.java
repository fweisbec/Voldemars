package com.example.voldemars.translation;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.example.voldemars.settings.Settings;

import android.os.Environment;




abstract public class WordListReader {
	abstract public void getWordList(WordList list, File file);
	static public WordListReader getWordListReader(File f)
	{
		if (f.getName().endsWith(".xml"))
			return new WordListXmlReader();
		else
			return new WordListCsvReader();
		
	}
	static public WordList getWordListAll(Set<String> filenames) {
		WordList list = new WordList();
		File dir = new File(Settings.wordlist_path);

		for (String filename : filenames) {
			File file = new File(dir, filename);
			WordListReader reader = getWordListReader(file);
			reader.getWordList(list, file);
		}

		return list;
	}
}
