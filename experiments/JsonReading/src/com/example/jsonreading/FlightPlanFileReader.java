package com.example.jsonreading;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.shigeodayo.ardrone.command.DroneCommand;

import android.os.Environment;
import android.util.Log;

public class FlightPlanFileReader implements IFlightPlanReader {

	private JsonFlightPlanParser jsonParser;

	public FlightPlanFileReader() {
		jsonParser = new JsonFlightPlanParser();
	}

	@Override
	public List<DroneCommand> getFlightPlan(String filename) {
		String jsonFlightPlan = getJsonFlightPlan(filename);
		return jsonParser.getFlightPlan(jsonFlightPlan);
	}

	private String getJsonFlightPlan(String filename) {
		String jsonFlightPlan = "";
		if (isExternalStorageReadable()) {
			File jsonFile = getAlbumStorageDir(filename);
			if (jsonFile.canRead() && jsonFile.exists()) {
				jsonFlightPlan = readFile(jsonFile);
			}
		}
		return jsonFlightPlan;
	}

	private String readFile(File jsonFile) {
		String json = "";
		FileInputStream fis = null;
		try {
			int fileLength = (int) jsonFile.length();
			fis = new FileInputStream(jsonFile);
			byte[] buffer = new byte[fileLength + 10];
			fis.read(buffer, 0, buffer.length);
			StringBuilder stringBuffer = new StringBuilder(buffer.length);
			for (int i = 0; i < fileLength; i++) {
				stringBuffer.append((char) buffer[i]);
			}
			json = stringBuffer.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	/* Checks if external storage is available to at least read */
	private boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	private File getAlbumStorageDir(String jsonFileName) {
		// Get the directory for the app's private pictures directory.
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
				jsonFileName);
		if (!file.mkdirs()) {
			Log.d("FlightPlanFileReader", "Directory not created");
		}
		return file;
	}

}
