package com.shigeodayo.ardrone.command;

public class StopCommand extends PCMDCommand {

	public StopCommand() {
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

}
