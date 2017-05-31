package com.poulpicious.pui;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

public class PUI implements RenderableComponent {

	// These fields represent the corners of the PUI component.
	public static final int TOP = 0x1;
	public static final int BOTTOM = 0x2;
	public static final int LEFT = 0x4;
	public static final int RIGHT = 0x8;

	public static final int TOP_LEFT = TOP + LEFT;
	public static final int TOP_RIGHT = TOP + RIGHT;
	public static final int BOTTOM_LEFT = BOTTOM + LEFT;
	public static final int BOTTOM_RIGHT = BOTTOM + RIGHT;

	protected Rectangle bounds;

	protected ArrayList<PUI> components;
	protected PUI parent;

	protected boolean visible;
	protected boolean hasFocus;

	protected ArrayList<MouseMoveListener> mouseMoveListeners;
	protected ArrayList<MouseWheelListener> mouseWheelListeners;
	protected ArrayList<MouseActionListener> mouseActionListeners;

	protected int windowLevel; // This field is used to determine which 'window' is the one we need to update.

	public PUI() {
		this(0, 0, 100, 100);
	}

	public PUI(float x, float y, float w, float h) {
		this.bounds = new Rectangle(x, y, w, h);

		this.components = new ArrayList<PUI>();
		this.parent = PUIManager.get().getScreen();

		this.visible = false;
		this.hasFocus = false;

		this.mouseMoveListeners = new ArrayList<MouseMoveListener>();
		this.mouseWheelListeners = new ArrayList<MouseWheelListener>();
		this.mouseActionListeners = new ArrayList<MouseActionListener>();

		this.windowLevel = PUIManager.get().getCurrentWindowLevel();
	}
	
	/**
	 * This method snaps a component to a specific border (see 8 first fields of PUI).
	 * The code checks the bites of the int border parameter. It could be changed.
	 * 
	 * @param comp
	 * @param border
	 * @param distX
	 * @param distY
	 */
	public final void snap(PUI comp, int border, float distX, float distY) {
		float ny = 0;
		if ((border & TOP) == TOP) {
			ny = this.bounds.getY() + distY;
		} else if ((border & BOTTOM) == BOTTOM) {
			ny = this.bounds.getMaxY() - comp.getBounds().getHeight() - distY;
		}

		float nx = 0;
		if ((border & LEFT) == LEFT) {
			nx = this.bounds.getX() + distX;
		} else if ((border & RIGHT) == RIGHT) {
			nx = this.bounds.getMaxX() - comp.getBounds().getWidth() - distX;
		}

		comp.move(nx - comp.getBounds().getX(), ny - comp.getBounds().getY());
	}

	public final void move(float mx, float my) {
		this.bounds.setLocation(this.bounds.getX() + mx, this.bounds.getY() + my);
		for (PUI comp : components)
			comp.move(mx, my);
	}

	public final void centerComponent(PUI comp) {
		this.centerComponentOnX(comp);
		this.centerComponentOnY(comp);
	}

	public final void centerComponentOnX(PUI comp) {
		float cx = comp.getBounds().getX();
		float nx = this.bounds.getCenterX() - comp.getBounds().getWidth() / 2;

		comp.move(nx - cx, 0);
	}

	public final void centerContentOnX() {
		for (PUI comp : components) {
			centerComponentOnX(comp);
		}
	}

	public final void centerComponentOnY(PUI comp) {
		float cy = comp.getBounds().getY();
		float ny = this.bounds.getCenterY() - comp.getBounds().getHeight() / 2;

		comp.move(0, ny - cy);
	}

	public final void centerContentOnY() {
		for (PUI comp : components) {
			centerComponentOnY(comp);
		}
	}

	// This method is just a lifecycle method (called automatically by PUIManager)
	@Override
	public final void render(Graphics g) {
		if (!visible)
			return;

		preRender(g);
		rendering(g);
		postRender(g);
	}

	// Called before the actual render
	protected void preRender(Graphics g) {

	}

	// Called after the rendering
	protected void postRender(Graphics g) {

	}

	// Called when the object renders itself.
	protected void renderSelf(Graphics g) {
	}

	// Does the rendering of itself and its children.
	private final void rendering(Graphics g) {
		renderSelf(g);
		for (PUI comp : components)
			comp.render(g);
	}

	@Override
	public void update(Input input, int delta) {
		if (!visible)
			return;

		for (PUI comp : components)
			comp.update(input, delta);
	}

	public final void addMouseActionListener(MouseActionListener mal) {
		this.mouseActionListeners.add(mal);
	}

	public final void addMouseMoveListener(MouseMoveListener mml) {
		this.mouseMoveListeners.add(mml);
	}

	public final void addComponent(PUI comp) {
		this.components.add(comp);
		comp.setParent(this);
		comp.move(this.bounds.getX(), this.bounds.getY());
	}

	public final boolean contains(float x, float y) {
		return this.bounds.contains(x, y);
	}

	public final void setLocation(float x, float y) {
		float mx = x - this.bounds.getX();
		float my = y - this.bounds.getY();
		this.bounds.setLocation(x, y);

		for (PUI pui : components) {
			pui.move(mx, my);
		}
	}

	public final Rectangle getBounds() {
		return bounds;
	}

	public final ArrayList<PUI> getComponents() {
		return components;
	}

	public final PUI getParent() {
		return parent;
	}

	public final void setParent(PUI parent) {
		this.parent = parent;
	}

	public final boolean isVisible() {
		return visible;
	}

	public final void setVisible(boolean visible) {
		this.visible = visible;
		for (PUI comp : components)
			comp.setVisible(visible);
	}

	public final boolean hasFocus() {
		return hasFocus;
	}

	public int getWindowLevel() {
		return windowLevel;
	}

	public void setWindowLevel(int windowLevel) {
		this.windowLevel = windowLevel;
		for (PUI comp : components)
			comp.setWindowLevel(windowLevel);
	}

	public boolean hasComponent(PUI pui) {
		return this.components.contains(pui);
	}

}
