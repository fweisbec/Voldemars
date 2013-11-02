package com.example.test;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	static MainActivity me;
	WordList list;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        Settings.init();
        ask_language();
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

    private void ask_language() {
    	Button latvian, russian;
    	ChoiceLanguageButtonListener listener;	

    	setContentView(R.layout.choose_language);
    	latvian = (Button) findViewById(R.id.latvian_button);
    	russian = (Button) findViewById(R.id.russian_button);
    	
    	listener = new ChoiceLanguageButtonListener(this, latvian, russian); 
    	latvian.setOnClickListener(listener);
    	russian.setOnClickListener(listener);
    }

    public void start_translations() throws InterruptedException {
    	EditText in_translated;
    	TextView out_native;
    	WordListIterator word_iter;
    	Button give_up_button, skip_button;
    	TranslationButtonsListener buttons_listener;
    	TranslationInputListener input_listener;
    	TranslationOutput out;

    	setContentView(R.layout.activity_main);
    	/* Hack to make runOnUiThread() happy :( */
    	me = this;
    	
    	/* Word list update, parse and load */
    	WordListLoader loadlist = new WordListLoader();
    	if (!loadlist.load())
    		System.exit(-1);

    	list = loadlist.getWordList();
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

    	/* Lets go */
        word_iter.init(list);
    }
}
