package com.example.voldemars.settings;

import java.util.HashMap;

import android.os.Environment;

public class Settings {
	public static String local_path;
	public static String wordlist_path;
	
	public static boolean init() {
		String state = Environment.getExternalStorageState();

		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			//throw new Error(state);
			return false;
		}

		local_path = Environment.getExternalStorageDirectory().getAbsolutePath();
		local_path += "/Voldemars";
		wordlist_path = local_path + "/wordlists/";
		
		return true;
	}
}
