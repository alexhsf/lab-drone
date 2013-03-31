package com.shigeodayo.ardrone.command;

public class PCMDMagCommand extends ATCommand {
	protected boolean hover;
	protected boolean combined_yaw_enabled;
	boolean absolutecontrol;
	protected float left_right_tilt;
	protected float front_back_tilt;
	protected float vertical_speed;
	protected float angular_speed;
	protected float magneto_psi;
	protected float magneto_psi_accuracy;

	public PCMDMagCommand(boolean hover, boolean combined_yaw_enabled, boolean absolutecontrol, float left_right_tilt,
			float front_back_tilt, float vertical_speed, float angular_speed, float magneto_psi,
			float magneto_psi_accuracy) {
		super();
		this.hover = hover;
		this.combined_yaw_enabled = combined_yaw_enabled;
		this.absolutecontrol = absolutecontrol;
		this.left_right_tilt = left_right_tilt;
		this.front_back_tilt = front_back_tilt;
		this.vertical_speed = vertical_speed;
		this.angular_speed = angular_speed;
		this.magneto_psi = magneto_psi;
		this.magneto_psi_accuracy = magneto_psi_accuracy;
	}

	@Override
	protected String getID() {
		return "PCMD_MAG";
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

		if (absolutecontrol) {
			mode |= (1 << 2);
		}

		return new Object[] { mode, left_right_tilt, front_back_tilt, vertical_speed, angular_speed, magneto_psi,
				magneto_psi_accuracy };
	}
}