package com.example.myftpclient;

//import java.io.FileInputStream;
//import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
//import java.net.ServerSocket;
//import java.net.Socket;
import java.net.SocketException;
//import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

//import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
//import org.apache.commons.net.io.CopyStreamEvent;
//import org.apache.commons.net.io.CopyStreamListener;
//import org.apache.commons.net.util.TrustManagerUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	private int MSG_FILENAMES_SET = 123;

	private ArrayList<String> mFileNames;
	private ArrayAdapter<String> mAdapter;
	private ListView mFileNameListView;
	private static Handler mFtpFileListHandler;
	private FtpFileLister mFtpFileLister; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mFileNames = new ArrayList<String>();
		mFileNameListView = (ListView) findViewById(R.id.fileNameList);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mFileNames);
		mAdapter.notifyDataSetChanged();
		mFileNameListView.setAdapter(mAdapter);

		mFtpFileListHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == MSG_FILENAMES_SET) {
					mFileNames.clear();
					@SuppressWarnings("unchecked")
					ArrayList<String> remoteFiles = (ArrayList<String>) msg.obj;
					for (String f : remoteFiles) {
						mFileNames.add(f);
					}
					mAdapter.notifyDataSetChanged();
				}
			}
		};
		
		mFtpFileLister = new FtpFileLister();

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				mFtpFileLister.LoadFileList();
			}
		}).start();

		mFileNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);
				Log.i("Selected item", item);
				String json = mFtpFileLister.LoadFile(item);
				Log.i("File contents:", json);
			}
		});

	}

	// {
	// ftpClient.changeWorkingDirectory(serverRoad);
	// ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	// BufferedInputStream buffIn=null;
	// buffIn=new BufferedInputStream(new FileInputStream(file));
	// ftpClient.storeFile("test.txt", buffIn);
	// buffIn.close();
	// ftpClient.logout();
	// ftpClient.disconnect();
	// }

	// if (binaryTransfer) {
	// ftp.setFileType(FTP.BINARY_FILE_TYPE);
	// } else {
	// // in theory this should not be necessary as servers should default to ASCII
	// // but they don't all do so - see NET-500
	// ftp.setFileType(FTP.ASCII_FILE_TYPE);
	// }
	// ftp.setUseEPSVwithIPv4(useEpsvWithIPv4);

	private class FtpFileLister {

		private String ftpAddress = "<address>";
		private String currentDir = "lab-drone";
//		private String user = "<username>";
//		private String password = "<password>";
		FTPClient ftp = null;
		ArrayList<String> remoteFileNames = null;

		public void LoadFileList() {
			ftp = new FTPClient();
			remoteFileNames = new ArrayList<String>();
			try {
				connect(ftpAddress);
				logIn();
				listDirectory(currentDir + "/");
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
			mFtpFileListHandler.sendMessage(msg);
		}
		
		public String LoadFile(String fileName) {
			// e.g. "ftp://mirror.csclub.uwaterloo.ca/index.html"
			String json = null;
			URL url;
			BufferedReader in = null;
			try {
				url = new URL(ftpAddress + "/" + fileName);
				in = new BufferedReader(new InputStreamReader(url.openStream()));
				StringBuilder jsonBuffer = new StringBuilder();
				String inputLine;
		        while ((inputLine = in.readLine()) != null)
		        {
		        	jsonBuffer.append(inputLine);
		        }
		        json = jsonBuffer.toString();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (in != null)
				{
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return json;
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
			//ftp.login(user, password);
			ftp.login("anonymous", "");
			String reply = ftp.getStatus();
			if (reply != null) {
				Log.d("login reply", reply);
			}
		}

		private void logOut() {
			try {
				ftp.logout();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void disconnect() {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private void listDirectory(String remote) throws IOException {
			FTPFile[] files = ftp.listFiles(remote);
			for (FTPFile file : files) {
				if (file.isFile()) {
					remoteFileNames.add(file.getName());
				}
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
