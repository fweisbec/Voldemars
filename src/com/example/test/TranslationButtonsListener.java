package com.example.test;

import android.view.View;
import android.widget.Button;

public class TranslationButtonsListener implements View.OnClickListener {
	private WordListIterator iter;
	private TranslationOutput out;
	private Button giveup, skip;

	TranslationButtonsListener(Button giveup, Button skip, WordListIterator iter, TranslationOutput out) {
		super();
		this.giveup = giveup;
		this.skip = skip;
		this.iter = iter;
		this.out = out;
	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == giveup)
			out.overwrite_translation(iter.curr_translation());
		else if (arg0 == skip)
			iter.iter();
	}
}