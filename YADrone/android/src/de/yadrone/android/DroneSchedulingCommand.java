package de.yadrone.android;

import java.util.List;

import com.shigeodayo.ardrone.command.DroneCommand;

public class DroneSchedulingCommand {
	
	private List<DroneCommand> mDroneCommands;
	private int mDuration;
	private int mRepetitions;
	
	DroneSchedulingCommand(List<DroneCommand> droneCommands, int duration, int repetitions) {
		this.mDroneCommands = droneCommands;
		this.mDuration = duration;
		this.mRepetitions = repetitions;
	}
	
	public void execute(DroneCommandScheduler scheduler){
		
	}
}
