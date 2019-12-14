package com.johnnietfeld.monty.one_minute_demo.ui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameGUI extends JPanel {

	/** Generated class ID for serialization */
	private static final long serialVersionUID = -4436194428355121272L;
	/** Title of the window */
	private static final String TITLE_STRING = "One Minute Game - DEMO";
	/** Frame that holds the one minute game */
	JFrame gui;
	/** Panel to hold main game content */
	JPanel pnlMain;
	/** Panel to hold score info and instructions */
	JPanel pnlHeader;
	/** Panel to hold categories */
	JPanel pnlCategories;
	
	public GameGUI() {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		gui = new JFrame(TITLE_STRING);
		gui.setSize(screenSize);
		gui.setLocationRelativeTo(null);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pnlMain = new JPanel();
		pnlHeader = new JPanel();
		pnlCategories = new JPanel();
		
		
			
		
		
	}
	
}
