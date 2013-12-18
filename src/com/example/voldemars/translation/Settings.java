package com.example.voldemars.translation;

import android.os.Environment;

public class Settings {
	static String local_path;
	static String wordlist_path;
	
	static void init() {
		String state = Environment.getExternalStorageState();

		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			//throw new Error(state);
			return;
		}

		local_path = Environment.getExternalStorageDirectory().getAbsolutePath();
		local_path += "/Voldemars";
		wordlist_path = local_path + "/wordlists";
	}
}
