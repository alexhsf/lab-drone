package de.yadrone.android;

import java.util.List;

import android.util.Log;

import com.shigeodayo.ardrone.command.ATCommand;
import com.shigeodayo.ardrone.command.CommandManager;

public class DroneSchedulingCommand {
	
	private List<ATCommand> mDroneCommands;
	private int mDuration;
	private int mRepetitions;
	
	DroneSchedulingCommand(List<ATCommand> droneCommands, int duration, int repetitions) {
		this.mDroneCommands = droneCommands;
		this.mDuration = duration;
		this.mRepetitions = repetitions;
	}
	
	public void execute(CommandManager commandManager) throws InterruptedException {
		int repetitions = mRepetitions > 0 ? mRepetitions : 1;
		for (int i = 0; i < repetitions; i++) {
			for (ATCommand command : mDroneCommands)
			{
				Log.d("FlyRoute", command.toString());
				commandManager.setCommand(command);
			}
			Thread.sleep(mDuration/repetitions);
		}
	}
}
