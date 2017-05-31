package com.poulpicious.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.poulpicious.game.Camera;
import com.poulpicious.network.PClient;
import com.poulpicious.network.ServerPlayer;
import com.poulpicious.network.packets.Packet13PlayerDeath;

/**
 * 
 * @author yann
 *
 *         This class represents the player. It handles the Input and sends
 *         information to server.
 */
public class Player extends ShootingEntity {

	private Camera camera;
	private int ticks;

	private Image flipped;

	public Player(String name, float x, float y, float w, float h) {
		super(name, x, y, w, h);
		try {
			this.image = new Image("./res/images/player.png");
			this.image.setFilter(Image.FILTER_NEAREST);
			this.flipped = image.getFlippedCopy(true, false);
			this.goingRight = true;
		} catch (SlickException e) {
			e.printStackTrace();
		}
		// spawn();
	}

	@Override
	public void update(Input input, int delta) {
		ticks++;

		// Every 3 ticks (3 fps actually) we send our position to the server
		// (which sends it back to every player in the same room as us)
		if (ticks % 3 == 0) {
			PClient.get().sendPlayerPosition(this.bounds.getX(), this.bounds.getY());
			ticks = 0;
		}

		this.hasDrag = !this.handleInput(input, delta);
		super.update(input, delta);
	}

	protected boolean handleInput(Input input, int delta) {
		boolean hasMoved = false;
		if (input.isKeyDown(Input.KEY_D)) {
			velocity.x = moveSpeed;
			hasMoved = true;
		} else if (input.isKeyDown(Input.KEY_A)) {
			velocity.x = -moveSpeed;
			hasMoved = true;
		}

		if (canJump && input.isKeyDown(Input.KEY_SPACE)) {
			velocity.y -= 16f;
			canJump = false;
		}

		// This portion of code handles the shooting.
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			Vector2f centerPos = new Vector2f(this.bounds.getCenterX(), this.bounds.getCenterY());
			Vector2f mousePos = new Vector2f(input.getMouseX() + camera.getPosition().x,
					input.getMouseY() + camera.getPosition().y);
			Vector2f direction = mousePos.sub(centerPos).normalise();

			goingRight = direction.x > 0;

			this.shoot(new Vector2f(this.bounds.getCenterX(), this.bounds.getCenterY()), direction.scale(10f));
			PClient.get().sendPlayerShoot(centerPos, direction);
		}

		return hasMoved;
	}

	@Override
	public void die(Entity damager) {
		ServerPlayer player = PClient.get().getRoom().getPlayer(damager.getName());
		Packet13PlayerDeath ppd = new Packet13PlayerDeath();
		ppd.killerID = player.getConnectionID();
		player.getPlayerEntity().incrementScore();

		PClient.get().getClient().sendTCP(ppd);
		spawn();
	}

	@Override
	public void render(Graphics g) {
		super.render(g);
		if (this.image != null) {
			if (goingRight)
				image.draw((int) this.bounds.getX() - 10, this.bounds.getY());
			else
				flipped.draw(this.bounds.getX() - 25, this.bounds.getY());
		} else {
			g.setColor(Color.green);
			g.draw(bounds);
		}
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public void onKill(int playerKilledLevel) {
		float ratio = playerKilledLevel / this.getStats().getLevel();

		this.getStats().giveExp((int) ratio * playerKilledLevel * 10);
	}

}
