package de.yadrone.android;

import java.util.List;

import android.util.Log;

import com.shigeodayo.ardrone.command.ATCommand;
import com.shigeodayo.ardrone.command.CommandManager;

public class DroneSchedulingCommand {
	
	private ATCommand mCommand;
	private int mDuration;
	private int mRepetitions;
	
	DroneSchedulingCommand(ATCommand command, int duration, int repetitions) {
		this.mCommand = command;
		this.mDuration = duration;
		this.mRepetitions = repetitions;
	}
	
	public void execute(CommandManager commandManager) throws InterruptedException {
		int repetitions = mRepetitions > 0 ? mRepetitions : 1;
		for (int i = 0; i < repetitions; i++) {
			Log.d("FlyRoute", mCommand.toString());
			commandManager.setCommand(mCommand);
			Thread.sleep(mDuration/repetitions);
		}
	}
}
