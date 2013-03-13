package com.example.sensorreading;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class SensorReadingActivity  extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGravitymeter;
    private Sensor mMagnetometer;
    private Sensor mLightmeter;
    private Sensor mProximitymeter;
    private Sensor mLinearAccelerometer;
    private Sensor mRotationVectormeter;

    public SensorReadingActivity() {
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor_reading);

		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        TextView accelerationDevice = (TextView) findViewById(R.id.acceleration_device);
        accelerationDevice.setText(mAccelerometer.getName());

        mGravitymeter = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        TextView gravityDevice = (TextView) findViewById(R.id.gravity_device);
        gravityDevice.setText(mGravitymeter.getName());

        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        TextView magneticfieldDevice = (TextView) findViewById(R.id.magnetic_field_device);
        magneticfieldDevice.setText(mMagnetometer.getName());
        
        mLinearAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        TextView linearAccelerationDevice = (TextView) findViewById(R.id.linear_acceleration_device);
        linearAccelerationDevice.setText(mLinearAccelerometer.getName());
        
        mRotationVectormeter = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        TextView rotationVectorDevice = (TextView) findViewById(R.id.rotation_vector_device);
        rotationVectorDevice.setText(mRotationVectormeter.getName());
        
        mLightmeter = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        TextView lightDevice = (TextView) findViewById(R.id.light_device);
        lightDevice.setText(mLightmeter.getName());

        mProximitymeter = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        TextView proximityDevice = (TextView) findViewById(R.id.proximity_device);
        proximityDevice.setText(mProximitymeter.getName());
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
        mSensorManager.registerListener(this, mLinearAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mRotationVectormeter, SensorManager.SENSOR_DELAY_NORMAL);
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
    		case Sensor.TYPE_LINEAR_ACCELERATION:
    			updateLinearAcceleration(event.values);
    			break;
    		case Sensor.TYPE_ROTATION_VECTOR:
    			updateRotationVector(event.values);
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
		accelerationX.setText(String.format("%6.3f", values[0]));
		TextView accelerationY = (TextView) findViewById(R.id.accelleration_y);
		accelerationY.setText(String.format("%6.3f", values[1]));
		TextView accelerationZ = (TextView) findViewById(R.id.accelleration_z);
		accelerationZ.setText(String.format("%6.3f", values[2]));
    }

    private void updateGravity(float[] values)
    {
		TextView gravityX = (TextView) findViewById(R.id.gravity_x);
		gravityX.setText(String.format("%6.3f", values[0]));
		TextView gravityY = (TextView) findViewById(R.id.gravity_y);
		gravityY.setText(String.format("%6.3f", values[1]));
		TextView gravityZ = (TextView) findViewById(R.id.gravity_z);
		gravityZ.setText(String.format("%6.3f", values[2]));
    }

    private void updateMagneticField(float[] values)
    {
		TextView magneticFieldX = (TextView) findViewById(R.id.magnetic_field_x);
		magneticFieldX.setText(String.format("%6.3f", values[0]));
		TextView magneticFieldY = (TextView) findViewById(R.id.magnetic_field_y);
		magneticFieldY.setText(String.format("%6.3f", values[1]));
		TextView magneticFieldZ = (TextView) findViewById(R.id.magnetic_field_z);
		magneticFieldZ.setText(String.format("%6.3f", values[2]));
    }

	private void updateLinearAcceleration(float[] values) {
		TextView linearAccelerationX = (TextView) findViewById(R.id.linear_acceleration_x);
		linearAccelerationX.setText(String.format("%6.3f", values[0]));
		TextView linearAccelerationY = (TextView) findViewById(R.id.linear_acceleration_y);
		linearAccelerationY.setText(String.format("%6.3f", values[1]));
		TextView linearAccelerationZ = (TextView) findViewById(R.id.linear_acceleration_z);
		linearAccelerationZ.setText(String.format("%6.3f", values[2]));
	}

    private void updateRotationVector(float[] values) {
		TextView rotationVectorX = (TextView) findViewById(R.id.rotation_vector_x);
		rotationVectorX.setText(String.format("%6.3f", values[0]));
		TextView rotationVectorY = (TextView) findViewById(R.id.rotation_vector_y);
		rotationVectorY.setText(String.format("%6.3f", values[1]));
		TextView rotationVectorZ = (TextView) findViewById(R.id.rotation_vector_z);
		rotationVectorZ.setText(String.format("%6.3f", values[2]));
	}

    private void updateLight(float[] values)
    {
		TextView lightX = (TextView) findViewById(R.id.light_x);
		lightX.setText(String.format("%6.3f", values[0]));
    }

    private void updateProximity(float[] values)
    {
		TextView proximityX = (TextView) findViewById(R.id.proximity_x);
		proximityX.setText(String.format("%6.3f", values[0]));
    }

	
}
