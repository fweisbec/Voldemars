package com.example.test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Color;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

class IteratorTimer extends TimerTask {
	WordListIterator word_iter;
	Timer timer;
	boolean success;

	public IteratorTimer(WordListIterator w, Timer t, boolean success) {
		this.word_iter = w;
		this.timer = t;
		this.success = success;
	}
	@Override
	public void run() {
		MainActivity.me.runOnUiThread(new Runnable() {

            @Override
            public void run() {
            	if (success)
            		word_iter.iter();
            	else
            		word_iter.fail();
            }
        });
	}
}

public class WordListIterator implements OnEditorActionListener {
	int idx;
	WordList curr_word;
	ArrayList<WordList> list;
	public EditText in_translated;
	private TextView out_native;
	Timer timer;

	public WordListIterator(EditText in_translated, TextView out_native) {
		this.in_translated = in_translated;
		this.out_native = out_native;
		timer = new Timer();
	}

	public void iter() {
		if (idx == list.size()) {
			Collections.shuffle(list);
			Debug.out("idx " + Integer.valueOf(idx));
			idx = 0;
		}
		curr_word = list.get(idx++);
		out_native.setText(curr_word.french);
		in_translated.setText("");
		in_translated.setTextColor(Color.BLACK);
		in_translated.setCursorVisible(true);		
	}

	void fail() {
		in_translated.setTextColor(Color.BLACK);
	}

	public void init(ArrayList<WordList> list) {
		this.list = list;
		Collections.shuffle(list);
		Debug.out("idx " + Integer.valueOf(idx));
		iter();
	}
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		boolean success;

        if (actionId != EditorInfo.IME_ACTION_SEND)
        	return false;

       	if (v.getText().toString().equals(curr_word.translation())) {
        	v.setTextColor(Color.GREEN);
        	in_translated.setCursorVisible(false);
        	success = true;
       	} else {
       		v.setTextColor(Color.RED);
       		success = false;
       	}

       	IteratorTimer iter_timer = new IteratorTimer(this, timer, success);
    	timer.schedule(iter_timer, 1000);

        return true;
	}
}