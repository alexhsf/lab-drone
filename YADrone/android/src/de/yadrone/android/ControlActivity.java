package de.yadrone.android;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.shigeodayo.ardrone.ARDrone;
import com.shigeodayo.ardrone.command.CommandManager;
import com.shigeodayo.ardrone.command.FlightAnimation;
import com.shigeodayo.ardrone.navdata.ControlState;
import com.shigeodayo.ardrone.navdata.DroneState;
import com.shigeodayo.ardrone.navdata.NavDataManager;
import com.shigeodayo.ardrone.navdata.StateListener;
import com.shigeodayo.ardrone.navdata.VisionData;
import com.shigeodayo.ardrone.navdata.VisionListener;
import com.shigeodayo.ardrone.navdata.VisionPerformance;
import com.shigeodayo.ardrone.navdata.VisionTag;

public class ControlActivity extends BaseActivity implements StateListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);

		YADroneApplication app = (YADroneApplication) getApplication();
		final ARDrone drone = app.getARDrone();
		final CommandManager cm = drone.getCommandManager();
		final NavDataManager nd = drone.getNavDataManager();

		initButtons(cm);
		initStateView(nd);

		// setStates(0xAAAAAAAA);
		nd.setVisionListener(new VisionListener() {
			private final FlightAnimation[] anims = new FlightAnimation[] { FlightAnimation.FLIP_AHEAD, FlightAnimation.FLIP_BEHIND, FlightAnimation.FLIP_LEFT, FlightAnimation.FLIP_RIGHT }; 
			private int nanim = 0;
			private long tlast = 0;
			private long timeout = 0;

			FlightAnimation current() {
				return anims[nanim];
			}

			private void next() {
				nanim = (nanim + 1) % anims.length;				
			}			
			
			@Override
			public void trackersSend(int[][] locked, int[][][] point) {
				// TODO Auto-generated method stub
			}

			@Override
			public void tagsDetected(VisionTag[] tags) {
				long t = System.currentTimeMillis();
				long dt = t - tlast;
				System.out.println("TAGS DETECTED: " + tags.length + " dt: " + dt);
				if (t - tlast > timeout) {
					FlightAnimation a = current();
					System.out.println("ANIMATION: " + a);
					cm.animate(a);
					timeout = a.getDefaultDuration() + 1000;
					tlast = t;
					next();
				}
			}

			@Override
			public void receivedVisionOf(float[] of_dx, float[] of_dy) {
				// TODO Auto-generated method stub

			}

			@Override
			public void receivedRawData(float[] vision_raw) {
				// TODO Auto-generated method stub

			}

			@Override
			public void receivedPerformanceData(VisionPerformance d) {
				// TODO Auto-generated method stub

			}

			@Override
			public void receivedData(VisionData d) {
				// TODO Auto-generated method stub

			}

			@Override
			public void typeDetected(int detection_camera_type) {
				// TODO Auto-generated method stub
				
			}
		});

		Toast.makeText(this, "Touch and hold the buttons", Toast.LENGTH_SHORT).show();
	}

	private void initStateView(final NavDataManager nd) {
		LinearLayout vg = (LinearLayout) findViewById(R.id.states);
		LayoutParams lp = new LinearLayout.LayoutParams(15, LinearLayout.LayoutParams.MATCH_PARENT);
		for (int n = 0; n < 32; n++) {
			CheckedTextView v = new CheckedTextView(this);
			v.setLayoutParams(lp);
			v.setBackgroundResource(R.drawable.selectstate);
			v.setClickable(false);
			v.setLongClickable(false);
			v.setDuplicateParentStateEnabled(false);
			v.setFocusable(false);
			v.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
			v.setText(String.valueOf(n));
			vg.addView(v);
		}

		System.out.println(vg.getWidth());
		System.out.println(vg.getHeight());
		nd.setStateListener(this);

	}

	private void initButtons(final CommandManager cm) {

		Button forward = (Button) findViewById(R.id.cmd_forward);
		forward.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					cm.forward(40);
				else if (event.getAction() == MotionEvent.ACTION_UP)
					cm.stop();

				return true;
			}
		});

		Button backward = (Button) findViewById(R.id.cmd_backward);
		backward.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					cm.backward(40);
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
					cm.spinLeft(40);
				else if (event.getAction() == MotionEvent.ACTION_UP)
					cm.stop();

				return true;
			}
		});

		Button spinRight = (Button) findViewById(R.id.cmd_spin_right);
		spinRight.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					cm.spinRight(40);
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
				cm.emergency();
			}
		});

		Button trim = (Button) findViewById(R.id.cmd_trim);
		trim.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cm.flatTrim();
			}
		});

		final int BUTTONSPERROW = 5;
		final FlightAnimation anims[] = FlightAnimation.values();

		TableLayout table = (TableLayout) findViewById(R.id.animations);
		TableRow row = null;
		for (int n = 0; n < anims.length; n++) {
			final FlightAnimation a = anims[n];
			if (n % BUTTONSPERROW == 0) {
				row = new TableRow(this);
				row.setGravity(Gravity.CENTER_HORIZONTAL);
				table.addView(row);
			}
			Button b = new Button(this);
			b.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
			String s = a.name().replace("_", "");
			int middle = s.length() / 2;
			b.setText(s.substring(0, middle) + "\n" + s.substring(middle));
			b.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					cm.animate(a);
				}
			});
			row.addView(b);
		}

	}

	private void setStates(final int s) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ViewGroup vg = (ViewGroup) findViewById(R.id.states);
				for (int n = 0; n < 32; n++) {
					CheckedTextView v = (CheckedTextView) vg.getChildAt(n);
					boolean b = (s & (1 << n)) != 0;
					v.setChecked(b);
				}
			}
		});

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_control, menu);
		return true;
	}

	@Override
	public void stateChanged(DroneState state) {
		setStates(state.getStateBits());
	}

	@Override
	public void controlStateChanged(ControlState state) {
	}

}
