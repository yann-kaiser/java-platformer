package com.poulpicious.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.poulpicious.pui.PUI;
import com.poulpicious.pui.PUILabel;
import com.poulpicious.pui.PUIPanel;
import com.poulpicious.util.Fonts;
import com.poulpicious.util.MathUtils;

/**
 * 
 * @author yann
 *
 *         This class handles the entities that represent the other players in
 *         our room. It's mainly here for handling the position and rendering.
 */
public class ServerPlayerEntity extends ShootingEntity {

	private Vector2f nextDestination;
	private PUI infoPUI;
	private PUILabel nameLabel;
	private PUIPanel healthBarBackground;
	private PUIPanel healthBarForeground;

	private Image flipped;
	
	public ServerPlayerEntity(String name, float x, float y, float w, float h) {
		super(name, x, y, w, h);

		this.nextDestination = this.bounds.getLocation();
		this.infoPUI = new PUI(this.bounds.getX(), this.bounds.getY(), this.bounds.getWidth(), this.bounds.getHeight());
		this.nameLabel = new PUILabel(name, Fonts.formFont, x, y);

		this.infoPUI.addComponent(nameLabel);
		this.nameLabel.getBounds().setY(infoPUI.getBounds().getY() - 30);

		healthBarBackground = new PUIPanel(0, 30, 50, 5);
		healthBarBackground.setBackgroundColor(Color.red);
		healthBarForeground = new PUIPanel(0, 30, 50, 5);
		healthBarForeground.setBackgroundColor(Color.green);

		this.infoPUI.addComponent(healthBarBackground);
		this.healthBarBackground.getBounds().setY(infoPUI.getBounds().getY() - 10);
		this.infoPUI.addComponent(healthBarForeground);
		this.healthBarForeground.getBounds().setY(infoPUI.getBounds().getY() - 10);

		this.infoPUI.centerContentOnX();
		this.infoPUI.setVisible(true);

		this.hasDrag = false;
		this.hasGravity = false;
	}

	public void setup() {
		try {
			this.image = new Image("./res/images/player.png");
			this.image.setFilter(Image.FILTER_NEAREST);
			this.flipped = image.getFlippedCopy(true, false);
			this.goingRight = true;
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void setupHealth(float maxHealth) {
		this.maxHealth = maxHealth;
	}

	@Override
	public void render(Graphics g) {
		if (this.image != null) {
			if (goingRight)
				image.draw((int) this.bounds.getX() - 10, this.bounds.getY());
			else
				flipped.draw(this.bounds.getX() - 25, this.bounds.getY());
		} else {
			g.setColor(Color.green);
			g.draw(bounds);
		}
		infoPUI.render(g);

		super.render(g);
	}

	@Override
	public void update(Input input, int delta) {
		super.update(input, delta);

		if (nextDestination.distanceSquared(this.getBounds().getLocation()) > Math.pow(100.0, 2))
			this.bounds.setLocation(nextDestination);
		else
			this.bounds.setLocation(MathUtils.lerp(this.bounds.getLocation(), nextDestination, 0.5f));

		this.infoPUI.setLocation(this.bounds.getX(), this.bounds.getY());
		if (this.getStats() != null) {
			float healthRatio = this.getStats().getHealth().getValue() / maxHealth;
			healthBarForeground.getBounds().setWidth(healthRatio * healthBarBackground.getBounds().getWidth());
		}
	}

	public Vector2f getNextDestination() {
		return nextDestination;
	}

	public void setNextDestination(float x, float y) {
		this.nextDestination.set(x, y);
	}

}
