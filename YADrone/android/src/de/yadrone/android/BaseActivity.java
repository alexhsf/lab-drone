package de.yadrone.android;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

public class BaseActivity extends Activity {

    public BaseActivity() {
        super();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
    	Class<? extends Activity> ac = null;
        switch (item.getItemId()) 
        {
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
        	case R.id.menuitem_remote:
        		ac = RemoteActivity.class;
    	    default:
    	        return super.onOptionsItemSelected(item);
        }
        Intent i = new Intent(this, ac);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        return true;
    }

}
