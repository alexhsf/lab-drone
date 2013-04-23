package de.yadrone.android;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.shigeodayo.ardrone.ARDrone;
import com.shigeodayo.ardrone.command.CommandManager;
import com.shigeodayo.ardrone.configuration.ConfigurationListener;
import com.shigeodayo.ardrone.configuration.ConfigurationManager;
import com.shigeodayo.ardrone.navdata.BatteryListener;
import com.shigeodayo.ardrone.navdata.NavDataManager;
//import android.media.Ringtone;
//import android.media.RingtoneManager;
//import android.net.Uri;

public class BaseActivity extends Activity implements BatteryListener {

	protected int menuitem_id;
	protected StringBuilder mCreationInfo;
	private int mBatteryAlarmLevel;
	private Date mLastBatteryLevelUpdate;
	// private ToneGenerator mTone;
	protected SoundPlayer soundPlayer;

	public BaseActivity(int menuitem_id) {
		super();
		this.menuitem_id = menuitem_id;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		soundPlayer = new SoundPlayer(this);
		mCreationInfo = new StringBuilder();

		YADroneApplication app = (YADroneApplication) getApplication();
		final ARDrone drone = app.getARDrone();

		try {
			mCreationInfo.append(configureDrone(drone));
			mCreationInfo.append(setupBatteryAlarm(drone));

			// not allowed to do networking on the main thread
			ConfigurationManager cfg = drone.getConfigurationManager();
			cfg.getConfiguration(new ConfigurationListener() {

				@Override
				public void result(final String s) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							mCreationInfo.append(s);
						}
					});
				}
			});
		} catch (Exception exc) {
			exc.printStackTrace();

			if (drone != null) {
				drone.stop();
			}
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.base_menu, menu);
		menu.removeItem(menuitem_id);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Class<? extends Activity> ac = null;
		switch (item.getItemId()) {
		case R.id.menuitem_flightplan:
			ac = FlightPlanActivity.class;
			break;
		case R.id.menuitem_settings:
			ac = DronePreferenceActivity.class;
			break;
		case R.id.menuitem_navdata:
			ac = NavDataActivity.class;
			break;
		case R.id.menuitem_control:
			ac = ControlActivity.class;
			break;
		case R.id.menuitem_main:
			ac = MainActivity.class;
			break;
		case R.id.menuitem_video:
			ac = VideoActivity.class;
			break;
		case R.id.menuitem_photo:
			ac = PhotoActivity.class;
			break;
		case R.id.menuitem_remote:
			ac = RemoteActivity.class;
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		Intent i = new Intent(this, ac);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		return true;
	}

	@Override
	public void batteryLevelChanged(int percentage) {
		int updateInterval = 5000;
		Date now = new Date();
		if (now.getTime() - mLastBatteryLevelUpdate.getTime() > updateInterval) {
			Log.i("BatteryLevel", String.format("%1$d %%", percentage));
			if (percentage < mBatteryAlarmLevel) {
				// TODO: still issue when flightplan and battery low try to play simultaneously
				soundPlayer.loadAndPlaySound(R.raw.battery_low);
				// mBatteryAlarmSound.play();
				// int durationMs = 500;
				// mTone.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, durationMs);
			}
			mLastBatteryLevelUpdate = now;
		}
	}

	@Override
	public void voltageChanged(int vbat_raw) {
		// Log.i("BatteryLevel", String.format("%1$d mV", vbat_raw));
	}

	private String configureDrone(ARDrone drone) {
		StringBuilder text = new StringBuilder();
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		CommandManager cm =drone.getCommandManager(); 

		int minAltitude = Integer.parseInt(sharedPrefs.getString("pref_min_altitude", "50"));
		text.append(String.format("Min. Altitude       = %1$d mm\n", minAltitude));
		cm.setMinAltitude(minAltitude);

		int maxAltitude = Integer.parseInt(sharedPrefs.getString("pref_max_altitude", "2000"));
		text.append(String.format("Max. Altitude       = %1$d mm\n", maxAltitude));
		cm.setMaxAltitude(maxAltitude);

		int maxVerticalSpeed = Integer.parseInt(sharedPrefs.getString("pref_vertical_speed", "1000"));
		text.append(String.format("Max. vertical speed = %1$d mm/s\n", maxVerticalSpeed));
		cm.setMaxVz(maxVerticalSpeed);

		float maxEulerAngle = (float)Double.parseDouble(sharedPrefs.getString("pref_max_euler_angle", "0.25"));
		text.append(String.format("Max. Euler angle    = %1$f radians\n", maxEulerAngle));
		cm.setMaxEulerAngle(maxEulerAngle);

		String hullType = sharedPrefs.getString("pref_hull_type", "Indoor");
		text.append(String.format("Hull type           = %1$s\n", hullType));
		String flightLocation = sharedPrefs.getString("pref_flight_location", "Indoor");
		text.append(String.format("Flight location     = %1$s\n", flightLocation));
		cm.setOutdoor(flightLocation.equals("Outdoor"), hullType.equals("Outdoor"));

		String batteryAlarmLevel = sharedPrefs.getString("pref_battery_alarm_level", "20");
		mBatteryAlarmLevel = Integer.parseInt(batteryAlarmLevel);
		text.append(String.format("Battery alarm level = %1$d\n", mBatteryAlarmLevel));
		
		return text.toString();
	}

	private String setupBatteryAlarm(ARDrone drone) {
		// Set time stamp to 1-1-1970, to make sure that the battery level is updated on next listening event
		mLastBatteryLevelUpdate = new Date(0);

		// int volume = 50;
		// mTone = new ToneGenerator(AudioManager.STREAM_ALARM, volume);

		NavDataManager nav = drone.getNavDataManager();
		nav.setBatteryListener(this);
		return "";
	}
}
