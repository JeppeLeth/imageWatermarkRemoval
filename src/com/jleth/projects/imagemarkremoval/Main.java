package com.jleth.projects.imagemarkremoval;

/**
 * Entry point for the application
 * @author JeppeLeth
 *
 */
public class Main {

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			System.out.println("Please provide at least one image filename or path");
		}
		
		ImageRewriter writer = new ImageRewriter();
		for (String filePath : args) {
			writer.unMarkImage(filePath);
		}
	}

}
