package com.johnnietfeld.monty.one_minute_demo.model.manager;

import java.util.ArrayList;

import com.johnnietfeld.monty.one_minute_demo.model.data.Category;
import com.johnnietfeld.monty.one_minute_demo.model.data.ClassifiedImage;
import com.johnnietfeld.monty.one_minute_demo.model.list.ImageList;

public class Game {

	// Instance variables

	/** Instructions for this Game. */
	private String instructions;
	/** List of Images that this Game can choose from */
	private ImageList list;
	/** List of Categories this Game uses */
	private ArrayList<Category> categories;
	/** The user's current score */
	private int score;
	/** Amount to increase the score by upon a correct image classification */
	private final int correctIncrement = 1;
	/** Amount to decrease the score by upon an incorrect image classification */
	private final int incorrectDecrement = -1;
	/**
	 * Used to toggle requirements that an ImageList have at least 60 images for a
	 * valid Game to be run from it.
	 */
	private boolean require60 = true;

	// Constants

	/** Minimum number of categories allowed for a valid Game */
	private static final int MIN_CATEGORIES = 2;
	/** Maximum number of categories allowed for a valid Game */
	private static final int MAX_CATEGORIES = 4;

	/**
	 * Creates a new Game with the provided data. Categories will be extracted from
	 * the ImageList provided. Instructions must be a non-null, non-blank string.
	 * List must contain at least 60 images wit a valid number of categories.
	 * 
	 * @param instructions the instructions to show to the user for this Game
	 * @param list         the ImageList of all images to be used in this Game
	 * @throws IllegalArgumentException if the provided data are not valid
	 */
	public Game(String instructions, ImageList list) {
		this(instructions, list, true);
	}

	/**
	 * Creates a new Game with the provided data, declaring whether or not at least
	 * 60 images will be required. Categories will be extracted from the ImageList
	 * provided.
	 * 
	 * Instructions must be a non-null, non-blank string.
	 * 
	 * List must contain images wit a valid number of categories. Additionally, if
	 * require60 is true, the list must have at least 60 images.
	 * 
	 * @param instructions the instructions to show to the user for this Game
	 * @param list         the ImageList of all images to be used in this Game
	 * @throws IllegalArgumentException if the provided data are not valid
	 */
	public Game(String instructions, ImageList list, boolean require60) {
		setRequire60(require60);
		setInstructions(instructions);
		setList(list);
		setCategories();
		score = 0;
	}

	/**
	 * Simple getter for this Game's instructions
	 * 
	 * @return the instructions field
	 */
	public String getInstructions() {
		return instructions;
	}

//	public ImageList getList() {
//		return list;
//	}

//	public ArrayList<Category> getCategories() {
//		return categories;
//	}

	/**
	 * Simple getter for the user's score in this game.
	 * 
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Retrieves whether this Game's ImageList must have 60 elements or not.
	 * 
	 * @return true if the ImageList must be 60 elements or longer, false if that
	 *         requirement is not active.
	 */
	public boolean getRequire60() {
		return require60;
	}

	/**
	 * Sets whether this Game requires ImageLists with at least 60 elements.
	 * 
	 * Only runs on Game creation. require60 cannot be modified after a Game is
	 * constructed.
	 * 
	 * @param assignment true to force ImageLists to contain at least 60 elements.
	 */
	private void setRequire60(boolean assignment) {
		this.require60 = assignment;
	}

	/**
	 * Sets the instructions for this Game to be displayed to the user.
	 * 
	 * @param instructions the instructions to set
	 */
	private void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	/**
	 * Sets this Game's ImageList. If this game requires that its lists be at least
	 * 60 elements long, will check that the provided list meets that requirement.
	 * 
	 * @param list the ImageList to set
	 * @throws IllegalArgumentException if the provided list is null or if it has
	 *                                  less than 60 elements while this Game
	 *                                  requires lists with >= 60 elements
	 */
	private void setList(ImageList list) {
		// Error checking
		// Ensure list is not null
		if (list == null) {
			throw new IllegalArgumentException();
		}
		// If requiring at least 60 images, ensure that minimum size is reached
		if (require60 && list.size() < 60) {
			throw new IllegalArgumentException(
					"ImageList only contains " + list.size() + " images, not at least 60 as required.");
		}
		this.list = list;
	}

	/**
	 * Scans this Game's ImageList for all Categories expressed by ClassifiedImages,
	 * and compiles them into this Game's Categories list.
	 * 
	 * @throws IllegalArgumentException if the ImageList's ClassifiedImages contain
	 *                                  less than the minimum or more than the
	 *                                  maximum number of allowed categories
	 */
	private void setCategories() {

		// Initialize a new categories ArrayList
		categories = new ArrayList<Category>();
		// Search through this Game's ImageList to extract all unique Categories
		for (ClassifiedImage image : list) {
			// If the Categories List does not contain this image's Category,
			if (!categories.contains(image.getCategory())) {
				// add it to the Categories List
				categories.add(image.getCategory());
			}
		}

		// Error checking
		// Ensure that number of categories is between valid min and max
		if (categories.size() < MIN_CATEGORIES) {
			throw new IllegalArgumentException("Image list has less than " + MIN_CATEGORIES + " unique categories");
		}
		if (categories.size() > MAX_CATEGORIES) {
			throw new IllegalArgumentException("Image list has more than " + MAX_CATEGORIES + " unique categories");
		}
		// Error checking complete
	}

	/**
	 * Modifies this Game's score based on a correct or incorrect result
	 * 
	 * @param correct whether the question was answered correctly or incorrectly
	 */
	private void changeScore(boolean correct) {
		score += correct ? correctIncrement : incorrectDecrement;
	}

	/**
	 * Scores an image categorized by the user.
	 * 
	 * @param image     the image being scored
	 * @param placement the category the user attempted to score the image as
	 * @return the new score
	 */
	public int scoreImage(ClassifiedImage image, Category placement) {
		boolean correct = image.scoreCategory(placement);
		changeScore(correct);
		return getScore();
	}

	/**
	 * Retrieves the next image to be shown to the user.
	 * 
	 * @return a ClassifiedImage from the sequence of images
	 */
	public ClassifiedImage nextImage() {
		return list.next();
	}

	/**
	 * Scores the image provided and retrieves the next image to be shown.
	 * 
	 * Combines usage of scoreImage() and nextImage()
	 * 
	 * @param image     the image that the user has just categorized
	 * @param placement the Category that the user categorized the image as
	 * @return the next image in the sequence, a ClassifiedImage
	 */
	public ClassifiedImage nextStep(ClassifiedImage image, Category placement) {
		scoreImage(image, placement);
		return nextImage();
	}
}
