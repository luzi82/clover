package com.luzi82.clover.runtime;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.luzi82.adb.Adb;
import com.luzi82.adb.Adb.Device;

public class DeviceListManager {

	private final CloverRuntime pCloverRuntime;
	private final DeviceListChangeNotifier mDeviceListChangeNotifier;

	public DeviceListManager(CloverRuntime aCloverRuntime) {
		pCloverRuntime = aCloverRuntime;

		mDeviceListChangeNotifier = new DeviceListChangeNotifier();
		mDeviceListChangeNotifier.init( //
				pCloverRuntime.getNioNotifier(), //
				pCloverRuntime.getBus(), //
				null //
		);
	}

	public void init() {
		pCloverRuntime.getBus().subscribe(this);
	}

	public void start() throws IOException {
		mDeviceListChangeNotifier.start();
	}

	public void handle(DeviceListChangeNotifier.DeviceListChanged dlc) {
		startRefreshDeviceList();
	}

	public void startRefreshDeviceList() {
		synchronized (refreshBusy) {
			dirty = true;
		}
		pCloverRuntime.getScheduledExecutorService().execute(refreshDeviceListRunnable);
	}

	private Runnable refreshDeviceListRunnable = new Runnable() {
		public void run() {
			try {
				synchronized (refreshBusy) {
					if (refreshBusy[0]) {
						return;
					}
					refreshBusy[0] = true;
				}
				while (true) {
					synchronized (refreshBusy) {
						if (!dirty) {
							refreshBusy[0] = false;
							break;
						}
						dirty = false;
					}
					pCloverRuntime.getBus().post(new DeviceListRefreshStartMessage()).asynchronously();
					refreshDeviceList();
					pCloverRuntime.getBus().post(new DeviceListRefreshDoneMessage()).asynchronously();
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	};

	private boolean[] refreshBusy = { false };
	private boolean dirty = false;

	private List<Device> mDeviceList = new LinkedList<Adb.Device>();

	private void refreshDeviceList() {
		List<Device> deviceList = null;
		try {
			deviceList = pCloverRuntime.getAdb().getDeviceList();
		} catch (IOException e) {
		} catch (InterruptedException e) {
		}
		if (deviceList == null) {
			deviceList = new LinkedList<Adb.Device>();
		}
		synchronized (DeviceListManager.this) {
			mDeviceList = deviceList;
		}
	}

	public synchronized List<Device> getDeviceList() {
		return mDeviceList;
	}

	public static class DeviceListRefreshStartMessage {
	}

	public static class DeviceListRefreshDoneMessage {
	}

}
