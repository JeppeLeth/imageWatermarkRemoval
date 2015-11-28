package com.jleth.projects.imagemarkremoval;


import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class ImageRewriterTest {
	
	@Test
	public void testRedCrossImage() {
		File f = new File("examples/exampleImage1-512_new.png");
		f.delete();
		ImageRewriter writer = new ImageRewriter();
		writer.unMarkImage("examples/exampleImage1-512.png");
		Assert.assertTrue(f.exists() && !f.isDirectory());
	}
	
	@Test
	public void testRobotImage() {
		File f = new File("examples/exampleImage2-512_new.png");
		f.delete();
		ImageRewriter writer = new ImageRewriter();
		writer.unMarkImage("examples/exampleImage2-512.png");
		Assert.assertTrue(f.exists() && !f.isDirectory());
	}
	
}
