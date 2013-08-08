package com.example.test;

import android.view.View;
import android.widget.Button;

public class TranslationButtonsListener implements View.OnClickListener {
	private WordListIterator iter;
	private Button giveup, skip;

	TranslationButtonsListener(Button giveup, Button skip, WordListIterator iter) {
		super();
		this.giveup = giveup;
		this.skip = skip;
		this.iter = iter;
	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == giveup)
			iter.in_translated.setText(iter.curr_word.translation());
		else if (arg0 == skip)
			iter.iter();
	}
}
