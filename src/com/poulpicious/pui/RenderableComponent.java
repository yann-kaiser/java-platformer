package com.poulpicious.pui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public interface RenderableComponent {
	
	public void render(Graphics g);
	public void update(Input input, int delta);

}
