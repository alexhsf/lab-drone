package com.shigeodayo.ardrone.command;

public class KeepAliveCommand extends ATCommand {

	@Override
	protected String getID() {
		return "COMWDG";
	}

	@Override
	protected Object[] getParameters() {
		return new Object[] {};
	}

	@Override
	public byte getPriority() {
		return VERY_HIGH_PRIORITY;
	}
}
