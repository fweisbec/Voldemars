package com.example.test;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;

public class TranslationOutput {
	public EditText in_translated;
	private TextView out_native;

	public TranslationOutput(EditText in_translated, TextView out_native) {
		this.in_translated = in_translated;
		this.out_native = out_native;
	}

	public void translation_ack() {
		in_translated.setTextColor(Color.GREEN);
    	in_translated.setCursorVisible(false);
	}
	
	public void translation_nack() {
		in_translated.setTextColor(Color.RED);
		in_translated.setCursorVisible(false);
	}
	
//	@SuppressLint("DefaultLocale")
	public void show_mark(int nr_asked, int nr_success) {
		String mark = String.format("Total: %d/%d", nr_asked, nr_success);
		out_native.setTextColor(Color.MAGENTA);
		out_native.setText(mark);
	}
	
	public void overwrite_translation(String s) {
		in_translated.setText(s);
	}
	
	public void reset_translation() {
		out_native.setTextColor(Color.BLACK);
		in_translated.setTextColor(Color.BLACK);
		in_translated.setCursorVisible(true);
	}
	
	public void set_next(String s) {
		out_native.setText(s);
		in_translated.setText("");
		reset_translation();		
	}
}