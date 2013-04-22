package de.yadrone.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class FlightPlanActivity extends BaseActivity {

	public final static String FLIGHTPLAN_URI = "de.yadrone.android.FLIGHTPLAN_URI";
	private int MSG_FILENAMES_SET = 123;

	private ArrayList<String> mFileNames;
	private ArrayAdapter<String> mAdapter;
	private ListView mFileNameListView;
	private static Handler mFileListHandler;
	private FtpFileLister mFtpFileLister;
	private FlightPlanActivity mFlightPlanActivity;
	private boolean mPhoneIsSource;
	private Button mToggleButton;

	public FlightPlanActivity() {
		super(R.id.menuitem_flightplan);
		mFlightPlanActivity = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flightplan);
		
		mFileNames = new ArrayList<String>();
		mFileNameListView = (ListView) findViewById(R.id.flightPlanList);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mFileNames);
		mFileNameListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();

		mFileListHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == MSG_FILENAMES_SET) {
					mFileNames.clear();
					@SuppressWarnings("unchecked")
					ArrayList<String> fileNames = (ArrayList<String>) msg.obj;
					for (String f : fileNames) {
						mFileNames.add(f);
					}
					mAdapter.notifyDataSetChanged();
				}
			}
		};

		mFileNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);
				Log.i("Selected item", item);
				
				Intent intent = new Intent(mFlightPlanActivity, FlightPlanProgressActivity.class);
				intent.putExtra(FLIGHTPLAN_URI, item);
				startActivity(intent);
			}
		});

		mToggleButton = (Button)findViewById(R.id.flightPlanSourceToggleButton);
		mToggleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				mPhoneIsSource = !mPhoneIsSource;
				FillFlightPlanList();
			}
		});
		
		mPhoneIsSource = false;
		FillFlightPlanList();
		
	}

	private void FillFlightPlanList() {
		if (mPhoneIsSource) {
			mToggleButton.setText("Show plans on Drone");
			
	        File downloadDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download");
	        File[] files = downloadDir.listFiles(new FileFilter() {

				@Override
				public boolean accept(File file) {
					
					return file.isFile() && file.getName().endsWith(".json");
				}
	        });
	        mFileNames.clear();
        	for (File file : files){
        		mFileNames.add(file.getPath());  
        	}
			mAdapter.notifyDataSetChanged();
		} else {
			mToggleButton.setText("Show plans on phone");

			mFtpFileLister = new FtpFileLister();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					mFtpFileLister.LoadFileList();
				}
			}).start();
		}
	}
	private class FtpFileLister {

		private String ftpAddress = "192.168.1.1";
		private String directory = "/boxes/test/";
		private String user = "anonymous";
		private String password = "";
		FTPClient ftp = null;
		ArrayList<String> remoteFileNames = null;

		public void LoadFileList() {
			ftp = new FTPClient();
			remoteFileNames = new ArrayList<String>();
			try {
				connect(ftpAddress);
				logIn();
				listDirectory(directory);
				ftp.noop(); // check that control connection is working OK
			} catch (FTPConnectionClosedException e) {
				System.err.println("Server closed connection.");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				logOut();
				disconnect();
			}
			Message msg;
			msg = Message.obtain();
			msg.what = MSG_FILENAMES_SET;
			msg.obj = remoteFileNames;
			mFileListHandler.sendMessage(msg);
		}
		
		private void connect(String server) throws SocketException, UnknownHostException, IOException {
			ftp.connect(InetAddress.getByName(server));
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
			}
			ftp.enterLocalPassiveMode();
		}

		private void logIn() throws IOException {
			ftp.login(user, password);
			String reply = ftp.getStatus();
			if (reply != null) {
				Log.d("login reply", reply);
			}
		}

		private void logOut() {
			try {
				ftp.logout();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void disconnect() {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void listDirectory(String remote) throws IOException {
			FTPFile[] files = ftp.listFiles(remote);
			for (FTPFile file : files) {
				if (file.isFile() && file.getName().endsWith(".json")) {
					remoteFileNames.add("ftp://" + ftpAddress + directory + file.getName());
				}
			}

		}

	}
	
//	public void onButtonClick(View view) {
//		// Do something in response to button
//		String flightPathUri;
//		switch (view.getId()) {
//		case R.id.button1:
//			flightPathUri = "flightplan1.json";
//			break;
//		case R.id.button2:
//			flightPathUri = "ftp://192.168.1.1/boxes/test/flightplan2.json";
//			break;
//		case R.id.button3:
//			flightPathUri = "ftp://192.168.1.1/boxes/test/flightplan3.json";
//			break;
//		case R.id.button4:
//			flightPathUri = "ftp://192.168.1.1/boxes/test/flightplan4.json";
//			break;
//		default:
//			flightPathUri = "";
//			break;
//		}
//		if (!flightPathUri.isEmpty()) {
//			Intent intent = new Intent(this, FlightPlanProgressActivity.class);
//			intent.putExtra(FLIGHTPLAN_URI, flightPathUri);
//			startActivity(intent);
//		}
//	}

}
