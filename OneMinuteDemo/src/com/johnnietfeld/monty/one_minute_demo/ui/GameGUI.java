package com.johnnietfeld.monty.one_minute_demo.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import com.johnnietfeld.monty.one_minute_demo.model.data.Category;
import com.johnnietfeld.monty.one_minute_demo.model.manager.Game;

public class GameGUI extends JPanel {

	/** Generated class ID for serialization */
	private static final long serialVersionUID = -4436194428355121272L;
	/** Title of the window */
	private static final String TITLE_STRING = "One Minute Game - DEMO";
	/** Frame that holds the one minute game */
	JFrame gui;
	/** Panel to hold score info and instructions */
	JPanel pnlHeader;
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
	/** Common grouping border */
	Border bdrGray;

	/** Panel to hold main game content */
	JPanel pnlMain;
	/** Panel to hold picture window */
	JPanel pnlPicture;
	/** Panel to hold categories */
	JPanel pnlCategories;

	/** Game object to hold all data about this Game */
	Game game;

	public GameGUI(Game toPlay) {
		super();

		// Check that toPlay game is not null
		if (toPlay == null) {
			throw new IllegalArgumentException("Cannot start a Game GUI with a null Game");
		}

		// Assign game to be played
		game = toPlay;

		// Get screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// Create fonts to use across GUI
		Font fntHeader = new Font("San Francisco", Font.PLAIN, 20);
		Font fntDetails = new Font("San Francisco", Font.PLAIN, 14);

		// Initialize common border
		bdrGray = BorderFactory.createEtchedBorder();

		// Set window title, fill screen and center, exit on close
		gui = new JFrame(TITLE_STRING);
		gui.setLocationRelativeTo(null);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setPreferredSize(new Dimension(screenSize.width / 2, screenSize.height / 2));
		gui.setMinimumSize(new Dimension(500, 500));

		// Setup content pane for adding content
		Container c = gui.getContentPane();
		c.setLayout(new BorderLayout());

		// Create panels for main components
		pnlMain = new JPanel();
		pnlCategories = new JPanel();

		// Create header panel
		pnlHeader = new JPanel();
		pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
		pnlHeader.setBorder(bdrGray);

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
		// Add top panel to header panel
		pnlHeader.add(pnlTopHeader);

		// Create bottom panel for instructions
		pnlInstructions = new JPanel();
//		pnlInstructions.setBorder(bdrGray);
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
		// Add instructions panel to header panel
		pnlHeader.add(pnlInstructions);

		// Add header panel to top of main frame
		c.add(pnlHeader, BorderLayout.NORTH);

		// Create main panel for game content
		pnlMain = new JPanel(new GridLayout(2, 1));
		pnlMain.setBorder(bdrGray);

		// Mid panel for displaying pictures
		pnlPicture = new JPanel();

		// TODO: Place picture in this frame, scale it to not be larger than each of the
		// Category panels

		// Add mid panel to main panel
		pnlMain.add(pnlPicture);

		// Store Categories List for constructing panel
		ArrayList<Category> categories = game.getCategories();
		System.out.println(categories);
		// Bottom panel for displaying categories
		pnlCategories = new JPanel(new GridLayout(1, categories.size()));
		// Iterate through categories, for each add a new JLabel
		for (Category category : categories) {
			// Make JPanel to show border and center JLabel
			CategoryPanel container = new CategoryPanel(category);
			container.setBorder(bdrGray);
			// Create custom JLabel that will remember its Category
			JLabel categoryLabel = new JLabel(category.getName());
			categoryLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			categoryLabel.setAlignmentY(JLabel.BOTTOM_ALIGNMENT); // TODO: Why is this aligning at top?
			categoryLabel.setFont(fntHeader);
			container.add(categoryLabel);
			pnlCategories.add(container);
		}

		// Add categories panel to main panel
		pnlMain.add(pnlCategories);

		// Add main panel to GUI
		c.add(pnlMain, BorderLayout.CENTER);

		// Finalize GUI
		gui.setVisible(true);

	}

	private class CategoryPanel extends JPanel {

		private Category saved;

		public CategoryPanel(Category category) {
			super();
			saved = category;
		}

		public Category getCategory() {
			return saved;
		}
	}

}
