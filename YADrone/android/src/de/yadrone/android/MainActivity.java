package de.yadrone.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.widget.TextView;

import com.shigeodayo.ardrone.ARDrone;
import com.shigeodayo.ardrone.command.CommandManager;
import com.shigeodayo.ardrone.command.DetectionType;
import com.shigeodayo.ardrone.command.EnemyColor;
import com.shigeodayo.ardrone.command.FlyingMode;
import com.shigeodayo.ardrone.command.H264;
import com.shigeodayo.ardrone.command.VideoBitRateMode;
import com.shigeodayo.ardrone.command.VideoCodec;
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

public class MainActivity extends BaseActivity {

	public MainActivity() {
		super(R.id.menuitem_main);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final TextView text = (TextView) findViewById(R.id.text_init);

		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		text.append("\nConnected to " + wifi.getConnectionInfo().getSSID() + "\n");
		text.append(mCreationInfo);

		YADroneApplication app = (YADroneApplication) getApplication();
		final ARDrone drone = app.getARDrone();
		drone.start();

		CommandManager cm = drone.getCommandManager();
		cm.setVideoData(true);
		cm.setVideoCodecFps(H264.MIN_FPS);
		cm.setVideoCodec(VideoCodec.H264_360P);
		cm.setVideoBitrateControl(VideoBitRateMode.DYNAMIC);
		//cm.setVideoBitrate(H264.MAX_BITRATE);

	//
		// try {
		// // CommandManager cmd = drone.getCommandManager();
		// // cmd.setAutonomousFlight(false);
		//
		// // Do we need video to enable horizontal detection?
		// // cmd.setVideoData(true);
		//
		// // cmd.setFlyingMode(FlyingMode.HOVER_ON_TOP_OF_ORIENTED_ROUNDEL);
		// // cmd.setFlyingMode(FlyingMode.FREE_FLIGHT);
		// ///cmd.setHoveringRange(500);
		//
		// } catch (Exception exc) {
		// exc.printStackTrace();
		//
		// if (drone != null)
		// drone.stop();
		// }

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

}
