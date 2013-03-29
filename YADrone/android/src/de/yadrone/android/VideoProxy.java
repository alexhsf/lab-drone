package de.yadrone.android;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.net.Uri;
import android.util.Log;

import com.shigeodayo.ardrone.command.CommandManager;
import com.shigeodayo.ardrone.command.H264;
import com.shigeodayo.ardrone.command.VideoBitRateMode;
import com.shigeodayo.ardrone.command.VideoCodec;
import com.shigeodayo.ardrone.utils.ARDroneUtils;

public class VideoProxy {
	public static final int H264_360P_CODEC = 129;
	public static final int VBC_MANUAL = 2;

	public static final int PaVE_HeaderSize = 68;
	public static final int PaVE_HeaderSizeWithoutSignature = 64;
	public static final int MAX_PAYLOAD_DATA = 64 * 1024;
    private static final String TAG = "YADrone";

    private static final int STREAMPORT = 8888;
    private static final int SERVERPORT = 8887;

    private static final InetAddress SERVERIPADDRESS;
    static {
        try {
            SERVERIPADDRESS = InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 }); //InetAddress.getLocalHost();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    private Thread thread;
    private Thread thread2;
    private boolean running;
    private Socket droneSocket;
    private ServerSocket streamsocket;
    private ServerSocket m3usocket;

    public VideoProxy(CommandManager manager) {
        manager.enableVideoData();
        manager.setVideoCodecFps(H264.MIN_FPS);
        manager.setVideoCodec(VideoCodec.H264_360P);
        manager.setVideoBitrateControl(VideoBitRateMode.MANUAL);
        manager.setVideoBitrate(H264.MAX_BITRATE);
     }

    public Uri getURI() {
        String url = "http://" + SERVERIPADDRESS + ":" + STREAMPORT;
        return Uri.parse(url);
    }

    protected void ticklePort(int port) {
        byte[] buf = { 0x01, 0x00, 0x00, 0x00 };
        try {
            OutputStream os = droneSocket.getOutputStream();
            os.write(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        running = true;

        thread = new Thread() {
            public void run() {
                try {
                    streamsocket = new ServerSocket(STREAMPORT, 0, SERVERIPADDRESS);
                    streamsocket.setSoTimeout(10000);
                } catch (UnknownHostException e) { // impossible
                } catch (IOException e) {
                    Log.e(TAG, "IOException initializing server", e);
                }

                while (running) {
                    try {
                        Socket client = streamsocket.accept();
                        if (client == null) {
                            continue;
                        }
                        Log.d(TAG, "client connected to streamsocket");

                       // int skip = processStreamRequest(client);
                       // if (skip >= 0) {
                            handleStreamRequest(client, 0);
                       // }

                    } catch (SocketTimeoutException e) {
                        // Do nothing
                    } catch (IOException e) {
                        Log.e(TAG, "Error connecting to client", e);
                    }
                }
                Log.d(TAG, "Proxy interrupted. Shutting down.");
            }
        };
        thread.start();

//        thread2 = new Thread() {
//            public void run() {
//                // Create server socket
//                try {
//                    m3usocket = new ServerSocket(SERVERPORT, 0, SERVERIPADDRESS);
//                    m3usocket.setSoTimeout(10000);
//                } catch (UnknownHostException e) { // impossible
//                } catch (IOException e) {
//                    Log.e(TAG, "IOException initializing server", e);
//                }
//                while (running) {
//                    try {
//                        Socket client = m3usocket.accept();
//                        if (client == null) {
//                            continue;
//                        }
//                        Log.d(TAG, "client connected to m3usocket");
//
//                        if (processM3URequest(client)) {
//                            handleM3URequest(client);
//                        }
//
//                    } catch (SocketTimeoutException e) {
//                        // Do nothing
//                    } catch (IOException e) {
//                        Log.e(TAG, "Error connecting to client", e);
//                    }
//                }
//                Log.d(TAG, "Proxy interrupted. Shutting down.");
//            }
//        };
//        thread2.start();
    }

    public void stop() {
        running = false;
        thread.interrupt();
        try {
            thread.join(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int processStreamRequest(Socket client) {
        int skip = 0;

        // Read HTTP headers
        String headers = "";
        try {
            headers = readTextStreamAvailable(client.getInputStream());
        } catch (IOException e) {
            Log.e(TAG, "Error reading HTTP request header from stream:", e);
            return -1;
        }

        // Get the important bits from the headers
        String[] headerLines = headers.split("\n");
        String urlLine = headerLines[0];
        if (!urlLine.startsWith("GET ")) {
            Log.e(TAG, "Only GET is supported");
            return -1;
        }
        urlLine = urlLine.substring(4);
        int charPos = urlLine.indexOf(' ');
        if (charPos != -1) {
            urlLine = urlLine.substring(1, charPos);
        }

        // See if there's a "Range:" header
        for (int i = 0; i < headerLines.length; i++) {
            String headerLine = headerLines[i];
            if (headerLine.startsWith("Range: bytes=")) {
                headerLine = headerLine.substring(13);
                charPos = headerLine.indexOf('-');
                if (charPos > 0) {
                    headerLine = headerLine.substring(0, charPos);
                }
                skip = Integer.parseInt(headerLine);
            }
        }
        return skip;
    }

        protected void handleStreamRequest(Socket client, int skip) {
            // connect to drone
            try {
                droneSocket = new Socket("192.168.1.1", ARDroneUtils.VIDEO_PORT);
                droneSocket.setSoTimeout(10000);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // ticklePort(ARDroneUtils.VIDEO_PORT);
            // ticklePort(ARDroneUtils.VIDEO_PORT);

            long fileSize = 100000;

            // Create HTTP header
            // String headers = "HTTP/1.0 200 OK\r\n";
            // headers += "Content-Type: video/H264\r\n";
            // headers += "Content-Length: " + Integer.MAX_VALUE + "\r\n";
            // headers += "Connection: close\r\n";
            // headers += "\r\n";

            StringBuilder sb = new StringBuilder();
            sb.append("HTTP/1.1 200 OK\r\n");
            sb.append("Content-Type: video/mp4\r\n");
            sb.append("Connection: close\r\n");
            sb.append("Accept-Ranges: bytes\r\n");
            sb.append("Content-Length: " + Integer.MAX_VALUE + "\r\n");

            String headers = sb.toString();
            int readHeader=1;
            // Begin with HTTP header
            long cbToSend = fileSize - skip;
            OutputStream output = null;
            byte[] payloadData = new byte[MAX_PAYLOAD_DATA];
            int payloadSize=0;
            
            try {
                output = new BufferedOutputStream(client.getOutputStream(),
                		                          4 * MAX_PAYLOAD_DATA);
             //   output.write(headers.getBytes());

                // Loop as long as there's stuff to send
                while (running && cbToSend > 0 && !client.isClosed()) {
                    try {
                        InputStream is = droneSocket.getInputStream();
                        int paveIndex = -1;
                        int skipBytes = 0;
                        int i;
                        while ((i = is.read()) != -1) {
                            if (skipBytes-- > 0) {
                                if (skipBytes == 0)
                                    paveIndex = -1;
                                continue;
                            }

                            byte b = (byte) i;
                            if ((paveIndex == -1) && (b == (byte) 'P'))
                                paveIndex = 0;
                            else if ((paveIndex == 0) && (b == (byte) 'a'))
                                paveIndex = 1;
                            else if ((paveIndex == 0) && (b != (byte) 'a')) {
                                output.write((byte) 'P');
                                paveIndex = -1;
                            } else if ((paveIndex == 1) && (b == (byte) 'V'))
                                paveIndex = 2;
                            else if ((paveIndex == 1) && (b != (byte) 'V')) {
                                output.write((byte) 'P');
                                output.write((byte) 'a');
                                paveIndex = -1;
                            } else if ((paveIndex == 2) && (b == (byte) 'E')) {
                                skipBytes = 0;
                                paveIndex = -1;
                                //paveIndex = 3;
                                // we have a PaVE header
                                int header_size = PaVE_HeaderSizeWithoutSignature; 
                                  
                            	// read PaVE header
                                byte[] buffer = new byte[header_size];
                                is.read(buffer,0, header_size);  
                                if (readHeader==1)
                                {
                                	
                                	readHeader = 0;  
                                    for (int h=0; h<header_size; h++)
                                    {
                                    	int value = buffer[h];
                                        String msg = "Buffer[" + h + "]=0x" + Integer.toHexString(value);
                                        Log.d(TAG, msg);
                                    }
                               }
                                 payloadSize = ((buffer[7] & 0xFF) << 24) | 
                                		      ((buffer[6] & 0xFF) << 16) | 
                                		      ((buffer[5] & 0xFF) << 8)  | 
                                		      (buffer[4] & 0xFF); 
                               	// receive from ardrone and and send to video player                                 
                               while (payloadSize > MAX_PAYLOAD_DATA )
                                {
                                	// write several times payload data
                                	payloadSize-=MAX_PAYLOAD_DATA;
                                    is.read(payloadData, 0, MAX_PAYLOAD_DATA); 
                                    output.write(payloadData, 0, MAX_PAYLOAD_DATA);                              	
                                }
                                is.read(payloadData, 0, payloadSize); 
                                output.write(payloadData, 0, payloadSize);
                              
                            }
//                              else if ((paveIndex == 2) && (b != (byte) 'E')) {
//                                output.write((byte) 'P');
//                                output.write((byte) 'a');
//                                output.write((byte) 'V');
//                                paveIndex = -1;
//                            }

//                            if (paveIndex == -1)
//                                
//                            else if ((paveIndex == 3) && (skipBytes == 0)) {
//                                paveIndex = -1;
//                                //System.out.println("PaVE skipped");
//                            }
                        }

                        // byte[] buffer = new byte[1024]; // Adjust if you want
                        // int bytesRead;
                        // while ((bytesRead =
                        // droneSocket.getInputStream().read(buffer)) != -1)
                        // {
                        // int startWriteIndex = 0;
                        // if ((buffer[0] == (byte)'P') && (buffer[1] ==
                        // (byte)'a') && (buffer[2] == (byte)'V') && (buffer[3]
                        // == (byte)'E'))
                        // {
                        // // it's pave
                        // startWriteIndex = 64;
                        // }
                        // // System.out.println("Read " + new String(buffer));
                        // // System.out.println("Write byte from " +
                        // startWriteIndex + " to " + (bytesRead -
                        // startWriteIndex));
                        // output.write(buffer, startWriteIndex, bytesRead -
                        // startWriteIndex);
                        // }
                    } catch (Exception exc) {
                        exc.printStackTrace();
                        running = false;
                    }

                }
            } catch (SocketException socketException) {
                Log.e(TAG,
                        "SocketException() thrown, proxy client has probably closed. This can exit harmlessly");
            } catch (Exception e) {
                Log.e(TAG, "Exception thrown from streaming task:");
                Log.e(TAG,
                        e.getClass().getName() + " : "
                                + e.getLocalizedMessage());
                e.printStackTrace();
            }

            // Cleanup
            try {
                if (output != null) {
                    output.close();
                }
               // client.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException while cleaning up streaming task:");
                Log.e(TAG,
                        e.getClass().getName() + " : "
                                + e.getLocalizedMessage());
                e.printStackTrace();
            }

        }

        private static int toInt(byte[] bytes, int offset) {
        	  int ret = 0;
        	  for (int i=0; i<4 && i+offset<bytes.length; i++) {
        	    ret <<= 8;
        	    ret |= (int)bytes[i] & 0xFF;
        	  }
        	  return ret;
        }
        
        private String readTextStreamAvailable(InputStream inputStream)
                throws IOException {
            byte[] buffer = new byte[4096];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(4096);

            // Do the first byte via a blocking read
            outputStream.write(inputStream.read());

            // Slurp the rest
            int available = inputStream.available();
            while (available > 0) {
                int cbToRead = Math.min(buffer.length, available);
                int cbRead = inputStream.read(buffer, 0, cbToRead);
                if (cbRead <= 0) {
                    throw new IOException("Unexpected end of stream");
                }
                outputStream.write(buffer, 0, cbRead);
                available -= cbRead;
            }
            return new String(outputStream.toByteArray());
        }


        private boolean processM3URequest(Socket client) {
            // Read HTTP headers
            String headers = "";
            try {
                headers = readTextStreamAvailable(client.getInputStream());
            } catch (IOException e) {
                Log.e(TAG, "Error reading HTTP request header from stream:", e);
                return false;
            }

            // Get the important bits from the headers
            String[] headerLines = headers.split("\n");
            String urlLine = headerLines[0];
            if (!urlLine.startsWith("GET ")) {
                Log.e(TAG, "Only GET is supported");
                return false;
            }
            urlLine = urlLine.substring(4);
            int charPos = urlLine.indexOf(' ');
            if (charPos != -1) {
                urlLine = urlLine.substring(1, charPos);
            }

            return true;
        }

        private void handleM3URequest(Socket client) {

            String content = "http://127.0.0.1:" + STREAMPORT
                    + "/ardrone.mp4\r\n";

            StringBuilder sb = new StringBuilder();
            sb.append("HTTP/1.1 200 OK\r\n");
            sb.append("Content-Type: application/m3u;\r\n");
            sb.append("Content-Transfer-Encoding: binary\r\n");
            sb.append("Connection: close\r\n");
            sb.append("Content-Length: " + content.length() + "\r\n");
            String headers = sb.toString();

            // Begin with HTTP header
            OutputStream output = null;
            try {
                output = new BufferedOutputStream(client.getOutputStream());
                output.write(headers.getBytes());
                output.write(content.getBytes());
            } catch (SocketException socketException) {
                Log.e(TAG,
                        "SocketException() thrown, proxy client has probably closed. This can exit harmlessly");
            } catch (Exception e) {
                Log.e(TAG, "Exception thrown from streaming task:");
                Log.e(TAG,
                        e.getClass().getName() + " : "
                                + e.getLocalizedMessage());
                e.printStackTrace();
            }

            // Cleanup
            try {
                if (output != null) {
                    output.close();
                }
                client.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException while cleaning up streaming task:");
                Log.e(TAG,
                        e.getClass().getName() + " : "
                                + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }

}
