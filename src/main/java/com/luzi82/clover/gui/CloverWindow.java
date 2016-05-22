package com.luzi82.clover.gui;

import org.apache.pivot.wtk.Orientation;
import org.apache.pivot.wtk.SplitPane;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.Window;

public class CloverWindow extends Window {

	public final CloverGuiMain main;

	public CloverWindow(CloverGuiMain main) {
		this.main = main;

		SplitPane splitPane = new SplitPane(Orientation.HORIZONTAL);

		TreeView navTreeView = new TreeView(main.mNavRoot);
		splitPane.setLeft(navTreeView);
		splitPane.setSplitRatio(0.2f);

		setContent(splitPane);
		setMaximized(true);
	}

}
