package com.poulpicious.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import com.poulpicious.network.PClient;
import com.poulpicious.network.ServerPlayer;

/**
 * 
 * @author yann
 *
 * The class holding the information of the bullet that a player can shoot.
 * As it is a moving entity, it has a velocity and moves.
 */
public class Bullet extends DynamicEntity {

	private ShootingEntity owner; // The player who shot the bullet

	public Bullet(String name, float x, float y, ShootingEntity entity, Vector2f velocity) {
		super(name, x - 3, y - 3, 5, 5);

		this.owner = entity;
		this.velocity = velocity;
		this.hasGravity = false;
		this.hasDrag = false;
		this.drag = 1f;
	}

	@Override
	public void update(Input input, int delta) {
		if (owner != PClient.get().getLocalPlayer()) {
			// Code for player damage.
			if (this.bounds.intersects(PClient.get().getLocalPlayer().getBounds())) {
				onCollide();
				PClient.get().getLocalPlayer().damage(this.owner, this.owner.getStats().getBulletDamage().getValue());
			}
		} else {
			// We just destroy it, the damage is done on the other client part.
			for (ServerPlayer sp : PClient.get().getRoom().getPlayers().values()) {
				if (this.bounds.intersects(sp.getPlayerEntity().getBounds())) {
					onCollide();
				}
			}
		}
		super.update(input, delta);
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fill(bounds);
		g.setColor(Color.gray);
		g.draw(bounds);
	}

	// This method is called whenever the bullet collides.
	@Override
	protected void onCollide() {
		owner.removeBullet(this);
	}

}
