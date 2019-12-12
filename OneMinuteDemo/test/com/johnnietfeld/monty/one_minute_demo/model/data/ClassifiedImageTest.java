package com.johnnietfeld.monty.one_minute_demo.model.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.BeforeClass;
import org.junit.Test;

public class ClassifiedImageTest {

	/** String of the file path of the Image used for testing */
	private static final String IMAGE_PATH_STRING = "test-files/a.jpg";
	/** File object of an image used for testing */
	private static final File IMAGE_FILE = new File(IMAGE_PATH_STRING);
	/** String used to make the Category used in testing */
	private static final String CATEGORY_STRING = "Cat";
	/** Category used for testing */
	private static final Category CATEGORY = new Category(CATEGORY_STRING);
	/** Name used for testing */
	private static final String NAME = "angry gay sounds";

	@Test
	public void test() {
		ClassifiedImage test = null;

		// Create BufferedImage for testing
		BufferedImage image = null;
		try {
			image = ImageIO.read(IMAGE_FILE);
		} catch (IOException e1) {
			fail("Could not load test image as a BufferedImage");
			e1.printStackTrace();
		}

		// Assert invalid make new image with null image argument
		try {
			test = new ClassifiedImage(null, CATEGORY, NAME);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(test);
		}

		// Assert invalid make new image with null category argument
		try {
			test = new ClassifiedImage(IMAGE_FILE, null, NAME);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(test);
		}

		// Assert invalid make new image with null name argument
		try {
			test = new ClassifiedImage(IMAGE_FILE, CATEGORY, null);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(test);
		}

		// Assert invalid make new image with blank name argument
		try {
			test = new ClassifiedImage(IMAGE_FILE, CATEGORY, "   ");
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(test);
		}

		// Assert valid make new image with valid data
		try {
			test = new ClassifiedImage(IMAGE_FILE, CATEGORY, NAME);
			assertEquals(IMAGE_FILE, test.getImageLocation());
			assertEquals(CATEGORY, test.getCategory());
			assertEquals(NAME, test.getName());

			// Test that loadImage is working seemingly correctly
			BufferedImage testImage = test.getLoadedImage();
			assertTrue(testImage instanceof BufferedImage);
			assertEquals(500, testImage.getWidth());
			assertEquals(500, testImage.getHeight());

			// Test that clearing an image is working properly
			test.flushLoadedImage();
			// Retrieve new BufferedImage, assert that they are not equal references
			BufferedImage newLoadedImage = test.getLoadedImage();
			// Assert that both BufferedImages are different references and not the same
			// object
			assertFalse(testImage == newLoadedImage);
		} catch (IllegalArgumentException e) {
			fail();
		}

		// Test scoring on an Image

		// Assert correct scoring on identical Category
		assertTrue(test.scoreCategory(CATEGORY));
		// Assert correct scoring on same category, different instance with same name
		assertTrue(test.scoreCategory(new Category(CATEGORY_STRING)));
		// Assert incorrect scoring on different category
		assertFalse(test.scoreCategory(new Category("DIFFERENT")));
	}

	@Test
	public void testToString() {
		ClassifiedImage test = new ClassifiedImage(IMAGE_FILE, CATEGORY, NAME);
		assertEquals(NAME + ": " + CATEGORY_STRING, test.toString());
	}

}
