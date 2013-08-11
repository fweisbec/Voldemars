package com.example.test;

import java.util.Timer;
import java.util.TimerTask;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class TranslationInputListener implements OnEditorActionListener {
	private WordListIterator iter;
	
	public TranslationInputListener(WordListIterator iter) {
		this.iter = iter;
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId != EditorInfo.IME_ACTION_SEND)
        	return false;

        iter.input(v.getText().toString());

        return true;
	}
}
