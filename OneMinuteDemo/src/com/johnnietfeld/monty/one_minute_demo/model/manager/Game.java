package com.johnnietfeld.monty.one_minute_demo.model.manager;

import java.util.ArrayList;

import com.johnnietfeld.monty.one_minute_demo.model.data.Category;
import com.johnnietfeld.monty.one_minute_demo.model.list.ImageList;

public class Game {

	/** Instructions for this Game. */
	private String instructions;
	/** List of Images that this Game can choose from */
	private ImageList list;
	/** List of Categories this Game uses */
	private ArrayList<Category> categories;
	/** The user's current score */
	private int score;

	// Used to toggle requirements that an ImageList have at least 60 images for a
	// valid Game to be run from it.
	private static final boolean REQUIRE_60 = true;

	public Game(String instructions, ImageList list, ArrayList<Category> categories) {
		setInstructions(instructions);
		setList(list);
		setCategories(categories);
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

	public int getScore() {
		return score;
	}

	private void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	private void setList(ImageList list) {
		// Error checking
		// Ensure list is not null
		if (list == null) {
			throw new IllegalArgumentException();
		}
		// If requiring at least 60 images, ensure that minimum size
		if (REQUIRE_60 && list.size() < 60) {
			throw new IllegalArgumentException("ImageList only contains " + list.size() + " images, not at least 60 as required.");
		}
		this.list = list;
	}

	private void setCategories(ArrayList<Category> categories) {
		// Error checking
		// Check that categories is not null and has between two and four elements
		if (categories == null || categories.size() < 2 || categories.size() > 4) {
			throw new IllegalArgumentException("Illegal Categories configuration in Game construction");
		}
		// Error checking complete

		this.categories = categories;
	}

}
