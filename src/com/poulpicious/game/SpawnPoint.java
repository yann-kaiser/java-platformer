package com.poulpicious.game;

import org.newdawn.slick.geom.Vector2f;

/**
 * 
 * @author yann
 *
 * This represents a SpawnPoint.
 */
public class SpawnPoint {
	private Vector2f position;
	private int team;

	public SpawnPoint(Vector2f position) {
		this.position = position;
		this.team = 0;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}
}
