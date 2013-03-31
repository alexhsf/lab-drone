package com.shigeodayo.ardrone.command;

public class TakeOffCommand extends RefCommand {
	public TakeOffCommand() {
		super(true, false);
	}

	public boolean isSticky() {
		return true;
	}

	public String getCategory() {
		return LAND_TAKEOFF_CATEGORY;
	}
}
