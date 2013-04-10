package de.yadrone.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class FlightPlanFtpReader {

	public String getFlightPlan(String ftpAddress) {
		// e.g. "ftp://mirror.csclub.uwaterloo.ca/index.html"
		String json = null;
		URL url;
		BufferedReader in = null;
		try {
			url = new URL(ftpAddress);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuilder jsonBuffer = new StringBuilder();
			String inputLine;
	        while ((inputLine = in.readLine()) != null)
	        {
	        	jsonBuffer.append(inputLine);
	        }
	        json = jsonBuffer.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null)
			{
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return json;
	}
}
