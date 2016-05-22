package com.luzi82.clover.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class CloverNavigatorPanel extends JPanel {

	private final CloverFrame pCloverFrame;

	private final DefaultMutableTreeNode mRootNode;
	private final JTree mTree;

	public CloverNavigatorPanel(CloverFrame aCloverFrame) {
		super(new BorderLayout());

		pCloverFrame = aCloverFrame;

		mRootNode = new DefaultMutableTreeNode("asdf");
		mTree = new JTree(mRootNode);

		mRootNode.add(new DefaultMutableTreeNode("a"));
		mRootNode.add(new DefaultMutableTreeNode("b"));

		add(mTree);

		mTree.expandRow(0);
		mTree.setRootVisible(false);

	}

	private static final long serialVersionUID = -7306432164038480116L;

}
