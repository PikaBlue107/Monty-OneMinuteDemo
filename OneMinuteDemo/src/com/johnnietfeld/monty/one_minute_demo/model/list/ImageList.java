package com.johnnietfeld.monty.one_minute_demo.model.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.johnnietfeld.monty.one_minute_demo.model.data.ClassifiedImage;

public class ImageList implements Iterable<ClassifiedImage> {

	/** Internal LinkedList of Images, used to store them in randomized order */
	LinkedList<ClassifiedImage> images;

	/**
	 * Whether this ImageList should cycle images instead of discarding used ones
	 */
	private boolean cycle = false;

	/**
	 * Whether this ImageList should randomize cycled images instead of presenting
	 * them in the same order
	 */
	private boolean randomizeCycle = false;

	/** LinkedList used to store Images that have already been used */
	LinkedList<ClassifiedImage> used;

	/** Random generator for shuffling of List elements */
	static Random rand = new Random();

	/**
	 * Creates an ImageList from a list of Images. This will not affect the provided
	 * List.
	 * 
	 * @param images List of Images to create this class from.
	 * @throws IllegalArgumentException if the provided List is empty
	 * @throws NullPointerException     if the provided List is null
	 */
	public ImageList(List<ClassifiedImage> images) {
		// Copy the provided list to prevent modification of the source list
		ArrayList<ClassifiedImage> copy = new ArrayList<ClassifiedImage>(images);
		// Assign the images into this ImageList's internal list
		setImages(copy);
		// Initialize the used images list
		used = new LinkedList<ClassifiedImage>();

	}

	/**
	 * Package utility function for testing randomization.
	 * 
	 * @param seed long seed to use
	 */
	static void setRandSeed(long seed) {
		rand.setSeed(seed);
	}

	/**
	 * Sets whether this ImageList will cycle images upon using each one in the
	 * list.
	 * 
	 * @param assignment true to activate cycling, false to disable
	 */
	public void setCycle(boolean assignment) {
		this.cycle = assignment;
	}

	/**
	 * Sets whether or not the list of images is scrambled between cycles. Does
	 * nothing if cycles() is false;
	 * 
	 * @param assignment true for randomize image order on cycle reset, else false
	 */
	public void setRandomizeCycle(boolean assignment) {
		this.randomizeCycle = assignment;
	}

	/**
	 * Tells whether this ImageList allows cycling of images upon exhausting the
	 * list.
	 * 
	 * @return true if the list cycles, else false
	 */
	public boolean doesCycle() {
		return cycle;
	}

	/**
	 * Tells whether this ImageList randomizes the list of images between cycles.
	 * 
	 * @return true if it will randomize, else false
	 */
	public boolean doesRandomize() {
		return randomizeCycle;
	}

	/**
	 * Transfers all elements of the provided list into this class' images list, in
	 * a random order.
	 * 
	 * @param provided a List to transfer Images from
	 * @throws IllegalArgumentException if the provided List is empty
	 * @throws NullPointerException     if the provided List is null
	 */
	private void setImages(List<ClassifiedImage> provided) {

		if (provided.isEmpty()) {
			throw new IllegalArgumentException();
		}

		images = new LinkedList<ClassifiedImage>();

		for (int i = 0; i < provided.size() + images.size(); ++i) {
			// Calculate a random index to remove from
			int removeIndex = rand.nextInt(provided.size());
			// Remove the image from the provided List, and add it to the internal one.
			images.add(provided.remove(removeIndex));
		}

	}

	/**
	 * Gets how many images are in the list in total.
	 * 
	 * @return the total amount of images in this ImageList
	 */
	public int size() {
		return images.size() + used.size();
	}

	/**
	 * Gets how many images are left in the images list
	 * 
	 * @return the size of the internal images aray
	 */
	public int remaining() {
		return images.size();
	}

	/**
	 * Retrieves the next Image from the series of randomized Images. If this
	 * ImageList is allowed to cycle images, will repeat. Can be randomized or
	 * same-order between cycles.
	 * 
	 * @return an Image from this class' list of images
	 * @throws IllegalStateException if the list is exhausted and cycle is not
	 *                               enabled
	 */
	public ClassifiedImage next() {

		// If we're out of images to show
		if (remaining() == 0) {
			// And if we're allowed to cycle images
			if (cycle) {
				// If we're randomizing between each cycle
				if (randomizeCycle) {
					// Use the randomize function. This will transfer all elements of the used list
					// to the images list in a random order.
					setImages(used);
				} else { // If we're not randomizing between each cycle
					// Discard the old images list, assign the images list to the used list, and
					// make a new used list.
					images = used;
					used = new LinkedList<ClassifiedImage>();
				}
			} else { // If we're out of images and not allowed to cycle, we cannot continue
				throw new IllegalStateException("Ran out of pictures to display");
			}
		}

		// Get the front image from the queue
		ClassifiedImage front = images.poll();

		// Add this image to the used images list
		used.add(front);

		// Return the retrieved Image
		return front;
	}

	/**
	 * Allows iteration over the Images in this ImageList without modification.
	 * 
	 * @return an Iterator over the ImageList for each ClassifiedImage in the List
	 */
	@Override
	public Iterator<ClassifiedImage> iterator() {
		return new Iterator<ClassifiedImage>() {

			int idx = 0;

			@Override
			public boolean hasNext() {
				return idx < images.size();
			}

			@Override
			public ClassifiedImage next() {
				return images.get(idx++);
			}
		};
	}

}
