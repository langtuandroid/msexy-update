package com.hdc.ultilities;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

public class FileManager {

	// file exist
	public static boolean fileIsExits(Activity instance, String filename) {
		ContextWrapper cw = new ContextWrapper(instance.getApplicationContext());
		File directory = cw.getDir("myuser", Context.MODE_PRIVATE);
		File mypath = new File(directory, filename);
		if (!mypath.exists())
			return false;

		return true;
	}

	// save userID
	public static void saveUserID(Activity instance, String user, String mfile) {
		FileOutputStream fos = null;
		DataOutputStream dos;
		try {
			ContextWrapper cw = new ContextWrapper(
					instance.getApplicationContext());
			File directory = cw.getDir("myuser", Context.MODE_PRIVATE);
			File file = new File(directory, mfile);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			fos = new FileOutputStream(file);
			dos = new DataOutputStream(fos);
			dos.writeUTF(user);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// load userID
	public static String loadUserAndPass(Activity instance, String name) {
		FileInputStream fis = null;
		DataInputStream dis;
		String userID = "";
		try {
			ContextWrapper cw = new ContextWrapper(
					instance.getApplicationContext());
			File directory = cw.getDir("myuser", Context.MODE_PRIVATE);
			File file = new File(directory, name);
			if (file.exists()) {
				fis = new FileInputStream(file);
				dis = new DataInputStream(fis);
				userID = dis.readUTF();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException f1) {
			f1.printStackTrace();
		}

		return userID;
	}

	// load file text in external storage
	public static ArrayList<String> loadfileExternalStorage(Activity instance,
			int file) {
		ArrayList<String> aa = new ArrayList<String>();
		String str = "";
		InputStream is = instance.getResources().openRawResource(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		try {
			if (is != null) {
				while ((str = reader.readLine()) != null) {
					aa.add(str.trim().toString());
				}
				is.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aa;
	}

}
