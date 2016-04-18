package com.luzi82.clover.gui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class CloverListCellRenderer extends DefaultListCellRenderer {

	public Component getListCellRendererComponent(JList<?> arg0, Object arg1, int arg2, boolean arg3, boolean arg4) {
		super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);

		Font oldFont = getFont();
		Font newFont = new Font(oldFont.getName(), Font.PLAIN, 24);
		setFont(newFont);

		return this;
	}

	private static final long serialVersionUID = 6463652093621000499L;

}
