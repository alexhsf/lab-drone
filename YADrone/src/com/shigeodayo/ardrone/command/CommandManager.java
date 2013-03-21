/*
 *
  Copyright (c) <2011>, <Shigeo Yoshida>
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
The names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.shigeodayo.ardrone.command;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.shigeodayo.ardrone.manager.AbstractManager;

public class CommandManager extends AbstractManager{

	public static final int H264_MIN_FPS = 15;
	public static final int H264_MAX_FPS = 30;

	public static final int H264_MIN_BITRATE = 250;
	public static final int H264_MAX_BITRATE = 4000;

	/* todo: enum? */
	public static final int VBC_MODE_DISABLED = 0; // no video bitrate control
	public static final int VBC_MODE_DYNAMIC = 1; // video bitrate control active
	public static final int VBC_MANUAL = 2; // video bitrate control active

	/* todo: enum? */
	public static final int NULL_CODEC = 0x00;
	public static final int UVLC_CODEC = 0x20; // codec_type value is used for START_CODE
	public static final int P264_CODEC = 0x40;
	public static final int MP4_360P_CODEC = 0x80;
	public static final int H264_360P_CODEC = 0x81;
	public static final int MP4_360P_H264_720P_CODEC = 0x82;
	public static final int H264_720P_CODEC = 0x83;
	public static final int MP4_360P_SLRS_CODEC = 0x84;
	public static final int H264_360P_SLRS_CODEC = 0x85;
	public static final int H264_720P_SLRS_CODEC = 0x86;
	public static final int H264_AUTO_RESIZE_CODEC = 0x87; // resolution is adjusted according to bitrate
	public static final int MP4_360P_H264_360P_CODEC = 0x88;

	/* todo: enum? */
	public static final int ZAP_CHANNEL_HORI = 0; // horizontal camera channel
	public static final int ZAP_CHANNEL_VERT = 1; // vertical camera channel
	public static final int ZAP_CHANNEL_LARGE_HORI_SMALL_VERT = 2; // horizontal camera with vertical camera picture inserted in the left-top corner
    public static final int ZAP_CHANNEL_LARGE_VERT_SMALL_HORI = 3; // vertical camera with horizontal camera picture inserted in the left-top corner
    public static final int ZAP_CHANNEL_NEXT = 4; // next available camera format

	private static final String CR="\r";

	private static final String SEQ = "$SEQ$";

	private static int seq=1;

	private FloatBuffer fb=null;
	private IntBuffer ib=null;

	private boolean landing=true;
	private boolean continuance=false;
	private String command=null;
	
	/** speed */
	private float speed=0.05f;//0.01f - 1.0f
		
	public CommandManager(InetAddress inetaddr){
		this.inetaddr=inetaddr;
		initialize();
	}

    public void setVideoChannel(int channel) {
        switch (channel) {
            case ZAP_CHANNEL_HORI:
            case ZAP_CHANNEL_VERT:
            case ZAP_CHANNEL_LARGE_HORI_SMALL_VERT:
            case ZAP_CHANNEL_LARGE_VERT_SMALL_HORI:
            case ZAP_CHANNEL_NEXT:
                break;
            default:
                throw new IllegalArgumentException("Invalid camera channel mode");
        }        
        command="AT*CONFIG="+SEQ+",\"video:video_channel\",\"" + channel + "\"";
        continuance=false;
        //setCommand("AT*ZAP="+SEQ+",0", false);
    }
	
	public void landing() {
		System.out.println("*** Landing");
		command="AT*REF=" + SEQ + ",290717696";
		continuance=false;
		//setCommand("AT*REF=" + SEQ + ",290717696", false);
		landing=true;		
	}

	
	public void takeOff() {
		System.out.println("*** Take off");
		sendCommand("AT*FTRIM="+SEQ);
		command="AT*REF=" + SEQ + ",290718208";
		continuance=false;
		//setCommand("AT*REF=" + SEQ + ",290718208", false);
		landing=false;		
	}

	
	public void reset() {
		System.out.println("*** Reset");
		command="AT*REF="+SEQ+",290717952";
		continuance=true;
		//setCommand("AT*REF="+SEQ+",290717952", true);
		landing=true;		
	}

	
	public void forward() {
		command="AT*PCMD="+SEQ+",1,0,"+intOfFloat(-speed)+",0,0"+"\r"+"AT*REF=" + SEQ + ",290718208";
		continuance=true;
		//setCommand("AT*PCMD="+SEQ+",1,0,"+intOfFloat(-speed)+",0,0"+"\r"+"AT*REF=" + SEQ + ",290718208", true);
	}

	
	public void forward(int speed) {
		setSpeed(speed);
		forward();
	}

	
	public void backward() {
		command="AT*PCMD="+SEQ+",1,0,"+intOfFloat(speed)+",0,0"+"\r"+"AT*REF=" + SEQ + ",290718208";
		continuance=true;
	}

	
	public void backward(int speed) {
		setSpeed(speed);
		backward();
	}

	
	public void spinRight() {
		command="AT*PCMD=" + SEQ + ",1,0,0,0," + intOfFloat(speed)+"\r"+"AT*REF=" + SEQ + ",290718208";
		continuance=true;
	}

	
	public void spinRight(int speed) {
		setSpeed(speed);
		spinRight();
	}

	
	public void spinLeft() {
		command="AT*PCMD=" + SEQ + ",1,0,0,0," + intOfFloat(-speed)+"\r"+"AT*REF=" + SEQ + ",290718208";
		continuance=true;
	}

	
	public void spinLeft(int speed) {
		setSpeed(speed);
		spinLeft();
	}

	
	public void up() {
		System.out.println("*** Up");
		command="AT*PCMD="+SEQ+",1,"+intOfFloat(0)+","+intOfFloat(0)+","+intOfFloat(speed)+","+intOfFloat(0)+"\r"+"AT*REF="+SEQ+",290718208";
		continuance=true;
	}

	
	public void up(int speed) {
		setSpeed(speed);
		up();
	}

	
	public void down() {
		System.out.println("*** Down");
		command="AT*PCMD="+SEQ+",1,"+intOfFloat(0)+","+intOfFloat(0)+","+intOfFloat(-speed)+","+intOfFloat(0)+"\r"+"AT*REF="+SEQ+",290718208";
		continuance=true;
	}

	
	public void down(int speed) {
		setSpeed(speed);
		down();
	}

	
	public void goRight() {
		command="AT*PCMD="+SEQ+",1,"+intOfFloat(speed)+",0,0,0"+"\r"+"AT*REF=" + SEQ + ",290718208";
		continuance=true;
	}

	
	public void goRight(int speed) {
		setSpeed(speed);
		goRight();
	}

	
	public void goLeft() {
		command="AT*PCMD="+SEQ+",1,"+intOfFloat(-speed)+",0,0,0"+"\r"+"AT*REF=" + SEQ + ",290718208";
		continuance=true;
	}

	
	public void goLeft(int speed) {
		setSpeed(speed);
		goLeft();
	}

	
	
	public void stop() {
		System.out.println("*** Stop (Hover)");
		command="AT*PCMD="+SEQ+",1,0,0,0,0";
		continuance=true;
	}

	
	public void setSpeed(int speed) {
		if(speed>100)
			speed=100;
		else if(speed<1)
			speed=1;

		this.speed=(float) (speed/100.0);
	}

	public void getDroneConfiguration()
	{
		command= "AT*CTRL="+SEQ+",5,0" + CR + "AT*CTRL="+SEQ+",4,0" + CR;
		continuance=false;
	}

	public void setVideoCodecFps(int fps){
		if (fps < H264_MIN_FPS)
		{
		    fps = H264_MIN_FPS;
		}
		else if (fps > H264_MAX_FPS)
		{
			fps = H264_MAX_FPS;
		}
		command="AT*CONFIG="+SEQ+",\"VIDEO:codec_fps\",\"" + fps + "\"" +CR+"AT*FTRIM="+SEQ;
		continuance=false;
	}
	
	/**
	 * Sets the automatic bitrate control of the video stream. Possible values:
	 *
	 * @param mode: VBC_MODE_DISABLED / VBC_MODE_DYNAMIC / VBC_MANUAL
	 */
	public void setVideoBitrateControl(int mode) {
	    if (mode != VBC_MODE_DISABLED && mode != VBC_MODE_DYNAMIC && mode != VBC_MANUAL) {
	        throw new IllegalArgumentException("Invalid bitrate control mode");
	    }
	    command = "AT*CONFIG=" + SEQ + ",\"VIDEO:bitrate_control_mode\",\"" + mode + "\"" + CR + "AT*FTRIM=" + SEQ;
	    continuance = false;
	}

	public void setVideoBitrate(int bitRate){
		if (bitRate < H264_MIN_BITRATE)
		{
			bitRate = H264_MIN_BITRATE;
		}
		else if (bitRate > H264_MAX_BITRATE)
		{
			bitRate = H264_MAX_BITRATE;
		}

		command="AT*CONFIG="+SEQ+",\"VIDEO:bitrate\",\"" + bitRate + "\"" + CR + "AT*FTRIM="+SEQ;
		continuance=false;
	}

	public void setVideoCodec(int codec){
        switch (codec) {
            case NULL_CODEC:
            case UVLC_CODEC:
            case P264_CODEC:
            case MP4_360P_CODEC:
            case H264_360P_CODEC:
            case MP4_360P_H264_720P_CODEC:
            case H264_720P_CODEC:
            case MP4_360P_SLRS_CODEC:
            case H264_360P_SLRS_CODEC:
            case H264_720P_SLRS_CODEC:
            case H264_AUTO_RESIZE_CODEC:
            case MP4_360P_H264_360P_CODEC:
                break;
            default:
                throw new IllegalArgumentException("Invalid video codec");
        }
		command="AT*CONFIG="+SEQ+",\"VIDEO:video_codec\",\"" + codec + "\""+CR+"AT*FTRIM="+SEQ;
		continuance=false;
	}

	public void enableVideoData(){
		command="AT*CONFIG="+SEQ+",\"general:video_enable\",\"TRUE\""+CR+"AT*FTRIM="+SEQ;
		continuance=false;
		//setCommand("AT*CONFIG="+SEQ+",\"general:video_enable\",\"TRUE\""+CR+"AT*FTRIM="+SEQ, false);
	}
	
	public void setExtendedNavData(boolean b){
		command="AT*CONFIG="+SEQ+",\"general:navdata_demo\",\"" + "FALSE" + "\""+CR+"AT*FTRIM="+SEQ;
		continuance=false;
		//setCommand("AT*CONFIG="+SEQ+",\"general:navdata_demo\",\"TRUE\""+CR+"AT*FTRIM="+SEQ, false);
	}

	public void sendControlAck(){
		command="AT*CTRL="+SEQ+",0";
		continuance=false;
		//setCommand("AT*CTRL="+SEQ+",0", false);
	}
	
	public int getSpeed(){
		return (int) (speed*100);
	}
	
	public void setMaxAltitude(int altitude){
		command="AT*CONFIG="+SEQ+",\"control:altitude_max\",\""+altitude+"\"";
		continuance=false;
	}
	
	public void setMinAltitude(int altitude){
		command="AT*CONFIG="+SEQ+",\"control:altitude_min\",\""+altitude+"\"";
		continuance=false;
	}

	/*
	 * Thanks, Tarquínio.
	 */
	public void move3D(int speedX, int speedY, int speedZ, int speedSpin) {
		if(speedX>100)
			speedX=100;
		else if(speedX<-100)
			speedX=-100;
		if(speedY>100)
			speedY=100;
		else if(speedY<-100)
			speedY=-100;
		if(speedZ>100)
			speedZ=100;
		else if(speedZ<-100)
			speedZ=-100;
		
		command="AT*PCMD="+SEQ+",1,"+intOfFloat(-speedY/100.0f)+","+intOfFloat(-speedX/100.0f)+","+intOfFloat(-speedZ/100.0f)+","+intOfFloat(-speedSpin/100.0f)+"\r"+"AT*REF="+SEQ+",290718208";
		continuance=true;
	}
	
	@Override
	public void run() {
		initARDrone();
		while(!doStop){
			if(this.command!=null){
				sendCommand();
				if(!continuance){
					command=null;
				}
			}else{
				if(landing){
					sendCommand("AT*PCMD="+SEQ+",1,0,0,0,0"+CR+"AT*REF="+SEQ+",290717696");
				}else{
					sendCommand("AT*PCMD="+SEQ+",1,0,0,0,0"+CR+"AT*REF="+SEQ+",290718208");
				}
			}
			if(seq%5==0){//<2000ms
				sendCommand("AT*COMWDG="+SEQ);
			}
		}
	}
	

	private void initialize(){
		ByteBuffer bb=ByteBuffer.allocate(4);
		fb=bb.asFloatBuffer();
		ib=bb.asIntBuffer();
	}
	
	private void initARDrone(){
		sendCommand("AT*CONFIG="+SEQ+",\"general:navdata_demo\",\"TRUE\""+CR+"AT*FTRIM="+SEQ);//1
		sendCommand("AT*PMODE="+SEQ+",2"+CR+"AT*MISC="+SEQ+",2,20,2000,3000"+CR+"AT*FTRIM="+SEQ+CR+"AT*REF="+SEQ+",290717696");//2-5
		sendCommand("AT*PCMD="+SEQ+",1,0,0,0,0"+CR+"AT*REF="+SEQ+",290717696"+CR+"AT*COMWDG="+SEQ);//6-8
		sendCommand("AT*PCMD="+SEQ+",1,0,0,0,0"+CR+"AT*REF="+SEQ+",290717696"+CR+"AT*COMWDG="+SEQ);//6-8
		sendCommand("AT*FTRIM="+SEQ);
		System.out.println("Initialize completed!");
	}
	
	/*private void setCommand(String command, boolean continuance){
		this.command=command;
		this.continuance=continuance;
	}*/

	
	private void sendCommand(){
		sendCommand(this.command);
	}
	
	private synchronized void sendCommand(String command){
		/**
		 * Each command needs an individual sequence number (this also holds for Hover/Stop commands)
		 * At first, only a placeholder is set for every command and this placeholder is replaced with a real sequence number below.
		 * Because one command string may contain chained commands (e.g. "AT...AT...AT...) the replacement needs to be done individually for every 'subcommand'
		 */
		int seqIndex = -1;
		while ((seqIndex = command.indexOf(SEQ)) != -1)
		{
			command = command.substring(0, seqIndex) + (seq++) + command.substring(seqIndex + SEQ.length());
		} 
		
		byte[] buffer=(command+CR).getBytes();
//		System.out.println(command);
		DatagramPacket packet=new DatagramPacket(buffer, buffer.length, inetaddr, 5556);
		try {
			socket.send(packet);
			Thread.sleep(20);//<50ms			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private int intOfFloat(float f) {
		fb.put(0, f);
		return ib.get(0);
	}
}