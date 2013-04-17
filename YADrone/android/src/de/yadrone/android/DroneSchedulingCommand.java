package de.yadrone.android;

import java.util.List;

import android.util.Log;

import com.shigeodayo.ardrone.ARDrone;
import com.shigeodayo.ardrone.command.ATCommand;
import com.shigeodayo.ardrone.command.CommandManager;
import com.shigeodayo.ardrone.navdata.ControlState;
import com.shigeodayo.ardrone.navdata.DroneState;
import com.shigeodayo.ardrone.navdata.NavDataManager;
import com.shigeodayo.ardrone.navdata.StateListener;

public class DroneSchedulingCommand {

	private ATCommand mCommand;
	private int mDuration;
	private int mRepetitions;
	private String mSound; 

	DroneSchedulingCommand(ATCommand command, int duration, int repetitions, String sound) {
		this.mCommand = command;
		this.mDuration = duration;
		this.mRepetitions = repetitions;
		this.mSound = sound;
	}

	public void execute(SoundPlayer soundPlayer, ARDrone drone) throws InterruptedException {
		final CommandManager cm = drone.getCommandManager();
		final NavDataManager nm = drone.getNavDataManager();

		if (!mSound.isEmpty()) {
			// Enable when mSound contains the resId for the sound. 
//			soundPlayer.loadAndPlaySound(mSound);
		}
		int repetitions = mRepetitions > 0 ? mRepetitions : 1;
		for (int i = 0; i < repetitions; i++) {
			Log.d("FlyRoute", mCommand.toString());

			nm.setStateListener(new StateListener() {
				private boolean done = false;

				@Override
				public synchronized void stateChanged(DroneState state) {
					if (!done && state.isEmergency()) {
						cm.emergency();
					}
					nm.setStateListener(null);
					done = true;
				}

				@Override
				public void controlStateChanged(ControlState state) {
					// ignore
				}
			});

			cm.setCommand(mCommand);
			Thread.sleep(mDuration / repetitions);
		}
	}
}
