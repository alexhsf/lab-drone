package de.yadrone.android;

import java.util.List;
import java.util.Locale;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

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
				//new StreamProxy(cmdManager).start();
				new VideoProxy(cmdManager).start();
				
		        runOnUiThread(new Runnable() {

					@Override
					public void run()
					{
					   //VideoView video = (VideoView) findViewById(R.id.video);
				       // String url = "http://127.0.0.1:8888";
				       // Uri uri = Uri.parse(url); 
				        
				       // video.setVideoURI(uri);
				        //video.start();
						startRemoteCameraView();
						
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

	//starts the video playback
	private void startRemoteCameraView() 
	{
		String urlstr = "";

			// Do video playback via VLC as VideoView can't handle MPEG-TS
			// or raw H.264

			// Arno, 2012-10-24: Volatile, if VLC radically changes package 
			// name,  we doomed. But normal Intent searching won't grok
			// VLC's hack with the demuxer in the scheme: http/h264:
			// so we have to do it this way.
			//
			String pkgname = getPackageNameForVLC("org.videolan.vlc.betav7neon");
			if (pkgname == "")
				return;

			Intent intent = null;
				// Arno, 2012-10-24: LIVESOURCE=ANDROID
				// Force VLC to use H.264 demuxer via URL, see
				// http://wiki.videolan.org/VLC_command-line_help
				urlstr = "tcp/h264://127.0.0.1:8888";
				//urlstr += " :network-caching=50";
				//urlstr += " :http-caching=50";

				Uri intentUri = Uri.parse(urlstr);

				intent = new Intent();
				ComponentName cn = new ComponentName(pkgname,pkgname+".gui.video.VideoPlayerActivity");
				intent.setComponent(cn);
			    intent.setAction(Intent.ACTION_VIEW);
			    intent.setData(intentUri);


		    startActivity(intent);

	}



	/*
	 * Arno: See if VLC is installed, if so find out current name. If not 
	 * installed, open Google Play.
	 */
	private String getPackageNameForVLC(String vlcCurrentPackageName)
	{
		String vlcpkgnameprefix = "org.videolan.vlc";
	    try
	    {
	    	// From http://stackoverflow.com/questions/2780102/open-another-application-from-your-own-intent
	        Intent intent = new Intent("android.intent.action.MAIN");
	        intent.addCategory("android.intent.category.LAUNCHER");

	        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	        List<ResolveInfo> resolveinfo_list = this.getPackageManager().queryIntentActivities(intent, 0);

	        for (ResolveInfo info:resolveinfo_list)
	        {
	        	String ilcpn = info.activityInfo.packageName.toLowerCase(Locale.US);
	            if (ilcpn.startsWith(vlcpkgnameprefix))
	            {
	            	return info.activityInfo.packageName;
	            }
	        }

	        // VLC not found, prompt user to install
	        openPlayStore(vlcCurrentPackageName);
	    }
	    catch (Exception e) 
	    {
	        openPlayStore(vlcCurrentPackageName);
	    }
	    return "";
	}


	private void openPlayStore(String packageName)
	{
	    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    startActivity(intent);
	}  

}
