package com.poulpicious.pui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

public class PUILabel extends PUIPanel {

	private String text;
	private Font font;
	private Color textColor;
	
	public PUILabel(String text, Font font, float x, float y) {
		super(x, y, font.getWidth(text), font.getHeight(text));
		this.text = text;
		this.font = font;
		this.textColor = Color.white;
		this.setBackgroundTransparent();
		
	}
	
	@Override
	protected void renderSelf(Graphics g) {
		font.drawString(this.bounds.getX(), this.bounds.getY(), text, textColor);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

}
