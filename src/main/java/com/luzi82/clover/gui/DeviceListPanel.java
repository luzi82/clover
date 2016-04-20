package com.luzi82.clover.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.luzi82.clover.runtime.CloverRuntime;
import com.luzi82.clover.runtime.DeviceListManager;

public class DeviceListPanel extends JPanel {

	private final CloverFrame pCloverFrame;
	private final CloverRuntime pCloverRuntime;
	private final DeviceListManager pDeviceListManager;

	public DeviceListPanel(CloverFrame aCloverFrame, CloverRuntime aCloverRuntime) {
		super(new BorderLayout());

		pCloverFrame = aCloverFrame;
		pCloverRuntime = aCloverRuntime;
		pDeviceListManager = aCloverRuntime.getDeviceListManager();
	}

	public static class Location extends CloverFrame.Location {

		private final CloverRuntime pCloverRuntime;

		public Location(CloverRuntime aCloverRuntime) {
			pCloverRuntime = aCloverRuntime;
		}

		@Override
		public String getName() {
			return "Device list";
		}

		@Override
		public JPanel createPanel(CloverFrame aFrame) {
			return new DeviceListPanel(aFrame, pCloverRuntime);
		}

	}

	private static final long serialVersionUID = -4879928777437856483L;

}
