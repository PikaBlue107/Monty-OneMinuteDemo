/**
 * 
 */
package com.johnnietfeld.monty.one_minute_demo.ui;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import com.johnnietfeld.monty.one_minute_demo.model.io.OneMinuteDemoIO;
import com.johnnietfeld.monty.one_minute_demo.model.manager.Game;

/**
 * @author Melody Griesen
 *
 */
public class OneMinuteDemoGUI extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8133686128543162596L;
	
	private static final String GAME_FOLDER = "games";

	/**
	 * Initializes the program by making a 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		ArrayList<Game> games = OneMinuteDemoIO.readGames(new File(GAME_FOLDER));
		
		if (games.size() == 0) {
			throw new IllegalArgumentException();
		}
		
		new GameGUI(games.get(0));
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
