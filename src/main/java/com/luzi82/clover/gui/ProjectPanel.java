package com.luzi82.clover.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class ProjectPanel extends JPanel {

	private final File mProjectDir;

	private JList<String> mList;
	private DefaultListModel<String> mListModel;

	public ProjectPanel(File projectDir) {
		super(new BorderLayout());

		this.mProjectDir = projectDir;

		this.mProjectDir.mkdirs();

		mListModel = new DefaultListModel<String>();
		mListModel.addElement("state list");
		mListModel.addElement("symbol list");
		mListModel.addElement("sample list");

		mList = new JList<String>(mListModel);
		mList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mList.setCellRenderer(new CloverListCellRenderer());
		JScrollPane listScrollPane = new JScrollPane(mList);

		add(listScrollPane, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		File f = new File(args[0]);
		f.mkdirs();

		// JFrame mainFrame = new JFrame();
		// mainFrame.add(new ProjectPanel(f));
		//
		// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// mainFrame.setSize((int) (screenSize.width / PHI), (int)
		// (screenSize.height / PHI));
		// mainFrame.setLocationRelativeTo(null);
		// mainFrame.setVisible(true);

		CloverFrame mainFrame = new CloverFrame();
		mainFrame.append(new Location(f));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setSize((int) (screenSize.width / PHI), (int) (screenSize.height / PHI));
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	public static class Location extends CloverFrame.Location {

		public final File mProjectDir;

		public Location(File aProjectDir) {
			this.mProjectDir = aProjectDir;
		}

		@Override
		public String getName() {
			return mProjectDir.getName();
		}

		@Override
		public JPanel createPanel() {
			return new ProjectPanel(mProjectDir);
		}

	}

	private static final long serialVersionUID = 6315601978199546311L;

	public static final float PHI = 1.61803398875f;

}
