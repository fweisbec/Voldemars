package com.example.voldemars.select_wordlist;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;

import com.example.voldemars.R;
import com.example.voldemars.settings.Settings;
import com.example.voldemars.translation.Debug;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class SelectWordlistActivity extends ListActivity {
	private ListView listview;
	private ExplorerAdapter adapter;
	public HashSet<String> files = new HashSet<String>();

	private void load_wordlist() {
		WordListLoader loader;

		loader = new WordListLoader();
		try {
			loader.load();
		}
		catch (Exception E) {
			Debug.out(E.getStackTrace());
		}
	}

	private void setPathAdapter(ExplorerAdapter adapter) throws URISyntaxException, IOException {
		this.adapter = adapter;
		listview.setAdapter(this.adapter);

		/* Recheck selected */
		for (int i = 0; i < listview.getCount(); i++) {
			WordListFile item = adapter.getItem(i);
			if (!item.isDirectory()) {
				if (files.contains(item.getWordListPath())) {
					// We need to wait for adapter.getView() before setting background
					listview.setItemChecked(i, true);
				}
			}
		}
	}

	public void update_dir_list() {
		ListView listview = getListView();
		for (int i = 0; i < listview.getCount(); i++) {
			WordListFile item = adapter.getItem(i);
			if (!item.isDirectory()) {
				String path;
				try {
					path = item.getWordListPath();
				} catch (IOException e) {
					// TODO Bloc catch généré automatiquement
					e.printStackTrace();
					return;
				}
				if (listview.isItemChecked(i))
					files.add(path);
				else
					files.remove(path);
				Debug.out(files);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_wordlist);

		Settings.init();
		load_wordlist();

		Button button = (Button) findViewById(R.id.wordlist_button);
		SelectWordlistButtonListener listener = new SelectWordlistButtonListener(this);
		button.setOnClickListener(listener);

		listview = getListView();
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		try {
			setPathAdapter(new ExplorerAdapter(this));
		} catch (Exception e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_wordlist, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean cwd(String path) {
		if (!this.adapter.pathAllowed(path))
			return false;

		ExplorerAdapter adapter;
		try {
			adapter = new ExplorerAdapter(this.adapter, path);
			setPathAdapter(adapter);
		} catch (Exception e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
			return false;
		}

		return true;
	}

	protected void onListItemClick (ListView l, View rowView, int position, long id) {
		if (getListView().isItemChecked(position))
			rowView.setBackgroundResource(android.R.color.holo_blue_dark);
		else
			rowView.setBackgroundResource(android.R.color.white);

		WordListFile item = adapter.getItem(position);
		if (!item.isDirectory()) {
			update_dir_list();
		} else {
			cwd(item.getName());
		}
	}

	public void onBackPressed () {
		if (!cwd("../"))
			super.onBackPressed();
	}
}
