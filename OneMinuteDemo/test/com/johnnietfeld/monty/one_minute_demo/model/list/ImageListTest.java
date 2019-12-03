package com.johnnietfeld.monty.one_minute_demo.model.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

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

	@BeforeClass
	public static void setupClass() {

		// Initialize the images List
		images = new ArrayList<ClassifiedImage>();
		invalidImages = new ArrayList<ClassifiedImage>();

		// Load the test files into the images list

		// Get the images folder
		File imageFolder = new File(IMAGES_LOCATION);
		assertTrue(imageFolder.isDirectory());

		// Make two Categories
		Category one = new Category("one");
		Category two = new Category("two");

		// Load each file in the images folder into the images list.
		// The first image will be category one, while all subsequent images will be
		// category two.
		for (File image : imageFolder.listFiles()) {

			// Read file from the File
			BufferedImage buff;
			try {
				buff = ImageIO.read(image);
			} catch (IOException e1) {
				throw new IllegalArgumentException();
			}

			// Get the name of the file
			String name = image.getName();

			// Make a new ClassifiedImage to be added
			ClassifiedImage classImg = new ClassifiedImage(buff, images.size() == 0 ? one : two, name);
			// Add classified image to images List
			images.add(classImg);

			// Make a second ClassifiedImage. These will all have the same Category.
			ClassifiedImage invalidImg = new ClassifiedImage(buff, one, name);
			// Add this classified image to the invalid images list
			invalidImages.add(invalidImg);

			// Increment the number of images loaded from the image folder
			++num_images;
		}
		// Image lists is now set up for testing
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
		String[] expected = new String[] { "man.jpg: two", "rest.png: two", "bot.jpg: two", "cat.jpg: two",
				"a.jpg: one" };

		// Assert correct order
		for (int i = 0; i < test.size(); i++) {
			assertEquals(expected[i], test.next().toString());
			assertEquals(4 - i, test.remaining());
		}

		// Set behavior for testing
		test.setCycle(false);

		// Assert invalid next when no elements remaining
		try {
			System.out.println(test.next());
			fail();
		} catch (IllegalStateException e) {
			assertEquals(0, test.remaining());
			assertEquals(5, test.size());
		}

		// Set cycling true for testing
		test.setCycle(true);

		// Assert correct order
		for (int i = 0; i < test.size(); i++) {
			assertEquals(expected[i], test.next().toString());
			assertEquals(4 - i, test.remaining());
		}

		// Set randomize true for testing
		test.setRandomizeCycle(true);
		
		// Set new expected array for second randomization
		expected = new String[] {
				"a.jpg: one",
				"cat.jpg: two",
				"rest.png: two",
				"bot.jpg: two",
				"man.jpg: two"
		};

		// Assert correct order
		for (int i = 0; i < test.size(); i++) {
			assertEquals(expected[i], test.next().toString());
			assertEquals(4 - i, test.remaining());
		}
	}

}
