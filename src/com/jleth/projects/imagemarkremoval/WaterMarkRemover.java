package com.jleth.projects.imagemarkremoval;

import java.awt.Color;
import java.util.Arrays;

/**
 * Best try removal of a diagonal pattern from images found in Iconfinder.com
 * As for not only image sized 512x512, 256x256 and 128x128 are supported
 * @author JeppeLeth
 *
 */
public class WaterMarkRemover {
	
	private static final Color TRANSPARENT = new Color(0f, 0f, 0f, 0f);

	public WaterMarkRemover() {
	}

	/**
	 * Erase the diagonal strokes from an image source.
	 * 
	 * @param inputImage
	 *            the image that should be corrected
	 */
	public void erase(ImageInterface inputImage) {

		int width = inputImage.getWidth();
		int height = inputImage.getHeight();

		if (!isSupportedResolution(width, height)) {
			throw new IllegalArgumentException("Resolution %d x %d not supported. Must be 128x128, 256x256 or 512x512");
		}

		int[][] nodes = getNodes(height, width);

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (nodes[j][i] == 0) {
					// continue; // Actually we dont need to convert these
					// nodes, but we do it anyway to smoothen the color
					// difference in the full image
				}

				int rgba = inputImage.getRGB(i, j);
				Color col = new Color(rgba, true);
				if (isTransparent(col)) {
					col = TRANSPARENT;
				} else {
					// if (i > 1 and nodes[index - 1] == 0) or (j < im.size[1] -
					// 1 and nodes[index + 1] == 0):
					if ((i > 1 && nodes[j][i - 1] == 0) || (i < width - 1 && nodes[j][i + 1] == 0)) {
						// System.out.println("1");
						col = colorAdjust(col, 0.015f);
						// elif (i > 2 and nodes[index - 2] == 0) or (j <
						// im.size[1] - 2 and nodes[index + 2] == 0):
					} else if ((i > 2 && nodes[j][i - 2] == 0) || (i < width - 2 && nodes[j][i + 2] == 0)) {
						// System.out.println("2");
						col = colorAdjust(col, 0.065f);
					} else {
						// System.out.println("3");
						col = colorAdjust(col, 0.073f);
					}
					// col = new Color(255 - col.getRed(),
					// 255 - col.getGreen(),
					// 255 - col.getBlue());
				}
				inputImage.setRGB(i, j, col.getRGB());
			}
		}

	}

	private static boolean isSupportedResolution(int width, int height) {
		return width == height && (width == 512 || width == 256 || width == 128);
	}

	private static int[][] getNodes(int height, int width) {
		if (height == width) {
			switch (height) {
			case 512:
			case 256:
			case 128:
				return getNodes(height, width, 13, 7, 3);
			case 48:
				return getNodes(height, width, 5, 5, 2);
			}
		}
		throw new IllegalArgumentException("Height and width not supported (" + width + "x" + height + ")");
	}

	/**
	 * Get an array of nodes that declared the pixels in the image that needs adjustment
	 * @param height height must match width
	 * @param width width must match height
	 * @param interval number of pixels between each adjustment section
	 * @param datalen the width of a diagonal stroke in pixels
	 * @param start starting offset for where the pattern begins
	 * @return nodes with 0 = no correction and 1 = correction needed
	 */
	private static int[][] getNodes(int height, int width, int interval, int datalen, int start) {
		// first line: 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1,
		// 1, 1, 1, 1, 0 ...
		int packlen = interval + datalen;
		int[] rawLine = new int[packlen * (width / packlen + 2)];
		for (int i = interval; i < rawLine.length; i = i + packlen) {
			Arrays.fill(rawLine, i, i + datalen, 1);
		}
		int[][] nodes = new int[width][height];
		start = packlen - start;
		for (int i = 0; i < nodes.length; i++) {
			if (start > packlen) {
				start = (start % packlen);
			}
			nodes[i] = Arrays.copyOfRange(rawLine, start, start + nodes[i].length);
			start++;
		}

		return nodes;
	}

	/**
	 * Adjust the RGB color spectrum by a predefined mask.
	 * @param col color to be adjusted
	 * @param i level of adjustment in percent
	 * @return new color
	 */
	private static Color colorAdjust(Color col, float i) {
		// return col;
		try {
			return abcd(
					adjustComponent(col.getRed(), -68, i),
					adjustComponent(col.getGreen(), -68, i),
					adjustComponent(col.getBlue(), -64, i),
					col.getAlpha());
		} catch (Exception d) {
			d.printStackTrace();
			System.out.println(String.format("(%d, %d, %d, %d)", (int) Math.max((col.getRed() - 68 * i) / (1 - i), 0), (int) Math.max((col.getGreen() - 68 * i) / (1 - i), 0),
					(int) Math.max((col.getBlue() - 64 * i) / (1 - i), 0),
					col.getAlpha()));
			return Color.WHITE;
		}
	}
	
	/**
	 * Adjust a single color component (red, green or blue) a get
	 * @param comp
	 * @return
	 */
	private static int adjustComponent(int comp, int level, float percent) {
		return Math.min((int) Math.max((comp + level * percent) / (1 - percent), 0), 255);
	}

	private static Color abcd(int r, int g, int b, int a) {
		// System.out.println(String.format("(%d, %d, %d, %d)", r,g,b,a));
		return new Color(r, g, b, a);
	}

	private static boolean isTransparent(Color col) {
		if (col.getAlpha() != 255 && (66 < col.getRed() && col.getRed() < 70) && (66 < col.getGreen() && col.getGreen() < 70) && (62 < col.getBlue() && col.getBlue() < 66)) {
			return true;
		}
		return false;
	}

}
