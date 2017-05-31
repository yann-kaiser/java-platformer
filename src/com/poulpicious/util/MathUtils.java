package com.poulpicious.util;

import org.newdawn.slick.geom.Vector2f;

public class MathUtils {
	
	public static Vector2f lerp(Vector2f start, Vector2f end, float percentage) {
		Vector2f diff = new Vector2f (end.x - start.x, end.y - start.y);
		diff.scale(percentage);
		return diff.add(start);
	}

}
