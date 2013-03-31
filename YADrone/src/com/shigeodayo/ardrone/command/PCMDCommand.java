package com.shigeodayo.ardrone.command;

public class PCMDCommand extends ATCommand {
	protected boolean hover;
	protected boolean combined_yaw_enabled;
	protected float left_right_tilt;
	protected float front_back_tilt;
	protected float vertical_speed;
	protected float angular_speed;

	public PCMDCommand(boolean hover, boolean combined_yaw_enabled, float left_right_tilt, float front_back_tilt,
			float vertical_speed, float angular_speed) {
		super();
		this.hover = hover;
		this.combined_yaw_enabled = combined_yaw_enabled;
		this.left_right_tilt = left_right_tilt;
		this.front_back_tilt = front_back_tilt;
		this.vertical_speed = vertical_speed;
		this.angular_speed = angular_speed;
	}

	@Override
	protected String getID() {
		return "PCMD";
	}

	@Override
	protected Object[] getParameters() {
		int mode;
		if (hover) {
			mode = 0;
		} else {
			mode = 1;
		}

		if (combined_yaw_enabled) {
			mode |= (1 << 1);
		}

		return new Object[] { mode, left_right_tilt, front_back_tilt, vertical_speed, angular_speed };
	}
}
