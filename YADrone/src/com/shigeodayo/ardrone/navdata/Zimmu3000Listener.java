package com.shigeodayo.ardrone.navdata;

import java.util.EventListener;

public interface Zimmu3000Listener extends EventListener {

	public void received(int vzimmuLSB, float vzfind);

}
