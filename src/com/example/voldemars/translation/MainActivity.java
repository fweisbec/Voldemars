package com.example.voldemars.translation;


import com.example.voldemars.R;
import com.example.voldemars.settings.IntentArgument;
import com.example.voldemars.settings.Settings;

import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	static MainActivity me;
	WordList list;
	IntentArgument argument;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!Settings.init()) {
			Debug.out("Can't load settings");
			return;
		}

		argument = IntentArgument.getActivityIntentArgument(this);
		if (argument == null) {
			Debug.out("Missing intent argument");
			return;
		}

		setContentView(R.layout.activity_main);
		try {
			start_translations();
		} catch (InterruptedException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (list != null)
			list.save_stats();
	}

	public void start_translations() throws InterruptedException {
		EditText in_translated;
		TextView out_native;
		WordListIterator word_iter;
		Button give_up_button, skip_button;
		TranslationButtonsListener buttons_listener;
		TranslationInputListener input_listener;
		TranslationOutput out;

		/* Hack to make runOnUiThread() happy :( */
		me = this;

		/* Word list update, parse and load */
		list = WordListReader.getWordListAll(argument.get_wordlist_files());
		if (list == null || list.size() == 0)
			System.exit(-1);

		list.load_rates();
		list = list.sort();
		//Debug.out(list);

		/* Translated input text */
		in_translated = (EditText) findViewById(R.id.in_latvian);
		in_translated.setGravity(Gravity.CENTER);
		in_translated.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

		/* Native word display */
		out_native = (TextView) findViewById(R.id.out_native);

		/* Give up button */
		give_up_button = (Button) findViewById(R.id.give_up_button);

		/* Skip button */
		skip_button = (Button) findViewById(R.id.skip_button);

		/* Word iterator and GUI */
		out = new TranslationOutput(in_translated, out_native);
		word_iter = new WordListIterator(out);

		/* Give up and skip buttons listener */
		buttons_listener = new TranslationButtonsListener(give_up_button, skip_button, word_iter, out);
		give_up_button.setOnClickListener(buttons_listener);
		skip_button.setOnClickListener(buttons_listener);

		/* User input listener */
		input_listener = new TranslationInputListener(word_iter);
		in_translated.setOnEditorActionListener(input_listener);
		
		/* filter */
/*		WordFilter filter = new WordFilter(list);
		in_translated.setFilters(new WordFilter[] {filter} );*/

		/* Lets go */
		word_iter.init(list);
	}
}
