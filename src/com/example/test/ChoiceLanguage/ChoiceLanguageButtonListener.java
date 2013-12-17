package com.example.test.ChoiceLanguage;

import com.example.test.MainActivity;
import com.example.test.Word;
import com.example.test.Word.Lang;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class ChoiceLanguageButtonListener implements View.OnClickListener {
	private Button latvian, russian;
	private ChooseLanguageActivity activity;

	public ChoiceLanguageButtonListener(ChooseLanguageActivity activity, Button latvian, Button russian) {
		super();
		this.latvian = latvian;
		this.russian = russian;
		this.activity = activity;
	}

	@Override
	public void onClick(View arg0) {
		String language = null;
		if (arg0 == this.latvian)
			language = "latvian";
		else if (arg0 == this.russian)
			language = "russian";

		//http://www.tutos-android.com/changement-vues-passage-donnees-android
		Intent intent = new Intent(activity, MainActivity.class);
		intent.putExtra("com.example.test.ChoiceLanguage.dest_language", language);
		activity.startActivity(intent);
	}
}

