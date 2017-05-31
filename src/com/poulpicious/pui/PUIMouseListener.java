package com.poulpicious.pui;

import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;

public class PUIMouseListener implements MouseListener {
	private Input input;
	private PUI pui;

	public PUIMouseListener(PUI pui) {
		this.pui = pui;
	}

	@Override
	public void setInput(Input i) {
		this.input = i;
	}

	@Override
	public boolean isAcceptingInput() {
		return pui.getWindowLevel() == PUIManager.get().getCurrentWindowLevel() && PUIManager.get().isShowingComponent(pui);
	}

	@Override
	public void inputStarted() {
	}

	@Override
	public void inputEnded() {
	}

	@Override
	public void mouseWheelMoved(int amount) {
		if (pui.hasFocus)
			callOnMouseWheel(amount);
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		if (!pui.visible)
			return;
		if (checkMouse(x, y)) {
			pui.hasFocus = true;
			callOnMouseClick(button);
		} else {
			pui.hasFocus = false;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (!pui.visible)
			return;
		if (checkMouse(x, y)) {
			callOnMouseDown(button);
		}
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if (!pui.visible)
			return;
		if (checkMouse(oldx, oldy)) {
			if (checkMouse(newx, newy)) {
				callOnMouseHover();
			} else {
				callOnMouseQuit();
			}
		} else {
			if (checkMouse(newx, newy)) {
				callOnMouseEnter();
			}
		}
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {

	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
	}

	private final boolean checkMouse(int posx, int posy) {
		return (pui.contains(posx, posy) && pui.getWindowLevel() == PUIManager.get().getCurrentWindowLevel());
	}

	private void callOnMouseEnter() {
		for (MouseMoveListener mml : pui.mouseMoveListeners)
			mml.onMouseEnter();
	}

	private void callOnMouseHover() {
		for (MouseMoveListener mml : pui.mouseMoveListeners)
			mml.onMouseHover();
	}

	private void callOnMouseQuit() {
		for (MouseMoveListener mml : pui.mouseMoveListeners)
			mml.onMouseQuit();
	}

	private void callOnMouseClick(int button) {
		for (MouseActionListener mal : pui.mouseActionListeners)
			mal.onMouseClick(button);
	}

	private void callOnMouseDown(int button) {
		for (MouseActionListener mal : pui.mouseActionListeners) {
			mal.onMouseDown(button);
		}
	}

	private void callOnMouseWheel(int amount) {
		for (MouseWheelListener mwl : pui.mouseWheelListeners)
			mwl.onMouseWheel(amount);
	}

	public Input getInput() {
		return input;
	}
}
