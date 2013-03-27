package de.yadrone.android;

//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
import java.util.List;
//import java.util.ListIterator;

//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.json.JSONTokener;

import com.shigeodayo.ardrone.ARDrone;

import android.content.Intent;
import android.os.Bundle;

public class FlightPlanProgressActivity extends BaseActivity {

	private String mFlightPlanUri;
	private DroneCommandScheduler mScheduler;
	private List<DroneCommand> mDroneCommands;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flightplan_progress);

		// Get the flight plan from the intent
		Intent intent = getIntent();
		mFlightPlanUri = intent.getStringExtra(FlightPlanActivity.FLIGHTPLAN_URI);

    	YADroneApplication app = (YADroneApplication)getApplication();
    	final ARDrone drone = app.getARDrone();
		mScheduler = new DroneCommandScheduler(drone);
		
		LoadFlightPlan();
		FlyRoute();
	}

    private void LoadFlightPlan() {
    	mDroneCommands = new ArrayList<DroneCommand>();
    	mDroneCommands.add(new DroneCommandMove(mScheduler, 1000,0,0,0,5000));
    	mDroneCommands.add(new DroneCommandMove(mScheduler, 1000,0,0,90,1000));
    	mDroneCommands.add(new DroneCommandMove(mScheduler, 1000,1000,0, 90, 5000));
    }

	private void FlyRoute() {
    	YADroneApplication app = (YADroneApplication)getApplication();
    	final ARDrone drone = app.getARDrone();
    	drone.takeOff();

    	for (DroneCommand command : mDroneCommands) {
			command.execute();
		}
    	
    	drone.landing();
	}

//    private void LoadFlightPlan() {
//		String json = "{" +
//				"\"commands\" : " +
//				"[" +
//				"{" +
//				"\"command\" : \"move\"" +
//				"\"x\" : 100" +
//				"\"y\" : 200" +
//				"\"z\" : 300" +
//				"}" +
//				"]" +
//				"}";
//		try {
//			ArrayList<DroneCommand> droneCommands = new ArrayList<DroneCommand>();
//			JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
//			JSONArray commands = object.getJSONArray("commands");
//			int l = commands.length();
//			
//			for (int i = 0; i < commands.length(); i++)
//			{
//				JSONObject command = commands.optJSONObject(i);
//				if (command != null)
//				{
//					String cmd = command.getString("command");
//					if (cmd.equals("command"))
//					{
//						double x = command.getDouble("x");
//						double y = command.getDouble("y");
//						double z = command.getDouble("z");
//						double orientation = command.getDouble("orientation");
//						double timespan = command.getDouble("timespan");
//						
//						droneCommands.add(new DroneCommandMove(x,y,z, orientation, timespan));
//					}
//				}
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

//	private void LoadFlightPlan() {
//		mCommands = null;
//		File file = new File(mFlightPlanUri);
//		InputStream in = null;
//		try {
//			in = new BufferedInputStream(new FileInputStream(file));
////			mCommands = readJsonStream(in);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (in != null) {
//				try {
//					in.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
	
	

//	private List<DroneCommand> readJsonStream(InputStream in) throws IOException {
//		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
//		try {
//			return readDroneCommandsArray(reader);
//		} finally {
//			reader.close();
//		}
//	}
//
//	private List<DroneCommand> readDroneCommandsArray(JsonReader reader) throws IOException {
//		List<DroneCommand> droneCommands = new ArrayList<DroneCommand>();
//
//		reader.beginArray();
//		while (reader.hasNext()) {
//			droneCommands.add(readDroneCommand(reader));
//		}
//		reader.endArray();
//		return droneCommands;
//	}
//
//	public DroneCommand readDroneCommand(JsonReader reader) throws IOException {
//		long id = -1;
//		String text = null;
//
//		reader.beginObject();
//		while (reader.hasNext()) {
//			String name = reader.nextName();
//			if (name.equals("id")) {
//				id = reader.nextLong();
//			} else if (name.equals("text")) {
//				text = reader.nextString();
//			} else {
//				reader.skipValue();
//			}
//		}
//		reader.endObject();
//		return new DroneCommand(id, text);
//	}

	// public boolean onCreateOptionsMenu(Menu menu)
	// {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.menu_flightplan, menu);
	// return true;
	// }

}
