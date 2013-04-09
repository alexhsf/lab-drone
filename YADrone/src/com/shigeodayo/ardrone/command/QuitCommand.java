package com.shigeodayo.ardrone.command;

public class QuitCommand extends DroneCommand {
	@Override
	public byte getPriority() {
		return MAX_PRIORITY;
	}
}
