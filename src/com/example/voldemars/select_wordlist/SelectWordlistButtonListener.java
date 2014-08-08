package com.example.voldemars.select_wordlist;

import android.content.Intent;
import android.view.View;

import com.example.voldemars.settings.IntentArgument;
import com.example.voldemars.translation.MainActivity;

public class SelectWordlistButtonListener implements View.OnClickListener {
	private SelectWordlistActivity activity;

	public SelectWordlistButtonListener(SelectWordlistActivity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void onClick(View arg0) {
		/* Get passed settings from old intent */
		//http://www.tutos-android.com/changement-vues-passage-donnees-android
		Intent intent = new Intent(activity, MainActivity.class);
		IntentArgument arg = new IntentArgument();

		arg.add_wordlist_files(activity.files);

		/* Launch main translation */
		intent.putExtra(IntentArgument.key, arg);
		activity.startActivity(intent);
	}
}
