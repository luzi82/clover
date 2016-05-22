package com.luzi82.common;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NioNotifier {

	private Selector mSelector;
	private ScheduledExecutorService pScheduledExecutorService;
	
	public void init(ScheduledExecutorService aScheduledExecutorService) {
		pScheduledExecutorService = aScheduledExecutorService;
	}

	public void start() throws IOException {
		mSelector = Selector.open();
		pScheduledExecutorService.scheduleWithFixedDelay(run, 0, 1, TimeUnit.MILLISECONDS);
	}

	public Selector getSelector() {
		return mSelector;
	}

	private Runnable run = new Runnable() {
		public void run() {
			try {
				int readyCount = mSelector.selectNow();
				if (readyCount <= 0)
					return;
				Set<SelectionKey> selectionKeySet = mSelector.selectedKeys();
				for (SelectionKey sk : selectionKeySet) {
					try {
						Unit unit = (Unit) sk.attachment();
						unit.onNio(sk);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
				selectionKeySet.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	public static interface Unit {
		public void onNio(SelectionKey sk) throws Throwable;
	}

}
