package de.yadrone.android;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.shigeodayo.ardrone.ARDrone;
import com.shigeodayo.ardrone.navdata.ControlState;
import com.shigeodayo.ardrone.navdata.DroneState;
import com.shigeodayo.ardrone.navdata.NavDataManager;
import com.shigeodayo.ardrone.navdata.StateListener;

public class NavDataActivity extends BaseActivity {

	public NavDataActivity() {
		super(R.id.menuitem_navdata);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navdata);
	}

	public void onResume() {
		super.onResume();
		final TextView text = (TextView) findViewById(R.id.text_navdata);

		YADroneApplication app = (YADroneApplication) getApplication();
		ARDrone drone = app.getARDrone();

		NavDataManager nm = drone.getNavDataManager();
		nm.setStateListener(new StateListener() {
			@Override
			public void stateChanged(final DroneState state) {
				runOnUiThread(new Runnable() {
					public void run() {
						text.setText(state + "");
					}
				});
			}

			@Override
			public void controlStateChanged(ControlState state) {
				// TODO Auto-generated method stub
			}

		});
	}

	public void onPause() {
		super.onPause();
		YADroneApplication app = (YADroneApplication) getApplication();
		ARDrone drone = app.getARDrone();

		NavDataManager nm;
		nm = drone.getNavDataManager();
		nm.setStateListener(null);
	}

}
