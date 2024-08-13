package net.wifizer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileReaderHelper {
	private static final String PREFS_NAME = "FileReaderHelperPrefs";
	private static final String DATA_KEY = "data";
	private SharedPreferences sharedPreferences;
	private List<String> data;
	// Constructor
	public FileReaderHelper(Context context) {
		sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		data = new ArrayList<>();
	}
	
	// Add data from file: read file, reset previous data, and store new data
	public void addDataFromFile(ContentResolver contentResolver, Uri uri) {
		resetData();  // Reset previous data
		
		try {
			InputStream inputStream = contentResolver.openInputStream(uri);
			if (inputStream != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				String line;
				
				while ((line = reader.readLine()) != null) {
					data.add(line);  // Add each line to the list
				}
				
				reader.close();
				
				// Save data to SharedPreferences
				saveDataToPreferences();
			}
			} catch (IOException e) {
			Log.e("FileReaderHelper", "Error reading file", e);
		}
	}
	
	// Fetch the saved data from SharedPreferences
	public String[] fetchData() {
		loadDataFromPreferences();  // Load data from SharedPreferences
		return data.toArray(new String[0]);  // Return data as an array
	}
	
	// Reset the data array
	private void resetData() {
		data.clear();  // Clears the current data
	}
	
	// Save data to SharedPreferences
	private void saveDataToPreferences() {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putStringSet(DATA_KEY, new HashSet<>(data)); // Convert List to Set
		editor.apply();
	}
	
	// Load data from SharedPreferences
	private void loadDataFromPreferences() {
		Set<String> savedData = sharedPreferences.getStringSet(DATA_KEY, new HashSet<>());
		data = new ArrayList<>(savedData); // Convert Set back to List
	}
}