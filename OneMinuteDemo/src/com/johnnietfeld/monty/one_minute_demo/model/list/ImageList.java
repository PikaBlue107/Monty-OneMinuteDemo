package com.johnnietfeld.monty.one_minute_demo.model.list;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.johnnietfeld.monty.one_minute_demo.model.data.ClassifiedImage;

public class ImageList {

	/** Internal LinkedList of Images, used to store them in randomized order */
	LinkedList<ClassifiedImage> images;
	/**
	 * Whether this ImageList should cycle images instead of discarding used ones
	 */
	private boolean cycle = true;
	/**
	 * Whether this ImageList should randomize cycled images instead of presenting
	 * them in the same order
	 */
	private boolean randomizeCycle = true;
	/** LinkedList used to store Images that have already been used */
	LinkedList<ClassifiedImage> used;

	/**
	 * Creates an ImageList from a list of Images. This will drain the provided
	 * List.
	 * 
	 * @param images List of Images to create this class from.
	 */
	public ImageList(List<ClassifiedImage> images) {
		setImages(images);
	}

	/**
	 * Transfers all elements of the provided list into this class' images list, in
	 * a random order.
	 * 
	 * @param provided a List to transfer Images from
	 */
	private void setImages(List<ClassifiedImage> provided) {

		if (provided == null || provided.isEmpty()) {
			throw new IllegalArgumentException();
		}

		images = new LinkedList<ClassifiedImage>();

		Random rand = new Random();

		for (int i = 0; i < provided.size() + images.size(); ++i) {
			// Calculate a random index to remove from
			int removeIndex = rand.nextInt() % provided.size();
			// Remove the image from the provided List, and add it to the internal one.
			images.add(provided.remove(removeIndex));
		}

	}

	/**
	 * Simple getter for this class' internal list's Size
	 * 
	 * @return
	 */
	public int size() {
		return images.size();
	}

	/**
	 * Retrieves the next Image from the series of randomized Images. If this
	 * ImageList is allowed to cycle images, will repeat. Can be randomized or
	 * same-order between cyclings.
	 * 
	 * @return an Image from this class' list of images
	 */
	public ClassifiedImage next() {

		// If we're out of images to show
		if (images.size() == 0) {
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

		// If we're cycling images, add this image to the used images list
		if (cycle) {
			used.add(front);
		}

		// Return the retrieved Image
		return front;
	}

}
