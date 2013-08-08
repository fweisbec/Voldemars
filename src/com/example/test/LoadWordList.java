package com.example.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import android.os.AsyncTask;
import android.os.Environment;


class UpdateLocalTask extends Thread {
	String remote, local;
	volatile boolean completed;

	UpdateLocalTask(String url, String file) {
		super();
		this.remote = url;
		this.local = file;
	}
    public void run() {
    	/*
    	 * Base on example shown at http://stackoverflow.com/questions/3028306/download-a-file-with-android-and-showing-the-progress-in-a-progressdialog
    	 */
    	try {
    		URL url = new URL(remote);
    		URLConnection connection = url.openConnection();
            connection.connect();

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(local);

            byte data[] = new byte[1024];
            int count;
            while ((count = input.read(data)) != -1)
            	output.write(data, 0, count);

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
        	//Just don't care
        }
        synchronized (this) {
        	completed = true;
           	this.notify();
        }
    }
}


public class LoadWordList {
	static String remote = "https://raw.github.com/fweisbec/Voldemars/master/";
	static String remote_list = remote + "wordlist";
	static String remote_ver = remote + "wordlist_ver";

	static String local;
	static String local_list;
	static String local_ver;
	static String local_tmp;


	private int get_local_version() {
		File file = new File(local_ver);
		if (!file.exists())
			return -1;

		try {
			Scanner scan = new Scanner(file);
			int ver = scan.nextInt();
			return ver;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -1;
	}

	public boolean load() throws InterruptedException {
		int old_version, new_version;

		String state = Environment.getExternalStorageState();

		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			//throw new Error(state);
			return false;
		}

		local = Environment.getExternalStorageDirectory().getAbsolutePath();
		local += "/Voldemars";
		local_list = local + "/wordlist";
		local_ver = local + "/wordlist_ver";

		old_version = get_local_version();
		update_local(remote_ver, local_ver);

		new_version = get_local_version();

		if (old_version != new_version)
			update_local(remote_list, local_list);

		return true;
	}

	private void parseLine(Scanner scan, ArrayList<WordList> list) {
		WordList wl;
		String french, english, latvian, russian;
		String s;

		if (!scan.hasNext())
			return;

		s = scan.next().trim();

		if (!s.equals("n") && !s.equals("a") && !s.equals("p") && !s.equals("c") && !s.equals("r") && !s.equals("v") && !s.equals("ad"))
			return;

		if (!scan.hasNext())
			return;

		french = scan.next();

		if (!scan.hasNext())
			return;

		english = scan.next();

		if (!scan.hasNext())
			return;

		latvian = scan.next();
		if (WordList.curr_translated == WordList.Lang.LATVIAN)
			if (latvian.equals("*"))
				return;

		if (!scan.hasNext())
			return;

		russian = scan.next();
		if (WordList.curr_translated == WordList.Lang.RUSSIAN)
			if (russian.equals("*"))
				return;

		wl = new WordList(french, english, latvian, russian);
		list.add(wl);
	}

	public ArrayList<WordList> getWordList() {
		File file = new File(local_list);
		ArrayList<WordList> list = new ArrayList<WordList>();

		try {
			Scanner scan = new Scanner(file);
			String s;

			while (scan.hasNext()) {
				s = scan.nextLine();
				parseLine(new Scanner(s), list);
			}
		} catch (FileNotFoundException E){}

		return list;
	}

	private void update_local(String url, String path) throws InterruptedException {
		File file = new File(local);
		UpdateLocalTask task;
		if (!file.exists())
			file.mkdir();

		task = new UpdateLocalTask(url, path);
		task.start();

		synchronized (task) {
			while (!task.completed)
				task.wait();
		}
	}
}
