package de.yadrone.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FlightPlanActivity extends BaseActivity {

	public final static String FLIGHTPLAN_URI = "de.yadrone.android.FLIGHTPLAN_URI";

	public FlightPlanActivity() {
		super(R.id.menuitem_flightplan);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flightplan);
	}

	public void onButtonClick(View view) {
		// Do something in response to button
		String flightPathUri;
		switch (view.getId()) {
		case R.id.button1:
			flightPathUri = "flightplan1.json";
			break;
		case R.id.button2:
			flightPathUri = "ftp://192.168.1.1/boxes/test/flightplan2.json";
			break;
		case R.id.button3:
			flightPathUri = "ftp://192.168.1.1/boxes/test/flightplan3.json";
			break;
		case R.id.button4:
			flightPathUri = "ftp://192.168.1.1/boxes/test/flightplan4.json";
			break;
		default:
			flightPathUri = "";
			break;
		}
		if (!flightPathUri.isEmpty()) {
			Intent intent = new Intent(this, FlightPlanProgressActivity.class);
			intent.putExtra(FLIGHTPLAN_URI, flightPathUri);
			startActivity(intent);
		}
	}

}
