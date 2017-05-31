package com.poulpicious.pui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.poulpicious.util.Fonts;

public class PUIPopup extends PUIPanel {
	
	private PUILabel titleLabel;

	public PUIPopup(String title, float x, float y, float w, float h, boolean hasCloseButton) {
		super(x, y, w, h);
		
		this.titleLabel = new PUILabel(title, Fonts.splashFont, x + 10, y + 10);
		this.addComponent(titleLabel);
		
		this.backgroundColor = Color.black;
		this.visible = false;
		
		if (hasCloseButton) {
			PUIButton closeButton = new PUIButton("Close", Fonts.mediumFont, 20, 20);
			
			closeButton.addMouseActionListener(new MouseActionListener() {
				@Override
				public void onMouseClick(int button) {
					PUIManager.get().closePopup(PUIPopup.this);
				}

				@Override
				public void onMouseDown(int button) {
					
				}
			});
			
			this.addComponent(closeButton);
			this.snap(closeButton, BOTTOM, 0, 20);
			this.centerComponentOnX(closeButton);
		}
	}
	
	@Override
	protected void renderSelf(Graphics g) {
		super.renderSelf(g);
	}
	
}
