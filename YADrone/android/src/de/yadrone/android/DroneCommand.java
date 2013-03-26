package de.yadrone.android;

public abstract class DroneCommand {
	
	protected DroneCommandScheduler scheduler;
	
	DroneCommand(DroneCommandScheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public void execute(){
		
	}
}
