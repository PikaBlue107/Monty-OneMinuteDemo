package com.johnnietfeld.monty.one_minute_demo.model.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class CategoryTest {

	/** Category name used for testing */
	private static final String NAME = "Hot";
	
	
	@Test
	public void testCategory() {
		Category test = null;
		
		// Assert invalid make category with null name
		try {
			test = new Category(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(test);
		}
		
		// Assert invalid make category with whitepsace name
		try {
			test = new Category("   ");
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(test);
		}
		
		// Assert valid category construction
		try {
			test = new Category(NAME);
			assertEquals(NAME, test.getName());
		} catch (IllegalArgumentException e) {
			fail();
		}
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEquals() {
		
		// Make a new Category
		Category c1 = new Category("one");
		// Assert self-comparison true
		assertTrue(c1.equals(c1));
		
		// Make a new, identical Category
		Category c2 = new Category("one");
		// Assert identical comparison true
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));
		
		// Make a null Category
		Category n = null;
		// Assert different to null
		assertFalse(c1.equals(n));
		
		// Make a non-Category object
		String notCategory = "not a category";
		// Assert different to non-Category
		assertFalse(c1.equals(notCategory));
		
		// Make a different Category object
		Category c3 = new Category("three");
		// Assert different to different Category object
		assertFalse(c1.equals(c3));
		assertFalse(c3.equals(c1));
		
	}
	
	@Test
	public void tesHashCode() {
		
		// Make a new Category
		Category c1 = new Category("one");
		// Assert self-comparison true
		assertEquals(c1.hashCode(), c1.hashCode());
		
		// Make a new, identical Category
		Category c2 = new Category("one");
		// Assert identical comparison true
		assertEquals(c1.hashCode(), c2.hashCode());
		
		// Make a different Category object
		Category c3 = new Category("three");
		// Assert different to different Category object
		assertNotEquals(c1.hashCode(), c3.hashCode());
		
	}
	
	@Test
	public void testToString() {
		Category c1 = new Category("test");
		assertEquals("test", c1.toString());
	}
}
