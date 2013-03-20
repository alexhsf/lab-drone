package de.yadrone.android;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.VideoView;

import com.shigeodayo.ardrone.ARDrone;
import com.shigeodayo.ardrone.command.CommandManager;

public class VideoActivity extends BaseActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        YADroneApplication app = (YADroneApplication) getApplication();
        final ARDrone drone = app.getARDrone();
        final CommandManager cmdManager = drone.getCommandManager();
        
        Thread t = new Thread(new Runnable() {

			@Override
			public void run()
			{
				new StreamProxy(cmdManager).start();
				
		        runOnUiThread(new Runnable() {

					@Override
					public void run()
					{
						VideoView video = (VideoView) findViewById(R.id.video);
				        String url = "http://127.0.0.1:8888";
				        Uri uri = Uri.parse(url); 
				        
				        video.setVideoURI(uri);
				        video.start();
						
//						MediaPlayer urlPlayer = MediaPlayer.create(VideoActivity.this, Uri.parse("tcp://192.168.1.1:5555"));
					}
					
				});		        
			}
        	
        });
        t.start();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_video, menu);
        return true;
    }

}
