package de.yadrone.android;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.shigeodayo.ardrone.command.ConfigureCommand;
import com.shigeodayo.ardrone.command.ControlCommand;
import com.shigeodayo.ardrone.command.DroneCommand;
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
import com.shigeodayo.ardrone.command.QuitCommand;
import com.shigeodayo.ardrone.command.RawCaptureCommand;
import com.shigeodayo.ardrone.command.StopCommand;
import com.shigeodayo.ardrone.command.TakeOffCommand;
import com.shigeodayo.ardrone.command.UserBox;
import com.shigeodayo.ardrone.command.VideoChannel;
import com.shigeodayo.ardrone.command.VideoChannelCommand;
import com.shigeodayo.ardrone.command.VisionOptionCommand;
import com.shigeodayo.ardrone.command.VisionParametersCommand;

public class DroneSchedulingCommandFactory {
	static DroneSchedulingCommand getDroneSchedulingCommand(String key, Object value)
	{
		DroneSchedulingCommand command = null;
		try {
	
			if (key.equals("Configure"))
			{
				command = getConfigureCommand(value);
			}
			else if (key.equals("FlightAnimation"))
			{
				command = getFlightAnimationCommand(value);
			}
//			else if (key.equals("LEDAnimation"))
//			{
//				command = getLEDAnimationCommand(value);
//			}
//			else if (key.equals("VideoChannel"))
//			{
//				command = getVideoChannelCommand(value);
//			}
//			else if (key.equals("Control"))
//			{
//				command = getControlCommand(value);
//			}
			else if (key.equals("FlatTrim"))
			{
				command = getFlatTrimCommand(value);
			}
			else if (key.equals("Gains"))
			{
				command = getGainsCommand(value);
			}
//			else if (key.equals("KeepAlive"))
//			{
//				command = new KeepAliveCommand();
//			}
			else if (key.equals("ManualTrim"))
			{
				command = getManualTrimCommand(value);
			}
			else if (key.equals("Misc"))
			{
				command = getMiscCommand(value);
			}
			else if (key.equals("PCMD"))
			{
				command = getPCMDCommand(value);
			}
//			else if (key.equals("Hover"))
//			{
//				command = new HoverCommand();
//			}
			else if (key.equals("Move"))
			{
				command = getMoveCommand(value);
			}
			else if (key.equals("PCMDMag"))
			{
				command = getPCMDMagCommand(value);
			}
//			else if (key.equals("Stop"))
//			{
//				command = new StopCommand();
//			}
			else if (key.equals("PlayAnimation"))
			{
				command = getPlayAnimationCommand(value);
			}
			else if (key.equals("PMODE"))
			{
				command = getPMODECommand(value);
			}
			else if (key.equals("RawCapture"))
			{
				command = getRawCaptureCommand(value);
			}
//			else if (key.equals("Emergency"))
//			{
//				command = new EmergencyCommand();
//			}
//			else if (key.equals("Land"))
//			{
//				command = new LandCommand();
//			}
//			else if (key.equals("TakeOff"))
//			{
//				command = new TakeOffCommand();
//			}
			else if (key.equals("VisionOption"))
			{
				command = getVisionOptionCommand(value);
			}
			else if (key.equals("VisionParameters"))
			{
				command = getVisionParametersCommand(value);
			}
//			else if (key.equals("Quit"))
//			{
//				command = new QuitCommand();
//			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return command;
	}

	private static DroneSchedulingCommand getConfigureCommand(Object value) throws JSONException {
		DroneSchedulingCommand schedulingCommand = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			List<DroneCommand> commands = new ArrayList<DroneCommand>();
			// TODO: introduce CompositeCommand
			// Temporarily the output will be 1 DroneCommand (the last one read from the json description)
			// Later we will probably have a CompositeCommand that contains multiple DroneCommands, and
			// then we will add all DroneCommands from the json description.
			// So for the time being, restrict to one configure command in a json configure description 
			Iterator<String> keys = parameters.keys();
			int duration = 0;
			int repetitions = 1;
			while (keys.hasNext())
			{
				String configKey = keys.next();
				if (configKey.equals("control:control_level") ||
						configKey.equals("control:flying_mode") ||
						configKey.equals("control:hovering_range") ||
						configKey.equals("control:altitude_max") ||
						configKey.equals("control:altitude_min") ||
						configKey.equals("control:control_vz_max") ||
						configKey.equals("control:control_yaw") ||
						configKey.equals("detect:enemy_colors") ||
						configKey.equals("detect:detections_select_v") ||
						configKey.equals("detect:detections_select_h") ||
						configKey.equals("detect:detections_select_v_hsync") ||
						configKey.equals("pic:ultrasound_freq") ||
						configKey.equals("network:wifi_mode")) {
					int configValue = parameters.getInt(configKey); 
					commands.add(new ConfigureCommand(configKey, configValue));
				} 
				else if (configKey.equals("control:outdoor") ||
						configKey.equals("control:flight_without_shell") ||
						configKey.equals("control:autonomous_flight")) {
					boolean configValue = parameters.getBoolean(configKey);
					commands.add(new ConfigureCommand(configKey, configValue));
				}
				else if (configKey.equals("gps:latitude") ||
						configKey.equals("gps:longitude") ||
						configKey.equals("gps:altitude")) {
					double configValue = parameters.getDouble(configKey);
					commands.add(new ConfigureCommand(configKey, configValue));
				}
				else if (configKey.equals("network:ssid_single_player") ||
						configKey.equals("network:ssid_multi_player") ||
						configKey.equals("network:owner_mac") ||
						configKey.equals("control:control_iphone_tilt") ||	// Float as string!
						configKey.equals("control:euler_angle_max")) {		// Float as string!
					String configValue = parameters.getString(configKey);
					commands.add(new ConfigureCommand(configKey, configValue));
				}
				else if (configKey.equals("Duration"))
				{
					duration = parameters.getInt(configKey);
				}
				else if (configKey.equals("Repetitions"))
				{
					repetitions = parameters.getInt(configKey);
				}
			}
			schedulingCommand = new DroneSchedulingCommand(commands, duration, repetitions);
		}
		// TODO: add support for video recording
		//SLA EVEN OVER "userbox:userbox_cmd", String.valueOf(UserBox.START.ordinal()) + "," + dirname)
		//SLA EVEN OVER "userbox:userbox_cmd", UserBox.CANCEL.ordinal()
		//SLA EVEN OVER "userbox:userbox_cmd", UserBox.STOP.ordinal()
		//SLA EVEN OVER "userbox:userbox_cmd", String.valueOf(UserBox.SCREENSHOT.ordinal()) + ","
		//              + String.valueOf(delay) + "," + String.valueOf(nshots) + "," + dirname
		
		return schedulingCommand;
	}
	
	private static DroneSchedulingCommand getFlightAnimationCommand(Object value) 
			throws JSONException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		DroneSchedulingCommand schedulingCommand = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			List<DroneCommand> commands = new ArrayList<DroneCommand>();
			String animation = parameters.getString("animation");
			int animationDuration = parameters.getInt("duration");
			FlightAnimation anim = null;
			// TODO: can this be done in a more easy way?
			if (animation.equals("PHI_M30_DEG"))
			{
				anim = FlightAnimation.PHI_M30_DEG;
			}
			else if (animation.equals("PHI_30_DEG"))
			{
				anim = FlightAnimation.PHI_M30_DEG;
			}
			else if (animation.equals("THETA_M30_DEG"))
			{
				anim = FlightAnimation.THETA_M30_DEG;
			}
			else if (animation.equals("THETA_30_DEG"))
			{
				anim = FlightAnimation.THETA_30_DEG;
			}
			else if (animation.equals("THETA_20DEG_YAW_200DEG"))
			{
				anim = FlightAnimation.THETA_20DEG_YAW_200DEG;
			}
			else if (animation.equals("THETA_20DEG_YAW_M200DEG"))
			{
				anim = FlightAnimation.THETA_20DEG_YAW_M200DEG;
			}
			else if (animation.equals("TURNAROUND"))
			{
				anim = FlightAnimation.TURNAROUND;
			}
			else if (animation.equals("TURNAROUND_GODOWN"))
			{
				anim = FlightAnimation.TURNAROUND_GODOWN;
			}
			else if (animation.equals("YAW_SHAKE"))
			{
				anim = FlightAnimation.YAW_SHAKE;
			}
			else if (animation.equals("YAW_DANCE"))
			{
				anim = FlightAnimation.YAW_DANCE;
			}
			else if (animation.equals("PHI_DANCE"))
			{
				anim = FlightAnimation.PHI_DANCE;
			}
			else if (animation.equals("THETA_DANCE"))
			{
				anim = FlightAnimation.THETA_DANCE;
			}
			else if (animation.equals("VZ_DANCE"))
			{
				anim = FlightAnimation.VZ_DANCE;
			}
			else if (animation.equals("WAVE"))
			{
				anim = FlightAnimation.WAVE;
			}
			else if (animation.equals("PHI_THETA_MIXED"))
			{
				anim = FlightAnimation.PHI_THETA_MIXED;
			}
			else if (animation.equals("DOUBLE_PHI_THETA_MIXED"))
			{
				anim = FlightAnimation.DOUBLE_PHI_THETA_MIXED;
			}
			else if (animation.equals("FLIP_AHEAD"))
			{
				anim = FlightAnimation.FLIP_AHEAD;
			}
			else if (animation.equals("FLIP_BEHIND"))
			{
				anim = FlightAnimation.FLIP_BEHIND;
			}
			else if (animation.equals("FLIP_LEFT"))
			{
				anim = FlightAnimation.FLIP_LEFT;
			}
			else if (animation.equals("FLIP_RIGHT"))
			{
				anim = FlightAnimation.FLIP_RIGHT;
			}
			int duration = parameters.optInt("Duration", 0);
			int repetitions = parameters.optInt("Repetitions", 1);
			if  (anim != null)
			{
				commands.add(new FlightAnimationCommand(anim, animationDuration));
				schedulingCommand = new DroneSchedulingCommand(commands, duration, repetitions);
			}
		}
		return schedulingCommand;
	}

	private static DroneCommand getLEDAnimationCommand(Object value) throws JSONException {
		DroneCommand command = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			String animation = parameters.getString("animation");
			float frequency = (float)parameters.getDouble("frequency");
			int duration = parameters.getInt("duration");
			LEDAnimation anim = null;
			// TODO: can this be done in a more easy way?
			if (animation.equals("BLINK_GREEN_RED"))
			{
				anim = LEDAnimation.BLINK_GREEN_RED;
			}
			else if (animation.equals("BLINK_GREEN"))
			{
				anim = LEDAnimation.BLINK_GREEN;
			}
			else if (animation.equals("BLINK_RED"))
			{
				anim = LEDAnimation.BLINK_RED;
			}
			else if (animation.equals("BLINK_ORANGE"))
			{
				anim = LEDAnimation.BLINK_ORANGE;
			}
			else if (animation.equals("SNAKE_GREEN_RED"))
			{
				anim = LEDAnimation.SNAKE_GREEN_RED;
			}
			else if (animation.equals("FIRE"))
			{
				anim = LEDAnimation.FIRE;
			}
			else if (animation.equals("STANDARD"))
			{
				anim = LEDAnimation.STANDARD;
			}
			else if (animation.equals("RED"))
			{
				anim = LEDAnimation.RED;
			}
			else if (animation.equals("GREEN"))
			{
				anim = LEDAnimation.GREEN;
			}
			else if (animation.equals("RED_SNAKE"))
			{
				anim = LEDAnimation.RED_SNAKE;
			}
			else if (animation.equals("BLANK"))
			{
				anim = LEDAnimation.BLANK;
			}
			else if (animation.equals("RIGHT_MISSILE"))
			{
				anim = LEDAnimation.RIGHT_MISSILE;
			}
			else if (animation.equals("LEFT_MISSILE"))
			{
				anim = LEDAnimation.LEFT_MISSILE;
			}
			else if (animation.equals("DOUBLE_MISSILE"))
			{
				anim = LEDAnimation.DOUBLE_MISSILE;
			}
			else if (animation.equals("FRONT_LEFT_GREEN_OTHERS_RED"))
			{
				anim = LEDAnimation.FRONT_LEFT_GREEN_OTHERS_RED;
			}
			else if (animation.equals("FRONT_RIGHT_GREEN_OTHERS_RED"))
			{
				anim = LEDAnimation.FRONT_RIGHT_GREEN_OTHERS_RED;
			}
			else if (animation.equals("REAR_RIGHT_GREEN_OTHERS_RED"))
			{
				anim = LEDAnimation.REAR_RIGHT_GREEN_OTHERS_RED;
			}
			else if (animation.equals("REAR_LEFT_GREEN_OTHERS_RED"))
			{
				anim = LEDAnimation.REAR_LEFT_GREEN_OTHERS_RED;
			}
			else if (animation.equals("LEFT_GREEN_RIGHT_RED"))
			{
				anim = LEDAnimation.LEFT_GREEN_RIGHT_RED;
			}
			else if (animation.equals("LEFT_RED_RIGHT_GREEN"))
			{
				anim = LEDAnimation.LEFT_RED_RIGHT_GREEN;
			}
			else if (animation.equals("BLINK_STANDARD"))
			{
				anim = LEDAnimation.BLINK_STANDARD;
			}
			if  (anim != null)
			{
				command = new LEDAnimationCommand(anim, frequency, duration);
			}
		}
		return command;
	}

	private static DroneCommand getVideoChannelCommand(Object value) throws JSONException {
		DroneCommand command = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			String channel = parameters.getString("video_channel");
			VideoChannel videoChannel = null;
			// TODO: can this be done in a more easy way?
			if (channel.equals("HORI"))
			{
				videoChannel = VideoChannel.HORI;
			}
			else if (channel.equals("VERT"))
			{
				videoChannel = VideoChannel.VERT;
			}
			else if (channel.equals("LARGE_HORI_SMALL_VERT"))
			{
				videoChannel = VideoChannel.LARGE_HORI_SMALL_VERT;
			}
			else if (channel.equals("LARGE_VERT_SMALL_HORI"))
			{
				videoChannel = VideoChannel.LARGE_VERT_SMALL_HORI;
			}
			else if (channel.equals("NEXT"))
			{
				videoChannel = VideoChannel.HORI;
			}
			if (channel != null)
			{
				command = new VideoChannelCommand(videoChannel);
			}
		}
		return command;
	}

	private static DroneCommand getControlCommand(Object value) throws JSONException {
		DroneCommand command = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			int arg1 = parameters.getInt("arg1");
			int arg2 = parameters.getInt("arg2");
			command = new ControlCommand(arg1, arg2);
		}
		return command;
	}

	private static DroneSchedulingCommand getFlatTrimCommand(Object value) {
		DroneSchedulingCommand schedulingCommand = null;
		if (value.equals(""))
		{
			List<DroneCommand> commands = new ArrayList<DroneCommand>();
			commands.add(new FlatTrimCommand());
			schedulingCommand = new DroneSchedulingCommand(commands, 0, 1);
		}
		return schedulingCommand;
	}

	private static DroneSchedulingCommand getGainsCommand(Object value) throws JSONException {
		DroneSchedulingCommand schedulingCommand = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
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
			int duration = parameters.optInt("Duration", 0);
			int repetitions = parameters.optInt("Repetitions", 1);
			List<DroneCommand> commands = new ArrayList<DroneCommand>();
			commands.add(new GainsCommand(pq_kp, r_kp, r_ki, ea_kp, ea_ki, alt_kp, alt_ki,
					vz_kp, vz_ki, hovering_kp, hovering_ki, hovering_b_kp, hovering_b_ki));
			schedulingCommand = new DroneSchedulingCommand(commands, duration, repetitions);
		}
		return schedulingCommand;
	}

	private static DroneSchedulingCommand getManualTrimCommand(Object value) throws JSONException {
		DroneSchedulingCommand schedulingCommand = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			float pitch = (float) parameters.getDouble("pitch");
			float roll = (float) parameters.getDouble("roll");
			float yaw = (float) parameters.getDouble("yaw");
			int duration = parameters.optInt("Duration", 0);
			int repetitions = parameters.optInt("Repetitions", 1);
			List<DroneCommand> commands = new ArrayList<DroneCommand>();
			commands.add(new ManualTrimCommand(pitch, roll, yaw)); 
			schedulingCommand = new DroneSchedulingCommand(commands, duration, repetitions);
		}
		return schedulingCommand;
	}

	private static DroneSchedulingCommand getMiscCommand(Object value) throws JSONException {
		DroneSchedulingCommand schedulingCommand = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			int p1 = parameters.getInt("p1");
			int p2 = parameters.getInt("p2");
			int p3 = parameters.getInt("p3");
			int p4 = parameters.getInt("p4");
			int duration = parameters.optInt("Duration", 0);
			int repetitions = parameters.optInt("Repetitions", 1);
			List<DroneCommand> commands = new ArrayList<DroneCommand>();
			commands.add(new MiscCommand(p1, p2, p3, p4)); 
			schedulingCommand = new DroneSchedulingCommand(commands, duration, repetitions);
		}
		return schedulingCommand;
	}

	private static DroneSchedulingCommand getPCMDCommand(Object value) throws JSONException {
		DroneSchedulingCommand schedulingCommand = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			boolean hover = parameters.getBoolean("hover");
			boolean combined_yaw_enabled = parameters.getBoolean("combined_yaw_enabled");
			float left_right_tilt = (float)parameters.getDouble("left_right_tilt");
			float front_back_tilt = (float)parameters.getDouble("front_back_tilt");
			float vertical_speed = (float)parameters.getDouble("vertical_speed");
			float angular_speed = (float)parameters.getDouble("angular_speed");
			int duration = parameters.optInt("Duration", 0);
			int repetitions = parameters.optInt("Repetitions", 1);
			List<DroneCommand> commands = new ArrayList<DroneCommand>();
			commands.add(new PCMDCommand(hover, combined_yaw_enabled, 
					left_right_tilt, front_back_tilt, vertical_speed, angular_speed)); 
			schedulingCommand = new DroneSchedulingCommand(commands, duration, repetitions);
		}
		return schedulingCommand;
	}

	private static DroneSchedulingCommand getMoveCommand(Object value) throws JSONException {
		DroneSchedulingCommand schedulingCommand = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			boolean combined_yaw_enabled = parameters.getBoolean("combined_yaw_enabled");
			float left_right_tilt = (float)parameters.getDouble("left_right_tilt");
			float front_back_tilt = (float)parameters.getDouble("front_back_tilt");
			float vertical_speed = (float)parameters.getDouble("vertical_speed");
			float angular_speed = (float)parameters.getDouble("angular_speed");
			int duration = parameters.optInt("Duration", 0);
			int repetitions = parameters.optInt("Repetitions", 1);
			List<DroneCommand> commands = new ArrayList<DroneCommand>();
			commands.add(new MoveCommand(combined_yaw_enabled, 
					left_right_tilt, front_back_tilt, vertical_speed, angular_speed)); 
			schedulingCommand = new DroneSchedulingCommand(commands, duration, repetitions);
		}
		return schedulingCommand;
	}

	private static DroneSchedulingCommand getPCMDMagCommand(Object value) throws JSONException {
		DroneSchedulingCommand schedulingCommand = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			boolean hover = parameters.getBoolean("hover");
			boolean combined_yaw_enabled = parameters.getBoolean("combined_yaw_enabled");
			boolean absolute_control = parameters.getBoolean("absolute_control");
			float left_right_tilt = (float)parameters.getDouble("left_right_tilt");
			float front_back_tilt = (float)parameters.getDouble("front_back_tilt");
			float vertical_speed = (float)parameters.getDouble("vertical_speed");
			float angular_speed = (float)parameters.getDouble("angular_speed");
			float magneto_psi = (float)parameters.getDouble("magneto_psi");
			float magneto_psi_accuracy = (float)parameters.getDouble("magneto_psi_accuracy");
			int duration = parameters.optInt("Duration", 0);
			int repetitions = parameters.optInt("Repetitions", 1);
			List<DroneCommand> commands = new ArrayList<DroneCommand>();
			commands.add(new PCMDMagCommand(hover, combined_yaw_enabled, absolute_control, 
					left_right_tilt, front_back_tilt, vertical_speed, angular_speed, 
					magneto_psi, magneto_psi_accuracy));
			schedulingCommand = new DroneSchedulingCommand(commands, duration, repetitions);
		}
		return schedulingCommand;
	}

	private static DroneSchedulingCommand getPlayAnimationCommand(Object value) throws JSONException {
		DroneSchedulingCommand schedulingCommand = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			int animation_no = parameters.getInt("animation_no");
			int animationDuration = parameters.getInt("duration");
			int duration = parameters.optInt("Duration", 0);
			int repetitions = parameters.optInt("Repetitions", 1);
			List<DroneCommand> commands = new ArrayList<DroneCommand>();
			commands.add(new PlayAnimationCommand(animation_no, animationDuration));
			schedulingCommand = new DroneSchedulingCommand(commands, duration, repetitions);
		}
		return schedulingCommand;
	}

	private static DroneSchedulingCommand getPMODECommand(Object value) throws JSONException {
		DroneSchedulingCommand schedulingCommand = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			int mode = parameters.getInt("mode");
			int duration = parameters.optInt("Duration", 0);
			int repetitions = parameters.optInt("Repetitions", 1);
			List<DroneCommand> commands = new ArrayList<DroneCommand>();
			commands.add(new PMODECommand(mode));
			schedulingCommand = new DroneSchedulingCommand(commands, duration, repetitions);
		}
		return schedulingCommand;
	}

	private static DroneSchedulingCommand getRawCaptureCommand(Object value) throws JSONException {
		DroneSchedulingCommand schedulingCommand = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			boolean picture = parameters.getBoolean("picture");
			boolean video = parameters.getBoolean("video");
			int duration = parameters.optInt("Duration", 0);
			int repetitions = parameters.optInt("Repetitions", 1);
			List<DroneCommand> commands = new ArrayList<DroneCommand>();
			commands.add(new RawCaptureCommand(picture, video));
			schedulingCommand = new DroneSchedulingCommand(commands, duration, repetitions);
		}
		return schedulingCommand;
	}

	private static DroneSchedulingCommand getVisionOptionCommand(Object value) throws JSONException {
		DroneSchedulingCommand schedulingCommand = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			int option = parameters.getInt("option");
			int duration = parameters.optInt("Duration", 0);
			int repetitions = parameters.optInt("Repetitions", 1);
			List<DroneCommand> commands = new ArrayList<DroneCommand>();
			commands.add(new VisionOptionCommand(option));
			schedulingCommand = new DroneSchedulingCommand(commands, duration, repetitions);
		}
		return schedulingCommand;
	}

	private static DroneSchedulingCommand getVisionParametersCommand(Object value) throws JSONException {
		DroneSchedulingCommand schedulingCommand = null;
		if (value instanceof JSONObject)
		{
			JSONObject parameters = (JSONObject)value;
			int coarse_scale = parameters.getInt("coarse_scale");
			int nb_pair = parameters.getInt("nb_pair");
			int loss_per = parameters.getInt("loss_per");
			int nb_tracker_width = parameters.getInt("nb_tracker_width");
			int nb_tracker_height = parameters.getInt("nb_tracker_height");
			int scale = parameters.getInt("scale");
			int trans_max = parameters.getInt("trans_max");
			int max_pair_dist = parameters.getInt("max_pair_dist");
			int noise = parameters.getInt("noise");
			
			int duration = parameters.optInt("Duration", 0);
			int repetitions = parameters.optInt("Repetitions", 1);
			List<DroneCommand> commands = new ArrayList<DroneCommand>();
			commands.add(new VisionParametersCommand(coarse_scale, nb_pair, loss_per, nb_tracker_width,
					nb_tracker_height, scale, trans_max, max_pair_dist, noise));
			schedulingCommand = new DroneSchedulingCommand(commands, duration, repetitions);
		}
		return schedulingCommand;
	}
}
