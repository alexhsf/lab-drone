package de.yadrone.android;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shigeodayo.ardrone.ARDrone;

public class FlightPlanProgressActivity extends BaseActivity {

	public FlightPlanProgressActivity() {
		super(0);
	}

	private String mFlightPlanUri;
	// private DroneCommandScheduler mScheduler;
	private List<DroneSchedulingCommand> mFlightPlan;
	private Thread mThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flightplan_progress);

		// Get the flight plan from the intent
		Intent intent = getIntent();
		mFlightPlanUri = intent.getStringExtra(FlightPlanActivity.FLIGHTPLAN_URI);

		mThread = new Thread() {
			public void run() {
				LoadFlightPlan();
				FlyRoute();
			}
		};
	}

	@Override
	protected void onPause() {
		super.onPause();
		mThread.interrupt();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mThread.start();
	}

	private void LoadFlightPlan() {
		IFlightPlanReader reader;
		if (mFlightPlanUri.startsWith("ftp") || mFlightPlanUri.startsWith("http")) {
			reader = new FlightPlanFtpReader();
		} else {
			reader = new FlightPlanFileReader();
		}
		String jsonFlightPLan = reader.getFlightPlan(mFlightPlanUri);
		JsonFlightPlanParser jsonParser = new JsonFlightPlanParser();
		mFlightPlan = jsonParser.getFlightPlan(jsonFlightPLan);
	}

	private void FlyRoute() {
		YADroneApplication app = (YADroneApplication) getApplication();
		final ARDrone drone = app.getARDrone();
		// NavDataManager nd = drone.getNavDataManager();
		// nd.setVelocityListener(new VelocityListener() {
		//
		// @Override
		// public void velocityChanged(float vx, float vy, float vz) {
		// if (vx != 0f || vy != 0f || vz != 0f) {
		// System.out.println("Velocity vx:" + vx + " vy:" + vy + " vz: " + vz);
		// }
		// }
		// });

		for (DroneSchedulingCommand command : mFlightPlan) {
			try {
				Log.d("FlyRoute", command.toString());
				command.execute(drone);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}

}
