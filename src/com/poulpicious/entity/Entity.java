package com.poulpicious.entity;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import com.poulpicious.game.MapManager;

/**
 * 
 * @author yann
 *
 * This class is an abstract representation of an entity.
 * An entity is just an object, which has a name and bounds.
 */
public abstract class Entity {

	protected Shape bounds; // The "collider box" of the entity.
	protected Image image; // It may have an image to draw.
	protected String name; // The name (for storage mainly and retrieving other players' entities)

	// The number of other player this entity killed (player or ServerPlayer).
	protected int score;
	public Entity(String name, float x, float y, float w, float h) {
		this.name = name;
		this.bounds = new Rectangle(x, y, w, h);
	}

	public void render(Graphics g) {

	}

	public void update(Input input, int delta) {
	}

	// This method just respawns the entity.
	public void spawn() {
		Vector2f spawnPos = MapManager.get().getCurrentMap().getRandomSpawn().getPosition().copy();
		this.getBounds().setLocation(
				spawnPos.add(new Vector2f(32f - this.bounds.getWidth() / 2f, 62f - this.bounds.getHeight())));
	}

	public Shape getBounds() {
		return bounds;
	}

	public void setBounds(Shape bounds) {
		this.bounds = bounds;
	}

	public int getScore() {
		return score;
	}

	public void incrementScore() {
		this.score++;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
