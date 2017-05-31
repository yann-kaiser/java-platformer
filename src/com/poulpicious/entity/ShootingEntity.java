package com.poulpicious.entity;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

/**
 * 
 * @author yann
 *
 * This class is a child of DynamicEntity since it can shoot and move.
 * It contains the shooting code and the bullet list handling.
 */
public class ShootingEntity extends DynamicEntity {
	protected ArrayList<Bullet> bullets; // The list of bullet shot, that
											// are still moving.

	public ShootingEntity(String name, float x, float y, float w, float h) {
		super(name, x, y, w, h);
		this.bullets = new ArrayList<Bullet>();

	}

	@Override
	public void render(Graphics g) {
		super.render(g);

		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			b.render(g);
		}
	}

	@Override
	public void update(Input input, int delta) {
		super.update(input, delta);
		
		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			b.update(input, delta);
		}
	}
	
	public final void shoot(Vector2f start, Vector2f direction) {
		bullets.add(new Bullet("bullet" + bullets.size(), start.x, start.y, this, direction));
		this.goingRight = direction.x > 0;
	}

	public final void removeBullet(Bullet b) {
		this.bullets.remove(b);
	}
}