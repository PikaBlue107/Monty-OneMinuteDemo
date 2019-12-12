package com.johnnietfeld.monty.one_minute_demo.model.manager;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

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
	/** Invalid source list of images, too short */
	private static final ArrayList<ClassifiedImage> INVALID_LIST_TOO_SHORT = new ArrayList<ClassifiedImage>();
	/** Folder location containing at least 60 test images */
	private static final String IMAGES_LOCATION = "test-files";
	/** String instructions paired with the Game for testing */
	private static final String INSTRUCTIONS = "Do the thing";
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
			ClassifiedImage image = new ClassifiedImage(image_file, SOURCE_IMAGE_LIST.size() == 0 ? one : two,
					image_file.getName());
			// Add image to the SOURCE_IMAGE_LIST
			SOURCE_IMAGE_LIST.add(image);
			// If invalid list (too short) is below 10 elements, add to that list as well
			if (INVALID_LIST_TOO_SHORT.size() < 10) {
				INVALID_LIST_TOO_SHORT.add(image);
			}

			// Add image to INVALID_LIST_FEW_CATEGORIES, all with the same category
			ClassifiedImage sameCategory = new ClassifiedImage(image_file, one, image_file.getName());
			// Add image to invalid list for few categories
			INVALID_LIST_FEW_CATEGORIES.add(sameCategory);

			// Add image to INVALID_LIST_FEW_CATEGORIES, all with the same category
			ClassifiedImage manyCategory = new ClassifiedImage(image_file, new Category(image_file.getName()),
					image_file.getName());
			// Add image to invalid list for few categories
			INVALID_LIST_MANY_CATEGORIES.add(manyCategory);
		}

	}

	@Before
	public void setUp() throws Exception {
		ImageList list = new ImageList(SOURCE_IMAGE_LIST);
		test = new Game(INSTRUCTIONS, list);
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

		// Rigorously test the first image
		// Retrieve the first image of the Game
		ClassifiedImage actualObject = test.nextImage();
		// Test varying conditions from the ClassifiedImage and its constructed
		// BufferedImage
		// Make our own BufferedImage for comparison
		try {
			BufferedImage expected = ImageIO.read(actualObject.getImageLocation());
			// Retrieve actual's real loaded image
			BufferedImage actual = actualObject.getLoadedImage();
			// Compare the two on a number of properties
			// Compare equal width
			assertEquals(expected.getWidth(), actual.getWidth());
			// Compare equal height
			assertEquals(expected.getHeight(), actual.getHeight());
			// Compare image type
			assertEquals(expected.getType(), actual.getType());
			// Compare all image pixels for equality
			// Store width and height
			int w = expected.getWidth();
			int h = expected.getHeight();
			// Retrieve the RGB arrays for expected and actual
			int[] expectedRGB = expected.getRGB(0, 0, w, h, null, 0, w);
			int[] actualRGB = actual.getRGB(0, 0, w, h, null, 0, w);
			assertArrayEquals(expectedRGB, actualRGB);
		} catch (IOException e) {
			e.printStackTrace();
			fail(" Could not create a test BufferedImage");
		}

		// Create variables to test that images are loaded quickly enough
		long startTime = 0;
		long endTime = 0;
		long elapsed = 0;
		// Create variable to hold loaded image
		ClassifiedImage loaded = null;
		// Ensure Game list is not set to cycle
		test.setCycle(false);
		System.out.println("Beginning testing of elapsed time of loading all images in test folder.");
		
		// Loop through all images in the Game
		while (test.hasNext()) {
			// Register the time before each image is loaded
			startTime = System.currentTimeMillis();
			// Load the next image in the Game
			loaded = test.nextImage();
			assertNotNull(loaded);
			// Assert that the difference in time is under a second
			// Retrieve the current time
			endTime = System.currentTimeMillis();
			// Calculate the amount of miliseconds it took to load this image
			elapsed = endTime - startTime;
			// Assert that the elapsed time is under a second
			assertTrue("Image took longer than one second to load.\nName: " + loaded.getName() + "\nElapsed time: " + elapsed / 1000 + "." + elapsed % 1000 + " seconds.", elapsed < 1000);
			System.out.println("Finished loading image " + loaded.getName());
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
		fail("Not yet implemented");
	}

	/**
	 * Tests simply that the nextStep() method is a combination of the two methods
	 * under test above.
	 */
	@Test
	public void testNextStep() {
		fail("Not yet implemented");
	}

}
