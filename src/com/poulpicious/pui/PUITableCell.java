package com.poulpicious.pui;

import org.newdawn.slick.Graphics;

import com.poulpicious.util.Fonts;

public class PUITableCell extends PUIPanel {
	
	private PUILabel label;

	public PUITableCell(String content, float x, float y, float w, float h) {
		super(x, y, w, h);
		
		this.label = new PUILabel(content, Fonts.mediumFont, x + 20, 20);
		
		this.addComponent(this.label);
		this.centerContentOnY();
		this.setVisible(true);
	}
	
	@Override
	protected void renderSelf(Graphics g) {
		label.getBounds().setX(this.getBounds().getX() + 20);
	}
	
}
