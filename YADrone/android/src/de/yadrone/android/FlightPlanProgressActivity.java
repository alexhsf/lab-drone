package de.yadrone.android;

import java.util.List;

import com.shigeodayo.ardrone.ARDrone;
import com.shigeodayo.ardrone.navdata.NavDataManager;
import com.shigeodayo.ardrone.navdata.VelocityListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class FlightPlanProgressActivity extends BaseActivity {

	private String mFlightPlanUri;
//	private DroneCommandScheduler mScheduler;
	private List<DroneSchedulingCommand> mFlightPlan;
	private Thread mThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flightplan_progress);

		// Get the flight plan from the intent
		Intent intent = getIntent();
		mFlightPlanUri = intent.getStringExtra(FlightPlanActivity.FLIGHTPLAN_URI);
		LoadFlightPlan();
		
		// TODO: start threat in onResumed and stop thread in onPaused?
		mThread = new Thread() {
			public void run() {
				FlyRoute();
			}
		};
	}
	

    @SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mThread.interrupt();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mThread.start();
	}


	private void LoadFlightPlan() {
//    	mDroneCommands = new ArrayList<DroneCommand>();
//    	mDroneCommands.add(new DroneCommandMove(mScheduler, 2000,    0, 0,  0, 10000));
//    	mDroneCommands.add(new DroneCommandMove(mScheduler, 1000,    0, 0, 90, 1000));
//    	mDroneCommands.add(new DroneCommandMove(mScheduler, 1000, 1000, 0, 90, 3000));
    	FlightPlanFileReader reader = new FlightPlanFileReader();
    	String jsonFlightPLan = reader.getFlightPlan(mFlightPlanUri);
    	JsonFlightPlanParser jsonParser = new JsonFlightPlanParser();
    	mFlightPlan = jsonParser.getFlightPlan(jsonFlightPLan);
    }

	private void FlyRoute() {
		YADroneApplication app = (YADroneApplication)getApplication();
		final ARDrone drone = app.getARDrone();
		NavDataManager nd = drone.getNavDataManager();
		nd.setVelocityListener(new VelocityListener() {
			
			@Override
			public void velocityChanged(float vx, float vy, float vz) {
				System.out.println("Velocity vx:" + vx + " vy:" + vy + "vz: " + vz);
				
			}
		});
	
		for (DroneSchedulingCommand command : mFlightPlan) {
			try {
				Log.d("FlyRoute", command.toString());
				command.execute(drone.getCommandManager());
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}


	// public boolean onCreateOptionsMenu(Menu menu)
	// {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.menu_flightplan, menu);
	// return true;
	// }

}
