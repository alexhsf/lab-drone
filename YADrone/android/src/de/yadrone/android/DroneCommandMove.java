package de.yadrone.android;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.shigeodayo.ardrone.ARDrone;

import android.util.Log;

public class DroneCommandMove extends DroneCommand {

	private DroneMovement targetLocation; // in the room
	private Timer timer;
	private Object lock = new Object();

	DroneCommandMove(DroneCommandScheduler scheduler, float x, float y, float z, float orientation, float timespan) {
		super(scheduler);
		targetLocation = new DroneMovement(x, y, z, orientation, timespan);
	}

	public void execute() {
		Log.i("DroneCommandMove", String.format("x=%1$f y=%2$f z=%3$f orientation=%4$f timespan=%5$f\n",
				targetLocation.x, targetLocation.y, targetLocation.z, targetLocation.r, targetLocation.t));
		DroneMovement speed = scheduler.determineSpeedVector(targetLocation);

		ARDrone drone = scheduler.getDrone();
		drone.move3D((int) speed.x, (int) speed.y, (int) speed.z, (int) speed.r);

		// if expires then send a stop command and the execution for this
		// command is finished
		timer = new Timer();
		timer.schedule(new StopMovementTask(drone), new Date((long) speed.t));
		synchronized (lock) {
			try {
				lock.wait(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	class StopMovementTask extends TimerTask {
		private ARDrone drone;

		public StopMovementTask(ARDrone drone) {
			this.drone = drone;
		}

		public void run() {
			Log.i("DroneCommandMove", String.format("Stopped movement after %1$f sec\n", targetLocation.t));
			drone.stop();
			timer.cancel(); // Terminate the timer thread
			synchronized (lock) {
				lock.notifyAll();
			}
		}
	}
}
