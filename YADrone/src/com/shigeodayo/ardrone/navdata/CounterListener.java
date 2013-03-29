package com.shigeodayo.ardrone.navdata;

import java.util.EventListener;


public interface CounterListener extends EventListener {

	public void update(Counters d);

}
