package com.example.sensorreading;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.MatrixCursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends Activity {

    private SensorManager mSensorManager;

    public MainActivity() {
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		
        String[] columnNames = {"_id", "sensor_type", "sensor_name"};
        MatrixCursor cursor = new MatrixCursor(columnNames, sensors.size());
        int id = 1;
        for (Sensor sensor : sensors)
        {
			cursor.addRow(new Object[] { id, getSensorTypeName(sensor.getType()), sensor.getName() });
			id++;
		}
        String[] fromColumns =  {"sensor_type", "sensor_name"};
        int[] toViews = {R.id.sensorType, R.id.sensorName};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.sensor_type_and_name, 
        		cursor, fromColumns, toViews, 0);
        ListView listView = (ListView) findViewById(R.id.sensorListview);
		listView.setAdapter(adapter);
	}
	
	public void readSensorValues(View view)
	{
        // Do something in response to button
    	Intent intent = new Intent(this, SensorReadingActivity.class);
    	startActivity(intent);
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
	
}
