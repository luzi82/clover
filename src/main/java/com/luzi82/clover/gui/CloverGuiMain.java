package com.luzi82.clover.gui;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.content.TreeBranch;

import com.luzi82.clover.runtime.CloverRuntime;
import com.luzi82.common.NioNotifier;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.error.IPublicationErrorHandler;
import net.engio.mbassy.bus.error.PublicationError;

public class CloverGuiMain implements Application {

	public CloverRuntime mCloverRuntime;
	public TreeBranch mNavRoot;

	public DeviceListUnit mDeviceListNodeManager;

	public void startup(Display display, Map<String, String> properties) throws Exception {
		ScheduledExecutorService scheduledExecutorService = Executors
				.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
		MBassador<Object> bus = new MBassador<Object>(busErrorHandler);
		NioNotifier nn = new NioNotifier();
		nn.init(scheduledExecutorService);
		nn.start();
		File adbFile = new File(properties.get("adb"));
		
		mCloverRuntime = new CloverRuntime(scheduledExecutorService, bus, nn, adbFile);
		mNavRoot = new TreeBranch();

		mDeviceListNodeManager = new DeviceListUnit(this);

		mNavRoot.add(mDeviceListNodeManager.getTreeNode());

		mCloverRuntime.init();
		mCloverRuntime.start();

		CloverWindow cloverWindow = new CloverWindow(this);
		cloverWindow.open(display);
	}

	public boolean shutdown(boolean optional) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public void suspend() throws Exception {
		// TODO Auto-generated method stub

	}

	public void resume() throws Exception {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		DesktopApplicationContext.main(CloverGuiMain.class, args);
	}
	
	private static final IPublicationErrorHandler busErrorHandler = new IPublicationErrorHandler(){
		public void handleError(PublicationError arg0) {
			arg0.getCause().printStackTrace();
		}
		
	};

}
