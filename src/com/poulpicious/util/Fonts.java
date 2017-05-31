package com.poulpicious.util;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class Fonts {

	public static UnicodeFont splashTitleFont;
	public static UnicodeFont splashFont;
	public static UnicodeFont mediumFont;
	public static UnicodeFont formFont;

	public static void drawXCentered(String text, UnicodeFont font, float screenWidth, float addX, float y) {
		float width = font.getWidth(text);
		font.drawString(addX + screenWidth / 2 - width / 2, y, text);
	}
	
	public static void drawXCentered(String text, UnicodeFont font, float screenWidth, float addX, float y, Color color) {
		float width = font.getWidth(text);
		font.drawString(addX + screenWidth / 2 - width / 2, y, text, color);
	}
	
	public static void drawYCentered(String text, UnicodeFont font, float screenHeight, float addY, float x) {
		float height = font.getHeight(text);
		font.drawString(x, addY + screenHeight / 2 - height / 2, text);
	}
	
	public static void drawYCentered(String text, UnicodeFont font, float screenHeight, float addY, float x, Color color) {
		float height = font.getHeight(text);
		font.drawString(x, addY + screenHeight / 2 - height / 2, text, color);
	}
	
	public static void drawXYCentered(String text, UnicodeFont font, float screenWidth, float screenHeight, float addX, float addY) {
		float width = font.getWidth(text);
		float height = font.getHeight(text);
		font.drawString(addX + screenWidth / 2 - width / 2, addY + screenHeight / 2 - height / 2, text);
	}
	
	public static void drawXYCentered(String text, UnicodeFont font, float screenWidth, float screenHeight, float addX, float addY, Color color) {
		float width = font.getWidth(text);
		float height = font.getHeight(text);
		font.drawString(addX + screenWidth / 2 - width / 2, addY + screenHeight / 2 - height / 2, text, color);
	}

	@SuppressWarnings("unchecked")
	private static UnicodeFont createFont(String name, int size, boolean bold, boolean italic) {
		UnicodeFont font;
		try {
			font = new UnicodeFont("./res/fonts/" + name + ".ttf", size, bold, italic);
			font.addAsciiGlyphs();
 			font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
			font.loadGlyphs();

			return font;
		} catch (SlickException e) {
			e.printStackTrace();
			return null;
		}
	}

	static {
		splashTitleFont = createFont("upheaval", 60, false, false);
		splashFont = createFont("upheaval", 40, false, false);
		mediumFont = createFont("upheaval", 30, false, false);
		formFont = createFont("visitor", 20, false, false);
	}

}
