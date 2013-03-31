package com.shigeodayo.ardrone.command;

public class RefCommand extends ATCommand {
	protected int value;

	protected RefCommand(boolean takeoff, boolean emergency) {
		value = (1 << 18) | (1 << 20) | (1 << 22) | (1 << 24) | (1 << 28);

		if (emergency) {
			value |= (1 << 8);
		}

		if (takeoff) {
			value |= (1 << 9);
		}
	}

	@Override
	protected String getID() {
		return "REF";
	}

	@Override
	protected Object[] getParameters() {
		return new Object[] { value };
	}
}
