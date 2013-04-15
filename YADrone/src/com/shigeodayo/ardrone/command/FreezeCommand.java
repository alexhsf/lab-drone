package com.shigeodayo.ardrone.command;

public class FreezeCommand extends PCMDCommand {

	public FreezeCommand() {
		super(false, false, 0f, 0f, 0f, 0f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shigeodayo.ardrone.command.DroneCommand#isSticky()
	 */
	@Override
	public boolean isSticky() {
		return false;
	}

	/**
	 * Defines if this command clears a previous sticky command
	 */
	@Override
	public boolean clearSticky() {
		return true;
	}
	
}
