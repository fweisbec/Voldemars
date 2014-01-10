package com.example.voldemars.select_wordlist;

import java.io.File;

import com.example.voldemars.settings.Settings;

import android.widget.ArrayAdapter;

public class ExplorerAdapter extends ArrayAdapter<String> {
	
	private void get_wordlist_array() {
		File dir = new File(Settings.wordlist_path);
		File[] files = dir.listFiles();
		
		if (files == null)
			return;

		for (File file : files)
			this.add(file.getName());
	}
	
	public ExplorerAdapter(SelectWordlistActivity activity) {
		super(activity, android.R.layout.simple_list_item_multiple_choice);
		get_wordlist_array();
	}

}
