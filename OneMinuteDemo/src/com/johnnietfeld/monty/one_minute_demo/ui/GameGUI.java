package com.johnnietfeld.monty.one_minute_demo.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.johnnietfeld.monty.one_minute_demo.model.data.Category;
import com.johnnietfeld.monty.one_minute_demo.model.data.ClassifiedImage;
import com.johnnietfeld.monty.one_minute_demo.model.manager.Game;

public class GameGUI extends JPanel {

	// TODO: Change from BufferedImage loading to ImageIcon loading

	// Constants

	/** Minimum width in pixels for a Category: 200 */
	private static final int MIN_CATEGORY_WIDTH = 200;
	/** Minimum height in pixels for a Category: 200 */
	private static final int MIN_CATEGORY_HEIGHT = 200;
	/** Maximum percent of a Category's width or height that an Image may take */
	private static final double MAX_IMAGE_RATIO = 0.75;
	/** Minimum GUI width */
	private static final int MIN_WIDTH = 500;
	/** Minimum GUI height */
	private static final int MIN_HEIGHT = 500;
	/** Maximum GUI width */
	private static final int MAX_WIDTH = 1000;
	/** Maximum GUI height */
	private static final int MAX_HEIGHT = 1000;

	// Instance variables

	/** Generated class ID for serialization */
	private static final long serialVersionUID = -4436194428355121272L;
	/** Title of the window */
	private static final String TITLE_STRING = "One Minute Game - DEMO";
	/** Frame that holds the one minute game */
	JFrame gui;
	/** Panel to hold all GUI content but the image */
	JPanel main;
	/** Common grouping border */
	Border bdrGray;
	/** Highlighted border for hovering over Category panels */
	Border bdrHighlight;
	/** Game object to hold all data about this Game */
	Game game;
	/** Header font to use across GUI */
	Font fntHeader;
	/** Body font to use across GUI */
	Font fntDetails;
	/** Header panel to show score, timer, and instructions */
	HeaderPanel hp;
	/** Classify panel for dragging images */
	ClassifyPanel cp;
	/** Image to be dragged around the GUI */
	DraggableImage di;
	/** DragListener to keep track of when the user is dragging an image */
	DragListener dl;
	/** Buffer used to keep loaded images available */
	ImageBuffer buffer;

	public GameGUI(Game toPlay) {
		super();

		// Check that Game toPlay is not null
		if (toPlay == null) {
			throw new IllegalArgumentException("Cannot start a Game GUI with a null Game");
		}

		// Assign game to be played
		game = toPlay;

		// Get screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// Create fonts to use across GUI
		fntHeader = new Font("San Francisco", Font.PLAIN, 20);
		fntDetails = new Font("San Francisco", Font.PLAIN, 14);

		// Initialize common border
		bdrGray = BorderFactory.createEtchedBorder();
		bdrHighlight = BorderFactory.createEtchedBorder(Color.ORANGE, Color.YELLOW);

		// Set window title, fill screen and center, exit on close
		gui = new JFrame(TITLE_STRING);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension halfScreen = new Dimension(screenSize.width / 2, screenSize.height / 2);
		gui.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		gui.setPreferredSize(min(halfScreen, new Dimension(MAX_WIDTH, MAX_HEIGHT)));

		// Setup content pane for adding content
//		Container c = gui.getContentPane();
		gui.add(this);
		setLayout(new OverlayLayout(this));

		// Create Draggable Image to place over main GUI
		di = new DraggableImage();
		// Create panel to hold Draggable Image
		JPanel dragPanel = new JPanel(null);
		dragPanel.setOpaque(false);
		dragPanel.add(di);

		// Add draggable to final GUI
		add(dragPanel);

		// Create main GUI content
		main = new JPanel(new BorderLayout());
		// Add header panel to main
		hp = new HeaderPanel();
		main.add(hp, BorderLayout.NORTH);
		// Add classify panel to main
		cp = new ClassifyPanel();
		main.add(cp, BorderLayout.CENTER);
		// Add main to final GUI
		add(main);

		// Finalize GUI
		gui.pack();
		gui.setLocationRelativeTo(null);

		// Initialize the Image Buffer with 5 elements
		buffer = new ImageBuffer(2);

		// Load first element into DraggableImage
		ClassifiedImage nextImage = game.nextImage();
		System.out.println(nextImage);
		di.transferData(new DraggableImage(nextImage));
//		di.transferData(buffer.nextImage());
//		buffer.prepareImage();
		di.fitImage();

//		ImageResizer resizer = new ImageResizer();
//		gui.addComponentListener(resizer);
//		gui.addMouseListener(resizer);
		// Run update on ClassifyPanel to use first image
		gui.setVisible(true);
	}

	private class HeaderPanel extends JPanel {
		/** ID for serialization */
		private static final long serialVersionUID = -8964188691812914865L;
		/** Panel to hold Timer and Score information */
		JPanel pnlTopHeader;
		/** Panel for time */
		JPanel pnlTime;
		/** Time title */
		JLabel lblTimeTitle;
		/** Time value */
		JLabel lblTimeValue;
		/** Panel for score */
		JPanel pnlScore;
		/** Score title */
		JLabel lblScoreTitle;
		/** Score value */
		JLabel lblScoreValue;
		/** Panel to hold instructions */
		JPanel pnlInstructions;
		/** Instructions title */
		JLabel lblInstructionsTitle;
		/** Instructions value */
		JTextArea lblInstructionsValue;

		public HeaderPanel() {
			super();
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.setBorder(bdrGray);

			// Create top header panel for time and score
			pnlTopHeader = new JPanel(new GridLayout(1, 2));
			// Panel for Time
			pnlTime = new JPanel();
			pnlTime.setBorder(bdrGray);
			// Time title
			lblTimeTitle = new JLabel("Time:");
			lblTimeTitle.setFont(fntHeader);
			pnlTime.add(lblTimeTitle);
			// Time value
			lblTimeValue = new JLabel(game.getTime() + "");
			lblTimeValue.setFont(fntHeader);
			pnlTime.add(lblTimeValue);
			// Add time panel to top header panel
			pnlTopHeader.add(pnlTime);
			// Panel for Score
			pnlScore = new JPanel();
			pnlScore.setBorder(bdrGray);
			// Score title
			lblScoreTitle = new JLabel("Score:");
			lblScoreTitle.setFont(fntHeader);
			pnlScore.add(lblScoreTitle);
			// Score value
			lblScoreValue = new JLabel(game.getScore() + "");
			lblScoreValue.setFont(fntHeader);
			pnlScore.add(lblScoreValue);
			// Add score panel to top header panel
			pnlTopHeader.add(pnlScore);
			// Add top panel to this Header panel
			this.add(pnlTopHeader);

			// Create bottom panel for instructions
			pnlInstructions = new JPanel();
			pnlInstructions.setBorder(bdrGray);
			pnlInstructions.setLayout(new BoxLayout(pnlInstructions, BoxLayout.Y_AXIS));
			pnlInstructions.setAlignmentX(JPanel.CENTER_ALIGNMENT);
			// Instructions title
			lblInstructionsTitle = new JLabel("Instructions:");
			lblInstructionsTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			lblInstructionsTitle.setFont(fntHeader);
			pnlInstructions.add(lblInstructionsTitle);
			// Instructions value
			lblInstructionsValue = new JTextArea(game.getInstructions());
			lblInstructionsValue.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			lblInstructionsValue.setLineWrap(true);
			lblInstructionsValue.setWrapStyleWord(true);
			lblInstructionsValue.setEditable(false);
			lblInstructionsValue.setFont(fntDetails);
			pnlInstructions.add(lblInstructionsValue);
			// Add instructions panel to this Header panel
			this.add(pnlInstructions);
		}

		public void updateScore(int score) {
			lblScoreValue.setText(score + "");
		}

	}

	@SuppressWarnings("unused")
	private class ImageResizer extends ComponentAdapter implements MouseListener {

		private boolean resizing = false;

		@Override
		public void mousePressed(MouseEvent e) {
			resizing = false;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (resizing == true) {
//				draggableImage.checkResize();
			}
		}

		@Override
		public void componentResized(ComponentEvent e) {
			resizing = true;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// Unused
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// Unused
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// Unused
		}

	}

	private Dimension min(Dimension one, Dimension two) {
		int width = one.width < two.width ? one.width : two.width;
		int height = one.height < two.height ? one.height : two.height;
		return new Dimension(width, height);
	}

	/**
	 * Panel to hold the main drag-and-drop game content.
	 * 
	 * @author Melody
	 *
	 */
	private class ClassifyPanel extends JPanel {
		/** ID for serialization */
		private static final long serialVersionUID = -6679876061602718639L;

		/** Panel to place draggable images over */
		JPanel pnlImages;
		/** Panel to hold categories */
		JPanel pnlCategories;
		/** Array to hold CategoryPanels */
		CategoryPanel[] categoryPanels;
//		/** The current ClassifiedImage being shown */
//		ClassifiedImage currentImage = null;
//		/** The next ClassifiedImage to be shown */
//		ClassifiedImage nextImage = null;

		/**
		 * Creates the Classify Panel. Uses a GridBagLayout to place the image and
		 * classify sub-panels with even space.
		 */
		public ClassifyPanel() {
			super(new GridBagLayout());

			// Create main panel for game content
			setBorder(bdrGray);

			addImagePanel();
			addCategoryPanel();
		}

		public Point getImageCenter() {
			Rectangle bounds = pnlImages.getBounds();
			Point center = new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
			return center;
		}

		public JPanel getCategoriesPanel() {
			return pnlCategories;
		}

		/**
		 * Adds the main Image panel to "hold" the DraggableImage
		 */
		private void addImagePanel() {

			// Category panels

			// Add mid panel to main panel
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1;
			gbc.weighty = 0.5;
			pnlImages = new JPanel();
			add(pnlImages, gbc);
		}

		/**
		 * Adds the Category panel to hold the category boxes
		 */
		private void addCategoryPanel() {

			// Store Categories List for constructing panel
			ArrayList<Category> categories = game.getCategories();
			// Bottom panel for displaying categories
			pnlCategories = new JPanel(new GridLayout(1, categories.size()));
			// Initialize array to hold CategoryPanels
			categoryPanels = new CategoryPanel[categories.size()];
			// Iterate through categories, for each add a new JLabel
			for (int i = 0; i < categories.size(); ++i) {
				// Get category at index i from Categories ArrayList
				Category category = categories.get(i);
				// Make JPanel to show border and center JLabel
				categoryPanels[i] = new CategoryPanel(category);
				categoryPanels[i].setBorder(bdrGray);
				categoryPanels[i].setMinimumSize(new Dimension(MIN_CATEGORY_WIDTH, MIN_CATEGORY_HEIGHT));
				// Create custom JLabel that will remember its Category
				JLabel categoryLabel = new JLabel(category.getName());
				categoryLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				categoryLabel.setAlignmentY(JLabel.BOTTOM_ALIGNMENT); // TODO: Why is this aligning at top?
				categoryLabel.setFont(fntHeader);
				categoryPanels[i].add(categoryLabel);
				pnlCategories.add(categoryPanels[i]);
			}

			// Add categories panel to main panel
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weightx = 1;
			gbc.weighty = 0.5;
			this.add(pnlCategories, gbc);
		}

		private CategoryPanel hoveredPanel(Point p) {
			for (CategoryPanel cp : categoryPanels) {
				Rectangle location = cp.getBounds();
				if (location.contains(p)) {
					return cp;
				}
			}
			return null;
		}

		private Dimension getCurrentCategorySize() {
			Dimension size = new Dimension(categoryPanels[0].getWidth(), categoryPanels[0].getHeight());
			return size;
		}

	}

	/**
	 * Custom ImageIcon gui component that allows dragging with the mouse
	 * 
	 * @author Melody
	 *
	 */
	private class DraggableImage extends JLabel {

		/** ID for serialization */
		private static final long serialVersionUID = -1796984509726502070L;
		/** ClassifiedImage that this DraggableImage is using for its image. */
		private ClassifiedImage content = null;
		/** ImageIcon being used to show BufferedImages */
		private ImageIcon icon = null;
		/** Dimension that the DraggableImage must fit inside */
		private Dimension maxSize = null;

		/**
		 * Creates a DraggableImage with no saved Image.
		 */
		public DraggableImage() {
			this(null);
		}

		/**
		 * Creates a DraggableImage from the provided ClassifiedImage. Will show its
		 * image if not null.
		 * 
		 * @param image ClassifiedImage to show
		 */
		public DraggableImage(ClassifiedImage image) {
			super();
			icon = new ImageIcon();
			this.setIcon(icon);
			maxSize = new Dimension();
			DragListener dl = new DragListener();
			addMouseListener(dl);
			addMouseMotionListener(dl);
			setSource(image);
			// Debug
			setBorder(bdrGray);
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
				icon.setImage(content.getLoadedImage());
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
			this.maxSize = other.maxSize;
		}

		/**
		 * If the ClassifiedImage content of this DraggableImage is not null, will
		 * resize the image to fit within a Category Panel
		 */
		public void fitImage() {
			if (content == null)
				return;

			// Update max size based on current category size
			maxSize.setSize((int) (cp.getCurrentCategorySize().width * MAX_IMAGE_RATIO),
					(int) (cp.getCurrentCategorySize().height * MAX_IMAGE_RATIO));

//			BufferedImage scaled = ensureImageWithin(content.getLoadedImage(), (BufferedImage) icon.getImage(),
//					maxSize);

			// Get the current Dimension of the image
			Dimension currentSize = new Dimension(((BufferedImage) icon.getImage()).getWidth(),
					((BufferedImage) icon.getImage()).getHeight()); // TODO: Try removing cast and using
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
				scaleRatio *= currentSize.getWidth() / content.getLoadedImage().getWidth();
			}

			// Scale ratio should be correctly calculated now

			// Create new Dimension that the image will be scaled to
			Dimension destinationSize = new Dimension((int) (currentSize.getWidth() * scaleRatio),
					(int) (currentSize.getHeight() * scaleRatio));

			// Create new BufferedImage
			BufferedImage scaledImage = new BufferedImage((int) destinationSize.getWidth(),
					(int) destinationSize.getHeight(), BufferedImage.TYPE_INT_ARGB);

			// Create AffineTransform and Operation
			final AffineTransform at = AffineTransform.getScaleInstance(scaleRatio, scaleRatio);
			final AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);

			// Apply transformation to image
			scaledImage = ato.filter(content.getLoadedImage(), scaledImage);

			// Set image and update minimum size
			icon.setImage(scaledImage);
			this.setSize(destinationSize);

			// Move this JLabel to center the image
			Point centerLocation = cp.getImageCenter();

			// Calculate desired location with center point and this object's width and
			// height
			Point desiredLocation = new Point(centerLocation.x - this.getWidth() / 2,
					centerLocation.y - this.getHeight() / 2 + hp.getHeight());

			this.setLocation(desiredLocation);

		}

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
		/** Keeps track of the Category Panel that the dragger is currently hovering */
		CategoryPanel hoveredCategory;

		@Override
		public void mousePressed(MouseEvent e) {
			// Save initial click location
			screenX = e.getXOnScreen();
			screenY = e.getYOnScreen();
			// Save initial Component location
			myX = di.getX();
			myY = di.getY();
			// Start in middle, so not over category
			hoveredCategory = null;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// Return to starting position
			di.setLocation(myX, myY);
			gui.repaint();
			// If we're hovering over a Category panel, process the image classification
			if (hoveredCategory != null) {
				System.out.println("Started drop");
				// Set the hovered category's border back to normal
				hoveredCategory.setBorder(bdrGray);
				// Process the scoring of the image by the selected Category
				game.scoreImage(di.getClassifiedImage(), hoveredCategory.getCategory());
				// Update score
				hp.updateScore(game.getScore());
				// Load next image from Game
				System.out.println("Retrieving next image");
				di = buffer.nextImage();
				System.out.println("Got image");
				di.fitImage();
				System.out.println("Resizing image");
				// Clear hovered category from Dragger memory
				hoveredCategory = null;
				// Tell the Image Loader to prepare another image
				System.out.println("Starting load");
				buffer.prepareImage();
				System.out.println("Finished load");
			}
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
			di.setLocation(location);
			gui.repaint();

			// Check if we need to highlight or un-highlight a Category panel
			// Get the location of the cursor relative to the Categories Panel
			Point locationOnCategoriesPanel = SwingUtilities.convertPoint(di, e.getPoint(), cp.getCategoriesPanel());
			// Get the hovered panel from that point. If it's null, there's no selected
			// panel
			CategoryPanel foundCategory = cp.hoveredPanel(locationOnCategoriesPanel);

			if (foundCategory != hoveredCategory) {
				if (hoveredCategory != null)
					hoveredCategory.setBorder(bdrGray);
				hoveredCategory = foundCategory;
				if (foundCategory != null)
					foundCategory.setBorder(bdrHighlight);
			}
		}
	}

	/**
	 * JPanel that remembers its Category for use in scoring ClassifiedImages
	 * 
	 * @author Melody
	 *
	 */
	private class CategoryPanel extends JPanel {

		/** Class ID for serialization */
		private static final long serialVersionUID = -1154560654699487164L;
		/** The Category saved in this JPanel */
		private Category saved;

		public CategoryPanel(Category category) {
			super();
			saved = category;
		}

		public Category getCategory() {
			return saved;
		}
	}

	/**
	 * Holds a buffer of ClassifiedImages with loaded BufferedImages
	 * 
	 * @author Melody
	 *
	 */
	private class ImageBuffer {

		/** Internal Queue used to store ClassifiedImages */
		private Queue<DraggableImage> queue;
		/** Thread to load images */
		private Thread loader;
		/** Number of queued load events */
		private volatile int loadsRemaining;

		public ImageBuffer(int initialSize) {
			queue = new LinkedList<DraggableImage>();
			loadsRemaining = initialSize;

			// Prepare the initial queue
			loader = new LoaderThread();

			// Load all initial Images
			if (initialSize > 0)
				loader.run();
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
				loader.run();
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

				System.out.println();

				// Attempt to get the next ClassifiedImage from the Game's stream and load its
				// BufferedImage
				try {
					ClassifiedImage buffer = game.nextImage();

					buffer.loadImage();

					DraggableImage newImage = new DraggableImage(buffer);
					newImage.fitImage();

					queue.add(newImage);

					// If there's still loads to be done, run the thread again
					if (--loadsRemaining >= 0) {
						this.run();
					}

				} catch (IllegalStateException e) {
					// Do nothing but let the list dwindle
				}
			}
		}

	}

}
