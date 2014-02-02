package com.example.voldemars.select_wordlist;

import java.io.File;
import java.io.IOException;

import com.example.voldemars.settings.Settings;

public class WordListFile extends File {
	private static final long serialVersionUID = -7877173087106697462L;
	
	public WordListFile(File file) throws IOException {
		super(file.getCanonicalPath());
	}
	
	/*public WordListFile(String path) {
		super(Settings.wordlist_path, path);
	}*/
	
	public String getWordListPath() throws IOException {
		String abs = this.getCanonicalPath();
		return abs.replaceFirst(Settings.wordlist_path, "");
	}

}
