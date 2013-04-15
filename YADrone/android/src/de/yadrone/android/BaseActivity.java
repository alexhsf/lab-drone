package de.yadrone.android;

import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.shigeodayo.ardrone.ARDrone;
import com.shigeodayo.ardrone.configuration.ConfigurationListener;
import com.shigeodayo.ardrone.configuration.ConfigurationManager;
import com.shigeodayo.ardrone.navdata.BatteryListener;
import com.shigeodayo.ardrone.navdata.NavDataManager;

public class BaseActivity extends Activity implements BatteryListener {

	protected int menuitem_id;
	protected StringBuilder mCreationInfo;
	private Ringtone mBatteryAlarmSound;
	private int mBatteryAlarmLevel;
	private Date mLastPlayed;

	public BaseActivity(int menuitem_id) {
		super();
		this.menuitem_id = menuitem_id;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
		Log.i("BatteryLevel", String.format("%1$d %%", percentage));
		if (percentage < mBatteryAlarmLevel) {
			Date now = new Date();
			if (now.getTime() - mLastPlayed.getTime() > 10000) {
				mBatteryAlarmSound.play();
				mLastPlayed = now;
			}
		}
	}

	@Override
	public void voltageChanged(int vbat_raw) {
		Log.i("BatteryLevel", String.format("%1$d Volt", vbat_raw));
	}

	private String configureDrone(ARDrone drone) {
		StringBuilder text = new StringBuilder();
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		String ssid = sharedPrefs.getString("pref_ssid", "foo");
		text.append(String.format("Set SSID to %1$s\n", ssid));
		// drone.setSsid(ssid);

		double maxAltitude = Double.parseDouble(sharedPrefs.getString("pref_altitude", "3"));
		text.append(String.format("Set max. Altitude to %1$f m\n", maxAltitude));
		drone.setMaxAltitude(2000);
		drone.setMinAltitude(1000);

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

	private String setupBatteryAlarm(ARDrone drone) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mBatteryAlarmLevel = Integer.parseInt(sharedPrefs.getString("battery_alarm_level", "10"));

		final Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		mBatteryAlarmSound = RingtoneManager.getRingtone((Activity) this, ringtoneUri);
		mLastPlayed = new Date();
		// mBatteryAlarmSound.play();
		// mBatteryAlarmSound.stop();

		int volume = 50;
		int durationMs = 500;
		ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_ALARM, volume);
		tone.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, durationMs);

		// Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		// MediaPlayer mediaPlayer = new MediaPlayer();
		// try {
		// mediaPlayer.setDataSource(this, alert);
		// final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		// if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
		// mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
		// mediaPlayer.setLooping(false);
		// mediaPlayer.prepare();
		// mediaPlayer.start();
		// mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
		//
		// @Override
		// public void onCompletion(MediaPlayer mp) {
		// mp.stop();
		// }
		// });
		// }
		// } catch (IllegalArgumentException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (SecurityException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IllegalStateException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		NavDataManager nav = drone.getNavDataManager();
		nav.setBatteryListener(this);
		return String.format(Locale.US, "Battery alarm settings:\nLevel = %1$d %%\nTitle = %2$s\n", mBatteryAlarmLevel,
				mBatteryAlarmSound.getTitle(this));
	}

}
