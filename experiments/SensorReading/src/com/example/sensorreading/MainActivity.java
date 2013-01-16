package com.example.sensorreading;

import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGravitymeter;
    private Sensor mMagnetometer;
    private Sensor mLightmeter;
    private Sensor mProximitymeter;

    public MainActivity() {
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		StringBuilder text = new StringBuilder("Available sensors:\n");
		for (Sensor sensor : sensors) {
			text.append(getSensorTypeName(sensor.getType()) + ": "+sensor.getName()+"\n");
		}
		TextView sensorList = (TextView)findViewById(R.id.sensor_list);
		sensorList.setText(text.toString());
        
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        TextView accelerationDevice = (TextView) findViewById(R.id.acceleration_device);
        accelerationDevice.setText(mAccelerometer.getName());

        mGravitymeter = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        TextView gravityDevice = (TextView) findViewById(R.id.gravity_device);
        gravityDevice.setText(mGravitymeter.getName());

        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        TextView magneticfieldDevice = (TextView) findViewById(R.id.magnetic_field_device);
        magneticfieldDevice.setText(mMagnetometer.getName());
        
        mLightmeter = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        TextView lightDevice = (TextView) findViewById(R.id.light_device);
        lightDevice.setText(mLightmeter.getName());

        mProximitymeter = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        TextView proximityDevice = (TextView) findViewById(R.id.proximity_device);
        proximityDevice.setText(mProximitymeter.getName());
	}

	private String getSensorTypeName(int sensorType)
	{
		String sensorTypeName = "Unknown";
		switch (sensorType)
		{
			case Sensor.TYPE_ACCELEROMETER:
				sensorTypeName="Accelerometer";
				break;
			case Sensor.TYPE_AMBIENT_TEMPERATURE:
				sensorTypeName="Ambient Temperature";
				break;
			case Sensor.TYPE_GRAVITY:
				sensorTypeName="Gravity";
				break;
			case Sensor.TYPE_GYROSCOPE:
				sensorTypeName="Gyroscope";
				break;
			case Sensor.TYPE_LIGHT:
				sensorTypeName="Light";
				break;
			case Sensor.TYPE_LINEAR_ACCELERATION:
				sensorTypeName="Linear Accelleration";
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				sensorTypeName="Magnetic Field";
				break;
			case Sensor.TYPE_PRESSURE:
				sensorTypeName="Pressure";
				break;
			case Sensor.TYPE_PROXIMITY:
				sensorTypeName="Proximity";
				break;
			case Sensor.TYPE_RELATIVE_HUMIDITY:
				sensorTypeName="Humidity";
				break;
			case Sensor.TYPE_ROTATION_VECTOR:
				sensorTypeName="Rotation Vector";
				break;
			default:
				break;
		}
		return sensorTypeName;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGravitymeter, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mLightmeter, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mProximitymeter, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
    	switch (event.sensor.getType())
    	{
    		case Sensor.TYPE_ACCELEROMETER:
    			updateAcceleration(event.values);
    			break;
    		case Sensor.TYPE_GRAVITY:
    			updateGravity(event.values);
    			break;
    		case Sensor.TYPE_MAGNETIC_FIELD:
    			updateMagneticField(event.values);
    			break;
    		case Sensor.TYPE_LIGHT:
    			updateLight(event.values);
    			break;
    		case Sensor.TYPE_PROXIMITY:
    			updateProximity(event.values);
    			break;
    		default:
    			break;
    	}
    }
    
    private void updateAcceleration(float[] values)
    {
		TextView accelerationX = (TextView) findViewById(R.id.accelleration_x);
		accelerationX.setText(String.valueOf(values[0]));
		TextView accelerationY = (TextView) findViewById(R.id.accelleration_y);
		accelerationY.setText(String.valueOf(values[1]));
		TextView accelerationZ = (TextView) findViewById(R.id.accelleration_z);
		accelerationZ.setText(String.valueOf(values[2]));
    }

    private void updateGravity(float[] values)
    {
		TextView gravityX = (TextView) findViewById(R.id.gravity_x);
		gravityX.setText(String.valueOf(values[0]));
		TextView gravityY = (TextView) findViewById(R.id.gravity_y);
		gravityY.setText(String.valueOf(values[1]));
		TextView gravityZ = (TextView) findViewById(R.id.gravity_z);
		gravityZ.setText(String.valueOf(values[2]));
    }

    private void updateMagneticField(float[] values)
    {
		TextView magneticFieldX = (TextView) findViewById(R.id.magnetic_field_x);
		magneticFieldX.setText(String.valueOf(values[0]));
		TextView magneticFieldY = (TextView) findViewById(R.id.magnetic_field_y);
		magneticFieldY.setText(String.valueOf(values[1]));
		TextView magneticFieldZ = (TextView) findViewById(R.id.magnetic_field_z);
		magneticFieldZ.setText(String.valueOf(values[2]));
    }

    private void updateLight(float[] values)
    {
		TextView lightX = (TextView) findViewById(R.id.light_x);
		lightX.setText(String.valueOf(values[0]));
    }

    private void updateProximity(float[] values)
    {
		TextView proximityX = (TextView) findViewById(R.id.proximity_x);
		proximityX.setText(String.valueOf(values[0]));
    }

}
