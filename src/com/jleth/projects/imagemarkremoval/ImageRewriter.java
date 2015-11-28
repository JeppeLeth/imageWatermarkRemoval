package com.jleth.projects.imagemarkremoval;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageRewriter {
	
	private static final String IMAGE_TYPE = "png";
	
	public ImageRewriter() {}

	/**
	 * Read a file from disk, convert and store with the postfix "_new"
	 * @param imageName name or file path
	 */
	public void unMarkImage(String imageName) {
		BufferedImage inputImage = null;
		try {
			inputImage = ImageIO.read(new File(imageName));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		WaterMarkRemover remover = new WaterMarkRemover();
		remover.erase(new AwtImage(inputImage));

		try {
			int insertPoint = imageName.lastIndexOf('.');
			String postFix = "_new";
			StringBuilder b = new StringBuilder(imageName);
			if (insertPoint <= 0) {
				insertPoint = imageName.length();
				postFix += "."+IMAGE_TYPE;
			}
			b.insert(insertPoint, postFix);
			File outputFile = new File(b.toString());
			ImageIO.write(inputImage, IMAGE_TYPE, outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static class AwtImage implements ImageInterface {
		
		private BufferedImage image;

		AwtImage(BufferedImage image) {
			this.image = image;
		}

		@Override
		public int getWidth() {
			return image.getWidth();
		}

		@Override
		public int getHeight() {
			return image.getHeight();
		}

		@Override
		public int getRGB(int x, int y) {
			return image.getRGB(x, y);
		}

		@Override
		public void setRGB(int x, int y, int rgb) {
			image.setRGB(x, y, rgb);
		}
		
	}

}
