package de.yadrone.android;

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

        updateSummaryWithParameterValue("pref_min_altitude");
        updateSummaryWithParameterValue("pref_max_altitude");
        updateSummaryWithParameterValue("pref_vertical_speed");
        updateSummaryWithParameterValue("pref_max_euler_angle");
        updateSummaryWithParameterValue("pref_hull_type");
        updateSummaryWithParameterValue("pref_flight_location");
        updateSummaryWithParameterValue("pref_battery_alarm_level");
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
    }
    
    private void updateSummaryWithParameterValue(String key)
    {
        Preference pref = findPreference(key);
    	if (pref instanceof EditTextPreference)
    	{
    		int id;
    		if (key.equals("pref_min_altitude"))
    		{
        		id = R.string.pref_min_altitude_summ;
    		}
    		else if (key.equals("pref_max_altitude"))
    		{
    			id = R.string.pref_max_altitude_summ;
    		}
    		else if (key.equals("pref_vertical_speed"))
    		{
    			id = R.string.pref_vertical_speed_summ;
    		}
    		else if (key.equals("pref_max_euler_angle"))
    		{
    			id = R.string.pref_max_euler_angle_summ;
    		}
    		else if (key.equals("pref_battery_alarm_level"))
    		{
    			id = R.string.pref_battery_alarm_level_summ;
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
