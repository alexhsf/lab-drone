package com.shigeodayo.ardrone.command;

public abstract class DroneCommand {
	protected static final byte MIN_PRIORITY = 100;
	protected static final byte HIGH_PRIORITY = 50;
	protected static final byte VERY_HIGH_PRIORITY = 10;
	protected static final byte MAX_PRIORITY = 0;
	protected static final byte DEFAULT_STICKY_RATE_MS = 100;

	public abstract byte getPriority();

	// TODO hide this into the CommandQueue
	// a command should not know about the queue order
	protected long qorder;
	
	public long getQorder() {
		return qorder;
	}

	public void setQorder(long qorder) {
		this.qorder = qorder;
	}

	public boolean isSticky() {
		return false;
	}
	
	/**
	 * Defines if this command clears a previous sticky command
	 */
	public boolean clearSticky() {
		return false;
	}
		
	/**
	 * For sticky packets indicates delay between sending repeated packets;
	 */
	public long getStickyRate() {
		return DEFAULT_STICKY_RATE_MS;
	}

}
