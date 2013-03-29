package com.shigeodayo.ardrone.navdata;

import java.util.EventListener;


public interface GyroListener extends EventListener {
	public void receivedRawData(GyroRawData d);

	public void receivedPhysData(GyroPhysData d);

	public void receivedOffsets(float[] offset_g);
}
