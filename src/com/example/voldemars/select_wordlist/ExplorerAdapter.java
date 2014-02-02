package com.example.voldemars.select_wordlist;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import com.example.voldemars.settings.Settings;
import com.example.voldemars.translation.Debug;
import com.example.voldemars.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExplorerAdapter extends ArrayAdapter<WordListFile>{
	private ArrayList<View> rows;
	private SelectWordlistActivity activity;
	private File path; // relative to wordlist dir
	
	private void get_wordlist_array() throws Exception {
		File[] files = path.listFiles();
		
		if (files == null)
			return;

		for (File file : files)
			this.add(new WordListFile(file));
	}
	
	public ExplorerAdapter(SelectWordlistActivity activity, File path) throws Exception {
		super(activity, R.layout.select_wordlist_files);
		this.activity = activity;
		this.path = path;
		rows = new ArrayList<View>();
		get_wordlist_array();
	}
	
	public ExplorerAdapter(SelectWordlistActivity activity) throws Exception {
		this(activity, new File(Settings.wordlist_path));
	}
	
	public ExplorerAdapter(ExplorerAdapter parent, String path) throws Exception {
		this(parent.activity, new File(parent.path, path));
	}
	
	public boolean pathAllowed(String rel) {
		File root = new File(Settings.wordlist_path);
		File test = new File(this.path, rel);

		try {
			if (test.getCanonicalPath().startsWith(root.getCanonicalPath()))	
				return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/* http://www.learn2crack.com/2013/10/android-custom-listview-images-text-example.html */
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View row = inflater.inflate(R.layout.select_wordlist_files, null, true);
		rows.add(position, row);
		TextView path = (TextView) row.findViewById(R.id.wordlist_row_path);
		ImageView icon = (ImageView) row.findViewById(R.id.wordlist_row_img);
		
		WordListFile file = this.getItem(position);
		path.setText(file.getName());
		if (file.isDirectory()) {
			icon.setImageResource(android.R.drawable. ic_input_add);
		} else {
			if (activity.getListView().isItemChecked(position))
				row.setBackgroundResource(android.R.color.holo_blue_dark);
		}
		
		return row;
	}
}
