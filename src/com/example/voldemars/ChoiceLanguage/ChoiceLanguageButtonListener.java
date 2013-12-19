package com.example.voldemars.ChoiceLanguage;

import com.example.voldemars.select_wordlist.SelectWordlistActivity;
import com.example.voldemars.settings.IntentArgument;
import com.example.voldemars.translation.MainActivity;
import com.example.voldemars.translation.Word;
import com.example.voldemars.translation.Word.Lang;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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
		Intent intent = new Intent(activity, SelectWordlistActivity.class);
		IntentArgument arg = new IntentArgument();
		
		arg.set_dest_lang(language);
		intent.putExtra(IntentArgument.key, arg);
		activity.startActivity(intent);
	}
}

