package com.johnnietfeld.monty.one_minute_demo.model.data;

public class Category {

	/** The name of this Category */
	private String name;

	/**
	 * Creates a new Category with the provided name. Requires that name be
	 * non-null, non-blank.
	 * 
	 * @param name the name of this Category
	 * @throws IllegalArgumentException if name is null or blank
	 */
	public Category(String name) {
		setName(name);
	}

	/**
	 * Sets this Category's name. Ensures it is not null or blank.
	 * 
	 * @param name the name to set
	 * @throws IllegalArgumentException if name is null or blank.
	 */
	private void setName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException();
		}
		this.name = name;
	}

	/**
	 * Simple getter for this Category's name
	 * 
	 * @return the name field
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Generates a unique hashcode for this Category object by name.
	 * 
	 * @return a hashcode unique by name
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + name.hashCode();
		return result;
	}

	/**
	 * Compares two categories by String name only.
	 * 
	 * @return true if the two categories have the same name String, otherwise false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * Returns this Category's name
	 * 
	 * @return this Category's name
	 */
	@Override
	public String toString() {
		return getName();
	}

}
