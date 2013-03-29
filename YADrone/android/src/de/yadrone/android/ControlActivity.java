package de.yadrone.android;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

import com.shigeodayo.ardrone.ARDrone;
import com.shigeodayo.ardrone.command.CommandManager;
import com.shigeodayo.ardrone.command.FlightAnimation;

public class ControlActivity extends BaseActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);

		initButtons();

		Toast.makeText(this, "Touch and hold the buttons", Toast.LENGTH_SHORT).show();
	}

	private void initButtons() {
		YADroneApplication app = (YADroneApplication) getApplication();
		final ARDrone drone = app.getARDrone();
		final CommandManager cm = drone.getCommandManager();

		Button forward = (Button) findViewById(R.id.cmd_forward);
		forward.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					cm.forward(20);
				else if (event.getAction() == MotionEvent.ACTION_UP)
					drone.stop();

				return true;
			}
		});

		Button backward = (Button) findViewById(R.id.cmd_backward);
		backward.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					cm.backward(20);
				else if (event.getAction() == MotionEvent.ACTION_UP)
					cm.stop();

				return true;
			}
		});

		Button left = (Button) findViewById(R.id.cmd_left);
		left.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					cm.goLeft(20);
				else if (event.getAction() == MotionEvent.ACTION_UP)
					cm.stop();

				return true;
			}
		});

		Button right = (Button) findViewById(R.id.cmd_right);
		right.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					cm.goRight(20);
				else if (event.getAction() == MotionEvent.ACTION_UP)
					cm.stop();

				return true;
			}
		});

		Button up = (Button) findViewById(R.id.cmd_up);
		up.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					cm.up(40);
				else if (event.getAction() == MotionEvent.ACTION_UP)
					cm.stop();

				return true;
			}
		});

		Button down = (Button) findViewById(R.id.cmd_down);
		down.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					cm.down(40);
				else if (event.getAction() == MotionEvent.ACTION_UP)
					cm.stop();

				return true;
			}
		});

		Button spinLeft = (Button) findViewById(R.id.cmd_spin_left);
		spinLeft.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					cm.spinLeft(20);
				else if (event.getAction() == MotionEvent.ACTION_UP)
					cm.stop();

				return true;
			}
		});

		Button spinRight = (Button) findViewById(R.id.cmd_spin_right);
		spinRight.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					cm.spinRight(20);
				else if (event.getAction() == MotionEvent.ACTION_UP)
					cm.stop();

				return true;
			}
		});

		Button landing = (Button) findViewById(R.id.cmd_landing);
		landing.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cm.landing();
			}
		});

		Button takeoff = (Button) findViewById(R.id.cmd_takeoff);
		takeoff.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cm.takeOff();
			}
		});

		Button emergency = (Button) findViewById(R.id.cmd_emergency);
		emergency.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cm.reset();
			}
		});

		Button test = (Button) findViewById(R.id.PHI_M30_DEG);
		test.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cm.animate(null);
			}
		});

		final int animIds[] = { R.id.PHI_M30_DEG, R.id.PHI_30_DEG, R.id.THETA_M30_DEG, R.id.THETA_30_DEG,
				R.id.THETA_20DEG_YAW_200DEG, R.id.THETA_20DEG_YAW_M200DEG, R.id.TURNAROUND, R.id.TURNAROUND_GODOWN,
				R.id.YAW_SHAKE, R.id.YAW_DANCE, R.id.PHI_DANCE, R.id.THETA_DANCE, R.id.VZ_DANCE, R.id.WAVE,
				R.id.PHI_THETA_MIXED, R.id.DOUBLE_PHI_THETA_MIXED, R.id.FLIP_AHEAD, R.id.FLIP_BEHIND, R.id.FLIP_LEFT,
				R.id.FLIP_RIGHT };
		final FlightAnimation animCmds[] = { FlightAnimation.PHI_M30_DEG, FlightAnimation.PHI_30_DEG,
				FlightAnimation.THETA_M30_DEG, FlightAnimation.THETA_30_DEG, FlightAnimation.THETA_20DEG_YAW_200DEG,
				FlightAnimation.THETA_20DEG_YAW_M200DEG, FlightAnimation.TURNAROUND, FlightAnimation.TURNAROUND_GODOWN,
				FlightAnimation.YAW_SHAKE, FlightAnimation.YAW_DANCE, FlightAnimation.PHI_DANCE,
				FlightAnimation.THETA_DANCE, FlightAnimation.VZ_DANCE, FlightAnimation.WAVE,
				FlightAnimation.PHI_THETA_MIXED, FlightAnimation.DOUBLE_PHI_THETA_MIXED, FlightAnimation.FLIP_AHEAD,
				FlightAnimation.FLIP_BEHIND, FlightAnimation.FLIP_LEFT, FlightAnimation.FLIP_RIGHT };

		for (int n = 0; n < animCmds.length; n++) {
			final FlightAnimation a = animCmds[n];
			Button b = (Button) findViewById(animIds[n]);
			b.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					cm.animate(a);
				}
			});
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_control, menu);
		return true;
	}

}
