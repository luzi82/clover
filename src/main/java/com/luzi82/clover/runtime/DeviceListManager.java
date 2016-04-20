package com.luzi82.clover.runtime;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.luzi82.adb.Adb;
import com.luzi82.adb.Adb.Device;

public class DeviceListManager {

	private final CloverRuntime pCloverRuntime;

	public DeviceListManager(CloverRuntime aCloverRuntime) {
		pCloverRuntime = aCloverRuntime;
	}

	private boolean refreshDeviceListBusy = false;
	private List<Device> mDeviceList = new LinkedList<Adb.Device>();

	public boolean refreshDeviceList() {
		synchronized (this) {
			if (refreshDeviceListBusy)
				return false;
			refreshDeviceListBusy = true;
		}
		pCloverRuntime.getBus().post(new DeviceListRefreshStartMessage()).asynchronously();
		pCloverRuntime.getScheduledExecutorService().execute(new Runnable() {
			public void run() {
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
					refreshDeviceListBusy = false;
				}
				pCloverRuntime.getBus().post(new DeviceListRefreshDoneMessage()).asynchronously();
			}
		});
		return true;
	}

	public synchronized List<Device> getDeviceList() {
		return mDeviceList;
	}

	public static class DeviceListRefreshStartMessage {
	}

	public static class DeviceListRefreshDoneMessage {
	}

}
