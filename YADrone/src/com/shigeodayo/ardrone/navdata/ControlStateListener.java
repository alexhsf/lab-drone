package com.shigeodayo.ardrone.navdata;

import java.util.EventListener;


public interface ControlStateListener  extends EventListener {
	public void controlStateChanged(ControlState state);
}
