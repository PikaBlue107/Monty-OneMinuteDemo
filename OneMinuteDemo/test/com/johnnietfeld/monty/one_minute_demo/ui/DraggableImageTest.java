package com.johnnietfeld.monty.one_minute_demo.ui;

import static org.junit.Assert.assertFalse;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import org.junit.Test;

import com.johnnietfeld.monty.one_minute_demo.model.data.Category;
import com.johnnietfeld.monty.one_minute_demo.model.data.ClassifiedImage;

public class DraggableImageTest {

	@Test
	public void test() {
		
		JFrame frame = new JFrame("DraggableImageTest");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(new OverlayLayout(frame.getContentPane()));
		
		// Make ClassifiedImage to hold the source image
		ClassifiedImage ciTest = new ClassifiedImage("test-files/a.jpg", new Category("cat"), "a");
		
		// Create the DraggableImage for testing
		DraggableImage image = new DraggableImage(ciTest);
		image.setBorder(BorderFactory.createLineBorder(Color.red));
		
		JPanel dragPanel = new JPanel(null);
		dragPanel.setOpaque(false);
		
		dragPanel.add(image);
		JLabel test = new JLabel("Test");
		dragPanel.add(test);
		test.setLocation(0, 0);
		System.out.println(test.isVisible());
		dragPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		
		frame.getContentPane().add(dragPanel);
//		frame.getContentPane().add(new JLabel("Test"));
//		frame.getContentPane().add(image);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		System.out.println(image.getBounds());
		
		while(frame.isShowing()) {
			Thread.onSpinWait();
		};
		
		assertFalse(frame.isShowing());
	}

}
