package de.yadrone.android;

import java.text.RuleBasedCollator;

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
import com.shigeodayo.ardrone.command.DetectionType;
import com.shigeodayo.ardrone.command.EnemyColor;
import com.shigeodayo.ardrone.command.FlyingMode;
import com.shigeodayo.ardrone.command.VisionTagType;
import com.shigeodayo.ardrone.configuration.ConfigurationListener;
import com.shigeodayo.ardrone.configuration.ConfigurationManager;
import com.shigeodayo.ardrone.navdata.CadType;
import com.shigeodayo.ardrone.navdata.NavDataManager;
import com.shigeodayo.ardrone.navdata.TrackerData;
import com.shigeodayo.ardrone.navdata.VisionData;
import com.shigeodayo.ardrone.navdata.VisionListener;
import com.shigeodayo.ardrone.navdata.VisionPerformance;
import com.shigeodayo.ardrone.navdata.VisionTag;

public class MainActivity extends BaseActivity implements VisionListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final TextView text = (TextView) findViewById(R.id.text_init);

		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		text.append("\nConnected to " + wifi.getConnectionInfo().getSSID() + "\n");
		text.append(mCreationInfo);
		
		YADroneApplication app = (YADroneApplication) getApplication();
		final ARDrone drone = app.getARDrone();

		try {
			CommandManager cmd = drone.getCommandManager();
			cmd.setAutonomousFlight(false);

			// Do we need video to enable horizontal detection?
			// cmd.setVideoData(true);
			cmd.setEnemyColors(EnemyColor.ORANGE_BLUE);
			cmd.setDetectionType(CadType.MULTIPLE_DETECTION_MODE);
			cmd.setDetectionType(DetectionType.VERTICAL, new VisionTagType[] { VisionTagType.ORIENTED_ROUNDEL,
					VisionTagType.BLACK_ROUNDEL, VisionTagType.ROUNDEL });
			cmd.setDetectionType(DetectionType.HORIZONTAL, new VisionTagType[] { VisionTagType.SHELL_TAG_V2,
					VisionTagType.STRIPE, VisionTagType.TOWER_SIDE });

			// cmd.setFlyingMode(FlyingMode.HOVER_ON_TOP_OF_ORIENTED_ROUNDEL);
			cmd.setFlyingMode(FlyingMode.FREE_FLIGHT);
			cmd.setHoveringRange(500);

			NavDataManager nav = drone.getNavDataManager();
			// nav.setStateListener(this);
			nav.setVisionListener(this);

		} catch (Exception exc) {
			exc.printStackTrace();

			if (drone != null)
				drone.stop();
		}

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
							drone.stop();

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
	public void tagsDetected(VisionTag[] tags) {
		System.out.println("tagsDetected: ");
		for (int n = 0; n < tags.length; n++) {
			System.out.println("tagsDetected: " + tags[n]);
		}
	}

	@Override
	public void trackersSend(TrackerData d) {
		// System.out.println(d);
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
		// System.out.println(d);
	}

	@Override
	public void receivedVisionOf(float[] of_dx, float[] of_dy) {
		// System.out.println("Visionof: " + of_dx + " " + of_dy);
	}

	@Override
	public void typeDetected(int type) {
		System.out.println("type detected: " + type);
	}

}
