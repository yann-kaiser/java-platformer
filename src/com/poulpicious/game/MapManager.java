package com.poulpicious.game;

import java.io.File;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 * 
 * @author yann
 *
 * The MapManager handles the map loading, and keeps track of the 'current map'.
 */
public class MapManager {
	private static class MapManagerHolder {
		private static final MapManager _instance = new MapManager();
	}
	
	public static MapManager get() {
		return MapManagerHolder._instance;
	}
	
	private File mapFolder;
	
	private File[] mapFiles;
	
	private Map currentMap;

	private MapManager() {
		this.mapFolder = new File("./res/maps/");
		this.mapFiles = this.mapFolder.listFiles();
		
		this.currentMap = null;
	}
	
	/**
	 * 
	 * This method simply loads a map and sets it as currentMap.
	 * 
	 * @param index
	 * @return The map loaded
	 */
	public Map loadMap(int index) {
		if (index >= mapFiles.length)
			return null;
		
		try {
			this.currentMap = new Map(new TiledMap(this.mapFiles[index].getAbsolutePath()));
			return this.currentMap;
		} catch (SlickException e) {
			return null;
		}
	}

	public Map getCurrentMap() {
		return currentMap;
	}
}
