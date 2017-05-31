package com.poulpicious.game;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;

/**
 * 
 * @author yann
 *
 * This class handles the tiledMap. It loads a map from a .tmx file, and create the collision shapes.
 */
public class Map {
	private float x, y;
	private TiledMap tiledMap;
	private Rectangle[] collisions;
	private ArrayList<SpawnPoint> spawns;
	private Random rand;

	public Map(TiledMap tiledMap) {
		this.tiledMap = tiledMap;
		this.spawns = new ArrayList<SpawnPoint>();
		this.rand = new Random();
		this.setLocation(0, 0);

		calculateCollisionShapes();
	}

	/**
	 * This method is responsible for the creation of the collision shapes for every tile that needs one.
	 */
	public void calculateCollisionShapes() {
		collisions = new Rectangle[this.tiledMap.getWidth() * this.tiledMap.getHeight()];
		float tileSize = this.tiledMap.getTileHeight();
		
		for (int y = 0; y < this.tiledMap.getHeight(); y++) {
			for (int x = 0; x < this.tiledMap.getWidth(); x++) {
				int tileID = this.tiledMap.getTileId(x, y, 0);
				if (this.tiledMap.getTileProperty(tileID, "isColliding", "false").equals("true"))
					collisions[x + this.tiledMap.getWidth() * y] = new Rectangle(x * tileSize, y * tileSize, tileSize, tileSize);
				
				if (this.tiledMap.getTileProperty(tileID, "isSpawn", "false").equals("true"))
					spawns.add(new SpawnPoint(new Vector2f(x * tileSize, y * tileSize)));
			}
		}
	}

	public void render(Graphics g) {
		tiledMap.render((int) x, (int) y);
	}

	public Rectangle getCollisionShape(int x, int y) {
		int index = x + this.tiledMap.getWidth() * y;
		if (index < 0 || index >= this.collisions.length)
			return null;
		return this.collisions[index];
	}
	
	public SpawnPoint getRandomSpawn() {
		int randPos = this.rand.nextInt(this.spawns.size());
		
		return this.spawns.get(randPos);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public TiledMap getTiledMap() {
		return tiledMap;
	}
}
