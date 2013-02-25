package com.example.appconfig;

import com.example.appconfig.R;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	protected void onStart() {
		super.onStart();
		showPreferences();
	}
	
	private void showPreferences() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	    StringBuilder builder = new StringBuilder();
	    builder.append("Application preferences:");
	    builder.append("\n" + sharedPrefs.getString("pref_ssid", ""));
	    builder.append("\n" + sharedPrefs.getString("pref_altitude", "-1"));
	    builder.append("\n" + sharedPrefs.getString("pref_vertical_speed", "NULL"));
	    TextView settingsTextView = (TextView) findViewById(R.id.main_text);
	    settingsTextView.setText(builder.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
		case R.id.action_settings:
			Intent intent = new Intent(this, PreferencesActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
