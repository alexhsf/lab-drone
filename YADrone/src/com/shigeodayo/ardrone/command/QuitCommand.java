package com.shigeodayo.ardrone.command;

public class QuitCommand extends DroneCommand {
	@Override
	public int getPriority() {
		return MAX_PRIORITY;
	}
}
