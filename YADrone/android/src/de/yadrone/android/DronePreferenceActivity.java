package de.yadrone.android;

import com.shigeodayo.ardrone.ARDrone;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class DronePreferenceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        updateSummaryWithParameterValue("pref_ssid");
        updateSummaryWithParameterValue("pref_altitude");
        updateSummaryWithParameterValue("pref_vertical_speed");
        updateSummaryWithParameterValue("pref_max_yaw");
        updateSummaryWithParameterValue("pref_max_tilt");
        updateSummaryWithParameterValue("pref_hull_type");
        updateSummaryWithParameterValue("pref_flight_location");
    }
    
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener( this );
    }

    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener( this );
    }

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    	updateSummaryWithParameterValue(key);
    	
//    	YADroneApplication app = (YADroneApplication)getApplication();
//    	final ARDrone drone = app.getARDrone();
//    	
//        Preference pref = findPreference(key);
//		if (key.equals("pref_altitude"))
//		{
//			EditTextPreference etp = (EditTextPreference) pref;
//			String preferenceValue = etp.getText();
//			drone.setMaxAltitude(Integer.parseInt(preferenceValue));
//		}
    }
    
    private void updateSummaryWithParameterValue(String key)
    {
        Preference pref = findPreference(key);
    	if (pref instanceof EditTextPreference)
    	{
    		int id;
    		if (key.equals("pref_ssid"))
    		{
        		id = R.string.pref_ssid_summ;
    		}
    		else if (key.equals("pref_altitude"))
    		{
    			id = R.string.pref_altitude_summ;
    		}
    		else if (key.equals("pref_vertical_speed"))
    		{
    			id = R.string.pref_vertical_speed_summ;
    		}
    		else if (key.equals("pref_max_yaw"))
    		{
    			id = R.string.pref_max_yaw_summ;
    		}
    		else if (key.equals("pref_max_yaw"))
    		{
    			id = R.string.pref_max_yaw_summ;
    		}
    		else if (key.equals("pref_max_tilt"))
    		{
    			id = R.string.pref_max_tilt_summ;
    		}
    		else
    		{
    			id = 0;
    		}
    		if (id != 0)
    		{
    			EditTextPreference etp = (EditTextPreference) pref;
    			String preferenceValue = etp.getText();
    			String summaryFormatString = getResources().getString(id);
    			pref.setSummary(String.format(summaryFormatString, preferenceValue));
    		}
    	}
    	else if (pref instanceof ListPreference)
    	{
    		int id;
    		if (key.equals("pref_hull_type"))
    		{
    			id = R.string.pref_hull_type_summ;
    		}
    		else if (key.equals("pref_flight_location"))
    		{
    			id = R.string.pref_flight_location_summ;
    		}
    		else
    		{
    			id = 0;
    		}
    		if (id != 0)
    		{
    			ListPreference lp = (ListPreference) pref;
    			String preferenceValue = lp.getValue();
    			String summaryFormatString = getResources().getString(id);
    			pref.setSummary(String.format(summaryFormatString, preferenceValue));
    		}
    	}
    }
}
