package com.luzi82.clover.runtime;

import java.io.File;
import java.util.concurrent.ScheduledExecutorService;

import com.luzi82.adb.Adb;
import com.luzi82.common.NioNotifier;

import net.engio.mbassy.bus.MBassador;

public class CloverRuntime {

	private final ScheduledExecutorService mScheduledExecutorService;
	private final MBassador<Object> mBus;
	private final NioNotifier mNioNotifier;

	private final Adb mAdb;

	private final DeviceListManager mDeviceListManager;
	private Workspace mWorkspace;

	public CloverRuntime( //
			ScheduledExecutorService aScheduledExecutorService, //
			MBassador<Object> aBus, //
			NioNotifier aNioNotifier, //
			File aAdbFile //
	) {
		mScheduledExecutorService = aScheduledExecutorService;
		mBus = aBus;
		mNioNotifier = aNioNotifier;

		mAdb = new Adb(aAdbFile);

		mDeviceListManager = new DeviceListManager(this);
	}

	public Adb getAdb() {
		return mAdb;
	}

	public MBassador<Object> getBus() {
		return mBus;
	}

	public ScheduledExecutorService getScheduledExecutorService() {
		return mScheduledExecutorService;
	}

	public DeviceListManager getDeviceListManager() {
		return mDeviceListManager;
	}

	public NioNotifier getNioNotifier() {
		return mNioNotifier;
	}

	public Workspace getWorkspace() {
		return mWorkspace;
	}

}
