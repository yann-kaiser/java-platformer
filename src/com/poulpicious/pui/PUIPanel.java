package com.poulpicious.pui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class PUIPanel extends PUI {

	protected Color backgroundColor;

	public PUIPanel(float x, float y, float w, float h) {
		super(x, y, w, h);
		this.backgroundColor = new Color(0, 0, 0, 0.6f);
	}

	@Override
	protected final void preRender(Graphics g) {
		g.setColor(backgroundColor);
		g.fill(this.bounds);
		g.setColor(Color.white);
	}
	
	public final void setBackgroundTransparent() {
		this.backgroundColor = Color.transparent;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
