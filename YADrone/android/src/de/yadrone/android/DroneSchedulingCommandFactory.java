package de.yadrone.android;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.shigeodayo.ardrone.command.ATCommand;
import com.shigeodayo.ardrone.command.ConfigureCommand;
import com.shigeodayo.ardrone.command.ControlCommand;
import com.shigeodayo.ardrone.command.ControlMode;
import com.shigeodayo.ardrone.command.EmergencyCommand;
import com.shigeodayo.ardrone.command.FlatTrimCommand;
import com.shigeodayo.ardrone.command.FlightAnimation;
import com.shigeodayo.ardrone.command.FlightAnimationCommand;
import com.shigeodayo.ardrone.command.GainsCommand;
import com.shigeodayo.ardrone.command.HoverCommand;
import com.shigeodayo.ardrone.command.KeepAliveCommand;
import com.shigeodayo.ardrone.command.LEDAnimation;
import com.shigeodayo.ardrone.command.LEDAnimationCommand;
import com.shigeodayo.ardrone.command.LandCommand;
import com.shigeodayo.ardrone.command.ManualTrimCommand;
import com.shigeodayo.ardrone.command.MiscCommand;
import com.shigeodayo.ardrone.command.MoveCommand;
import com.shigeodayo.ardrone.command.PCMDCommand;
import com.shigeodayo.ardrone.command.PCMDMagCommand;
import com.shigeodayo.ardrone.command.PMODECommand;
import com.shigeodayo.ardrone.command.PlayAnimationCommand;
import com.shigeodayo.ardrone.command.RawCaptureCommand;
import com.shigeodayo.ardrone.command.FreezeCommand;
import com.shigeodayo.ardrone.command.TakeOffCommand;
import com.shigeodayo.ardrone.command.VideoChannel;
import com.shigeodayo.ardrone.command.VideoChannelCommand;
import com.shigeodayo.ardrone.command.VisionOptionCommand;
import com.shigeodayo.ardrone.command.VisionParametersCommand;

public class DroneSchedulingCommandFactory {
	static DroneSchedulingCommand getDroneSchedulingCommand(String key, Object value) {
		DroneSchedulingCommand schedulingCommand = null;
		if (value instanceof JSONObject) {
			JSONObject parameters = (JSONObject) value;
			ATCommand command = null;
			int duration = parameters.optInt("Duration", 0);
			int repetitions = parameters.optInt("Repetitions", 1);
			try {
				Object commandValues = parameters.optJSONObject(key);
				if (key.equals("Configure")) {
					command = getConfigureCommand(commandValues);
				} else if (key.equals("Control")) {
					command = getControlCommand(commandValues);
				} else if (key.equals("Emergency")) {
					command = getEmergencyCommand(commandValues);
				} else if (key.equals("FlatTrim")) {
					command = getFlatTrimCommand(commandValues);
				} else if (key.equals("FlightAnimation")) {
					command = getFlightAnimationCommand(commandValues);
				} else if (key.equals("Gains")) {
					command = getGainsCommand(commandValues);
				} else if (key.equals("Hover")) {
					command = getHoverCommand(commandValues);
				} else if (key.equals("KeepAlive")) {
					command = getKeepAliveCommand(commandValues);
				} else if (key.equals("Land")) {
					command = getLandCommand(commandValues);
				} else if (key.equals("LEDAnimation")) {
					command = getLEDAnimationCommand(commandValues);
				} else if (key.equals("ManualTrim")) {
					command = getManualTrimCommand(commandValues);
				} else if (key.equals("Misc")) {
					command = getMiscCommand(commandValues);
				} else if (key.equals("Move")) {
					command = getMoveCommand(commandValues);
				} else if (key.equals("PCMD")) {
					command = getPCMDCommand(commandValues);
				} else if (key.equals("PCMDMag")) {
					command = getPCMDMagCommand(commandValues);
				} else if (key.equals("PlayAnimation")) {
					command = getPlayAnimationCommand(commandValues);
				} else if (key.equals("PMODE")) {
					command = getPMODECommand(commandValues);
				}
				// else if (key.equals("Quit"))
				// {
				// command = getQuitCommand(commandValues);
				// }
				else if (key.equals("RawCapture")) {
					command = getRawCaptureCommand(commandValues);
				} else if (key.equals("Stop")) {
					command = getStopCommand(commandValues);
				} else if (key.equals("TakeOff")) {
					command = getTakeOffCommand(commandValues);
				} else if (key.equals("VideoChannel")) {
					command = getVideoChannelCommand(commandValues);
				} else if (key.equals("VisionOption")) {
					command = getVisionOptionCommand(commandValues);
				} else if (key.equals("VisionParameters")) {
					command = getVisionParametersCommand(commandValues);
				}
				if (command != null) {
					schedulingCommand = new DroneSchedulingCommand(command, duration, repetitions);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		return schedulingCommand;
	}

	private static ATCommand getConfigureCommand(Object value) throws JSONException {
		ATCommand command = null;
		if (value instanceof JSONObject) {
			JSONObject parameters = (JSONObject) value;
			if (parameters.length() == 1) {
				Iterator<String> keys = parameters.keys();
				while (keys.hasNext()) {
					String configKey = keys.next();
					if (configKey.equals("control:control_level") || configKey.equals("control:flying_mode")
							|| configKey.equals("control:hovering_range") || configKey.equals("control:altitude_max")
							|| configKey.equals("control:altitude_min") || configKey.equals("control:control_vz_max")
							|| configKey.equals("control:control_yaw") || configKey.equals("detect:enemy_colors")
							|| configKey.equals("detect:detections_select_v")
							|| configKey.equals("detect:detections_select_h")
							|| configKey.equals("detect:detections_select_v_hsync")
							|| configKey.equals("pic:ultrasound_freq") || configKey.equals("network:wifi_mode")) {
						int configValue = parameters.getInt(configKey);
						command = new ConfigureCommand(configKey, configValue);
					} else if (configKey.equals("control:outdoor") || configKey.equals("control:flight_without_shell")
							|| configKey.equals("control:autonomous_flight")) {
						boolean configValue = parameters.getBoolean(configKey);
						command = new ConfigureCommand(configKey, configValue);
					} else if (configKey.equals("gps:latitude") || configKey.equals("gps:longitude")
							|| configKey.equals("gps:altitude")) {
						double configValue = parameters.getDouble(configKey);
						command = new ConfigureCommand(configKey, configValue);
					} else if (configKey.equals("network:ssid_single_player")
							|| configKey.equals("network:ssid_multi_player") || configKey.equals("network:owner_mac")
							|| configKey.equals("control:control_iphone_tilt") || // Float as string!
							configKey.equals("control:euler_angle_max")) { // Float as string!
						String configValue = parameters.getString(configKey);
						command = new ConfigureCommand(configKey, configValue);
					}
				}
			} else {
				Log.e("DroneSchedulingCommandFactory", String.format(
						"'Configure' json command only supports a single configuration option, found %d options",
						parameters.length()));
			}
		}
		// TODO: add support for video recording
		// SLA EVEN OVER "userbox:userbox_cmd", String.valueOf(UserBox.START.ordinal()) + "," + dirname)
		// SLA EVEN OVER "userbox:userbox_cmd", UserBox.CANCEL.ordinal()
		// SLA EVEN OVER "userbox:userbox_cmd", UserBox.STOP.ordinal()
		// SLA EVEN OVER "userbox:userbox_cmd", String.valueOf(UserBox.SCREENSHOT.ordinal()) + ","
		// + String.valueOf(delay) + "," + String.valueOf(nshots) + "," + dirname

		return command;
	}

	private static ATCommand getControlCommand(Object value) throws JSONException {
		ATCommand command = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			String modeString = parameters.getString("mode");
			int arg2 = parameters.getInt("arg2");
			try
			{
				ControlMode mode  = ControlMode.valueOf(modeString);
				command = new ControlCommand(mode, arg2);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		return command;
	}

	private static ATCommand getEmergencyCommand(Object value) {
		return new EmergencyCommand();
	}

	private static ATCommand getFlatTrimCommand(Object value) {
		return new FlatTrimCommand();
	}

	private static ATCommand getFlightAnimationCommand(Object value) throws JSONException {
		ATCommand command = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			String animationString = parameters.getString("animation");
			int animationDuration = parameters.getInt("duration");
			try
			{
				FlightAnimation animation = FlightAnimation.valueOf(animationString);
				command = new FlightAnimationCommand(animation, animationDuration);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		return command;
	}

	private static ATCommand getGainsCommand(Object value) throws JSONException {
		ATCommand command = null;
		if (value instanceof JSONObject) {
			JSONObject parameters = (JSONObject) value;
			int pq_kp = parameters.getInt("pq_kp");
			int r_kp = parameters.getInt("r_kp");
			int r_ki = parameters.getInt("r_ki");
			int ea_kp = parameters.getInt("ea_kp");
			int ea_ki = parameters.getInt("ea_ki");
			int alt_kp = parameters.getInt("alt_kp");
			int alt_ki = parameters.getInt("alt_ki");
			int vz_kp = parameters.getInt("vz_kp");
			int vz_ki = parameters.getInt("vz_ki");
			int hovering_kp = parameters.getInt("hovering_kp");
			int hovering_ki = parameters.getInt("hovering_ki");
			int hovering_b_kp = parameters.getInt("hovering_b_kp");
			int hovering_b_ki = parameters.getInt("hovering_b_ki");
			command = new GainsCommand(pq_kp, r_kp, r_ki, ea_kp, ea_ki, alt_kp, alt_ki, vz_kp, vz_ki, hovering_kp,
					hovering_ki, hovering_b_kp, hovering_b_ki);
		}
		return command;
	}

	private static ATCommand getHoverCommand(Object value) {
		return new HoverCommand();
	}

	private static ATCommand getKeepAliveCommand(Object value) {
		return new KeepAliveCommand();
	}

	private static ATCommand getLandCommand(Object value) {
		return new LandCommand();
	}

	private static ATCommand getLEDAnimationCommand(Object value) throws JSONException {
		ATCommand command = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			String animationString = parameters.getString("animation");
			float frequency = (float)parameters.getDouble("frequency");
			int animationDuration = parameters.getInt("duration");
			try
			{
				LEDAnimation animation = LEDAnimation.valueOf(animationString);
				command = new LEDAnimationCommand(animation, frequency, animationDuration);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		return command;
	}

	private static ATCommand getManualTrimCommand(Object value) throws JSONException {
		ATCommand command = null;
		if (value instanceof JSONObject) {
			JSONObject parameters = (JSONObject) value;
			float pitch = (float) parameters.getDouble("pitch");
			float roll = (float) parameters.getDouble("roll");
			float yaw = (float) parameters.getDouble("yaw");
			command = new ManualTrimCommand(pitch, roll, yaw);
		}
		return command;
	}

	private static ATCommand getMiscCommand(Object value) throws JSONException {
		ATCommand command = null;
		if (value instanceof JSONObject) {
			JSONObject parameters = (JSONObject) value;
			int p1 = parameters.getInt("p1");
			int p2 = parameters.getInt("p2");
			int p3 = parameters.getInt("p3");
			int p4 = parameters.getInt("p4");
			command = new MiscCommand(p1, p2, p3, p4);
		}
		return command;
	}

	private static ATCommand getMoveCommand(Object value) throws JSONException {
		ATCommand command = null;
		if (value instanceof JSONObject) {
			JSONObject parameters = (JSONObject) value;
			boolean combined_yaw_enabled = parameters.getBoolean("combined_yaw_enabled");
			float left_right_tilt = (float) parameters.getDouble("left_right_tilt");
			float front_back_tilt = (float) parameters.getDouble("front_back_tilt");
			float vertical_speed = (float) parameters.getDouble("vertical_speed");
			float angular_speed = (float) parameters.getDouble("angular_speed");
			command = new MoveCommand(combined_yaw_enabled, left_right_tilt, front_back_tilt, vertical_speed,
					angular_speed);
		}
		return command;
	}

	private static ATCommand getPCMDCommand(Object value) throws JSONException {
		ATCommand command = null;
		if (value instanceof JSONObject) {
			JSONObject parameters = (JSONObject) value;
			boolean hover = parameters.getBoolean("hover");
			boolean combined_yaw_enabled = parameters.getBoolean("combined_yaw_enabled");
			float left_right_tilt = (float) parameters.getDouble("left_right_tilt");
			float front_back_tilt = (float) parameters.getDouble("front_back_tilt");
			float vertical_speed = (float) parameters.getDouble("vertical_speed");
			float angular_speed = (float) parameters.getDouble("angular_speed");
			command = new PCMDCommand(hover, combined_yaw_enabled, left_right_tilt, front_back_tilt, vertical_speed,
					angular_speed);
		}
		return command;
	}

	private static ATCommand getPCMDMagCommand(Object value) throws JSONException {
		ATCommand command = null;
		if (value instanceof JSONObject) {
			JSONObject parameters = (JSONObject) value;
			boolean hover = parameters.getBoolean("hover");
			boolean combined_yaw_enabled = parameters.getBoolean("combined_yaw_enabled");
			boolean absolute_control = parameters.getBoolean("absolute_control");
			float left_right_tilt = (float) parameters.getDouble("left_right_tilt");
			float front_back_tilt = (float) parameters.getDouble("front_back_tilt");
			float vertical_speed = (float) parameters.getDouble("vertical_speed");
			float angular_speed = (float) parameters.getDouble("angular_speed");
			float magneto_psi = (float) parameters.getDouble("magneto_psi");
			float magneto_psi_accuracy = (float) parameters.getDouble("magneto_psi_accuracy");
			command = new PCMDMagCommand(hover, combined_yaw_enabled, absolute_control, left_right_tilt,
					front_back_tilt, vertical_speed, angular_speed, magneto_psi, magneto_psi_accuracy);
		}
		return command;
	}

	private static ATCommand getPlayAnimationCommand(Object value) throws JSONException {
		ATCommand command = null;
		if (value instanceof JSONObject) {
			JSONObject parameters = (JSONObject) value;
			int animation_no = parameters.getInt("animation_no");
			int animationDuration = parameters.getInt("duration");
			command = new PlayAnimationCommand(animation_no, animationDuration);
		}
		return command;
	}

	private static ATCommand getPMODECommand(Object value) throws JSONException {
		ATCommand command = null;
		if (value instanceof JSONObject) {
			JSONObject parameters = (JSONObject) value;
			int mode = parameters.getInt("mode");
			command = new PMODECommand(mode);
		}
		return command;
	}

	// private static ATCommand getQuitCommand(Object value) {
	// return new QuitCommand();
	// }

	private static ATCommand getRawCaptureCommand(Object value) throws JSONException {
		ATCommand command = null;
		if (value instanceof JSONObject) {
			JSONObject parameters = (JSONObject) value;
			boolean picture = parameters.getBoolean("picture");
			boolean video = parameters.getBoolean("video");
			command = new RawCaptureCommand(picture, video);
		}
		return command;
	}

	private static ATCommand getStopCommand(Object value) {
		return new FreezeCommand();
	}

	private static ATCommand getTakeOffCommand(Object value) {
		return new TakeOffCommand();
	}

	private static ATCommand getVideoChannelCommand(Object value) throws JSONException {
		ATCommand command = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			String videoChannelString = parameters.getString("video_channel");
			VideoChannel videoChannel = null;
			try
			{
				videoChannel = VideoChannel.valueOf(videoChannelString);
				command = new VideoChannelCommand(videoChannel);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		return command;
	}

	private static ATCommand getVisionOptionCommand(Object value) throws JSONException {
		ATCommand command = null;
		if (value instanceof JSONObject) {
			JSONObject parameters = (JSONObject) value;
			int option = parameters.getInt("option");
			command = new VisionOptionCommand(option);
		}
		return command;
	}

	private static ATCommand getVisionParametersCommand(Object value) throws JSONException {
		ATCommand command = null;
		if (value instanceof JSONObject) {
			JSONObject parameters = (JSONObject) value;
			int coarse_scale = parameters.getInt("coarse_scale");
			int nb_pair = parameters.getInt("nb_pair");
			int loss_per = parameters.getInt("loss_per");
			int nb_tracker_width = parameters.getInt("nb_tracker_width");
			int nb_tracker_height = parameters.getInt("nb_tracker_height");
			int scale = parameters.getInt("scale");
			int trans_max = parameters.getInt("trans_max");
			int max_pair_dist = parameters.getInt("max_pair_dist");
			int noise = parameters.getInt("noise");
			command = new VisionParametersCommand(coarse_scale, nb_pair, loss_per, nb_tracker_width, nb_tracker_height,
					scale, trans_max, max_pair_dist, noise);
		}
		return command;
	}
}
