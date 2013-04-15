package de.yadrone.android;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseActivity extends Activity {

	protected int menuitem_id;

	public BaseActivity(int menuitem_id) {
		super();
		this.menuitem_id = menuitem_id;
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

}
