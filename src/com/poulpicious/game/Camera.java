package com.poulpicious.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.poulpicious.entity.Entity;
import com.poulpicious.util.MathUtils;

/**
 * 
 * @author yann
 *
 * This class contains the code for the Camera handling.
 * It moves the graphics depending on the player position.
 */
public class Camera {

	private Vector2f position, offsetToMove;
	private Entity target;
	private Rectangle bounds;
	private Vector2f screenSize;

	public Camera(GameContainer gc, Entity target, Rectangle bounds) {
		this.position = new Vector2f();
		this.offsetToMove = new Vector2f(gc.getWidth() / 2, gc.getHeight() / 2 + 100);
		this.target = target;
		this.bounds = bounds;
		this.screenSize = new Vector2f(gc.getWidth(), gc.getHeight());
	}

	// Move the graphics according to the player position and clamps the value according to the bounds.
	public void moveGraphics(Graphics g) {
		position = MathUtils.lerp(position, new Vector2f(target.getBounds().getCenter()).sub(offsetToMove), 0.1f);

		if (position.x < this.bounds.getX())
			position.x = this.bounds.getX();

		if (position.y < this.bounds.getY())
			position.y = this.bounds.getY();
		
		if (position.x + this.screenSize.x > this.bounds.getMaxX())
			position.x = this.bounds.getMaxX() - this.screenSize.x;

		if (position.y + this.screenSize.y > this.bounds.getMaxY())
			position.y = this.bounds.getMaxY() - this.screenSize.y;
		
		g.translate(-position.x, -position.y);
	}

	public void restoreGraphics(Graphics g) {
		g.translate(0, 0);
	}

	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

}
