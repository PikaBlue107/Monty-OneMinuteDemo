package com.johnnietfeld.monty.one_minute_demo.model.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileFilter;
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
			ClassifiedImage image = new ClassifiedImage(ImageIO.read(image_file),
					SOURCE_IMAGE_LIST.size() == 0 ? one : two, image_file.getName());
			// Add image to the SOURCE_IMAGE_LIST
			SOURCE_IMAGE_LIST.add(image);
			// If invalid list (too short) is below 10 elements, add to that list as well
			INVALID_LIST_TOO_SHORT.add(image);

			// Add image to INVALID_LIST_FEW_CATEGORIES, all with the same category
			ClassifiedImage sameCategory = new ClassifiedImage(ImageIO.read(image_file), one, image_file.getName());
			// Add image to invalid list for few categories
			INVALID_LIST_FEW_CATEGORIES.add(sameCategory);

			// Add image to INVALID_LIST_FEW_CATEGORIES, all with the same category
			ClassifiedImage manyCategory = new ClassifiedImage(ImageIO.read(image_file),
					new Category(image_file.getName()), image_file.getName());
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

	@Test
	public void testScoreImage() {
		fail("Not yet implemented");
	}

	@Test
	public void testNextImage() {
		fail("Not yet implemented");
	}

	@Test
	public void testNextStep() {
		fail("Not yet implemented");
	}

}
