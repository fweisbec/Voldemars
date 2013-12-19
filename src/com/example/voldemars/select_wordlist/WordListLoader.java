package com.example.voldemars.select_wordlist;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.example.voldemars.settings.Settings;


abstract class AbstractHttpToFileAsync extends Thread {
	protected String remote;
	protected volatile boolean completed;

	AbstractHttpToFileAsync(String url) {
		super();
		this.remote = url;
	}

	protected abstract void record_file(InputStream input) throws Exception;

	public void run() {
    	/*
    	 * Base on example shown at http://stackoverflow.com/questions/3028306/download-a-file-with-android-and-showing-the-progress-in-a-progressdialog
    	 */
    	try {
    		URL url = new URL(remote);
    		URLConnection connection = url.openConnection();
            connection.connect();

            BufferedInputStream binput = new BufferedInputStream(url.openStream());
            record_file(binput);
            binput.close();
        } catch (Exception e) {
        	//Just don't care
        }

        synchronized (this) {
        	completed = true;
           	this.notify();
        }
    }
}

class HttpToFileAsync extends AbstractHttpToFileAsync {
	private String remote, local;

	HttpToFileAsync(String url, String path) {
		super(url);
		this.local = path;
	}

	protected void record_file(InputStream input) throws Exception
	{
		OutputStream output = new FileOutputStream(local);

		byte data[] = new byte[1024];
		int count;

		while ((count = input.read(data)) != -1)
			output.write(data, 0, count);

		output.flush();
		output.close();
	}
}

class ZipHttpToFileAsync extends AbstractHttpToFileAsync  {
	ZipHttpToFileAsync(String url) {
		super(url);
	}

	private void write_zentry(ZipInputStream zis, ZipEntry ze) throws Exception
	{
		String filename = Settings.wordlist_path + "/" + ze.getName();
		OutputStream output = new FileOutputStream(filename);

		byte data[] = new byte[1024];
		int count;

		while ((count = zis.read(data)) != -1)
			output.write(data, 0, count);

		output.flush();
		output.close();
	}

	protected void record_file(InputStream input) throws Exception
	{
		ZipInputStream zinput = new ZipInputStream(input);
		ZipEntry ze;

		while ((ze = zinput.getNextEntry()) != null)
			write_zentry(zinput, ze);

		zinput.close();
	}
}

public class WordListLoader {
	static String remote = "https://raw.github.com/fweisbec/Voldemars/master/";
	static String remote_list = remote + "wordlist.zip";
	static String remote_ver = remote + "wordlist_ver";

	private String local_ver;


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
		
		local_ver = local_path + "/wordlist_ver";

		old_version = get_local_version();
		update_local_ver(remote_ver, local_ver);

		new_version = get_local_version();

		if (old_version != new_version)
			update_local_wordlist(remote_list);

		return true;
	}

	private void update_local(AbstractHttpToFileAsync task) throws InterruptedException {
		task.start();

		synchronized (task) {
			while (!task.completed)
				task.wait();
		}
	}

	private void update_local_ver(String url, String path) throws InterruptedException {
		HttpToFileAsync task;

		task = new HttpToFileAsync(url, path);
		update_local(task);
	}

	private void update_local_wordlist(String url) throws InterruptedException {
		ZipHttpToFileAsync task;

		task = new ZipHttpToFileAsync(url);
		update_local(task);
	}
}
