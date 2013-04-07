package com.example.jsonreading;

import java.util.List;

import com.shigeodayo.ardrone.command.DroneCommand;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FlightPlanFileReader reader = new FlightPlanFileReader();
		List<DroneCommand> flightPlan = reader.getFlightPlan("flightplan1.json");
		
		String textDescription = flightPlanToText(flightPlan);
		TextView feedback = (TextView)findViewById(R.id.feedback);
		feedback.setText(textDescription);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private String flightPlanToText(List<DroneCommand> flightPlan) {
		StringBuilder text = new StringBuilder();
		for (DroneCommand command : flightPlan) {
			text.append(command.toString());
			text.append("\n");
		}
		return text.toString();
	}

}
