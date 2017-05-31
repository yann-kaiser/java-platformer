package com.poulpicious.pui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class PUIManager {

	// Singleton code
	private static class PUIManagerHolder {
		private static final PUIManager _instance = new PUIManager();
	}

	public static PUIManager get() {
		return PUIManagerHolder._instance;
	}

	private Input input;
	private PUI screen; // The screen represents a PUI component of the size of the screen.

	private int currentWindowLevel;

	private PUIManager() {
		currentWindowLevel = 0;
	}

	public void setup(GameContainer gc) {
		this.input = gc.getInput();
		this.screen = new PUI(0, 0, gc.getWidth(), gc.getHeight());
		this.screen.setVisible(true);
	}

	// Adds a component to the screen (which is then rendered)
	public void addView(PUI view) {
		this.screen.addComponent(view);
		view.setWindowLevel(this.currentWindowLevel);
		view.setVisible(true);
	}

	// Same as addView, it also changes the 'window that needs to be rendered'
	public void showPopup(PUIPopup popup) {
		this.currentWindowLevel++;
		this.addView(popup);
	}

	public void closePopup(PUIPopup popup) {
		this.currentWindowLevel--;
		popup.setVisible(false);
	}
	
	public boolean isShowingComponent(PUI pui) {
		boolean res = screen.hasComponent(pui);
		while (pui.parent != null) {
			res |= screen.hasComponent(pui.parent);
			pui = pui.parent;
		}
		return res;
	}

	public void render(Graphics g) {
		this.screen.render(g);
	}

	public void update(Input i, int delta) {
		for (PUI comp : screen.getComponents()) {
			comp.update(i, delta);
		}
	}

	public void clearScreen() {
		this.screen.components.clear();
	}

	public PUI getScreen() {
		return screen;
	}

	public Input getInput() {
		return input;
	}

	public int getCurrentWindowLevel() {
		return currentWindowLevel;
	}

	public void setCurrentWindowLevel(int currentWindowLevel) {
		this.currentWindowLevel = currentWindowLevel;
	}

}
