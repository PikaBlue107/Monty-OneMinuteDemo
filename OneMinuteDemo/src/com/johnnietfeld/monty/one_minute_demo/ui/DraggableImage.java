package com.johnnietfeld.monty.one_minute_demo.ui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.johnnietfeld.monty.one_minute_demo.model.data.ClassifiedImage;

/**
 * Custom ImageIcon gui component that allows dragging with the mouse
 * 
 * @author Melody
 *
 */
public class DraggableImage extends JLabel {

	/** ID for serialization */
	private static final long serialVersionUID = -1796984509726502070L;
	/** ClassifiedImage that this DraggableImage is using for its image. */
	private ClassifiedImage content = null;
	/** ImageIcon being used to show BufferedImages */
	private ImageIcon icon = null;
	/** Mouse Listener for GUI events that happen on mouse release before moving */
	private MouseListener beforeReleaseListener = null;
	/** Mouse Listener for GUI events that happen on mouse drag after moving */
	private MouseListener afterDragListener = null;
	/** Mouse Listener for GUI events that happen on mouse click */
	private MouseListener onClickListener = null;
	/** Maximum percent of a Category's width or height that an Image may take */
	private static final double MAX_IMAGE_RATIO = 0.75;

	/**
	 * Creates a DraggableImage with no saved Image.
	 */
	public DraggableImage() {
		this(null);
	}

	/**
	 * Creates a DraggableImage from the provided ClassifiedImage. Uses the
	 * ClassifiedImage's loaded image. If it is not loaded when passed to this
	 * constructor, it will be loaded.s
	 * 
	 * @param image ClassifiedImage to show
	 */
	public DraggableImage(ClassifiedImage image) {
		super();
		icon = new ImageIcon();
		this.setIcon(icon);
		DragListener dl = new DragListener();
		addMouseListener(dl);
		addMouseMotionListener(dl);
		setSource(image);
		// Debug
//		setBorder(bdrGray);	// TODO: Move to main
	}

	/**
	 * Sets the MouseListener this DraggableImage will use before relocating
	 * 
	 * @param beforeListener a MouseListener for events to be processed before the
	 *                       image is moved on mouse release
	 */
	public void setMouseListenerBeforeRelease(MouseListener beforeListener) {
		this.beforeReleaseListener = beforeListener;
	}

	/**
	 * Sets the MouseListener this DraggableImage will use after relocating
	 * 
	 * @param afterListener a MouseListener for events to be processed after the
	 *                      image is moved on mouse drag
	 */
	public void setMouseListenerAfterDrag(MouseListener afterListener) {
		this.afterDragListener = afterListener;
	}

	/**
	 * Sets the MouseListener this DraggableImage will use on click
	 * 
	 * @param afterListener a MouseListener for events to be processed when the
	 *                      image is clicked
	 */
	public void setMouseListenerOnClick(MouseListener afterListener) {
		this.afterDragListener = afterListener;
	}

	/**
	 * Makes this DraggableImage use a new ClassifiedImage for its image
	 * 
	 * @param newImage new ClassifiedImage to use
	 */
	public void setSource(ClassifiedImage newImage) {
		if (content != null)
			content.flushLoadedImage();
		content = newImage;
		if (newImage != null) {
			icon.setImage(content.getLoadedImage().getImage());
			this.setSize(icon.getIconWidth(), icon.getIconHeight());
		}
	}

	/**
	 * Retrieves this DraggableImage's ClassifiedImage
	 * 
	 * @return the content field
	 */
	public ClassifiedImage getClassifiedImage() {
		return content;
	}

	public void transferData(DraggableImage other) {
		this.content = other.content;
		this.icon.setImage(other.icon.getImage());
	}

	/**
	 * If the ClassifiedImage content of this DraggableImage is not null, will
	 * resize the image to fit within a Category Panel
	 * 
	 * @param maxSize the maximum size to allow the image to take up
	 * @param center the center at which to locate the fit image
	 */
	public void fitImage(Dimension maxSize, Point center) {
		if (content == null)
			return;

//		BufferedImage scaled = ensureImageWithin(content.getLoadedImage(), (BufferedImage) icon.getImage(),
//				maxSize);

		// Get the current Dimension of the image
		Dimension currentSize = new Dimension(icon.getIconWidth(),
				icon.getIconHeight()); // TODO: Try removing cast and using
																// ImageIcon.getIconWidth() and ...Height()

		// Figure out what ratio we need to scale it by
		double scaleRatio = 1.0;

		// First, if it's smaller than the ideal size, scale it up
		if (currentSize.getWidth() * scaleRatio < maxSize.getWidth()) {
			scaleRatio *= maxSize.getWidth() / (currentSize.getWidth() * scaleRatio);
		}
		if (currentSize.getHeight() * scaleRatio < maxSize.getHeight()) {
			scaleRatio *= maxSize.getWidth() / (currentSize.getHeight() * scaleRatio);
		}
		// Next, if it's larger than the ideal size, shrink it down
		if (currentSize.getWidth() * scaleRatio > maxSize.getWidth()) {
			scaleRatio *= maxSize.getWidth() / (currentSize.getWidth() * scaleRatio);
		}
		if (currentSize.getHeight() * scaleRatio > maxSize.getHeight()) {
			scaleRatio *= maxSize.getHeight() / (currentSize.getHeight() * scaleRatio);
		}

		// If the scale ratio is 1, then we don't need to make a new BufferedImage
		if (scaleRatio - 1.0 < 0.01 && scaleRatio - 1.0 > -0.01) {
			return;
		} else {
			// Otherwise we're gonna have to scale from the source image
			// Multiply the ratio by the difference between current scaling and source size
//			scaleRatio *= currentSize.getWidth() / content.getLoadedImage().getIconWidth();
		}
		
		// Scale down by the maximum category fill percentage
		scaleRatio *= MAX_IMAGE_RATIO;

		// Scale ratio should be correctly calculated now

		// Create new Dimension that the image will be scaled to
		Dimension destinationSize = new Dimension((int) (currentSize.getWidth() * scaleRatio),
				(int) (currentSize.getHeight() * scaleRatio));

		// Create new BufferedImage
		Image scaledImage = new BufferedImage((int) destinationSize.getWidth(), (int) destinationSize.getHeight(),
				BufferedImage.TYPE_INT_ARGB);

		// Apply transformation to image
		scaledImage = content.getLoadedImage().getImage().getScaledInstance(destinationSize.width,
				destinationSize.width, Image.SCALE_FAST);

		// Set image and update minimum size
		icon.setImage(scaledImage);
		this.setSize(destinationSize);

		// Calculate desired location with center point and this object's width and
		// height
		Point desiredLocation = new Point(center.x - this.getWidth() / 2, center.y - this.getHeight() / 2);

		this.setLocation(desiredLocation);

	}

	/**
	 * Allows a component to be dragged by the mouse.
	 * 
	 * @author Melody
	 *
	 */
	private class DragListener extends MouseAdapter {
		/** This component's starting position */
		private int myX, myY;
		/** The mouse's starting position for a drag click */
		private int screenX, screenY;

		@Override
		public void mousePressed(MouseEvent e) {
			// Save initial click location
			screenX = e.getXOnScreen();
			screenY = e.getYOnScreen();
			// Save initial Component location
			myX = DraggableImage.this.getX();
			myY = DraggableImage.this.getY();
			// Trigger the click listener
			if (onClickListener != null) {
				onClickListener.mousePressed(e);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// Trigger the before-release listener
			if (beforeReleaseListener != null) {
				beforeReleaseListener.mouseReleased(e);
			}
			// Return to starting position
			DraggableImage.this.setLocation(myX, myY);
			DraggableImage.this.repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// Calculate difference between current mouse position and initial click
			// location
			int deltaX = e.getXOnScreen() - screenX;
			int deltaY = e.getYOnScreen() - screenY;
			// Create location point
			Point location = new Point(myX + deltaX, myY + deltaY);
			// Set location of component as a displacement from initial position
			DraggableImage.this.setLocation(location);
			DraggableImage.this.repaint();
			// Trigger the after-drag listener
			if (afterDragListener != null) {
				afterDragListener.mouseReleased(e);
			}
		}
	}
}