package de.yadrone.android;

import android.app.Application;
import android.util.Log;

import com.shigeodayo.ardrone.ARDrone;

public class YADroneApplication extends Application {
	/**
	 * The drone is kept in the application context so that all activities use the same drone instance
	 */
	private ARDrone drone;

	public void onCreate() {
		drone = new ARDrone("192.168.1.1", null);
		// drone = new ARDrone("10.56.42.211", null);
		Log.i("YADroneApplication", "Start Drone");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public ARDrone getARDrone() {
		return drone;
	}

}
