package de.yadrone.android;

import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.shigeodayo.ardrone.ARDrone;
import com.shigeodayo.ardrone.command.CommandManager;

public class PhotoActivity extends BaseActivity {

	public PhotoActivity() {
		super(R.id.menuitem_photo);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_photo);
		
		YADroneApplication app = (YADroneApplication) getApplication();
		ARDrone drone = app.getARDrone();
		final CommandManager cm = drone.getCommandManager();
		final ImageButton button = (ImageButton) findViewById(R.id.photoButton);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					cm.startRecordingNavData("20130415_163200");
					cm.recordPictures(1, 1, "20130415_163200");

					Thread.sleep(2000);
					cm.stopRecordingNavData();

					final Bitmap bmp = cm.getRecordedPicture("20130415_163200");

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (bmp != null) {
							button.setImageBitmap(bmp);
							}
						}
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});
	}
}
