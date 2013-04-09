package de.yadrone.android;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonFlightPlanParser {

	public List<DroneSchedulingCommand> getFlightPlan(String jsonFlightPlan) {
		return getDroneCommands(jsonFlightPlan);
//		return getDroneCommands(stubJson());
//		return stubDroneCommandList();
	}

	private List<DroneSchedulingCommand> getDroneCommands(String jsonFlightPlan) {
		ArrayList<DroneSchedulingCommand> droneCommands = new ArrayList<DroneSchedulingCommand>();
		try {
			JSONObject root = (JSONObject) new JSONTokener(jsonFlightPlan).nextValue();
			JSONArray flightplan = root.getJSONArray("FlightPlan");
			for (int i = 0; i < flightplan.length(); i++) {
				JSONObject jsonObject = flightplan.optJSONObject(i);
				if (jsonObject != null) {
					Iterator<String> keys = jsonObject.keys();
					while(keys.hasNext())
					{
						String key = keys.next();
						if (!key.equals("Duration") && !key.equals("Repetitions"))
						{
							DroneSchedulingCommand command = DroneSchedulingCommandFactory.getDroneSchedulingCommand(key, jsonObject);
							if (command != null) {
								droneCommands.add(command);
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return droneCommands;
	}

//	private DroneCommand getMoveCommand(JSONObject jsonCommand) throws JSONException {
//		double x = jsonCommand.getDouble("x");
//		double y = jsonCommand.getDouble("y");
//		double z = jsonCommand.getDouble("z");
//		double r = jsonCommand.getDouble("r");
//		double t = jsonCommand.getDouble("t");
//		return new DroneCommandMove((float) x, (float) y, (float) z, (float) r, (float) t);
//
//	}

	private String stubJson() {
		String json = 
		"{" +
		"	\"FlightPlan\" :" +
		"	[" +
		"		{" +
		"			\"Configure\" :" +
		"			{" + 
		"				\"control:control_level\" : 1" +
		"			}" +
		"		}," +
		"		{" +
		"			\"Configure\" :" +
		"			{" +
		"				\"control:outdoor\" : true" +
		"			}" +
		"		}," +
		"		{" +
		"			\"Configure\" :" +
		"			{" +
		"				\"gps:latitude\" : 12.3" +
		"			}" +
		"		}," +
		"		{" +
		"			\"Configure\" :" +
		"			{" +
		"				\"network:ssid_single_player\" : \"ab:cd:ef:gh\"" +
		"			}" +
		"		}," +
		"		{" +
		"			\"FlightAnimation\" :" +
		"			{" +
		"               \"animation\" : \"FLIP_LEFT\"," +
		"               \"duration\" : 10" +
		"			}" +
		"		}," +
		"		{" +
		"			\"LEDAnimation\" :" +
		"			{" +
		"	              \"animation\" : \"BLINK_GREEN_RED\"," +
		"				\"frequency\" : 34.5," +
		"               \"duration\" : 10" +
		"			}" +
		"		}," +
		"		{" +
		"			\"VideoChannel\" :" +
		"			{" +
		"               \"video_channel\" : \"HORI\"" +
		"			}" +
		"		}," +
		"		{" +
		"			\"Control\" :" +
		"			{" +
		"               \"arg1\" : 1," +
		"			   \"arg2\" : 2" +
		"			}" +
		"		}," +
		"		{" +
		"			\"Emergency\" : \"\"" +
		"		}," +
		"		{" +
		"			\"FlatTrim\" : \"\"" +
		"		}," +
		"		{" +
		"			\"Gains\" :" +
		"			{" +
		"               \"pq_kp\" : 1," +
		"			    \"r_kp\" : 2," +
		"               \"r_ki\" : 3," +
		"               \"ea_kp\" : 4," +
		"               \"ea_ki\" : 5," +
		"               \"alt_kp\" : 6," +
		"               \"alt_ki\" : 7," +
		"               \"vz_kp\" : 8," +
		"               \"vz_ki\" : 9," +
		"               \"hovering_kp\" : 10," +
		"               \"hovering_ki\" : 11," +
		"			    \"hovering_b_kp\" : 12," +
		"			    \"hovering_b_ki\" : 13" +
		"			}" +
		"		}," +
		"		{" +
		"			\"ManualTrim\" :" +
		"			{" +
		"               \"pitch\" : 1.2," +
		"			    \"roll\" : 2.3," +
		"                \"yaw\" : 3.4" +
		"			}" +
		"		}," +
		"		{" +
		"			\"Misc\" :" +
		"			{" +
		"               \"p1\" : 1," +
		"			    \"p2\" : 2," +
		"			    \"p3\" : 3," +
		"               \"p4\" : 4" +
		"			}" +
		"		}," +
		"		{" +
		"			\"PCMD\" :" +
		"			{" +
		"               \"hover\" : true," +
		"			    \"combined_yaw_enabled\" : false," +
		"			    \"left_right_tilt\" : 3.4," +
		"               \"front_back_tilt\" : 4.5," +
		"			    \"vertical_speed\" : 5.6," +
		"			    \"angular_speed\" : 6.7" +
		"			}" +
		"		}," +
		"		{" +
		"			\"Hover\" : \"\"" +
		"		}," +
		"		{" +
		"			\"KeepAlive\" : \"\"" +
		"		}," +
		"		{" +
		"			\"Land\" : \"\"" +
		"		}," +
		"		{" +
		"			\"Move\" :" +
		"			{" +
		"			   	\"combined_yaw_enabled\" : false," +
		"			   	\"left_right_tilt\" : 3.4," +
		"               \"front_back_tilt\" : 4.5," +
		"			   	\"vertical_speed\" : 5.6," +
		"			   	\"angular_speed\" : 6.7" +
		"			}" +
		"		}," +
		"		{" +
		"			\"PCMDMag\" :" +
		"			{" +
		"               \"hover\" : true," +
		"			   \"combined_yaw_enabled\" : false," +
		"			   \"absolute_control\" : true," +
		"			   \"left_right_tilt\" : 3.4," +
		"               \"front_back_tilt\" : 4.5," +
		"			   \"vertical_speed\" : 5.6," +
		"			   \"angular_speed\" : 6.7," +
		"			   \"magneto_psi\" : 7.8," +
		"			   \"magneto_psi_accuracy\" : 8.9" + 
		"			}" +
		"		}," +
		"		{" +
		"			\"PlayAnimation\" :" +
		"			{" +
		"               \"animation_no\" : 12," +
		"			   	\"duration\" : 13" +
		"			}" +
		"		}," +
		"		{" +
		"			\"PMODE\" :" +
		"			{" +
		"               \"mode\" : 14" +
		"			}" +
		"		}," +
		"		{" +
		"			\"RawCapture\" :" +
		"			{" +
		"				\"picture\" : true," +
		"				\"video\" : false" +
		"			}" +
		"		}," +
		"		{" +
		"			\"Stop\" : \"\"" +
		"		}," +
		"		{" +
		"			\"TakeOff\" : \"\"" +
		"		}," +
		"		{" +
		"			\"VisionOption\" :" +
		"			{" +
		"				\"option\" : 15" +
		"			}" +
		"		}," +
		"		{" +
		"			\"VisionParameters\" :" +
		"			{" +
		"				\"coarse_scale\" : 16," +
		"				\"nb_pair\" : 17," +
		"				\"loss_per\" : 18," +
		"				\"nb_tracker_width\" : 19," +
		"				\"nb_tracker_height\" : 20," +
		"				\"scale\" : 21," +
		"				\"trans_max\" : 22," +
		"				\"max_pair_dist\" : 23," +
		"				\"noise\" : 24" +
		"			}" +
		"		}," +
		"		{" +
		"			\"Quit\" : \"\"" +
		"		}" +
		"	]" +
		"}";
		return json;
	}

//	private List<MyDroneCommand> stubDroneCommandList() {
//		List<MyDroneCommand> flightPlan = new ArrayList<MyDroneCommand>();
//
//		flightPlan.add(new MyDroneCommandMove(1, 2, 3, 4, 5));
//		flightPlan.add(new MyDroneCommandMove(11, 12, 13, 14, 15));
//		flightPlan.add(new MyDroneCommandMove(21, 22, 23, 24, 25));
//		return flightPlan;
//	}

}
