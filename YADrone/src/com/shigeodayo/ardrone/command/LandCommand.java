package com.shigeodayo.ardrone.command;

public class LandCommand extends RefCommand {
	public LandCommand() {
		super(false, false);
		// 9th bit set to 0
	}

	@Override
	public int getPriority() {
		return HIGH_PRIORITY;
	}

	public String getCategory() {
		return LAND_TAKEOFF_CATEGORY;
	}

}
