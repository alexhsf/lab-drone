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
package com.shigeodayo.ardrone;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import com.shigeodayo.ardrone.command.CommandManager;
import com.shigeodayo.ardrone.command.VideoChannel;
import com.shigeodayo.ardrone.configuration.ConfigurationManager;
import com.shigeodayo.ardrone.navdata.NavDataManager;
import com.shigeodayo.ardrone.utils.ARDroneUtils;
import com.shigeodayo.ardrone.video.VideoDecoder;
import com.shigeodayo.ardrone.video.VideoManager;

public class ARDrone implements ARDroneInterface {

	/** default ip address */
	private static final String IP_ADDRESS = "192.168.1.1";

	private String ipaddr = null;
	private InetAddress inetaddr = null;
	private VideoDecoder videoDecoder = null;

	// managers
	private CommandManager commandManager = null;
	private VideoManager videoManager = null;
	private NavDataManager navdataManager = null;
	private ConfigurationManager configurationManager = null;

	/** constructor */
	public ARDrone() {
		this(IP_ADDRESS, null);
	}

	/**
	 * constructor
	 * 
	 * @param ipaddr
	 */
	public ARDrone(String ipaddr, VideoDecoder videoDecoder) {
		this.ipaddr = ipaddr;
		this.videoDecoder = videoDecoder;
	}

	public CommandManager getCommandManager() {
		if (commandManager == null) {
			InetAddress ia = getInetAddress();
			commandManager = new CommandManager(ia);
		}
		return commandManager;
	}

	public NavDataManager getNavDataManager() {
		if (navdataManager == null) {
			InetAddress ia = getInetAddress();
			CommandManager cm = getCommandManager();
			navdataManager = new NavDataManager(ia, cm);
		}
		return navdataManager;
	}

	public VideoManager getVideoManager() {
		if (videoManager == null) {
			InetAddress ia = getInetAddress();
			CommandManager cm = getCommandManager();
			videoManager = new VideoManager(ia, cm, videoDecoder);
		}
		return videoManager;
	}

	public ConfigurationManager getConfigurationManager() {
		if (configurationManager == null) {
			InetAddress ia = getInetAddress();
			CommandManager cm = getCommandManager();
			configurationManager = new ConfigurationManager(ia, cm);
		}
		return configurationManager;
	}

	@Override
	public boolean connect() {
		CommandManager cm = getCommandManager();		
		if (!cm.isConnected()) {
			return cm.connect(ARDroneUtils.PORT);
		}
		return true;
	}

	@Override
	public boolean connectVideo() {
		VideoManager vm = getVideoManager();		
		if (!vm.isConnected()) {
			return vm.connect(ARDroneUtils.VIDEO_PORT);
		}
		return true;
	}

	@Override
	public boolean connectNav() {
		NavDataManager nm = getNavDataManager();
		if (!nm.isConnected()) {
			return nm.connect(ARDroneUtils.NAV_PORT);
		}		
		return true;
	}

	@Override
	public void disconnect() {
		stop();
		landing();
		getCommandManager().close();
		getNavDataManager().close();
		getVideoManager().close();
	}

	@Override
	public void start() {
		CommandManager cm = getCommandManager();
		new Thread(cm, cm.getClass().getName()).start();
		NavDataManager nm = getNavDataManager();
		new Thread(nm, nm.getClass().getName()).start();
		VideoManager vm = getVideoManager();
		new Thread(vm, vm.getClass().getName()).start();
	}

	@Override
	public void setHorizontalCamera() {
		if (commandManager != null)
			commandManager.setVideoChannel(VideoChannel.HORI);
	}

	@Override
	public void setVerticalCamera() {
		if (commandManager != null)
			commandManager.setVideoChannel(VideoChannel.VERT);
	}

	@Override
	public void setHorizontalCameraWithVertical() {
		if (commandManager != null)
			commandManager.setVideoChannel(VideoChannel.LARGE_HORI_SMALL_VERT);
	}

	@Override
	public void setVerticalCameraWithHorizontal() {
		if (commandManager != null)
			commandManager.setVideoChannel(VideoChannel.LARGE_VERT_SMALL_HORI);
	}

	@Override
	public void toggleCamera() {
		if (commandManager != null)
			commandManager.setVideoChannel(VideoChannel.NEXT);
	}

	@Override
	public void landing() {
		if (commandManager != null)
			commandManager.landing();
	}

	@Override
	public void takeOff() {
		if (commandManager != null)
			commandManager.takeOff();
	}

	@Override
	public void reset() {
		if (commandManager != null)
			commandManager.reset();
	}

	@Override
	public void forward() {
		if (commandManager != null)
			commandManager.forward();
	}

	@Override
	public void forward(int speed) {
		if (commandManager != null)
			commandManager.forward(speed);
	}

	@Override
	public void backward() {
		if (commandManager != null)
			commandManager.backward();
	}

	@Override
	public void backward(int speed) {
		if (commandManager != null)
			commandManager.backward(speed);
	}

	@Override
	public void spinRight() {
		if (commandManager != null)
			commandManager.spinRight();
	}

	@Override
	public void spinRight(int speed) {
		if (commandManager != null)
			commandManager.spinRight(speed);
	}

	@Override
	public void spinLeft() {
		if (commandManager != null)
			commandManager.spinLeft();
	}

	@Override
	public void spinLeft(int speed) {
		if (commandManager != null)
			commandManager.spinLeft(speed);
	}

	@Override
	public void up() {
		if (commandManager != null)
			commandManager.up();
	}

	@Override
	public void up(int speed) {
		if (commandManager != null)
			commandManager.up(speed);
	}

	@Override
	public void down() {
		if (commandManager != null)
			commandManager.down();
	}

	@Override
	public void down(int speed) {
		if (commandManager != null)
			commandManager.down(speed);
	}

	@Override
	public void goRight() {
		if (commandManager != null)
			commandManager.goRight();
	}

	@Override
	public void goRight(int speed) {
		if (commandManager != null)
			commandManager.goRight(speed);
	}

	@Override
	public void goLeft() {
		if (commandManager != null)
			commandManager.goLeft();
	}

	@Override
	public void goLeft(int speed) {
		if (commandManager != null)
			commandManager.goLeft(speed);
	}

	@Override
	public void setSpeed(int speed) {
		if (commandManager != null)
			commandManager.setSpeed(speed);
	}

	@Override
	public void stop() {
		if (commandManager != null)
			commandManager.stop();
	}

	/**
	 * 0.01-1.0 -> 1-100%
	 * 
	 * @return 1-100%
	 */
	@Override
	public int getSpeed() {
		if (commandManager == null)
			return -1;
		return commandManager.getSpeed();
	}

	@Override
	public void setMaxAltitude(int altitude) {
		if (commandManager != null)
			commandManager.setMaxAltitude(altitude);
	}

	@Override
	public void setMinAltitude(int altitude) {
		if (commandManager != null)
			commandManager.setMinAltitude(altitude);
	}

	@Override
	public void move3D(int speedX, int speedY, int speedZ, int speedSpin) {
		if (commandManager != null)
			commandManager.move(speedX, speedY, speedZ, speedSpin);
	}

	/**
	 * print error message
	 * 
	 * @param message
	 * @param obj
	 */
	public static void error(String message, Object obj) {
		System.err.println("[" + obj.getClass() + "] " + message);
	}

	private InetAddress getInetAddress() {
		if (inetaddr == null) {
			StringTokenizer st = new StringTokenizer(ipaddr, ".");
			byte[] ipBytes = new byte[4];
			if (st.countTokens() == 4) {
				for (int i = 0; i < 4; i++) {
					ipBytes[i] = (byte) Integer.parseInt(st.nextToken());
				}
			} else {
				error("Incorrect IP address format: " + ipaddr, this);
				return null;
			}
			try {
				inetaddr = InetAddress.getByAddress(ipBytes);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return inetaddr;
	}

}