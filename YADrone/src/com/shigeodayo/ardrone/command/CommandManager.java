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
import java.util.concurrent.TimeUnit;

import com.shigeodayo.ardrone.manager.AbstractManager;
import com.shigeodayo.ardrone.utils.ARDroneUtils;

public class CommandManager extends AbstractManager {

	// TODO replace by PriorityBlockingQueue
	private BlockingQueue<QueueCommand> q;

	private static final String CR = "\r";

	private static final String SEQ = "$SEQ$";

	private static final String ATLAND = "AT*REF=" + SEQ + ",290717696";
	private static final String ATRESET = "AT*REF=" + SEQ + ",290717952";
	private static final String ATTAKEOFF = "AT*REF=" + SEQ + ",290718208";

	private static int seq = 1;

	/** speed */
	private float speed = 0.05f;// 0.01f - 1.0f

	public CommandManager(InetAddress inetaddr) {
		super(inetaddr);
		this.q = new ArrayBlockingQueue<QueueCommand>(100);
		initARDrone();
	}

	public void resetCommunicationWatchDog() {
		q.add(new QueueCommand(false, "AT*COMWDG=" + SEQ));
	}

	public void setVideoChannel(VideoChannel c) {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"video:video_channel\",\"" + c.ordinal() + "\""));
	}

	public void landing() {
		System.out.println("*** Landing");
		q.add(new QueueCommand(false, ATLAND));
	}

	public void trim() {
		q.add(new QueueCommand(false, "AT*FTRIM=" + SEQ));
	}

	public void takeOff() {
		System.out.println("*** Take off");
		trim();
		q.add(new QueueCommand(false, ATTAKEOFF));
	}

	public void reset() {
		System.out.println("*** Reset");
		q.add(new QueueCommand(true, ATRESET));
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
		System.out.println("*** Stop (Hover)");
		q.add(new QueueCommand(true, "AT*PCMD=" + SEQ + ",1,0,0,0,0"));
	}

	public void setSpeed(int speed) {
		if (speed > 100)
			speed = 100;
		else if (speed < 1)
			speed = 1;

		this.speed = (float) (speed / 100.0);
	}

	public void getDroneConfiguration() {
		q.add(new QueueCommand(false, "AT*CTRL=" + SEQ + ",5,0" + CR + "AT*CTRL=" + SEQ + ",4,0" + CR));
	}

	public void setVideoCodecFps(int fps) {
		fps = limit(fps, H264.MIN_FPS, H264.MAX_FPS);
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"VIDEO:codec_fps\",\"" + fps + "\"" + SEQ));
	}

	/**
	 * Sets the automatic bitrate control of the video stream. Possible values:
	 * 
	 * @param mode
	 *            : VBC_MODE_DISABLED / VBC_MODE_DYNAMIC / VBC_MANUAL
	 */
	public void setVideoBitrateControl(VideoBitRateMode mode) {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"VIDEO:bitrate_control_mode\",\"" + mode.ordinal() + "\""));
	}

	public void setVideoBitrate(int rate) {
		rate = limit(rate, H264.MIN_BITRATE, H264.MAX_BITRATE);
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"VIDEO:bitrate\",\"" + rate + "\""));
	}

	public void setVideoCodec(VideoCodec c) {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"VIDEO:video_codec\",\"" + c.getValue() + "\""));
	}

	public void enableVideoData() {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"general:video_enable\",\"TRUE\""));
	}

	public void setExtendedNavData(boolean b) {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"general:navdata_demo\",\"" + String.valueOf(!b) + "\""));
	}

	public void setNavDataOptions(int mask) {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"general:navdata_options\",\"" + mask + "\""));
	}

	public void setLedsAnimation(LEDAnimation anim, float freq, int duration) {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"leds:leds_anim\",\"" + anim.ordinal() + ","
				+ float2Int(freq) + "," + duration + "\""));
	}

	public void setDetectEnemyWithoutShell(boolean b) {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"detect:enemy_colors\",\"" + (b ? "1" : "0") + "\""));
	}

	// TODO Developer guide uses hex representation in the command;
	// is this necessary?
	public void setGroundStripeColors(GroundStripeColor c) {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"detect:enemy_colors\",\"" + c.getValue() + "\""));
	}

	public void setEnemyColors(EnemyColor c) {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"detect:enemy_colors\",\"" + c.getValue() + "\""));
	}

	public void setVerticalDetectionType(int mask) {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"detect:detect_type\",\"" + 10 + "\""));
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"detect:detections_select_v\",\"" + mask + "\""));
	}

	public void setHorizonalDetectionType(int mask) {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"detect:detect_type\",\"" + 10 + "\""));
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"detect:detections_select_h\",\"" + mask + "\""));
	}

	public void setVerticalHsyncDetectionType(int mask) {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"detect:detect_type\",\"" + 10 + "\""));
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"detect:detections_select_v_hsync\",\"" + mask + "\""));
	}

	public void setFlyingMode(FlyingMode mode) {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"control:flying_mode\",\"" + mode.ordinal() + "\""));
	}

	public void sendControlAck() {
		q.add(new QueueCommand(false, "AT*CTRL=" + SEQ + ",0"));
	}

	public int getSpeed() {
		return (int) (speed * 100);
	}

	public void setMaxAltitude(int altitude) {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"control:altitude_max\",\"" + altitude + "\""));
	}

	public void setMinAltitude(int altitude) {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"control:altitude_min\",\"" + altitude + "\""));
	}

	public void animate(FlightAnimation a) {
		q.add(new QueueCommand(false, "AT*CONFIG=" + SEQ + ",\"control:flight_anim\",\"" + a.ordinal() + "\""));
	}

	private int limit(int i, int min, int max) {
		return (i > max ? max : (i < min ? min : i));
	}

	private float limit(float f, float min, float max) {
		return (f > max ? max : (f < min ? min : f));
	}

	public void move(float lrtilt, float fbtilt, float vspeed, float aspeed) {
		lrtilt = limit(lrtilt, -1f, 1f);
		fbtilt = limit(fbtilt, -1f, 1f);
		vspeed = limit(vspeed, -1f, 1f);
		aspeed = limit(aspeed, -1f, 1f);
		q.add(new QueueCommand(true, "AT*PCMD=" + SEQ + ",1," + float2Int(lrtilt) + "," + float2Int(fbtilt) + ","
				+ float2Int(vspeed) + "," + float2Int(aspeed)));
	}

	public void move(int speedX, int speedY, int speedZ, int speedSpin) {
		move(-speedY / 100.0f, -speedX / 100.0f, -speedZ / 100.0f, -speedSpin / 100.0f);
	}

	// AT*MISC undocumented, but needed to initialize
	// see https://github.com/bklang/ARbDrone/wiki/UndocumentedCommands
	private void sendMisc(int p1, int p2, int p3, int p4) {
		q.add(new QueueCommand(false, "AT*MISC=" + SEQ + "," + p1 + "," + p2 + "," + p3 + "," + p4));
	}

	// AT*PMODE undocumented, but needed to initialize
	// see https://github.com/bklang/ARbDrone/wiki/UndocumentedCommands
	private void sendPMode(int mode) {
		q.add(new QueueCommand(false, "AT*PMODE=" + SEQ + "," + mode));
	}

	@Override
	public void run() {
		while (!doStop) {
			QueueCommand c;
			try {
				c = q.poll(50, TimeUnit.MILLISECONDS);
				if (c != null) {
					sendCommand(c.getCommand());
					if (c.isContinuance() && q.isEmpty()) {
						q.add(c);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void initARDrone() {
		sendPMode(2);
		sendMisc(2, 20, 2000, 3000);
		stop();
		landing();
		System.out.println("Initialize completed!");
	}

	private synchronized void sendCommand(String command) {
		/**
		 * Each command needs an individual sequence number (this also holds for Hover/Stop commands) At first, only a
		 * placeholder is set for every command and this placeholder is replaced with a real sequence number below.
		 * Because one command string may contain chained commands (e.g. "AT...AT...AT...) the replacement needs to be
		 * done individually for every 'subcommand'
		 */
		System.out.println(command);
		int seqIndex = -1;
		while ((seqIndex = command.indexOf(SEQ)) != -1) {
			command = command.substring(0, seqIndex) + (seq++) + command.substring(seqIndex + SEQ.length());
		}

		byte[] buffer = (command + CR).getBytes();
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetaddr, ARDroneUtils.PORT);
		try {
			socket.send(packet);
			Thread.sleep(20);// <50ms
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private int float2Int(float f) {
		return Float.floatToIntBits(f);
	}
}
