package com.johnnietfeld.monty.one_minute_demo.model.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.johnnietfeld.monty.one_minute_demo.model.data.Category;
import com.johnnietfeld.monty.one_minute_demo.model.data.ClassifiedImage;

public class ImageListTest {

	/** List of images to be used in making ImageList test objects */
	private static List<ClassifiedImage> images;
	/** List of images all with the same category */
	private static List<ClassifiedImage> invalidImages;
	/** Folder location of test images */
	private static final String IMAGES_LOCATION = "test-files";
	/** Number of images in the test folder */
	private static int num_images;
	/** Long seed for Random to use for testing */
	private static final long RAND_SEED = 1927367212973612983L;
	/** Start time for running the test */
	private static final long START_TIME = System.nanoTime();
	/** Time of the most recently completed action */
	private static long actionTime = START_TIME;

	private static void printTime(String message) {
		System.out.printf("%s\nElapsed time: %d\nTotal time: %d\n", message, System.nanoTime() - actionTime,
				System.nanoTime() - START_TIME);
		actionTime = System.nanoTime();
	}

	private static void printMemory() {
		System.out
				.println("Total memory available: " + Runtime.getRuntime().maxMemory() / (1024 * 1024) + " megabytes.");
		System.out.println("Free memory: " + Runtime.getRuntime().maxMemory() / (1024 * 1024) + " megabytes");
		System.out.println("Memory consumed: " + Runtime.getRuntime().totalMemory() / (1024 * 1024) + " megabytes.");
	}

	@BeforeClass
	public static void setupClass() {

		System.out.println("Begin setup");

		// Initialize the images List
		images = new ArrayList<ClassifiedImage>();
		invalidImages = new ArrayList<ClassifiedImage>();

		printTime("Created List objects");

		// Load the test files into the images list

		// Get the images folder
		File imageFolder = new File(IMAGES_LOCATION);
		assertTrue(imageFolder.isDirectory());

		printTime("Create folder object");

		// Make two Categories
		Category one = new Category("one");
		Category two = new Category("two");

		printTime("Make Categories");

		System.out.println("Starting looping through image folder. # Images: " + imageFolder.listFiles().length);
		printMemory();

		// Load each file in the images folder into the images list.
		// The first image will be category one, while all subsequent images will be
		// category two.
		for (File image_loc : imageFolder.listFiles()) {

			// Get the name of the file
			String name = image_loc.getName();

			// Make a new ClassifiedImage to be added
			ClassifiedImage classImg = new ClassifiedImage(image_loc.getAbsolutePath(), images.size() == 0 ? one : two, name);
			// Print the amount of memory consumed by this ClassifiedImage
			System.out.println(classImg.getClass());
			// Add classified image to images List
			images.add(classImg);

			printTime("Created first image");
			printMemory();

			// Make a second ClassifiedImage. These will all have the same Category.
			ClassifiedImage invalidImg = new ClassifiedImage(image_loc.getAbsolutePath(), one, name);
			// Add this classified image to the invalid images list
			invalidImages.add(invalidImg);

			printTime("Created second image");
			printMemory();

			// Increment the number of images loaded from the image folder
			++num_images;

			printTime("Finished loading image: " + image_loc.getName());
			printMemory();

		}
		// Image lists is now set up for testing

		printTime("Finished iterating through image folder");
	}

	@Test
	public void testImageList() {
		// Assert invalid make new ImageList with null data set
		try {
			new ImageList(null);
			fail();
		} catch (NullPointerException e) {
			// Correct exception thrown
		}

		// Assert invalid make new ImageList with empty data set
		try {
			new ImageList(new ArrayList<ClassifiedImage>());
			fail();
		} catch (IllegalArgumentException e) {
			// Correct exception thrown
		}

		// Make valid ImageList
		ImageList test = new ImageList(images);

		// Assert correct size
		assertEquals(num_images, test.size());

//		// Assert that you cannot make an ImageList from an list of images with only one shared Category
//		try {
//			new ImageList(invalidImages);
//			fail();
//		} catch (IllegalArgumentException e) {
//			// Correct exception thrown
//		}
	}

	@Test
	public void testNext() {

		// Set the random seed
		ImageList.setRandSeed(RAND_SEED);

		// Make valid ImageList
		ImageList test = new ImageList(images);

		// Assert default behavior
		assertFalse(test.doesCycle());
		assertFalse(test.doesRandomize());

		// Make expected string array
//		String[] expected = new String[] { "man.jpg: two", "rest.png: two", "bot.jpg: two", "cat.jpg: two",
//				"a.jpg: one" };

		// Assert correct order
		assertEquals(num_images, test.remaining());
		for (int i = 0; i < test.size(); i++) {
//			assertEquals(expected[i], test.next().toString());
			test.next();
			assertEquals(num_images - 1 - i, test.remaining());
		}

		// Set behavior for testing
		test.setCycle(false);

		// Assert invalid next when no elements remaining
		try {
			System.out.println(test.next());
			fail();
		} catch (IllegalStateException e) {
			assertEquals(0, test.remaining());
			assertEquals(num_images, test.size());
		}

		// Set cycling true for testing
		test.setCycle(true);

		// Assert correct order
		for (int i = 0; i < test.size(); i++) {
//			assertEquals(expected[i], test.next().toString());
			test.next();
			assertEquals(num_images - 1 - i, test.remaining());
		}

		// Set randomize true for testing
		test.setRandomizeCycle(true);

//		// Set new expected array for second randomization
//		expected = new String[] { "a.jpg: one", "cat.jpg: two", "rest.png: two", "bot.jpg: two", "man.jpg: two" };
//
		// Assert correct order
		for (int i = 0; i < test.size(); i++) {
//			assertEquals(expected[i], test.next().toString());
			test.next();
			assertEquals(num_images - 1 - i, test.remaining());
		}
	}

//	private void writeExpectedOrder() {
//		
//	}

	@Test
	public void testIterator() {

		// Set the random seed
//		ImageList.setRandSeed(RAND_SEED);

		// Make expected string array
//		String[] expected = new String[] { "man.jpg: two", "rest.png: two", "bot.jpg: two", "cat.jpg: two",
//				"a.jpg: one" };

		// Make ImageList from image set
		ImageList test = new ImageList(images);

		// Run iterator on list, pop a next element off. Repeat until remaining is 0
		// Loop through the ImageList
		for (int i = 0; i < test.size(); ++i) {

			// Start an iterator at the head of the list, assert correct number of next
			// elements
			// Get iterator
			Iterator<ClassifiedImage> it = test.iterator();
			for (int j = 0; j < num_images - i; ++j) {
				assertTrue("i = " + i + ", j = " + j, it.hasNext());
				it.next();
				//				assertEquals(expected[i+j], it.next().toString());
			}
			assertFalse(it.hasNext());

			// Pop off the top element of the ImageList
//			assertEquals(expected[i], test.next().toString());
			test.next();

		}
	}

}
