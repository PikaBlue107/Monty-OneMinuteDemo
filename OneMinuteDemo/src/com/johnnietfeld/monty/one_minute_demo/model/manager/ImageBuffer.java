package com.johnnietfeld.monty.one_minute_demo.model.manager;

import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

import com.johnnietfeld.monty.one_minute_demo.model.data.ClassifiedImage;
import com.johnnietfeld.monty.one_minute_demo.ui.DraggableImage;

/**
 * Holds a buffer of ClassifiedImages with loaded BufferedImages
 * 
 * @author Melody
 *
 */
public class ImageBuffer {

	/** Internal Queue used to store ClassifiedImages */
	private Queue<DraggableImage> queue;
	/** Thread to load images */
	private Thread loader;
	/** Number of queued load events */
	private volatile int loadsRemaining;
	/** Game to retrieve the series of ClassifiedImages from */
	Game game;
	/** Preferred size for Images to fit in */
	private Dimension preferredSize;
	/** Preferred center point for Images to sit at */
	private Point center;

	/**
	 * Creates an ImageBuffer with the specified number of initial images. If
	 * initialSize is 0,
	 * 
	 * @param initialSize the initial size of the buffer. Will load that many images
	 *                    at the start of the game, and attempt to maintain that
	 *                    size throughout the game.
	 * @param game        the Game to get the seris of ClassifiedImages from
	 */
	public ImageBuffer(int initialSize, Game game) {
		this(initialSize, game, null, null);
	}

	/**
	 * Creates an ImageBuffer with the specified number of initial images. If
	 * initialSize is 0,
	 * 
	 * @param size   the number of images to load into the Buffer
	 * @param game   the Game to get the series of ClassifiedImages from
	 * @param bounds the Dimension that all loaded Images will be scaled to fit
	 *               within
	 * @param center the Point at which all loaded Images will be centered
	 */
	public ImageBuffer(int size, Game game, Dimension bounds, Point center) {
		// Make sure that initial size is at least 0
		if (size < 0) {
			throw new IllegalArgumentException();
		}

		// Make sure we have a valid game
		if (game == null) {
			throw new IllegalArgumentException("Game cannot be null!");
		}
		this.game = game;

		// Initialize queue and images to load
		queue = new LinkedList<DraggableImage>();
		loadsRemaining = size;

		// Set bounds and center
		setPreferredSize(bounds);
		setCenter(center);

		// Prepare the initial queue
		loader = new LoaderThread();

		// Load all initial Images
		if (size > 0)
			loader.start();
	}

	/**
	 * Sets the preferred size that all Images must fit within
	 * 
	 * @param bounds the Dimension that all loaded images will be scaled to fit
	 *               within
	 */
	public void setPreferredSize(Dimension bounds) {
		if (bounds == null) {
			throw new IllegalArgumentException("Bounds cannot be null!");
		}
		this.preferredSize = bounds;
	}

	/**
	 * Sets the center point that all loaded Images will be at
	 * 
	 * @param center the Point to center loaded images
	 */
	public void setCenter(Point center) {
		if (center == null) {
			throw new IllegalArgumentException("Center cannot be null!");
		}
		this.center = center;
	}

	/**
	 * Retrieves the next image from the queue.
	 * 
	 * @return the nextClassifiedImage in the queue
	 * @throws IllegalStateException if the queue is empty
	 */
	public DraggableImage nextImage() {
		if (queue.isEmpty()) {
			throw new IllegalStateException("Out of images to show!");
		}

		// Remove and return head of queue
		return queue.poll();

	}

	/**
	 * Loads the next ClassifiedImage into the queue and prepares its graphics.
	 */
	public void prepareImage() {

		// Increment the number of loads to be done
		loadsRemaining++;

		// If the thread is dead, start it
		if (!loader.isAlive()) {
			loader = new LoaderThread();
			loader.start();
		}

	}

	/**
	 * Gets the number of elements in the queue
	 * 
	 * @return the queue's size
	 */
	public int size() {
		return queue.size();
	}

	/**
	 * Thread to load images. Runs in sequence until loadsRemaining is 0
	 * 
	 * @author Melody
	 *
	 */
	private class LoaderThread extends Thread {
		public void run() {

			// Attempt to get the next ClassifiedImage from the Game's stream and load its
			// BufferedImage
			try {
				ClassifiedImage buffer = game.nextImage();

				buffer.loadImage();

				DraggableImage newImage = new DraggableImage(buffer);
				newImage.fitImage(preferredSize, center); // TODO: Get Center Point

				queue.add(newImage);

				// If there's still loads to be done, run the thread again
				if (--loadsRemaining >= 0) {
					loader = new LoaderThread();
					loader.start();
				}

			} catch (IllegalStateException e) {
				// Do nothing but let the list dwindle
			}
		}
	}

}