/*package com.example.voldemars.translation;

import android.text.InputFilter;
import android.text.Spanned;

public class WordFilter implements InputFilter {
	private WordList wordlist;
	
	public WordFilter(WordList wordlist) {
		this.wordlist = wordlist;
	}
	@Override
	public CharSequence filter(CharSequence source, int start, int end,
								Spanned dest, int dstart, int dend) {
		Word w = wordlist.curr();
		if (!w.type.equals("he"))
			return null;
		
		StringBuffer dst = new StringBuffer(dest);
		if (dstart <= dst.length() - 1)
			dst.delete(dstart, dend);
		
		String src = source.toString().substring(start, end);
		
		dst.insert(dstart, src);
		if (!dst.toString().startsWith(w.french))
			return "";
		
		Debug.out("source: ");
		Debug.out(source);
		//Debug.out(" ");
		Debug.out(start);
		//Debug.out(" ");
		Debug.out(end);
		//Debug.out("\n");
		Debug.out("dest: ");
		Debug.out(dest);
		//Debug.out(" ");
		Debug.out(dstart);
		//Debug.out(" ");
		Debug.out(dend);
		//Debug.out("\n");
		Debug.out("new dest: ");
		Debug.out(dst);
		
		return null;
	}

}
*/