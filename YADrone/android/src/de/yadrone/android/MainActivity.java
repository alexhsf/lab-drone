package de.yadrone.android;

import java.util.ArrayList;

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
import com.shigeodayo.ardrone.command.CommandManager;
import com.shigeodayo.ardrone.command.FlyingMode;
import com.shigeodayo.ardrone.configuration.ConfigurationManager;
import com.shigeodayo.ardrone.navdata.ControlState;
import com.shigeodayo.ardrone.navdata.DroneState;
import com.shigeodayo.ardrone.navdata.NavDataManager;
import com.shigeodayo.ardrone.navdata.StateListener;
import com.shigeodayo.ardrone.navdata.VisionData;
import com.shigeodayo.ardrone.navdata.VisionListener;
import com.shigeodayo.ardrone.navdata.VisionPerformance;
import com.shigeodayo.ardrone.navdata.VisionTag;

public class MainActivity extends BaseActivity implements StateListener, VisionListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final TextView text = (TextView) findViewById(R.id.text_init);

		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		text.append("\nConnected to " + wifi.getConnectionInfo().getSSID());

		YADroneApplication app = (YADroneApplication) getApplication();
		final ARDrone drone = app.getARDrone();

		try {
			text.append("\n\nConnect Drone Controller\n");
			drone.connect();
			text.append("Connect Drone Navdata Socket\n");
			drone.connectNav();
			// text.append("Connect Drone Video Socket\n");
			// ardrone.connectVideo();
			text.append("Start Drone\n");
			drone.start();

			CommandManager cmd = drone.getCommandManager();

			cmd.setAutonomousFlight(false);
			
			cmd.setVerticalDetectionType((1 << 9) - 1);

			cmd.setHorizonalDetectionType((1 << 9) - 1);

			//cmd.setFlyingMode(FlyingMode.HOVER_ON_TOP_OF_ORIENTED_ROUNDEL);
			cmd.setFlyingMode(FlyingMode.FREE_FLIGHT);
			cmd.setHoveringRange(500);
			
			NavDataManager nav = drone.getNavDataManager();
			nav.setStateListener(this);
			nav.setVisionListener(this);

			text.append(configureDrone(drone));

			// not allowed to do networking on the main thread
			new Thread() {
				public void run() {
					ConfigurationManager cfg = drone.getConfigurationManager();
					final StringBuilder builder = new StringBuilder();
					builder.append(cfg.getConfiguration());
					builder.append(cfg.getPreviousRunLogs());
					builder.append(cfg.getCustomCofigurationIds());
					final String configs = builder.toString();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							text.append(configs);
						}
					});
				}
			}.start();

		} catch (Exception exc) {
			exc.printStackTrace();

			if (drone != null)
				drone.disconnect();
		}

	}

	private String configureDrone(ARDrone drone) {
		StringBuilder text = new StringBuilder();
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		String ssid = sharedPrefs.getString("pref_ssid", "foo");
		text.append(String.format("Set SSID to %1$s\n", ssid));
		// drone.setSsid(ssid);

		double maxAltitude = Double.parseDouble(sharedPrefs.getString("pref_altitude", "3"));
		text.append(String.format("Set max. Altitude to %1$f m\n", maxAltitude));
		drone.setMaxAltitude(2200);

		double maxVerticalSpeed = Double.parseDouble(sharedPrefs.getString("pref_vertical_speed", "1"));
		text.append(String.format("Set max. verticalspeed to %1$f m/s\n", maxVerticalSpeed));
		// drone.setMaxVerticalSpeed(maxVerticalSpeed);

		double maxYaw = Double.parseDouble(sharedPrefs.getString("pref_max_yaw", "1"));
		text.append(String.format("Set max. yaw to %1$f degrees\n", maxYaw));
		// drone.setMaxYaw(maxYaw);

		double maxTilt = Double.parseDouble(sharedPrefs.getString("pref_max_tilt", "1"));
		text.append(String.format("Set max. tilt to %1$f degrees\n", maxTilt));
		// drone.setMaxTilt(maxTilt);

		String hullType = sharedPrefs.getString("pref_hull_type", "Indoor");
		text.append(String.format("Set hull type to %1$s\n", hullType));
		// drone.setHullType(hullType);

		String flightLocation = sharedPrefs.getString("pref_flight_location", "Ïndoor");
		text.append(String.format("Set flight location to %1$s\n", flightLocation));
		// drone.setFlightLocation(flightLocation);

		return text.toString();
	}

	/**
	 * Upon pressing the BACK-button, the user has to confirm the connection to the drone is taken down.
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			new AlertDialog.Builder(this).setMessage("Upon exiting, drone will be disconnected !")
					.setTitle("Exit YADrone ?").setCancelable(false)
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							YADroneApplication app = (YADroneApplication) getApplication();
							ARDrone drone = app.getARDrone();
							drone.disconnect();

							finish();
						}
					}).setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
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

	@Override
	public void stateChanged(DroneState state) {
		//
	}

	@Override
	public void controlStateChanged(ControlState state) {
		// System.out.println("ControlState: " + state);
	}

	@Override
	public void tagsDetected(ArrayList<VisionTag> list) {
		if (list.size() > 0) {
			System.out.println("tagsDetected: " + list);
		}
	}

	@Override
	public void trackersSend(int[][] locked, int[][][] point) {
		// System.out.println("trackersSend: " + locked + " " + point);
	}

	@Override
	public void receivedPerformanceData(VisionPerformance d) {
		// System.out.println("Visionperf: " + d);
	}

	@Override
	public void receivedRawData(float[] vision_raw) {
		// System.out.println("Visionrawdata: " + vision_raw);
	}

	@Override
	public void receivedData(VisionData d) {
		// System.out.println("Visiondata: " + d);
	}

	@Override
	public void receivedVisionOf(float[] of_dx, float[] of_dy) {
		// System.out.println("Visionof: " + of_dx + " " + of_dy);
	}

}
