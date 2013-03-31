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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.shigeodayo.ardrone.manager.AbstractManager;
import com.shigeodayo.ardrone.utils.ARDroneUtils;

public class CommandManager extends AbstractManager {

	// TODO replace by PriorityBlockingQueue
	private BlockingQueue<ATCommand> q;

	private static int seq = 1;

	/** speed */
	private float speed = 0.05f;// 0.01f - 1.0f

	public CommandManager(InetAddress inetaddr) {
		super(inetaddr);
		this.q = new ArrayBlockingQueue<ATCommand>(100);
		initARDrone();
	}

	public void resetCommunicationWatchDog() {
		q.add(new KeepAliveCommand());
	}

	public void setVideoChannel(VideoChannel c) {
		q.add(new VideoChannelCommand(c));
	}

	public void landing() {
		q.add(new LandCommand());
	}

	public void flatTrim() {
		q.add(new FlatTrimCommand());
	}

	public void takeOff() {
		flatTrim();
		q.add(new TakeOffCommand());
	}

	public void reset() {
		q.add(new EmergencyCommand());
	}

	public void forward() {
		move(0f, -speed, 0f, 0f);
	}

	public void forward(int speed) {
		setSpeed(speed);
		forward();
	}

	public void backward() {
		move(0f, speed, 0f, 0f);
	}

	public void backward(int speed) {
		setSpeed(speed);
		backward();
	}

	public void spinRight() {
		move(0f, 0f, 0f, speed);
	}

	public void spinRight(int speed) {
		setSpeed(speed);
		spinRight();
	}

	public void spinLeft() {
		move(0f, 0f, 0f, -speed);
	}

	public void spinLeft(int speed) {
		setSpeed(speed);
		spinLeft();
	}

	public void up() {
		System.out.println("*** Up");
		move(0f, 0f, speed, 0f);
	}

	public void up(int speed) {
		setSpeed(speed);
		up();
	}

	public void down() {
		System.out.println("*** Down");
		move(0f, 0f, -speed, 0f);
	}

	public void down(int speed) {
		setSpeed(speed);
		down();
	}

	public void goRight() {
		move(speed, 0f, 0f, 0f);
	}

	public void goRight(int speed) {
		setSpeed(speed);
		goRight();
	}

	public void goLeft() {
		move(-speed, 0f, 0f, 0f);
	}

	public void goLeft(int speed) {
		setSpeed(speed);
		goLeft();
	}

	public void stop() {
		q.add(new MoveCommand(false, 0f, 0f, 0f, 0f));
	}

	public void setSpeed(int speed) {
		if (speed > 100)
			speed = 100;
		else if (speed < 1)
			speed = 1;

		this.speed = (float) (speed / 100.0);
	}

	public void getDroneConfiguration() {
		q.add(new ControlCommand(5, 0));
	}

	public void setVideoCodecFps(int fps) {
		fps = limit(fps, H264.MIN_FPS, H264.MAX_FPS);
		q.add(new ConfigureCommand("video:codec_fps", fps));
	}

	/**
	 * Sets the automatic bitrate control of the video stream.
	 */
	public void setVideoBitrateControl(VideoBitRateMode mode) {
		q.add(new ConfigureCommand("video:bitrate_control_mode", mode.ordinal()));
	}

	public void setVideoBitrate(int rate) {
		rate = limit(rate, H264.MIN_BITRATE, H264.MAX_BITRATE);
		q.add(new ConfigureCommand("video:bitrate", rate));
	}

	public void setMaxVideoBitrate(int rate) {
		rate = limit(rate, H264.MIN_BITRATE, H264.MAX_BITRATE);
		q.add(new ConfigureCommand("video:max_bitrate", rate));
	}

	public void setVideoCodec(VideoCodec c) {
		q.add(new ConfigureCommand("video:video_codec", c.getValue()));
	}

	public void setVideoOnUsb(boolean b) {
		q.add(new ConfigureCommand("video:video_on_usb", b));
	}

	public void setVideoData(boolean b) {
		q.add(new ConfigureCommand("general:video_enable", b));
	}

	public void setExtendedNavData(boolean b) {
		q.add(new ConfigureCommand("general:navdata_demo", !b));
	}

	public void setNavDataOptions(int mask) {
		q.add(new ConfigureCommand("general:navdata_options", mask));
	}

	public void setLedsAnimation(LEDAnimation anim, float freq, int duration) {
		q.add(new LEDAnimationCommand(anim, freq, duration));
	}

	public void setDetectEnemyWithoutShell(boolean b) {
		q.add(new ConfigureCommand("detect:enemy_colors", (b ? "1" : "0")));
	}

	// TODO Developer guide uses hex representation in the command;
	// is this necessary?
	public void setGroundStripeColors(GroundStripeColor c) {
		q.add(new ConfigureCommand("detect:enemy_colors", c.getValue()));
	}

	public void setEnemyColors(EnemyColor c) {
		q.add(new ConfigureCommand("detect:enemy_colors", c.getValue()));
	}

	public void setVerticalDetectionType(int mask) {
		q.add(new ConfigureCommand("detect:detect_type", 10));
		q.add(new ConfigureCommand("detect:detections_select_v", mask));
	}

	public void setHorizonalDetectionType(int mask) {
		q.add(new ConfigureCommand("detect:detect_type", 10));
		q.add(new ConfigureCommand("detect:detections_select_h", mask));
	}

	public void setVerticalHsyncDetectionType(int mask) {
		q.add(new ConfigureCommand("detect:detect_type", 10));
		q.add(new ConfigureCommand("detect:detections_select_v_hsync", mask));
	}

	public void setVisionParameters(int coarse_scale, int nb_pair, int loss_per, int nb_tracker_width,
			int nb_tracker_height, int scale, int trans_max, int max_pair_dist, int noise) {
		q.add(new VisionParametersCommand(coarse_scale, nb_pair, loss_per, nb_tracker_width, nb_tracker_height, scale,
				trans_max, max_pair_dist, noise));
	}

	// TODO find out if still supported in Drone 2.0 and what are the options
	public void setVisionOption(int option) {
		q.add(new VisionOptionCommand(option));
	}

	// TODO find out if still supported in Drone 2.0
	public void setGains(int pq_kp, int r_kp, int r_ki, int ea_kp, int ea_ki, int alt_kp, int alt_ki, int vz_kp,
			int vz_ki, int hovering_kp, int hovering_ki, int hovering_b_kp, int hovering_b_ki) {
		q.add(new GainsCommand(pq_kp, r_kp, r_ki, ea_kp, ea_ki, alt_kp, alt_ki, vz_kp, vz_ki, hovering_kp, hovering_ki,
				hovering_b_kp, hovering_b_ki));
	}

	// TODO find out if still supported in Drone 2.0
	public void setRawCapture(boolean picture, boolean video) {
		q.add(new RawCaptureCommand(picture, video));
	}

	public void setEnableCombinedYaw(boolean b) {
		int level = 1;
		if (b) {
			level |= (1 << 2);
		}
		q.add(new ConfigureCommand("control:control_level", level));
	}

	public void setFlyingMode(FlyingMode mode) {
		q.add(new ConfigureCommand("control:flying_mode", mode.ordinal()));
	}

	public void setHoveringRange(int range) {
		q.add(new ConfigureCommand("control:hovering_range", range));
	}

	public void sendControlAck() {
		q.add(new ControlCommand(4, 0));
	}

	public int getSpeed() {
		return (int) (speed * 100);
	}

	public void setMaxEulerAngle(float angle) {
		setMaxEulerAngle(Location.CURRENT, angle);
	}

	public void setMaxEulerAngle(Location l, float angle) {
		String command = "control:" + l.getCommandPrefix() + "euler_angle_max";
		q.add(new ConfigureCommand(command, String.valueOf(angle)));
	}

	public void setMaxAltitude(int altitude) {
		setMaxAltitude(Location.CURRENT, altitude);
	}

	public void setMaxAltitude(Location l, int altitude) {
		String command = "control:" + l.getCommandPrefix() + "altitude_max";
		q.add(new ConfigureCommand(command, altitude));
	}

	public void setMinAltitude(int altitude) {
		setMinAltitude(Location.CURRENT, altitude);
	}

	public void setMinAltitude(Location l, int altitude) {
		String command = "control:" + l.getCommandPrefix() + "altitude_min";
		q.add(new ConfigureCommand(command, altitude));
	}

	public void setMaxVz(int speed) {
		setMaxVz(Location.CURRENT, speed);
	}

	public void setMaxVz(Location l, int speed) {
		String command = "control:" + l.getCommandPrefix() + "control_vz_max";
		q.add(new ConfigureCommand(command, speed));
	}

	public void setMaxYaw(int speed) {
		setMaxYaw(Location.CURRENT, speed);
	}

	public void setMaxYaw(Location l, int speed) {
		String command = "control:" + l.getCommandPrefix() + "control_yaw";
		q.add(new ConfigureCommand(command, speed));
	}

	public void setOutdoor(boolean flying_outdoor, boolean outdoor_hull) {
		q.add(new ConfigureCommand("control:outdoor", flying_outdoor));
		q.add(new ConfigureCommand("control:flight_without_shell", outdoor_hull));
	}

	public void setAutonomousFlight(boolean b) {
		q.add(new ConfigureCommand("control:autonomous_flight", b));
	}

	public void setManualTrim(boolean b) {
		q.add(new ConfigureCommand("control:manual_trim", b));
	}

	public void setPhoneTilt(float tilt) {
		q.add(new ConfigureCommand("control:control_iphone_tilt", String.valueOf(tilt)));
	}

	public void animate(FlightAnimation a) {
		q.add(new FlightAnimationCommand(a));
	}

	public void move(float lrtilt, float fbtilt, float vspeed, float aspeed, float magneto_psi,
			float magneto_psi_accuracy) {
		lrtilt = limit(lrtilt, -1f, 1f);
		fbtilt = limit(fbtilt, -1f, 1f);
		vspeed = limit(vspeed, -1f, 1f);
		aspeed = limit(aspeed, -1f, 1f);
		magneto_psi = limit(magneto_psi, -1f, 1f);
		magneto_psi_accuracy = limit(magneto_psi_accuracy, -1f, 1f);
		q.add(new PCMDMagCommand(false, false, true, lrtilt, fbtilt, vspeed, aspeed, magneto_psi, magneto_psi_accuracy));
	}

	public void move(float lrtilt, float fbtilt, float vspeed, float aspeed) {
		lrtilt = limit(lrtilt, -1f, 1f);
		fbtilt = limit(fbtilt, -1f, 1f);
		vspeed = limit(vspeed, -1f, 1f);
		aspeed = limit(aspeed, -1f, 1f);
		q.add(new MoveCommand(false, lrtilt, fbtilt, vspeed, aspeed));
	}

	public void move(int speedX, int speedY, int speedZ, int speedSpin) {
		move(-speedY / 100.0f, -speedX / 100.0f, -speedZ / 100.0f, -speedSpin / 100.0f);
	}

	public void setPosition(double latitude, double longitude, double altitude) {
		q.add(new ConfigureCommand("gps:latitude", latitude));
		q.add(new ConfigureCommand("gps:longitude", longitude));
		q.add(new ConfigureCommand("gps:altitude", altitude));
	}

	public void setUltrasoundFrequency(UltrasoundFrequency f) {
		q.add(new ConfigureCommand("pic:ultrasound_freq", f.getValue()));
	}

	public void setSSIDSinglePlayer(String ssid) {
		q.add(new ConfigureCommand("network:ssid_single_player", ssid));
	}

	public void setSSIDMultiPlayer(String ssid) {
		q.add(new ConfigureCommand("network:ssid_multi_player", ssid));
	}

	public void setWifiMode(WifiMode mode) {
		q.add(new ConfigureCommand("network:wifi_mode", mode.ordinal()));
	}

	public void setOwnerMac(String mac) {
		q.add(new ConfigureCommand("network:owner_mac", mac));
	}

	// AT*MISC undocumented, but needed to initialize
	// see https://github.com/bklang/ARbDrone/wiki/UndocumentedCommands
	private void sendMisc(int p1, int p2, int p3, int p4) {
		q.add(new MiscCommand(p1, p2, p3, p4));
	}

	// AT*PMODE undocumented, but needed to initialize
	// see https://github.com/bklang/ARbDrone/wiki/UndocumentedCommands
	private void sendPMode(int mode) {
		q.add(new PMODECommand(mode));
	}

	@Override
	public void run() {
		ATCommand c;
		while (!doStop) {
			try {
				c = q.poll();
				if (c != null) {
					sendCommand(c);
					if (c.isSticky() && q.isEmpty()) {
						q.add(c);
					}
				}
			} catch (InterruptedException e) {
				doStop = true;
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	private void initARDrone() {
		// pmode parameter and first misc parameter are related
		sendPMode(2);
		sendMisc(2, 20, 2000, 3000);
		stop();
		landing();
		System.out.println("Initialize completed!");
	}

	private synchronized void sendCommand(ATCommand c) throws InterruptedException, IOException {
		/**
		 * Each command needs an individual sequence number (this also holds for Hover/Stop commands) At first, only a
		 * placeholder is set for every command and this placeholder is replaced with a real sequence number below.
		 * Because one command string may contain chained commands (e.g. "AT...AT...AT...) the replacement needs to be
		 * done individually for every 'subcommand'
		 */
		System.out.println(c.getCommandString(seq));
		byte[] buffer = c.getPacket(seq++);
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetaddr, ARDroneUtils.PORT);
		socket.send(packet);
		// TODO: sleep needed???
		Thread.sleep(20);// <50ms
	}

	private int limit(int i, int min, int max) {
		return (i > max ? max : (i < min ? min : i));
	}

	private float limit(float f, float min, float max) {
		return (f > max ? max : (f < min ? min : f));
	}

}
