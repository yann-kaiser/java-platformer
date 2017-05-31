package com.poulpicious.pui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class PUIImage extends PUI {

	protected Image image;

	public PUIImage(Image image, float x, float y, float w, float h) {
		super(x, y, w, h);
		this.image = image;
	}

	@Override
	protected void renderSelf(Graphics g) {
		image.draw(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
	}
}
