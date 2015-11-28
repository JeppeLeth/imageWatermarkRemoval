package com.jleth.projects.imagemarkremoval;

/**
 * Unused for now. Maybe use http://colorizer.org/ to find a better algorithm for color correction
 * @author JeppeLeth
 *
 */
@Deprecated 
class ColorUtil {
	public static int argb(int R, int G, int B) {
		return argb(Byte.MAX_VALUE, R, G, B);
	}

	public static int argb(int A, int R, int G, int B) {
		byte[] colorByteArr = { (byte) A, (byte) R, (byte) G, (byte) B };
		return byteArrToInt(colorByteArr);
	}

	public static int[] rgb(int argb) {
		return new int[] { (argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF };
	}

	public static int byteArrToInt(byte[] colorByteArr) {
		return (colorByteArr[0] << 24) + ((colorByteArr[1] & 0xFF) << 16) + ((colorByteArr[2] & 0xFF) << 8) + (colorByteArr[3] & 0xFF);
	}

	public static int[] rgb2lab(int R, int G, int B) {
		// http://www.brucelindbloom.com

		float r, g, b, X, Y, Z, fx, fy, fz, xr, yr, zr;
		float Ls, as, bs;
		float eps = 216.f / 24389.f;
		float k = 24389.f / 27.f;

		float Xr = 0.964221f; // reference white D50
		float Yr = 1.0f;
		float Zr = 0.825211f;

		// RGB to XYZ
		r = R / 255.f; // R 0..1
		g = G / 255.f; // G 0..1
		b = B / 255.f; // B 0..1

		// assuming sRGB (D65)
		if (r <= 0.04045)
			r = r / 12;
		else
			r = (float) Math.pow((r + 0.055) / 1.055, 2.4);

		if (g <= 0.04045)
			g = g / 12;
		else
			g = (float) Math.pow((g + 0.055) / 1.055, 2.4);

		if (b <= 0.04045)
			b = b / 12;
		else
			b = (float) Math.pow((b + 0.055) / 1.055, 2.4);

		X = 0.436052025f * r + 0.385081593f * g + 0.143087414f * b;
		Y = 0.222491598f * r + 0.71688606f * g + 0.060621486f * b;
		Z = 0.013929122f * r + 0.097097002f * g + 0.71418547f * b;

		// XYZ to Lab
		xr = X / Xr;
		yr = Y / Yr;
		zr = Z / Zr;

		if (xr > eps)
			fx = (float) Math.pow(xr, 1 / 3.);
		else
			fx = (float) ((k * xr + 16.) / 116.);

		if (yr > eps)
			fy = (float) Math.pow(yr, 1 / 3.);
		else
			fy = (float) ((k * yr + 16.) / 116.);

		if (zr > eps)
			fz = (float) Math.pow(zr, 1 / 3.);
		else
			fz = (float) ((k * zr + 16.) / 116);

		Ls = (116 * fy) - 16;
		as = 500 * (fx - fy);
		bs = 200 * (fy - fz);

		int[] lab = new int[3];
		lab[0] = (int) (2.55 * Ls + .5);
		lab[1] = (int) (as + .5);
		lab[2] = (int) (bs + .5);
		return lab;
	}

	/**
	 * Computes the difference between two RGB colors by converting them to the
	 * L*a*b scale and comparing them using the CIE76 algorithm {
	 * http://en.wikipedia.org/wiki/Color_difference#CIE76}
	 */
	public static double getColorDifference(int a, int b) {
		int r1, g1, b1, r2, g2, b2;
		r1 = (a >> 16) & 0xFF;
		g1 = (a >> 8) & 0xFF;
		b1 = a & 0xFF;
		r2 = (b >> 16) & 0xFF;
		g2 = (b >> 8) & 0xFF;
		b2 = b & 0xFF;
		int[] lab1 = rgb2lab(r1, g1, b1);
		int[] lab2 = rgb2lab(r2, g2, b2);
		return Math.sqrt(Math.pow(lab2[0] - lab1[0], 2) + Math.pow(lab2[1] - lab1[1], 2) + Math.pow(lab2[2] - lab1[2], 2));
	}
}