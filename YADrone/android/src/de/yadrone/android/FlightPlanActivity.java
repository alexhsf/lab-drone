package de.yadrone.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class FlightPlanActivity extends Activity {

	public FlightPlanActivity() {
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flightplan);
	}

    public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_flightplan, menu);	    
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		Intent i;
	    switch (item.getItemId()) 
	    {
	    	case R.id.menuitem_main:
	    		i = new Intent(this, MainActivity.class);
	    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		startActivity(i);
		        return true;
	    	case R.id.menuitem_navdata:
	    		i = new Intent(this, NavDataActivity.class);
	    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		startActivity(i);
		        return true;
	    	case R.id.menuitem_control:
		    	i = new Intent(this, ControlActivity.class);
		    	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    	startActivity(i);
		        return true;
	    	case R.id.menuitem_settings:
	    		i = new Intent(this, DronePreferenceActivity.class);
	    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		startActivity(i);
		        return true;
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}

}
