package com.example.test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

class IteratorTimer extends TimerTask {
	private WordListIterator word_iter;
	private boolean success;
	private boolean account;

	public IteratorTimer(WordListIterator w, boolean success, boolean account) {
		this.word_iter = w;
		this.success = success;
		this.account = account;
	}
	@Override
	public void run() {
		MainActivity.me.runOnUiThread(new Runnable() {

            @Override
            public void run() {
            	if (success) {
            		if (account)
            			word_iter.iter();
            		else
            			word_iter.iter_noaccount();
            	}
            	else
            		word_iter.fail();
            }
        });
	}
}

public class WordListIterator {
	private WordList list;
	private TranslationOutput out;
	private int nr_asked, nr_success;
	private Timer timer;
	private boolean curr_failed;

	public WordListIterator(TranslationOutput out) {
		this.out = out;
		timer = new Timer();
	}
	
	public void init(WordList list) {
		this.list = list;
		out.set_next(list.curr().french);
		curr_failed = false;
	}
	
	public void skip() {
		iter();
		/*
		if (list.is_last()) {
			iter_noaccount_last();
		} else {
			iter_noaccount();
		}*/
	}
	
	private void iter_noaccount_last() {
		last();
		iter_async(true, false);
	}
	
	protected void iter() {
		nr_asked++;
		if (!curr_failed) {
			nr_success++;
			list.curr().stats.add_good_answer();
		} else {
			list.curr().stats.add_bad_answer();
		}
		if (list.is_last())
			iter_noaccount_last();
		else
			iter_noaccount();
	}
	
	protected void fail() {
		curr_failed = true;
		out.reset_translation();
	}
	
	public void giveup() {
		curr_failed = true;
		out.overwrite_translation(curr_translation());
	}
	
	void input(String s) {		
       	if (s.equals(curr_translation())) {
        	out.translation_ack();
        	iter_async(true, true);
       	} else {
       		out.translation_nack();
       		iter_async(false, false);
       	}
	}
	
	protected void iter_noaccount() {
		if (list.is_last()) {
			nr_asked = 0;
			nr_success = 0;
		}
		out.set_next(list.next().french);
		curr_failed = false;
	}
	
	private void last() {
		out.show_mark(nr_success, nr_asked);
	}
	
	private void iter_async(boolean success, boolean account) {
		IteratorTimer iter_timer = new IteratorTimer(this, success, account);
    	timer.schedule(iter_timer, 1000);
	}
	
	public String curr_translation() {
		return list.curr().translation();
	}
}