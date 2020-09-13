package com.johnnietfeld.monty.one_minute_demo.model.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.johnnietfeld.monty.one_minute_demo.model.data.Category;
import com.johnnietfeld.monty.one_minute_demo.model.data.ClassifiedImage;
import com.johnnietfeld.monty.one_minute_demo.model.list.ImageList;

public class GameTest {

	/** Source list of images to make new ImageLists and Games for testing */
	private static final ArrayList<ClassifiedImage> SOURCE_IMAGE_LIST = new ArrayList<ClassifiedImage>();
	/** Invalid source list of images, too few categories */
	private static final ArrayList<ClassifiedImage> INVALID_LIST_FEW_CATEGORIES = new ArrayList<ClassifiedImage>();
	/** Invalid source list of images, too many categories */
	private static final ArrayList<ClassifiedImage> INVALID_LIST_MANY_CATEGORIES = new ArrayList<ClassifiedImage>();
	/** Invalid source list of images, too short. 10 elements */
	private static final ArrayList<ClassifiedImage> INVALID_LIST_TOO_SHORT = new ArrayList<ClassifiedImage>();
	/** Amount of elements that will be in the INVALID_LIST_TOO_SHORT List. */
	private static final int SHORT_LIST_LENGTH = 10;
	/** Folder location containing at least 60 test images */
	private static final String IMAGES_LOCATION = "test-files";
	/** String instructions paired with the Game for testing */
	private static final String INSTRUCTIONS = "Do the thing";
	/**
	 * Default correct increment for testing correct image categorization and score
	 * adjustment
	 */
	private static final int CORRECT_INCREMENT = 1;
	/**
	 * Default incorrect decrement for testing incorrect image categorization and
	 * score adjustment
	 */
	private static final int INCORRECT_DECREMENT = -2;
	/** Game object used for testing */
	private Game test;

	@BeforeClass
	public static void setUpClass() throws Exception {
		// Add images from test folder to source images list
		// Load the file with test images
		File image_folder = new File(IMAGES_LOCATION);

		// Assert that the file is a folder and contains at least 60 images
		assertTrue("Image destination is not a directory", image_folder.isDirectory());
		assertTrue("Image folder does not contain at least 60 files", image_folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		}).length >= 60);

		// Create categories for testing
		Category one = new Category("one");
		Category two = new Category("two");

		// Iterate through files in Image folder, add to source image list
		for (File image_file : image_folder.listFiles()) {
			// Make new ClassifiedImage with data from image_file

			// Add image to SOURCE_IMAGE_LIST wiht valid categories
			// First image will be category one, second will be category two
			ClassifiedImage image = new ClassifiedImage(image_file.getAbsolutePath(),
					SOURCE_IMAGE_LIST.size() == 0 ? one : two, image_file.getName());
			// Add image to the SOURCE_IMAGE_LIST
			SOURCE_IMAGE_LIST.add(image);
			// If invalid list (too short) is below preset number of elements, add to that
			// list as well
			if (INVALID_LIST_TOO_SHORT.size() < SHORT_LIST_LENGTH) {
				INVALID_LIST_TOO_SHORT.add(image);
			}

			// Add image to INVALID_LIST_FEW_CATEGORIES, all with the same category
			ClassifiedImage sameCategory = new ClassifiedImage(image_file.getAbsolutePath(), one, image_file.getName());
			// Add image to invalid list for few categories
			INVALID_LIST_FEW_CATEGORIES.add(sameCategory);

			// Add image to INVALID_LIST_FEW_CATEGORIES, all with the same category
			ClassifiedImage manyCategory = new ClassifiedImage(image_file.getAbsolutePath(),
					new Category(image_file.getName()), image_file.getName());
			// Add image to invalid list for few categories
			INVALID_LIST_MANY_CATEGORIES.add(manyCategory);
		}

	}

	/**
	 * Creates a fresh game with default arguments before each test
	 * 
	 * @throws Exception if there's any problems creating the game
	 */
	@Before
	public void setUp() throws Exception {
		makeGame(SOURCE_IMAGE_LIST, 1, -1, true);
	}

	/**
	 * Helper function to encapsulate the two lines for making an ImageList and
	 * adding that to a new Game
	 * 
	 * @param source             ImageList that this Game will use
	 * @param correctIncrement   amount to adjust game score by upon correct image
	 *                           categorization
	 * @param incorrectDecrement amount to adjust game score by upon incorrect image
	 *                           categorization
	 * @param require60          true if this game should require at least 60 images
	 *                           in its ImageList, otherwise false
	 */
	private void makeGame(ArrayList<ClassifiedImage> source, int correctIncrement, int incorrectDecrement,
			boolean require60) {
		ImageList list = new ImageList(source);
		test = new Game(INSTRUCTIONS, list, correctIncrement, incorrectDecrement, require60);
	}

	@Test
	public void testGame() {
		// Set game reference null for testing
		test = null;
		ImageList images = null;
		// Assert invalid make Game with null instructions
		images = new ImageList(SOURCE_IMAGE_LIST);
		try {
			test = new Game(null, images, false);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(test);
		}

		// Assert invalid make Game with blank instructions
		images = new ImageList(SOURCE_IMAGE_LIST);
		try {
			test = new Game("   ", images, false);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(test);
		}

		// Assert invalid make Game with null ImageList
		try {
			test = new Game(INSTRUCTIONS, null, false);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(test);
		}

		// Assert invalid make Game with too few categories
		images = new ImageList(INVALID_LIST_FEW_CATEGORIES);
		try {
			test = new Game(INSTRUCTIONS, images, false);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(test);
		}

		// Assert invalid make Game with too many categories
		images = new ImageList(INVALID_LIST_MANY_CATEGORIES);
		try {
			test = new Game(INSTRUCTIONS, images, false);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(test);
		}

		// Make valid Game
		images = new ImageList(SOURCE_IMAGE_LIST);
		try {
			test = new Game(INSTRUCTIONS, images, false);
			assertEquals(INSTRUCTIONS, test.getInstructions());
			assertFalse(test.getRequire60());
			assertEquals(0, test.getScore());
			assertEquals(SOURCE_IMAGE_LIST.size(), test.available());
			assertEquals(SOURCE_IMAGE_LIST.size(), test.remaining());
		} catch (IllegalArgumentException e) {
			fail();
		}

		// Make valid Game with require60 false and less than 60 images
		images = new ImageList(INVALID_LIST_TOO_SHORT);
		try {
			test = new Game(INSTRUCTIONS, images, false);
			assertEquals(INSTRUCTIONS, test.getInstructions());
			assertFalse(test.getRequire60());
			assertEquals(0, test.getScore());
			assertEquals(SHORT_LIST_LENGTH, test.available());
			assertEquals(SHORT_LIST_LENGTH, test.remaining());
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	@Test
	public void testGameRequire60() {
		// Set Game reference null for testing, setup ImageList reference
		test = null;
		ImageList images;

		// Assert invalid make Game with require60 true and less than 60 images
		images = new ImageList(INVALID_LIST_TOO_SHORT);
		try {
			test = new Game(INSTRUCTIONS, images);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(test);
		}
	}

	/**
	 * Tests the functionality of loading the next image to be presented to the
	 * user. Must assert that each image can be loaded within 1 second.
	 */
	@Test
	public void testNextImage() {

		/*
		 * This method takes a long amount of time to run. It should not be run with
		 * each test suite. If this test needs to be run, the following field should be
		 * set to 'true'.
		 */
		boolean runTest = false;
		if (!runTest)
			return;

		// Assert beginning available and remaining images
		assertEquals(SOURCE_IMAGE_LIST.size(), test.available());
		assertEquals(SOURCE_IMAGE_LIST.size(), test.remaining());

		// Rigorously test the first image
		// Retrieve the first image of the Game
		ClassifiedImage actualObject = test.nextImage();
		// Assert that remaining has gone down by one, available same as always
		assertEquals(SOURCE_IMAGE_LIST.size() - 1, test.remaining());
		assertEquals(SOURCE_IMAGE_LIST.size(), test.available());
		// Test varying conditions from the ClassifiedImage and its constructed
		// BufferedImage
		// Make our own BufferedImage for comparison
		ImageIcon expected = new ImageIcon(actualObject.getImageLocation());
		// Retrieve actual's real loaded image
		ImageIcon actual = actualObject.getLoadedImage();
		// Compare the two on a number of properties
		// Compare equal width
		assertEquals(expected.getIconWidth(), actual.getIconWidth());
		// Compare equal height
		assertEquals(expected.getIconHeight(), actual.getIconHeight());
		// Compare image type
//		assertEquals(expected.getType(), actual.getType());
//		// Compare all image pixels for equality
//		// Store width and height
//		int w = expected.getWidth();
//		int h = expected.getHeight();
//		// Retrieve the RGB arrays for expected and actual
//		int[] expectedRGB = expected.getRGB(0, 0, w, h, null, 0, w);
//		int[] actualRGB = actual.getRGB(0, 0, w, h, null, 0, w);
//		// Assert they are equal
//		assertArrayEquals(expectedRGB, actualRGB);

		// Create variables to test that images are loaded quickly enough
		long startTime = 0;
		long endTime = 0;
		long elapsed = 0;
		// Create variable to hold loaded image
		ClassifiedImage loaded = null;
		// Create variable to hold number of seen images
		int seenImages = 1;
		// Ensure Game list is not set to cycle
		test.setCycle(false);
		System.out.println("Beginning testing of elapsed time of loading all images in test folder.");

		// Loop through all images in the Game
		while (test.hasNext()) {
			// Register the time before each image is loaded
			startTime = System.currentTimeMillis();
			// Load the next image in the Game
			loaded = test.nextImage();
			// Assert that remaining number of images has gone down by one
			assertEquals(SOURCE_IMAGE_LIST.size() - ++seenImages, test.remaining());
			// Assert that the difference in time is under a second
			// Retrieve the current time
			endTime = System.currentTimeMillis();
			// Calculate the amount of milliseconds it took to load this image
			elapsed = endTime - startTime;
			// Assert that the elapsed time is under a second
			assertTrue("Image took longer than one second to load.\nName: " + loaded.getName() + "\nElapsed time: "
					+ elapsed / 1000 + "." + elapsed % 1000 + " seconds.", elapsed < 1000);
			System.out.println("Finished loading image " + seenImages + ": " + loaded.getName());
			System.out.printf("Elapsed time: %d.%d seconds.\n", elapsed / 1000, elapsed % 1000);
			// Flush loaded image for memory's sake
			loaded.flushLoadedImage();
		}

		// The first image is exactly as it should be, and each after that takes less
		// than a second to load. Test complete.
	}

	/**
	 * Tests the functionality of scoring an image by the Game.
	 */
	@Test
	public void testScoreImage() {

		// Make game with planned score modifiers
		makeGame(SOURCE_IMAGE_LIST, CORRECT_INCREMENT, INCORRECT_DECREMENT, true);
		// Assert that score starts at 0
		assertEquals(0, test.getScore());

		// Test that correct categorization has the correct effect
		// Retrieve the first image
		ClassifiedImage first = test.nextImage();
		// Score it correctly
		assertEquals(CORRECT_INCREMENT, test.scoreImage(first, first.getCategory()));
		assertEquals(CORRECT_INCREMENT, test.getScore());

		// Test that incorrect categorization has the desired effect
		// Retrieve the second image
		ClassifiedImage second = test.nextImage();
		// Score it correctly and assert score has changed as a result
		assertEquals(CORRECT_INCREMENT + INCORRECT_DECREMENT, test.scoreImage(second, new Category("Different")));
		assertEquals(CORRECT_INCREMENT + INCORRECT_DECREMENT, test.getScore());
	}

	/**
	 * Tests simply that the nextStep() method is a combination of the two methods
	 * under test above.
	 */
	@Test
	public void testNextStep() {

		/*
		 * This method takes a long amount of time to run. It should not be run with
		 * each test suite. If this test needs to be run, the following field should be
		 * set to 'true'.
		 */
		boolean runTest = false;
		if (!runTest)
			return;

		System.out.println("Testing nextStep() on all images in test folder");

		// Make game with planned score modifiers
		makeGame(SOURCE_IMAGE_LIST, CORRECT_INCREMENT, INCORRECT_DECREMENT, true);
		// Assert that score start at 0
		assertEquals(0, test.getScore());

		// Make Random object for random guessing
		Random random = new Random();
		// Make score counter for keeping track of expected score
		int expectedScore = 0;
		// Make incorrect Category for all incorrect guesses
		Category badCat = new Category("Different");

		// Retrieve first image
		ClassifiedImage next = test.nextImage();
		// Create references for looping
		boolean guessCorrect;
		Category guess;

		// While the test Game has more images
		while (test.hasNext()) {
			// Randomly determine by 50% chance whether this guess will be correct or not.
			guessCorrect = random.nextBoolean();
			// Adjust expected score by whether our guess will be correct or not
			expectedScore += guessCorrect ? CORRECT_INCREMENT : INCORRECT_DECREMENT;
			// Save the correct or incorrect Category guess
			guess = guessCorrect ? next.getCategory() : badCat;
			// Print current test status to console
			System.out.printf("Testing image %s.\nGuess: %s\nExpected score: %d\n", next.getName(), guess.getName(),
					expectedScore);
			// Score the image and retrieve the next
			next = test.nextStep(next, guess);
			System.out.println("Actual score: " + test.getScore());
			// Assert that score has changed expectedly
			assertEquals(expectedScore, test.getScore());
		}

		// Last image is not scored but that's fine
	}

	@Test
	public void testCycling() {
		// Make new game from short list of images
		makeGame(INVALID_LIST_TOO_SHORT, CORRECT_INCREMENT, INCORRECT_DECREMENT, false);
		// Set cycling false, randomizing true
		test.setCycle(false);
		assertFalse(test.doesCycle());
		test.setRandomizeCycle(false);
		assertFalse(test.doesRandomize());

		ArrayList<ClassifiedImage> order = new ArrayList<ClassifiedImage>();

		for (int i = 0; i < SHORT_LIST_LENGTH; ++i) {
			// Append the next image to the end of the order arrayList
			order.add(test.nextImage());
			// We don't need the loaded images for this test
			order.get(i).flushLoadedImage();
		}

		// Assert that the Game does not offer more images
		assertFalse(test.hasNext());

		// Set cycling true, assert changed
		test.setCycle(true);
		assertTrue(test.doesCycle());

		// Assert that the Game now does offer more images
		assertTrue(test.hasNext());

		// Assert that the order of images is the same without randomization
		for (int i = 0; i < SHORT_LIST_LENGTH; ++i) {
			assertTrue(order.get(i) == test.nextImage());
			order.get(i).flushLoadedImage();
		}

		// Set randomization true, assert changed
		test.setRandomizeCycle(true);
		assertTrue(test.doesRandomize());

		// Assert that the order has changed
		boolean orderChanged = false;
		for (int i = 0; i < SHORT_LIST_LENGTH; ++i) {
			// Retrieve next image from Game
			ClassifiedImage nextImage = test.nextImage();
			// Clear memory
			order.get(i).flushLoadedImage();
			// If this image is different than the image in this spot previously
			if (order.get(i) != nextImage) {
				// Save that the order has changed and escape from the loop
				orderChanged = true;
				break;
			}
		}

		// Assert that we did find an image that was in the different place as before.
		assertTrue(orderChanged);
		/*
		 * The only situation where this will be correctly false is if the randomization
		 * yields the exact same order as before, a probability of one over
		 * SHORT_LIST_LENGTH factorial.
		 * 
		 * With the current default of 10, that's one in 3,628,800.
		 */
	}

}
