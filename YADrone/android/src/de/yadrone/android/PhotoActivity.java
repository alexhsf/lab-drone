package de.yadrone.android;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.shigeodayo.ardrone.ARDrone;
import com.shigeodayo.ardrone.command.CommandManager;

public class PhotoActivity extends BaseActivity {

	private ImageButton button = null;
	private ImageAdapter imageAdapter;

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

		button = (ImageButton) findViewById(R.id.photoButton);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {					
					cm.startRecordingPictures(2, 5);
					Thread.sleep(12000);
					cm.stopRecording();
					updateImages();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});

		Gallery gallery = (Gallery) findViewById(R.id.photoGallery);
		imageAdapter = new ImageAdapter(this);
		gallery.setAdapter(imageAdapter);
		updateImages();
		gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				button.setImageBitmap((Bitmap) imageAdapter.getItem(position));
			}
		});
	}

	public void updateImages() {
		new Thread("ImageRetriever") {
			public void run() {
				//URL[] urls;
				try {
					YADroneApplication app = (YADroneApplication) getApplication();
					ARDrone drone = app.getARDrone();
					final CommandManager cm = drone.getCommandManager();
					final Bitmap bmps[] = cm.getRecordedPictures();
					// urls = cm.getRecordedPictures();
					//
					// final Bitmap bmps[] = new Bitmap[urls.length];
					// for (int n = 0; n < urls.length; n++) {
					// InputStream is = urls[n].openStream();
					// if (is != null) {
					// bmps[n] = BitmapFactory.decodeStream(is);
					// }
					// }
					imageAdapter.setImages(bmps);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							imageAdapter.notifyDataSetChanged();
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}

class ImageAdapter extends BaseAdapter {

	Context context;
	Bitmap[] images = new Bitmap[0];

	public ImageAdapter(Context c) {
		this.context = c;
	}

	/**
	 * @param images
	 *            the images to set
	 */
	public void setImages(Bitmap[] images) {
		this.images = images;
	}

	public int getCount() {
		return images.length;
	}

	public View getView(int position, View arg1, ViewGroup arg2) {
		ImageView image = new ImageView(context);
		image.setImageBitmap(images[position]);
		return image;
	}

	@Override
	public Object getItem(int i) {
		return images[i];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
