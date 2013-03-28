package com.example.jsonreading;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonFlightPlanParser {

	public List<DroneCommand> getFlightPlan(String jsonFlightPlan) {
		return getDroneCommands(jsonFlightPlan);
//		return getDroneCommands(stubJson());
//		return stubDroneCommandList();
	}

	private List<DroneCommand> getDroneCommands(String jsonFlightPlan) {
		ArrayList<DroneCommand> droneCommands = new ArrayList<DroneCommand>();
		try {
			JSONObject root = (JSONObject) new JSONTokener(jsonFlightPlan).nextValue();
			JSONArray flightplan = root.getJSONArray("flightplan");
			for (int i = 0; i < flightplan.length(); i++) {
				JSONObject command = flightplan.optJSONObject(i);
				if (command != null) {
					DroneCommand droneCommand = null;
					JSONObject moveCommand = command.optJSONObject("move");
					if (moveCommand != null) {
						droneCommand = getMoveCommand(moveCommand);
					}

					if (droneCommand != null) {
						droneCommands.add(droneCommand);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return droneCommands;
	}

	private DroneCommand getMoveCommand(JSONObject jsonCommand) throws JSONException {
		double x = jsonCommand.getDouble("x");
		double y = jsonCommand.getDouble("y");
		double z = jsonCommand.getDouble("z");
		double r = jsonCommand.getDouble("r");
		double t = jsonCommand.getDouble("t");
		return new DroneCommandMove((float) x, (float) y, (float) z, (float) r, (float) t);

	}

	private String stubJson() {
		String json = 
				"{" + "" +
				"    \"commands\" : [" +
				"        {" +
				"            \"move\" : " +
				"            {"	+
				"                \"x\" : 1," +
				"                \"y\" : 2," +
				"                \"z\" : 3," +
				"                \"r\" : 4," +
				"                \"t\" : 5" +
				"            }" +
				"        }" +
				"    ]" +
				"}";
		return json;
	}

	private List<DroneCommand> stubDroneCommandList() {
		List<DroneCommand> flightPlan = new ArrayList<DroneCommand>();

		flightPlan.add(new DroneCommandMove(1, 2, 3, 4, 5));
		flightPlan.add(new DroneCommandMove(11, 12, 13, 14, 15));
		flightPlan.add(new DroneCommandMove(21, 22, 23, 24, 25));
		return flightPlan;
	}

}
