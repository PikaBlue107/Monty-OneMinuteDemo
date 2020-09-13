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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;

import com.johnnietfeld.monty.one_minute_demo.model.data.Category;
import com.johnnietfeld.monty.one_minute_demo.model.data.ClassifiedImage;
import com.johnnietfeld.monty.one_minute_demo.model.manager.Game;
import com.johnnietfeld.monty.one_minute_demo.model.manager.ImageBuffer;

public class GameGUI extends JPanel {

	// TODO: Change from BufferedImage loading to ImageIcon loading

	// Constants

	/** Minimum width in pixels for a Category: 200 */
	private static final int MIN_CATEGORY_WIDTH = 200;
	/** Minimum height in pixels for a Category: 200 */
	private static final int MIN_CATEGORY_HEIGHT = 200;
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
	/** CategoryDragListener to keep track of when the user is dragging an image */
	CategoryDragListener dl;
	/** Buffer used to keep loaded images available */
	ImageBuffer buffer;
	/** Game timer. */
	Timer timer;
	/** Panel holding the draggable image. */
	JPanel dragPanel;

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
		dragPanel = new JPanel(null);
		dragPanel.setOpaque(false);
		dragPanel.add(di);

		// Add draggable to final GUI
		add(dragPanel);
		// Create drag listener
		dl = new CategoryDragListener();

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
		
		// Set up timer
		setupTimer();

		// Finalize GUI
		gui.pack();
		gui.setLocationRelativeTo(null);

		// Initialize the Image Buffer with 5 elements
		buffer = new ImageBuffer(5, game, cp.getCurrentCategorySize(), cp.getImageCenter());

		// Load first element into DraggableImage
		ClassifiedImage nextImage = game.nextImage();
		di.transferData(new DraggableImage(nextImage));
//		di.transferData(buffer.nextImage());
//		buffer.prepareImage();
		Point centerPoint = cp.getImageCenter();
		centerPoint.translate(0, hp.getHeight());
		di.fitImage(cp.getCurrentCategorySize(), centerPoint);
		di.addMouseListener(dl);
		di.addMouseMotionListener(dl);

//		ImageResizer resizer = new ImageResizer();
//		gui.addComponentListener(resizer);
//		gui.addMouseListener(resizer);
		// Run update on ClassifyPanel to use first image
		gui.setVisible(true);
		timer.start();
	}
	
	private void setupTimer() {
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get current time
				int time = Integer.parseInt(hp.lblTimeValue.getText());
				// If time is 0
				if (time == 0) {
					// Stop the timer
					timer.stop();
					// Hide the category panel
					// Disable the panels
					dragPanel.setEnabled(false);
					dragPanel.setVisible(false);
					cp.setVisible(false);
					cp.setEnabled(false);

				}
				// Otherwise, lower the timer
				else {
					hp.setTimer(time - 1);
				}
			}
		});
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
		
		public void setTimer(int time) {
			lblTimeValue.setText("" + time);
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
	 * Mouse listener that handles highlighting categories with the Draggable Image
	 * 
	 * @author Melody
	 */
	private class CategoryDragListener extends MouseAdapter {
		
		/** Keeps track of the Category Panel that the dragger is currently hovering */
		CategoryPanel hoveredCategory;
		
		@Override
		public void mousePressed(MouseEvent e) {
			// Start in middle, so not over category
			hoveredCategory = null;
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// If we're hovering over a Category panel, process the image classification
			if (hoveredCategory != null) {
				// Set the hovered category's border back to normal
				hoveredCategory.setBorder(bdrGray);
				// Process the scoring of the image by the selected Category
				game.scoreImage(di.getClassifiedImage(), hoveredCategory.getCategory());
				// Update score
				hp.updateScore(game.getScore());
				// Load next image from Game
				DraggableImage image = buffer.nextImage();
				di.transferData(image);

				// TODO: Modified herePoint centerPoint = cp.getImageCenter();
				Point centerPoint = cp.getImageCenter();
				centerPoint.translate(0, hp.getHeight());
				di.fitImage(cp.getCurrentCategorySize(), centerPoint); // TODO: Needs to be scaled down category size
				// Clear hovered category from Dragger memory
				hoveredCategory = null;
				// Tell the Image Loader to prepare another image
				buffer.prepareImage();
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
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

}
