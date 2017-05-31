package com.poulpicious.pui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class PUIProgressBar extends PUIPanel {

	private PUIPanel foreground;

	private float value;
	private float maxValue;
	private float incrementFactor;

	public PUIProgressBar(Color background, Color foreground, float x, float y, float w, float h) {
		this(background, foreground, 0, 100, 1, x, y, w, h);
	}

	public PUIProgressBar(Color background, Color foreground, float value, float maxValue, float incrementFactor, float x, float y,
			float w, float h) {
		super(x, y, w, h);
		this.backgroundColor = background;

		this.foreground = new PUIPanel(0, 0, 0, h);
		this.foreground.setBackgroundColor(foreground);

		this.maxValue = maxValue;
		this.value = value;
		this.incrementFactor = incrementFactor;
		
		this.addComponent(this.foreground);
	}

	@Override
	protected void renderSelf(Graphics g) {
		super.renderSelf(g);
	}

	@Override
	public void update(Input input, int delta) {
		super.update(input, delta);

		this.foreground.getBounds().setWidth(value / maxValue * this.getBounds().getWidth());
	}

	public void increment() {
		this.value += incrementFactor;
		
		if (this.value > this.maxValue)
			this.value = this.maxValue;
	}

	public void decrement() {
		this.value -= incrementFactor;
		
		if (this.value < 0)
			this.value = 0;
	}

	public void setProgress(float progress) {
		this.value = progress * maxValue;
	}
	
	public float getProgress() {
		return this.value;
	}
	
	public float getMaxValue() {
		return this.maxValue;
	}
}
