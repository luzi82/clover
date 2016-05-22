package com.luzi82.clover.gui;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.content.TreeBranch;
import org.apache.pivot.wtk.content.TreeNode;

public class DeviceUnit implements CloverUnit {

	private final CloverGuiMain main;
	public final String serialCode;
	private final TreeBranch node;

	public DeviceUnit(CloverGuiMain main,String serialCode) {
		this.main = main;
		this.serialCode = serialCode;
		
		this.node = new TreeBranch(serialCode);
		this.node.setUserData(this);
	}

	public TreeNode getTreeNode() {
		return node;
	}

	public Component createComponent() {
		// TODO Auto-generated method stub
		return null;
	}

}
