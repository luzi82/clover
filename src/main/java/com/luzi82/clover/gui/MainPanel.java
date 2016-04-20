package com.luzi82.clover.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.luzi82.clover.runtime.CloverRuntime;

import net.engio.mbassy.bus.MBassador;

public class MainPanel extends JPanel {

	private final CloverFrame pCloverFrame;
	private final CloverRuntime pCloverRuntime;

	private JList<Item> mList;
	private DefaultListModel<Item> mListModel;

	public MainPanel(CloverFrame aCloverFrame, CloverRuntime aCloverRuntime) {
		super(new BorderLayout());

		pCloverFrame = aCloverFrame;
		pCloverRuntime = aCloverRuntime;

		mListModel = new DefaultListModel<Item>();
		mListModel.addElement(new DeviceListItem());
		mListModel.addElement(new ProjectListItem());
		// mListModel.addElement("device list");

		mList = new JList<Item>(mListModel);
		mList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mList.setCellRenderer(new CloverListCellRenderer());
		mList.addMouseListener(mListMouseListener);
		JScrollPane listScrollPane = new JScrollPane(mList);

		add(listScrollPane, BorderLayout.CENTER);
	}

	private final MouseListener mListMouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if ((e.getButton()) == MouseEvent.BUTTON1 && (e.getClickCount() == 2)) {
				int idx = mList.locationToIndex(e.getPoint());
				mListModel.getElementAt(idx).onChoose();
				// mList.getSelectedValue().onChoose();
			}
		}
	};

	public static void main(String[] args) {
		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
		MBassador<Object> bus = new MBassador<Object>();
		File adbFile = new File("import-dev/android-sdk-linux/platform-tools/adb");

		CloverRuntime cloverRuntime = new CloverRuntime(scheduledThreadPoolExecutor, bus, adbFile);

		CloverFrame mainFrame = new CloverFrame();
		mainFrame.append(new Location(cloverRuntime));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setSize((int) (screenSize.width / CloverFrame.PHI), (int) (screenSize.height / CloverFrame.PHI));
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	public static abstract class Item {
		final String mName;

		public Item(String aName) {
			mName = aName;
		}

		abstract public void onChoose();

		public String toString() {
			return mName;
		}
	}

	public class DeviceListItem extends Item {
		public DeviceListItem() {
			super("Device list");
		}

		@Override
		public void onChoose() {
			// System.err.println("DEDRIWOR DeviceListItem.onChoose");
			pCloverFrame.append(new DeviceListPanel.Location(pCloverRuntime));
		}
	}

	public class ProjectListItem extends Item {
		public ProjectListItem() {
			super("Project list");
		}

		@Override
		public void onChoose() {
		}
	}

	public static class Location extends CloverFrame.Location {

		private final CloverRuntime pCloverRuntime;

		public Location(CloverRuntime aCloverRuntime) {
			pCloverRuntime = aCloverRuntime;
		}

		@Override
		public String getName() {
			return "Clover";
		}

		@Override
		public JPanel createPanel(CloverFrame aFrame) {
			return new MainPanel(aFrame, pCloverRuntime);
		}

	}

	private static final long serialVersionUID = 3012747466390954317L;

}
