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
	private static final String IMAGE_PATH_STRING = "A.jpg";
	/** File path to the Image used for testing */
	private static final File IMAGE_PATH = new File(IMAGE_PATH_STRING);
	/** BufferedImage object used for testing */
	private static BufferedImage IMAGE;
	/** String used to make the Category used in testing */
	private static final String CATEGORY_STRING = "Cat";
	/** Category used for testing */
	private static final Category CATEGORY = new Category(CATEGORY_STRING);

	@BeforeClass
	public static void setupBefore() {
		try {
			IMAGE = ImageIO.read(IMAGE_PATH);
		} catch (IOException e) {
			fail("Could not load test image");
			e.printStackTrace();
		}
	}

	@Test
	public void test() {
		ClassifiedImage test = null;
		
		// Assert invalid make new image with null image argument
		try {
			test = new ClassifiedImage(null, CATEGORY);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(test);
		}
		
		// Assert invalid make new image with null category argument
		try {
			test = new ClassifiedImage(IMAGE, null);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(test);
		}
		
		// Assert valid make new image with valid data
		try {
			test = new ClassifiedImage(IMAGE, CATEGORY);
			assertEquals(IMAGE, test.getImage());
			assertEquals(CATEGORY, test.getCategory());
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

}
