package de.yadrone.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class FlightPlanActivity extends BaseActivity {

	public final static String FLIGHTPLAN_URI = "de.yadrone.android.FLIGHTPLAN_URI";
	
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

	public void OnButtonClick(View view)
	{
        // Do something in response to button
    	String flightPathUri;
    	switch (view.getId())
    	{
    	case R.id.buttonTakeOffAndLand:
        	flightPathUri = "flightplan1.json";
    		break;
    	case R.id.buttonTakeOffSpinAndLand:
        	flightPathUri = "flightplan2.json";
    		break;
    	case R.id.buttonTakeOffFly2MetersLand:
        	flightPathUri = "flightplan3.json";
    		break;
    	case R.id.buttonTakeOffFly2mSpinFlyBackLand:
        	flightPathUri = "flightplan4.json";
    		break;
    	default:
        	flightPathUri = "";
    		break;
    	}
    	if (!flightPathUri.isEmpty())
    	{
        	Intent intent = new Intent(this, FlightPlanProgressActivity.class);
        	intent.putExtra(FLIGHTPLAN_URI, flightPathUri);
        	startActivity(intent);
    	}
	}
}
