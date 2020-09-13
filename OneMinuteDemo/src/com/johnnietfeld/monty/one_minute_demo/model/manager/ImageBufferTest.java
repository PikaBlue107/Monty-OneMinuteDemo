package com.johnnietfeld.monty.one_minute_demo.model.manager;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.johnnietfeld.monty.one_minute_demo.model.data.Category;
import com.johnnietfeld.monty.one_minute_demo.model.data.ClassifiedImage;
import com.johnnietfeld.monty.one_minute_demo.model.list.ImageList;

public class ImageBufferTest {

	/** Game used for testing */
	private static Game testGame;
	/** ImageList used for testing */
	private static ImageList testList;
	/** ArrayList of Images to create new ImageLists */
	private static final ArrayList<ClassifiedImage> testImages = new ArrayList<ClassifiedImage>();
	/** File location of test images folder */
	private static final String IMAGES_FOLDER = "test-files";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Category cat = new Category("a");
		for (File imageFile : new File(IMAGES_FOLDER).listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		})) {
			testImages.add(new ClassifiedImage(imageFile.getAbsolutePath(), cat, imageFile.getName()));
		}
	}

	@Before
	public void setUp() {
		testList = new ImageList(testImages);
		testGame = new Game("a", testList, false);
	}

	@Test
	public void testImageBufferIntGame() {
		fail("Not yet implemented");
	}

	@Test
	public void testImageBufferIntGameDimensionPoint() {
		Dimension bounds = new Dimension(100, 100);
		Point center = new Point(50, 10);
		ImageBuffer test = null;
		// Assert invalid null bounds
		try {
			test = new ImageBuffer(0, testGame, null, center);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(test);
		}
		// Assert invalid null center
		try {
			test = new ImageBuffer(0, testGame, bounds, null);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(test);
		}
		
		// Assert valid arguments
		try {
			test = new ImageBuffer(0, testGame, bounds, center);
			// TODO: Pick up here
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	@Test
	public void testSetPreferredSize() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetCenter() {
		fail("Not yet implemented");
	}

	@Test
	public void testNextImage() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrepareImage() {
		fail("Not yet implemented");
	}

	@Test
	public void testSize() {
		fail("Not yet implemented");
	}

}
