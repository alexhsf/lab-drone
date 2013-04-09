package com.shigeodayo.ardrone.command;

public class EmergencyCommand extends RefCommand {
	public EmergencyCommand() {
		super(false, true);
	}
	
	@Override
	public byte getPriority() {
		return MAX_PRIORITY;
	}
}
