package com.shigeodayo.ardrone.command;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class CommandQueue extends PriorityBlockingQueue<ATCommand> {

	public CommandQueue(int capacity) {
		super(capacity, new Comparator<ATCommand>() {

			@Override
			public int compare(final ATCommand lhs, final ATCommand rhs) {
				final int lp = lhs.getPriority();
				final int rp = rhs.getPriority();
				// make the common case fast
				if (lp == rp) {
					return 0;
				}
				if (lp < lp) {
					return -1;
				}
				return 1;
			}
		});
	}

}
