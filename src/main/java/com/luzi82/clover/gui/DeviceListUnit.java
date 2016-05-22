package com.luzi82.clover.gui;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.pivot.collections.LinkedList;
import org.apache.pivot.wtk.ApplicationContext;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.content.TreeBranch;
import org.apache.pivot.wtk.content.TreeNode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luzi82.adb.Adb.Device;
import com.luzi82.clover.runtime.DeviceListManager.DeviceListRefreshDoneMessage;

import net.engio.mbassy.listener.Handler;

public class DeviceListUnit implements CloverUnit {

	private final CloverGuiMain main;
	private final TreeBranch node;

	public DeviceListUnit(CloverGuiMain main) {
		this.main = main;
		this.node = new TreeBranch("Device list");
		this.node.setUserData(this);

		this.main.mCloverRuntime.getBus().subscribe(this);

		startUpdateList();
	}

	public TreeNode getTreeNode() {
		return node;
	}
	
	public void startUpdateList(){
		ApplicationContext.queueCallback(updateListRunnable);
	}
	
	final private Runnable updateListRunnable=new Runnable() {
		public void run() {
			updateList();
		}
	};

	public synchronized void updateList() {
		List<Device> currentDeviceList = main.mCloverRuntime.getDeviceListManager().getDeviceList();

		SortedSet<String> currentSerialList = new TreeSet<String>();
		for (Device device : currentDeviceList) {
			currentSerialList.add(device.serial_number);
		}

		try {
			System.err.println("YPZKWTHQ " + new ObjectMapper().writeValueAsString(currentSerialList));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		// remove node
		LinkedList<TreeNode> delNodeList = new LinkedList<TreeNode>();
		for (TreeNode child : node) {
			DeviceUnit deviceUnit = (DeviceUnit) child.getUserData();
			String serialCode = deviceUnit.serialCode;
			if (currentSerialList.contains(serialCode))
				continue;
			delNodeList.add(child);
		}
		for (TreeNode delNode : delNodeList) {
			node.remove(delNode);
		}

		// add node
		SortedSet<String> mySerialList = new TreeSet<String>();
		for (TreeNode child : node) {
			DeviceUnit deviceUnit = (DeviceUnit) child.getUserData();
			String serialCode = deviceUnit.serialCode;
			mySerialList.add(serialCode);
		}

		LinkedList<String> newSerialList = new LinkedList<String>();
		for (String currentSerial : currentSerialList) {
			if (mySerialList.contains(currentSerial))
				continue;
			newSerialList.add(currentSerial);
		}

		for (String newSerial : newSerialList) {
			DeviceUnit deviceUnit = new DeviceUnit(main, newSerial);
			node.add(deviceUnit.getTreeNode());
		}
	}

	public Component createComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Handler
	public void handle(DeviceListRefreshDoneMessage m) {
		System.err.println("XIVGAGHJ");
		startUpdateList();
	}

}
