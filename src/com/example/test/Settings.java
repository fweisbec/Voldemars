package com.example.test;

import android.os.Environment;

public class Settings {
	static String local_path;
	
	static void init() {
		String state = Environment.getExternalStorageState();

		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			//throw new Error(state);
			return;
		}

		local_path = Environment.getExternalStorageDirectory().getAbsolutePath();
		local_path += "/Voldemars";
	}
}
