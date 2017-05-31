package com.poulpicious.entity;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.poulpicious.game.MapManager;

/**
 * 
 * @author yann
 *
 *         This class is an abstract representation of Dynamic Entities (they can move and can be subject to gravity).
 */
public abstract class DynamicEntity extends Entity {

	protected final float GRAVITY = 15f;

	protected Vector2f velocity;
	protected float drag;
	protected float moveSpeed;

	protected boolean canJump;
	protected boolean hasGravity;
	protected boolean hasDrag;
	
	protected boolean goingRight;
	
	protected Stats stats;
	protected float maxHealth;


	public DynamicEntity(String name, float x, float y, float w, float h) {
		super(name, x, y, w, h);

		this.velocity = new Vector2f();
		this.drag = 1.4f;
		this.moveSpeed = 5f;
		this.hasGravity = true;
		this.hasDrag = true;
	}

	private void applyGravity() {
		if (hasGravity && velocity.y < GRAVITY)
			velocity.y += 0.8f;
	}
	
	private void applyDrag() {
		if (hasDrag && drag > 0)
			velocity.x /= 1 + drag;
	}
	
	@Override
	public void update(Input input, int delta) {
		applyGravity();
		applyDrag();

		// Check collisions
		handleCollidingOnX();
		handleCollidingOnY();

		if (Math.abs(velocity.x) < 0.001f)
			velocity.x = 0;

		// The move code
		this.bounds.setLocation(this.bounds.getLocation().add(velocity));

		super.update(input, delta);
	}

	@Override
	public void render(Graphics g) {
		super.render(g);
	}

	/**
	 * This method simply handles the collision only the left and right of the player.
	 */
	protected void handleCollidingOnX() {
		Rectangle nBounds = new Rectangle(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
		nBounds.setLocation(nBounds.getLocation().add(new Vector2f(velocity.x, 0)));
		int xPosTile = (int) (nBounds.getX() / 64f);
		int xPosMaxTile = (int) (nBounds.getMaxX() / 64f);
		int yPosTile = (int) (nBounds.getY() / 64f);
		int yPosMaxTile = (int) (nBounds.getMaxY() / 64f);

		boolean colliding = false;
		float velocityX = this.velocity.x;
		float maxOffset = 0;
		for (int y = yPosTile - 1; y <= yPosMaxTile + 1; y++) {
			Rectangle r = null;

			if (velocity.x > 0)
				r = MapManager.get().getCurrentMap().getCollisionShape(xPosMaxTile, y);
			else
				r = MapManager.get().getCurrentMap().getCollisionShape(xPosTile, y);

			if (r == null)
				continue;

			if (nBounds.intersects(r)) {

				float currOffset = 0;
				if (velocity.x > 0)
					currOffset = Math.abs(nBounds.getMaxX() - r.getX());
				else
					currOffset = Math.abs(r.getMaxX() - nBounds.getX());

				if (currOffset > maxOffset) {
					colliding = true;
					maxOffset = currOffset;
				}
			}
		}
		if (maxOffset != 0)
			this.velocity.x = velocityX - (maxOffset + 0.001f) * Math.signum(this.velocity.x);

		if (colliding)
			onCollide();

	}

	/**
	 * This method simply handles the collision only the left and right of the player.
	 */
	private void handleCollidingOnY() {
		canJump = false;
		Rectangle nBounds = new Rectangle(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
		nBounds.setLocation(nBounds.getLocation().add(new Vector2f(0f, velocity.y)));

		int yPosTile = (int) (nBounds.getY() / 64f);
		int yPosMaxTile = (int) (nBounds.getMaxY() / 64f);
		int xPosTile = (int) (nBounds.getX() / 64f);
		int xPosMaxTile = (int) (nBounds.getMaxX() / 64f);

		boolean colliding = false;
		float maxOffset = 0;
		for (int x = xPosTile - 2; x <= xPosMaxTile + 2; x++) {
			Rectangle r = null;

			if (velocity.y > 0)
				r = MapManager.get().getCurrentMap().getCollisionShape(x, yPosMaxTile);
			else
				r = MapManager.get().getCurrentMap().getCollisionShape(x, yPosTile);

			if (r == null)
				continue;

			if (nBounds.intersects(r) || nBounds.contains(r)) {
				if (r.getY() > nBounds.getY())
					canJump = true;

				float currOffset = 0;
				if (velocity.y > 0)
					currOffset = Math.abs(nBounds.getMaxY() - r.getY());
				else
					currOffset = Math.abs(r.getMaxY() - nBounds.getY());

				if (currOffset > maxOffset) {
					colliding = true;
					maxOffset = currOffset;
				}
			}
		}
		
		if (colliding) {
			onCollide();
			this.velocity.y = 0;
		}
	}

	protected void onCollide() {

	}
	
	// This method is responsible of taking damages, and kill the entity if the health is too low.
	public final void damage(Entity damager, float damages) {
		this.stats.getHealth().sub(damages);

		if (this.stats.getHealth().getValue() <= 0) {
			this.stats.getHealth().set(maxHealth);
			this.die(damager);
		}
	}

	public void die(Entity damager) {

	}
	
	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(float maxHealth) {
		this.maxHealth = maxHealth;
	}
}
