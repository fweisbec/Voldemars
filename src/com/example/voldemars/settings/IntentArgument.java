package com.example.voldemars.settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.example.voldemars.translation.Debug;

import android.app.Activity;
import android.content.Intent;

public class IntentArgument implements Serializable{
	public static String key = "com.example.voldemars.settings";
	private HashSet<String> wordlists;

	public IntentArgument() {
		wordlists = new HashSet<String>();
	}
	public static IntentArgument getActivityIntentArgument(Activity activity) {
		Intent intent = activity.getIntent();

		if (intent == null) {
			Debug.out("Intent null");
			return null;
		}

		return (IntentArgument)intent.getSerializableExtra(key);
	}

	public void add_wordlist_files(HashSet<String> wordlists) {
		this.wordlists = wordlists;
	}

	public Set<String> get_wordlist_files() {
		return Collections.unmodifiableSet(wordlists);
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append(" Wordlist files: ");
		for (String wordlist : wordlists)
			buf.append(String.format("%s ", wordlist));

		return buf.toString();
	}
}
