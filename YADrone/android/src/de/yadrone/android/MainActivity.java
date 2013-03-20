package de.yadrone.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.shigeodayo.ardrone.ARDrone;

public class MainActivity extends BaseActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		final TextView text = (TextView) findViewById(R.id.text_init);
		text.append("\nConnected to " + wifi.getConnectionInfo().getSSID());

		initialize();

	}

	private void initialize() {
		final TextView text = (TextView) findViewById(R.id.text_init);

		YADroneApplication app = (YADroneApplication) getApplication();
		ARDrone drone = app.getARDrone();

		try {
			text.append("\n\nConnect Drone Controller\n");
			drone.connect();
			text.append("Connect Drone Navdata Socket\n");
			drone.connectNav();
//			text.append("Connect Drone Video Socket\n");
//			ardrone.connectVideo();
			text.append("Start Drone\n");
			drone.start();

			text.append(configureDrone(drone));
		} catch (Exception exc) {
			exc.printStackTrace();

			if (drone != null)
				drone.disconnect();
		}
	}

	private String configureDrone(ARDrone drone) {
		StringBuilder text = new StringBuilder();
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		String ssid = sharedPrefs.getString("pref_ssid", "foo");
		text.append(String.format("Set SSID to %1$s\n", ssid));
		// drone.setSsid(ssid);

		double maxAltitude = Double.parseDouble(sharedPrefs.getString(
				"pref_altitude", "3"));
		text.append(String.format("Set max. Altitude to %1$f m\n", maxAltitude));
		drone.setMaxAltitude(5);

		double maxVerticalSpeed = Double.parseDouble(sharedPrefs.getString(
				"pref_vertical_speed", "1"));
		text.append(String.format("Set max. verticalspeed to %1$f m/s\n",
				maxVerticalSpeed));
		// drone.setMaxVerticalSpeed(maxVerticalSpeed);

		double maxYaw = Double.parseDouble(sharedPrefs.getString(
				"pref_max_yaw", "1"));
		text.append(String.format("Set max. yaw to %1$f degrees\n", maxYaw));
		// drone.setMaxYaw(maxYaw);

		double maxTilt = Double.parseDouble(sharedPrefs.getString(
				"pref_max_tilt", "1"));
		text.append(String.format("Set max. tilt to %1$f degrees\n", maxTilt));
		// drone.setMaxTilt(maxTilt);

		String hullType = sharedPrefs.getString("pref_hull_type", "Indoor");
		text.append(String.format("Set hull type to %1$s\n", hullType));
		// drone.setHullType(hullType);

		String flightLocation = sharedPrefs.getString("pref_flight_location",
				"Ïndoor");
		text.append(String.format("Set flight location to %1$s\n",
				flightLocation));
		// drone.setFlightLocation(flightLocation);

		return text.toString();
	}

	/**
	 * Upon pressing the BACK-button, the user has to confirm the connection to
	 * the drone is taken down.
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			new AlertDialog.Builder(this)
					.setMessage("Upon exiting, drone will be disconnected !")
					.setTitle("Exit YADrone ?")
					.setCancelable(false)
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									YADroneApplication app = (YADroneApplication) getApplication();
									ARDrone drone = app.getARDrone();
									drone.disconnect();

									finish();
								}
							})
					.setNeutralButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// User selected Cancel, nothing to do here.
								}
							}).show();

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}

}
