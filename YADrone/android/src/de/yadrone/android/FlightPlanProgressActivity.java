package de.yadrone.android;

//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
import java.util.List;
//import java.util.ListIterator;

//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.json.JSONTokener;

import com.shigeodayo.ardrone.ARDrone;
import com.shigeodayo.ardrone.command.DroneCommand;

import android.content.Intent;
import android.os.Bundle;

public class FlightPlanProgressActivity extends BaseActivity {

	private String mFlightPlanUri;
	private DroneCommandScheduler mScheduler;
	private List<DroneSchedulingCommand> mFlightPlan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flightplan_progress);

		// Get the flight plan from the intent
		Intent intent = getIntent();
		mFlightPlanUri = intent.getStringExtra(FlightPlanActivity.FLIGHTPLAN_URI);

//    	YADroneApplication app = (YADroneApplication)getApplication();
//    	final ARDrone drone = app.getARDrone();
//		mScheduler = new DroneCommandScheduler(drone);
		
		LoadFlightPlan();
//		new Thread() {
//			public void run() {
//				FlyRoute();
//			}
//		}.start();
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

//	private void FlyRoute() {
//	YADroneApplication app = (YADroneApplication)getApplication();
//	final ARDrone drone = app.getARDrone();
//	drone.takeOff();
//
//	for (DroneSchedulingCommand command : mDroneCommands) {
//		command.execute();
//	}
//	
//	drone.landing();
//}


	// public boolean onCreateOptionsMenu(Menu menu)
	// {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.menu_flightplan, menu);
	// return true;
	// }

}
