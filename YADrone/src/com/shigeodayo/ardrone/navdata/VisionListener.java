package com.shigeodayo.ardrone.navdata;

import java.util.ArrayList;
import java.util.EventListener;


public interface VisionListener extends EventListener {
	public void tagsDetected(ArrayList<VisionTag> list);

	public void trackersSend(int[][] locked, int[][][] point);

	public void receivedPerformanceData(VisionPerormance d);

	public void receivedRawData(float[] vision_raw);

	public void receivedData(VisionData d);

	public void receivedVisionOf(float[] of_dx, float[] of_dy);
}
