package com.poulpicious.pui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class PUIButton extends PUIPanel {
	
	private PUILabel label;
	
	private Color normalColor;
	private Color hoverColor;
	private Color clickColor;
	private Color toggledColor;
	
	private boolean toggled;

	public PUIButton(String text, Font font, float x, float y) {
		super(x, y, font.getWidth(text), font.getHeight(text));
		PUIManager.get().getInput().addMouseListener(new PUIMouseListener(this));
		
		this.backgroundColor = Color.transparent;
		this.label = new PUILabel(text, font, 100, 100);
		this.addComponent(label);
		this.centerContentOnX();
		this.centerContentOnY();
		
		this.normalColor = new Color(0.9f, 0.9f, 0.9f, 1f);
		this.hoverColor = Color.white;
		this.clickColor = Color.red;
		this.toggledColor = normalColor;
		
		this.label.setTextColor(normalColor);
		
		this.addMouseActionListener(new MouseActionListener() {
			
			@Override
			public void onMouseClick(int button) {
				label.setTextColor(normalColor);
				toggled = !toggled;
				
				if (toggled)
					label.setTextColor(toggledColor);
				else
					label.setTextColor(normalColor);
			}

			@Override
			public void onMouseDown(int button) {
				label.setTextColor(clickColor);
			}
		});
		
		this.addMouseMoveListener(new MouseMoveListener() {
			
			@Override
			public void onMouseQuit() {
				if (!toggled)
					label.setTextColor(normalColor);
			}
			
			@Override
			public void onMouseHover() {
				if (!toggled)
					label.setTextColor(hoverColor);	
			}
			
			@Override
			public void onMouseEnter() {
			}
		});
	}
	
	@Override
	protected void renderSelf(Graphics g) {
	}
	
	@Override
	public void update(Input input, int delta) {
		super.update(input, delta);
		
	}

	public Color getToggledColor() {
		return toggledColor;
	}

	public void setToggledColor(Color toggledColor) {
		this.toggledColor = toggledColor;
	}

	public boolean isToggled() {
		return toggled;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}
}
