package de.yadrone.android;

import android.annotation.TargetApi;
import android.media.AudioManager;
import android.os.Build;
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

	public void execute(BaseActivity activity) throws InterruptedException {
		YADroneApplication app = (YADroneApplication) activity.getApplication();
		ARDrone drone = app.getARDrone();
		
		final CommandManager cm = drone.getCommandManager();
		final NavDataManager nm = drone.getNavDataManager();

		if (mSound.length() > 0) {
			int resId = activity.getResources().getIdentifier(mSound,"raw", activity.getPackageName());
			if (resId != 0) {
				activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
				activity.soundPlayer.loadAndPlaySound(resId);
			}
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
