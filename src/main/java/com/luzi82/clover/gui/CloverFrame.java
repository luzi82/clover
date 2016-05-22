package com.luzi82.clover.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CloverFrame extends JFrame {

	private final JPanel mMainPanel;
	// private final JPanel mLocationPanel;
	// private final LinkedList<Location> mLocationList;
	private final CloverNavigatorPanel mNavigatorPanel;

	private JPanel mActivePanel;

	public CloverFrame() {
		super();

		// mLocationList = new LinkedList<CloverFrame.Location>();

		mMainPanel = new JPanel(new BorderLayout());
		add(mMainPanel);

		// mLocationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// mMainPanel.add(mLocationPanel, BorderLayout.PAGE_START);

		mNavigatorPanel = new CloverNavigatorPanel(this);
		mMainPanel.add(mNavigatorPanel, BorderLayout.WEST);
	}

//	public void append(final Location aLocation) {
//		JButton button = new JButton(aLocation.getName());
//		button.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (mLocationList.getLast() == aLocation)
//					return;
//				if (!mLocationList.contains(aLocation))
//					return;
//				while (mLocationList.getLast() != aLocation) {
//					mLocationList.removeLast();
//					mLocationPanel.remove(mLocationList.size());
//				}
//				mLocationPanel.revalidate();
//				mLocationPanel.repaint();
//				updateActivePanel();
//			}
//		});
//
//		int buttonIdx = mLocationList.size();
//		mLocationPanel.add(button, buttonIdx);
//		mLocationPanel.revalidate();
//
//		mLocationList.add(aLocation);
//
//		updateActivePanel();
//	}

//	private void updateActivePanel() {
//		if (mActivePanel != null) {
//			mMainPanel.remove(mActivePanel);
//			mActivePanel = null;
//		}
//		Location location = mLocationList.getLast();
//		if (location != null) {
//			mActivePanel = location.createPanel(this);
//			mMainPanel.add(mActivePanel, BorderLayout.CENTER);
//		}
//		mMainPanel.revalidate();
//
//		StringBuffer titleBuffer = new StringBuffer();
//		titleBuffer.append("Clover");
//		boolean first = true;
//		for (Location l : mLocationList) {
//			titleBuffer.append(first ? " - " : " > ");
//			first = false;
//			titleBuffer.append(l.getName());
//		}
//		setTitle(titleBuffer.toString());
//	}

	public static abstract class Location {

		public abstract String getName();

		public abstract JPanel createPanel(CloverFrame aFrame);

	}

	public static void main(String[] args) {
		JFrame mainFrame = new CloverFrame();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setSize((int) (screenSize.width / PHI), (int) (screenSize.height / PHI));
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	private static final long serialVersionUID = 4048904371753611958L;

	public static final float PHI = 1.61803398875f;

}
