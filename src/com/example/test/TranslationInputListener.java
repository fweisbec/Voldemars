package com.example.test;

import java.util.Timer;
import java.util.TimerTask;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

class IteratorTimer extends TimerTask {
	private WordListIterator word_iter;
	private boolean success;

	public IteratorTimer(WordListIterator w, boolean success) {
		this.word_iter = w;
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

public class TranslationInputListener implements OnEditorActionListener {
	private TranslationOutput out;
	private WordListIterator iter;
	Timer timer;
	
	public TranslationInputListener(WordListIterator iter, TranslationOutput out) {
		this.iter = iter;
		this.out = out;
		timer = new Timer();
	}
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		boolean success;

        if (actionId != EditorInfo.IME_ACTION_SEND)
        	return false;

       	if (v.getText().toString().equals(iter.curr_translation())) {
        	out.translation_ack();
        	success = true;
       	} else {
       		out.translation_nack();
       		success = false;
       	}

       	IteratorTimer iter_timer = new IteratorTimer(iter, success);
    	timer.schedule(iter_timer, 1000);

        return true;
	}
}
