package com.johnnietfeld.monty.one_minute_demo.model.data;

import java.awt.image.BufferedImage;

public class ClassifiedImage {

	/** The Category this ClassifiedImage is correctly classified under */
	private Category category;
	/** The Image contained by this object */
	private BufferedImage image;
	/** The name of this ClassifiedImage */
	private String name;

	/**
	 * Makes a new ClassifiedImage with the provided Image and Category. Ensures that neither
	 * argument is null.
	 * 
	 * @param image    the BufferdImage for this Image
	 * @param category the Category for this Image
	 * @throws IllegalArgumentException if either image or category is null
	 */
	public ClassifiedImage(BufferedImage image, Category category, String name) {
		setImage(image);
		setCategory(category);
		setName(name);
	}

	/**
	 * Setter used for error checking. Ensures the BufferedImage provided is not
	 * null.
	 * 
	 * @param image the BufferedImage to set
	 * @throws IllegalArgumentException if image is null
	 */
	private void setImage(BufferedImage image) {
		if (image == null) {
			throw new IllegalArgumentException("BufferedImage used to construct ClassifiedImage object cannot be null");
		}

		this.image = image;
	}

	/**
	 * Setter used for error checking. Ensures the Category provided is not null.
	 * 
	 * @param category the Category to set
	 * @throws IllegalArgumentException if category is null
	 */
	private void setCategory(Category category) {
		if (category == null) {
			throw new IllegalArgumentException("Category used to construct ClassifiedImage object cannot be null");
		}
		this.category = category;
	}


	/**
	 * Setter used for error checking. Ensures the name provided is not null.
	 * 
	 * @param name the name to set
	 * @throws IllegalArgumentException if name is null or blank
	 */
	private void setName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name used to construct ClassifiedImage object cannot be null or blank");
		}
		this.name = name;
	}

	/**
	 * Simple getter for this Image's correct category
	 * 
	 * @return the category field
	 */
	public Category getCategory() {
		return this.category;
	}

	/**
	 * Simple getter for this Image's stored BufferedImage
	 * 
	 * @return the image field
	 */
	public BufferedImage getImage() {
		return this.image;
	}

	/**
	 * Simple getter for this Image's stored name
	 * 
	 * @return the name field
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Judges whether a certain categorization of an image is correct, based on the
	 * stored category. Uses Category.equals() to compare.
	 * 
	 * @param category the Category to score this ClassifiedImage under
	 * @return true if the provided category matches this Image's category, else
	 *         false
	 */
	public boolean scoreCategory(Category category) {
		return category.equals(this.getCategory());
	}
	
	@Override
	public String toString() {
		return name + ": " + category.toString();
	}
}
