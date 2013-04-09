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

package com.shigeodayo.ardrone.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import android.util.Log;

import com.shigeodayo.ardrone.command.CommandManager;
import com.shigeodayo.ardrone.command.ControlCommand;
import com.shigeodayo.ardrone.command.ControlMode;
import com.shigeodayo.ardrone.manager.AbstractTCPManager;
import com.shigeodayo.ardrone.utils.ARDroneUtils;

// TODO consider to connect to the control port permanently
public class ConfigurationManager extends AbstractTCPManager {
	private CommandManager manager = null;

	private static final String TAG = "YADrone";

	public ConfigurationManager(InetAddress inetaddr, CommandManager manager) {
		super(inetaddr);
		this.manager = manager;
	}

	@Override
	public void run() {
	}

	private String getControlCommandResult(ControlMode p1, int p2) {
		try {
			// TODO better have connect throw IOException if connection fails
			// this allows to call close only when connect succeeded

			manager.setCommand(new ControlCommand(p1, p2));

			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			InputStream inputStream = getInputStream();
			// TODO better getInputStream throw IOException to fail
			if (inputStream != null) {
				byte[] buf = new byte[1024];
				int n = 0;
				StringBuilder builder = new StringBuilder();
				try {
					while ((n = inputStream.read(buf)) != -1) {
						// output: multiple rows of "Parameter = value"
						builder.append(new String(buf, 0, n, "ASCII"));
					}
				} catch (SocketTimeoutException e) {
					// happens if the last byte happens to coincide with the end of the buffer
				}
				String s = builder.toString();
				Log.d(TAG, s);
				return s;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";

	}

	public String getCustomCofigurationIds() {
		String s = getControlCommandResult(ControlMode.CUSTOM_CFG_GET, 0);
		return s;
	}

	public String getPreviousRunLogs() {
		String s = getControlCommandResult(ControlMode.LOGS_GET, 0);
		return s;
	}

	public String getConfiguration() {
		String s = getControlCommandResult(ControlMode.CFG_GET, 0);
		return s;
	}

}
