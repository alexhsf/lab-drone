package com.shigeodayo.ardrone.navdata;

import java.util.EventListener;

public interface WatchdogListener extends EventListener {

	public void received(int watchdog);

}
