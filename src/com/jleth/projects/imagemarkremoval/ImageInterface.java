package com.jleth.projects.imagemarkremoval;

public interface ImageInterface {

	int getWidth();
	int getHeight();
	int getRGB(int x, int y);
	void setRGB(int x, int y, int rgb);

}
