package com.example.test;

import android.view.View;
import android.widget.Button;

public class ChoiceLanguageButtonListener implements View.OnClickListener {
	private Button latvian, russian;
	private MainActivity main;

	ChoiceLanguageButtonListener(MainActivity main, Button latvian, Button russian) {
		super();
		this.main = main;
		this.latvian = latvian;
		this.russian = russian;
	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == this.latvian)
			Word.curr_translated = Word.Lang.LATVIAN;
		else if (arg0 == this.russian)
			Word.curr_translated = Word.Lang.RUSSIAN;

		try {
			main.start_translations();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
	}
}

