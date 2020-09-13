/**
 * 
 */
package com.johnnietfeld.monty.one_minute_demo.ui;

import java.io.File;

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
	 * @param args commandline arguments
	 */
	public static void main(String[] args) {
		new OneMinuteDemoGUI();
	}

	public OneMinuteDemoGUI() {
		File loadFile;
		Game game = null;
		while (game == null) {
			try {
				loadFile = getLoadFile();
				game = OneMinuteDemoIO.readGameFolder(loadFile);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}

		new GameGUI(game);
	}

	private File getLoadFile() {

		// Make a File Chooser to pick the folder to be loaded from
		JFileChooser fileChooser = new JFileChooser("./" + GAME_FOLDER +  "/");
		fileChooser.setApproveButtonText("Select");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnVal = fileChooser.showOpenDialog(this);

		if (returnVal != JFileChooser.APPROVE_OPTION) {
			// Error or user canceled, either way no file name.
			throw new IllegalArgumentException("No file selected!");
		}

		return fileChooser.getSelectedFile();

	}

}
