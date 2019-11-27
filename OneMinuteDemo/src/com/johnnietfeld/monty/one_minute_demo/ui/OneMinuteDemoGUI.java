/**
 * 
 */
package com.johnnietfeld.monty.one_minute_demo.ui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 * @author Melody Griesen
 *
 */
public class OneMinuteDemoGUI extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8133686128543162596L;

	/**
	 * Initializes the program by making a 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		new OneMinuteDemoGUI();
	}
	
	public OneMinuteDemoGUI() {
		
		
		
	}
	
	private String getLoadFile() {
		
		// Make a File Chooser to pick the folder to be loaded from
		JFileChooser fileChooser = new JFileChooser("./");
		fileChooser.setApproveButtonText("Select");
		
		int returnVal = fileChooser.showOpenDialog(this);
		
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			// Error or user canceled, either way no file name.
			throw new IllegalArgumentException();
		}
		
		File loadFile = fileChooser.getSelectedFile();
		return loadFile.getAbsolutePath();
		
		
	}
	

}
