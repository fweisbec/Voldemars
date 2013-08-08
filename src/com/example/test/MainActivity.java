package com.example.test;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	static MainActivity me;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ask_language();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void ask_language() {
    	Button latvian, russian;
    	ChoiceLanguageButtonListener listener;	
    	
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
    	TranslationButtonsListener listener;

    	/* Hack to make runOnUiThread() happy :( */
    	me = this;

    	/* Word list update, parse and load */
    	LoadWordList loadlist = new LoadWordList();
    	if (!loadlist.load())
    		System.exit(-1);

    	ArrayList<WordList> list = loadlist.getWordList();
    	if (list == null || list.size() == 0)
    		System.exit(-1);

    	/* Translated input text */
    	in_translated = (EditText) findViewById(R.id.in_latvian);
    	in_translated.setVisibility(View.VISIBLE);
    	in_translated.setGravity(Gravity.CENTER);
    	in_translated.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

    	/* Native word display */
    	out_native = (TextView) findViewById(R.id.out_native);
    	out_native.setVisibility(View.VISIBLE);

    	/* Give up button */
    	give_up_button = (Button) findViewById(R.id.give_up_button);
    	give_up_button.setVisibility(View.VISIBLE);

    	/* Skip button */
    	skip_button = (Button) findViewById(R.id.skip_button);
    	skip_button.setVisibility(View.VISIBLE);

    	/* Give up and skip buttons listener */
    	word_iter = new WordListIterator(in_translated, out_native);
    	listener = new TranslationButtonsListener(give_up_button, skip_button, word_iter);

    	in_translated.setOnEditorActionListener(word_iter);
        word_iter.init(list);
        give_up_button.setOnClickListener(listener);
        skip_button.setOnClickListener(listener);
    }
}
