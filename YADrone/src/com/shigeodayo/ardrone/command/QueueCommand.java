package com.shigeodayo.ardrone.command;

public class QueueCommand {

	private boolean continuance;
	private String command;

	public QueueCommand(boolean continuance, String command) {
		super();
		this.continuance = continuance;
		this.command = command;
	}

	public boolean isContinuance() {
		return continuance;
	}

	public String getCommand() {
		return command;
	}

}
