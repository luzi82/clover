package com.luzi82.clover.gui;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.content.TreeNode;

public interface CloverUnit {

	public TreeNode getTreeNode();
	
	public Component createComponent();
	
}
