package com.example.voldemars.select_wordlist;

import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.voldemars.ChoiceLanguage.ChooseLanguageActivity;
import com.example.voldemars.settings.IntentArgument;
import com.example.voldemars.settings.Settings;
import com.example.voldemars.translation.Debug;
import com.example.voldemars.translation.MainActivity;

public class SelectWordlistButtonListener implements View.OnClickListener {
		private SelectWordlistActivity activity;

		public SelectWordlistButtonListener(SelectWordlistActivity activity) {
			super();
			this.activity = activity;
		}

		@Override
		public void onClick(View arg0) {
			ListView listview = activity.getListView();
			
			/* Get passed settings from old intent */
			IntentArgument arg = IntentArgument.getActivityIntentArgument(activity);
			if (arg == null) {
				Debug.out("Intent argument null");
				return;
			}
			
			arg.reset_wordlist_filenames();
			SparseBooleanArray sba = listview.getCheckedItemPositions();
			
			for (int i = 0; i < sba.size(); i++) {
				if (sba.valueAt(i)) {
					String filename = (String)listview.getItemAtPosition(sba.keyAt(i));
					arg.add_wordlist_filename(filename);
					//Debug.out((String)listview.getItemAtPosition(sba.keyAt(i)));
				}
			}

			/* Launch main translation */
			Intent intent = new Intent(activity, MainActivity.class);
			intent.putExtra(IntentArgument.key, arg);
			activity.startActivity(intent);
		}
}
