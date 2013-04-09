package com.shigeodayo.ardrone.command;

public class ResetControlAckCommand extends ControlCommand {

	public ResetControlAckCommand() {
		super(ControlMode.ACK, 0);
	}

	/* (non-Javadoc)
	 * @see com.shigeodayo.ardrone.command.ATCommand#getPriority()
	 */
	@Override
	public byte getPriority() {
		return MAX_PRIORITY;
	}

}
