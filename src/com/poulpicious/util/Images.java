package com.poulpicious.util;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Images {

	private static final String IMAGE_PATH = "./res/images/";

	public static Image loadImage(String name) {
		Image image;
		try {
			image = new Image(IMAGE_PATH + name.replace(".", "/") + ".png");

			image.setFilter(Image.FILTER_NEAREST);
			return image;
		} catch (SlickException e) {
			return null;
		}
	}

}
