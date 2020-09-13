package com.johnnietfeld.monty.one_minute_demo.model.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.johnnietfeld.monty.one_minute_demo.model.data.Category;
import com.johnnietfeld.monty.one_minute_demo.model.data.ClassifiedImage;
import com.johnnietfeld.monty.one_minute_demo.model.list.ImageList;
import com.johnnietfeld.monty.one_minute_demo.model.manager.Game;

/**
 * Loads one or more Games from given file locations.
 * 
 * @author Melody Griesen // TODO: Get eclipse to use my real name
 *
 */
public class OneMinuteDemoIO {

	/** Properties file name */
	private static final String PROPERTIES_FILE_NAME = "properties.omg";

	/**
	 * Read all game files in a 'games' folder
	 * 
	 * @param gamesFolder the folder that contains all game folders
	 * @return an ArrayList of Games that have been successfully loaded
	 */
	public static ArrayList<Game> readGames(File gamesFolder) {

		// Make a FileFilter to pick only directory files when searching a directory
		FileFilter onlyDirectories = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return !pathname.isFile();
			}
		};

		// Retrieve the list of game directories from the parent directory
		File[] gameFiles = gamesFolder.listFiles(onlyDirectories);

		// Create Game List. Can't assume all games will load properly, so this cannot
		// be an array with length gameFiles.length
		ArrayList<Game> games = new ArrayList<Game>();

		// Iterate through all game files, attempt to add them to the games array list
		for (File gameFile : gameFiles) {
			try {
				games.add(readGameFolder(gameFile));
			} catch (IllegalArgumentException e) {
				System.out.println("Game located at " + gameFile.getAbsolutePath() + " could not be loaded. Reason:");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}

		// Return all games that were successfully loaded
		return games;
	}

	/**
	 * Reads all information in a Game folder and loads it into a Game object, ready
	 * for use.
	 * 
	 * @param gameFolder a folder that contains a valid properties file and
	 *                   folders that contain images.
	 * @return the Game holding all information loaded from the folder
	 */
	public static Game readGameFolder(File gameFolder) {

		// Retrieve the properties file
		File propertiesFile = new File(gameFolder.getAbsolutePath() + "/" + PROPERTIES_FILE_NAME);

		// Read properties from file
		GameProperties properties = readPropertiesFile(propertiesFile);

		// Create ImageList from the folders in the game folder
		ImageList images = readImages(gameFolder);

		// Set cycle and randomize from properties
		images.setCycle(properties.isCycle());
		images.setRandomizeCycle(properties.isRandomize());

		// Create Game from ImageList and properties
		Game game = new Game(properties.getInstructions(), images, properties.getIncrement(), properties.getDecrement(),
				properties.isRequire60(), properties.getTime());

		// Return successfully created Game
		return game;
	}

	/**
	 * Splits the provided string into two components by the first "=" character in
	 * the string.
	 * 
	 * @param line the String to process
	 * @return a String array with 1 element if line does not contain an "="
	 *         character, or 2 elements if it does contain a "=" character.
	 */
	private static String[] readProperty(String line) {
		return line.split("=", 2);
	}

	private static GameProperties readPropertiesFile(File propertiesFile) {
		// Create variables to store fields read from properties file
		GameProperties gameProperties = new GameProperties();
		// Create variables to hold file read information
		String nextLine = null;
		String[] propertyPair;
		int currentLine = 1;
		Scanner propertiesReader = null;
		String[] expectedProperties = { "line 0 placeholder", "instructions", "increment", "decrement" };
		// Read properties from file
		try {
			// Create Scanner for properties file
			propertiesReader = new Scanner(propertiesFile);

			// Read instructions
			// Save next line of properties file to variable
			nextLine = propertiesReader.nextLine();
			// Split next line by '=' character into name and value
			propertyPair = readProperty(nextLine);
			// Check that the name of the property is the expected property for this line
			if (!propertyPair[0].contentEquals(expectedProperties[currentLine])) {
				propertiesReader.close();
				throw new IllegalArgumentException("Line one of " + propertiesFile.getAbsolutePath()
						+ "does not start with " + expectedProperties[currentLine]);
			}
			// Save property to variable
			gameProperties.setInstructions(propertyPair[1]);
			++currentLine;

			// Read increment
			// Save next line of properties file to variable
			nextLine = propertiesReader.nextLine();
			// Split next line by '=' character into name and value
			propertyPair = readProperty(nextLine);
			// Check that the name of the property is the expected property for this line
			if (!propertyPair[0].contentEquals(expectedProperties[currentLine])) {
				propertiesReader.close();
				throw new IllegalArgumentException("Line one of " + propertiesFile.getAbsolutePath()
						+ "does not start with " + expectedProperties[currentLine]);
			}
			// Save property to variable
			gameProperties.setIncrement(Integer.parseInt(propertyPair[1]));
			++currentLine;

			// Read decrement
			// Save next line of properties file to variable
			nextLine = propertiesReader.nextLine();
			// Split next line by '=' character into name and value
			propertyPair = readProperty(nextLine);
			// Check that the name of the property is the expected property for this line
			if (!propertyPair[0].contentEquals(expectedProperties[currentLine])) {
				propertiesReader.close();
				throw new IllegalArgumentException("Line one of " + propertiesFile.getAbsolutePath()
						+ "does not start with " + expectedProperties[currentLine]);
			}
			// Save property to variable
			gameProperties.setDecrement(Integer.parseInt(propertyPair[1]));
			++currentLine;

			// While there is extra data in the file
			while (propertiesReader.hasNext()) {
				// Read next line
				nextLine = propertiesReader.nextLine();
				// Split next line by '=' character into name and value
				propertyPair = readProperty(nextLine);
				// Check that it was split properly
				if (propertyPair.length == 1)
					throw new ArrayIndexOutOfBoundsException();

				// Determine which property we should modify
				switch (propertyPair[0]) {
				// If the next line is any of the following properties, parse its value and save
				// it
				case "cycle":
					if (propertyPair[1].contentEquals("true")) {
						gameProperties.setCycle(true);
					} else if (propertyPair[1].contentEquals("false")) {
						gameProperties.setCycle(false);
					} else {
						throw new IllegalArgumentException("Property file at " + propertiesFile.getAbsolutePath()
								+ ", line " + currentLine + ": \n" + nextLine
								+ "\nis not readable as a boolean. Please make the right-hand field 'true' or 'false'.");
					}
					break;
				case "randomize":
					if (propertyPair[1].contentEquals("true")) {
						gameProperties.setRandomize(true);
					} else if (propertyPair[1].contentEquals("false")) {
						gameProperties.setRandomize(false);
					} else {
						throw new IllegalArgumentException("Property file at " + propertiesFile.getAbsolutePath()
								+ ", line " + currentLine + ": \n" + nextLine
								+ "\nis not readable as a boolean. Please make the right-hand field 'true' or 'false'.");
					}
					break;
				case "require60":
					if (propertyPair[1].contentEquals("true")) {
						gameProperties.setRequire60(true);
					} else if (propertyPair[1].contentEquals("false")) {
						gameProperties.setRequire60(false);
					} else {
						throw new IllegalArgumentException("Property file at " + propertiesFile.getAbsolutePath()
								+ ", line " + currentLine + ": \n" + nextLine
								+ "\nis not readable as a boolean. Please make the right-hand field 'true' or 'false'.");
					}
					break;
				case "time":
					gameProperties.setTime(Integer.parseInt(propertyPair[1]));
					break;
				default:
					throw new IllegalArgumentException(
							"Property file at " + propertiesFile.getAbsolutePath() + ", line " + currentLine + ": \n"
									+ nextLine + "\n unrecognized property: " + propertyPair[1]);
				}
				// Property was successfully read, increment line count
				++currentLine;
			}

			propertiesReader.close();
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(
					"Properties file not found at destination " + propertiesFile.getAbsolutePath());
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Property file at " + propertiesFile.getAbsolutePath() + ", line "
					+ currentLine + ": \n" + nextLine + "\ndoes not follow the format of '[property]=[value].");
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Property file at " + propertiesFile.getAbsolutePath() + ", line "
					+ currentLine + ": \n" + nextLine + "\nis not readable as an integer.");
		} finally {
			if (propertiesReader != null) {
				propertiesReader.close();
			}
		}

		return gameProperties;
	}

	/**
	 * Private POJO to hold all information about a Game loaded from a properties
	 * file.
	 * 
	 * @author Melody Griesen
	 */
	private static class GameProperties {
		// Required data
		private String instructions = null;
		private int increment = 0;
		private int decrement = 0;

		// Optional data
		private int time = 60;
		private boolean cycle = false;
		private boolean randomize = false;
		private boolean require60 = true;

		/**
		 * @return the instructions
		 */
		String getInstructions() {
			return instructions;
		}

		/**
		 * @param instructions the instructions to set
		 */
		void setInstructions(String instructions) {
			this.instructions = instructions;
		}

		/**
		 * @return the increment
		 */
		int getIncrement() {
			return increment;
		}

		/**
		 * @param increment the increment to set
		 */
		void setIncrement(int increment) {
			this.increment = increment;
		}

		/**
		 * @return the decrement
		 */
		int getDecrement() {
			return decrement;
		}

		/**
		 * @param decrement the decrement to set
		 */
		void setDecrement(int decrement) {
			this.decrement = decrement;
		}

		/**
		 * @return the time
		 */
		int getTime() {
			return time;
		}

		/**
		 * @param time the time to set
		 */
		void setTime(int time) {
			this.time = time;
		}

		/**
		 * @return the cycle
		 */
		boolean isCycle() {
			return cycle;
		}

		/**
		 * @param cycle the cycle to set
		 */
		void setCycle(boolean cycle) {
			this.cycle = cycle;
		}

		/**
		 * @return the randomize
		 */
		boolean isRandomize() {
			return randomize;
		}

		/**
		 * @param randomize the randomize to set
		 */
		void setRandomize(boolean randomize) {
			this.randomize = randomize;
		}

		/**
		 * @return the require60
		 */
		boolean isRequire60() {
			return require60;
		}

		/**
		 * @param require60 the require60 to set
		 */
		void setRequire60(boolean require60) {
			this.require60 = require60;
		}
	}

	/**
	 * Private helper function to load all images from the game folder
	 * 
	 * @param gameFolder the game folder containing category folders, each of which
	 *                   contain images
	 * @return an ImageList of all images found in the GameFolder
	 */
	private static ImageList readImages(File gameFolder) {
		// Get a list of directories for all Categories in the game folder
		File[] categoryFolders = gameFolder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return !pathname.isFile();
			}
		});
		// Create a master ArrayList to hold all images in all categories
		ArrayList<ClassifiedImage> allImages = new ArrayList<ClassifiedImage>();

		// Iterate through the array of category folders
		for (File categoryFolder : categoryFolders) {
			// Load all images from one category folder into the master ArrayList
			readImagesInCategory(categoryFolder, allImages);
		}
		// All images have been correctly labeled with their Category and loaded into
		// the master ArrayList

		// If the master image list has no images, throw an exception
		if (allImages.size() == 0) {
			throw new IllegalArgumentException("Game folder at " + gameFolder.getAbsolutePath()
					+ "\nNo images within category folders. Place at folder with at least one image in the game folder");
		}

		// Make an ImageList object from the ArrayList and return it
		return new ImageList(allImages);
	}

	/**
	 * Private helper method to read all images in a Category folder
	 * 
	 * @param categoryFolder folder of images, titled with the category name
	 * @param images         ArrayList for loaded images to be added to
	 */
	private static void readImagesInCategory(File categoryFolder, ArrayList<ClassifiedImage> images) {
		// Make Category to apply to all images
		Category category = new Category(categoryFolder.getName());
		// Get the array of all Files in the directory
		File[] imageFiles = categoryFolder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});

		// Iterate through all files in the folder
		for (File imageFile : imageFiles) {
			// Create a new ClassifiedImage object
			ClassifiedImage image = new ClassifiedImage(imageFile.getAbsolutePath(), category, imageFile.getName());
			// Add image to the list of images
			images.add(image);
		}
	}

}
