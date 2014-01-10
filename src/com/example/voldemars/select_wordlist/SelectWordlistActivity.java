package com.example.voldemars.select_wordlist;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.example.voldemars.R;
import com.example.voldemars.ChoiceLanguage.ChoiceLanguageButtonListener;
import com.example.voldemars.R.layout;
import com.example.voldemars.R.menu;
import com.example.voldemars.settings.Settings;
import com.example.voldemars.translation.Debug;
import com.example.voldemars.translation.Word;
import com.example.voldemars.translation.WordList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class SelectWordlistActivity extends ListActivity {
	private ListView listview;
	
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
        listview.setAdapter(new ExplorerAdapter(this));
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
}
