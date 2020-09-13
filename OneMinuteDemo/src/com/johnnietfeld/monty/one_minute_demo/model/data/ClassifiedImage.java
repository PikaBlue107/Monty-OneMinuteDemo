package com.johnnietfeld.monty.one_minute_demo.model.data;

import javax.swing.ImageIcon;

public class ClassifiedImage { // TODO: Implement. Closable.

	/** The Category this ClassifiedImage is correctly classified under */
	private Category category;
	/** The location of the image contained by this object */
	private String image_location;
	/** The name of this ClassifiedImage */
	private String name;
	/** The image of this ClassifiedImage, loaded into memory */
	private ImageIcon loadedImage;

	/**
	 * Makes a new ClassifiedImage with the provided Image and Category. Ensures
	 * that neither argument is null.
	 * 
	 * @param image_location the BufferdImage for this Image
	 * @param category       the Category for this Image
	 * @param name 			 the file name for this image
	 * @throws IllegalArgumentException if either image or category is null
	 */
	public ClassifiedImage(String image_location, Category category, String name) {
		setImage(image_location);
		setCategory(category);
		setName(name);
	}

	/**
	 * Setter used for error checking. Ensures the BufferedImage provided is not
	 * null.
	 * 
	 * @param image_location the BufferedImage to set
	 * @throws IllegalArgumentException if image is null
	 */
	private void setImage(String image_location) {
		if (image_location == null) {
			throw new IllegalArgumentException("BufferedImage used to construct ClassifiedImage object cannot be null");
		}

		this.image_location = image_location;
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
	 * Simple getter for this Image's stored File location
	 * 
	 * @return the image_location field
	 */
	public String getImageLocation() {
		return this.image_location;
	}

	/**
	 * Loads the image associated with this ClassifiedImage into memory, and saves
	 * it to this ClassifiedImage.
	 * 
	 * @throws IllegalArgumentException if the image at the location saved by this
	 *                                  ClassifiedImage cannot be loaded.
	 */
	public void loadImage() {
//		try {
		loadedImage = new ImageIcon(image_location);
//		} catch (IOException e) {
//			throw new IllegalArgumentException("Image located at " + image_location + " could not be loaded.");
//		}
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

	/**
	 * Retrieves this ClassifiedImage's loaded BufferedImage in memory. If that
	 * image does not exist, make it first.
	 * 
	 * @return a BufferedImage described by this ClassifiedImage
	 * @throws IllegalArgumentException if the image at the location saved by this
	 *                                  ClassifiedImage cannot be loaded.
	 */
	public ImageIcon getLoadedImage() {
		if (loadedImage == null) {
			loadImage();
		}
		return loadedImage;
	}

	/**
	 * Clears the internal loaded image from memory, making the BufferedImage
	 * available for garbage collection.
	 */
	public void flushLoadedImage() {
		loadedImage = null;
	}

	/**
	 * Tells whether this ClassifiedImage's ImageIcon is loaded or not
	 * 
	 * @return true if the image is loaded, false if it is not
	 */
	public boolean isLoaded() {
		return loadedImage != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + category.hashCode();
		result = prime * result + image_location.hashCode();
		result = prime * result + name.hashCode();
		return result;
	}

	/**
	 * Compares two ClassifiedImages on category, image location, and name.
	 * 
	 * @return true if the ClassifiedImages share each of those three fields,
	 *         otherwise false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassifiedImage other = (ClassifiedImage) obj;
		if (!category.equals(other.category))
			return false;
		if (!image_location.equals(other.image_location))
			return false;
		if (!name.equals(other.name))
			return false;
		return true;
	}

}
