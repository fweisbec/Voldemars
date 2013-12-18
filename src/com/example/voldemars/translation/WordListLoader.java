package com.example.voldemars.translation;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

import android.os.Environment;


class HttpToFileAsync extends Thread {
	private String remote, local;
	volatile boolean completed;

	HttpToFileAsync(String url, String file) {
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


public class WordListLoader {
	static String remote = "https://raw.github.com/fweisbec/Voldemars/master/";
	static String remote_list = remote + "wordlist";
	static String remote_ver = remote + "wordlist_ver";

	private String local_list, local_ver;


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
	
	private void mkdir_local(String filename)
	{		
		File file = new File(filename);
		if (!file.exists())
			file.mkdir();
	}

	public boolean load() throws InterruptedException {
		int old_version, new_version;
		String local_path, wordlist_path;
		
		local_path = Settings.local_path;
		wordlist_path = Settings.wordlist_path;

		if (local_path == null || wordlist_path == null)
			return false;
		
		mkdir_local(local_path);
		mkdir_local(wordlist_path);
		
		local_list = wordlist_path + "/wordlist";
		local_ver = local_path + "/wordlist_ver";

		old_version = get_local_version();
		update_local(remote_ver, local_ver);

		new_version = get_local_version();

		if (old_version != new_version)
			update_local(remote_list, local_list);

		return true;
	}

	private boolean parseLine(String line, ArrayList<Word> list) {
		Word wl;
		String type, french, english, latvian, russian;
		String[] valid_types = {"n", "a", "p", "c", "r", "v", "ad"};
		String[] values;

		values = line.split(";");
		/* Malformed line */
		if (values.length != 5)
			return false;

		type = values[0].trim();
		french = values[1].trim();
		english = values[2].trim();
		latvian = values[3].trim();
		russian = values[4].trim();

		boolean type_is_valid = false;
		
		for (String valid_type : valid_types) {
			if (valid_type.equals(type)) {
				type_is_valid = true;
				break;
			}
		}
		
		if (!type_is_valid)
			return false;
		
		if (Word.curr_translated == Word.Lang.LATVIAN)
			if (latvian.equals("*"))
				return true;
		
		if (Word.curr_translated == Word.Lang.RUSSIAN)
			if (russian.equals("*"))
				return true;

		wl = new Word(french, english, latvian, russian);
		list.add(wl);
		
		return true;
	}

	/*
	 * Inspired by http://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
	 */
	public WordList getWordList() {
		WordList list = new WordList();
		
		try {
			FileReader fr = new FileReader(local_list);
			BufferedReader br = new BufferedReader(fr);
			String line;

			while ((line = br.readLine()) != null) {
				parseLine(line, list);
			}
			fr.close();
		} catch (FileNotFoundException E){}
		  catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	private void update_local(String url, String path) throws InterruptedException {
		HttpToFileAsync task;

		task = new HttpToFileAsync(url, path);
		task.start();

		synchronized (task) {
			while (!task.completed)
				task.wait();
		}
	}
}
