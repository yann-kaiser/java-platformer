package com.poulpicious;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.poulpicious.network.PClient;
import com.poulpicious.pui.PUIManager;
import com.poulpicious.states.CharacterState;
import com.poulpicious.states.GameState;
import com.poulpicious.states.LobbyState;
import com.poulpicious.states.LoginScreen;
import com.poulpicious.states.MainMenuState;
import com.poulpicious.states.RoomState;
import com.poulpicious.util.States;

/**
 * 
 * @author yann
 *
 *         This is the main class which starts the game and adds the different
 *         states.
 */
public class Poulpicious extends StateBasedGame {

	// private static Options options = new Options();

	// public static Options getOptions() {
	// return options;
	// }

	public Poulpicious(String name) {
		super(name);
	}

	// This method simply adds every states, and enter in the splash / login
	// state.
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		PUIManager.get().setup(gc);
		PClient.get().init();

		this.addState(new LoginScreen());
		this.addState(new MainMenuState());
		this.addState(new CharacterState());
		this.addState(new LobbyState());
		this.addState(new RoomState());
		this.addState(new GameState());

		this.enterState(States.LOGIN);
	}

	public static void main(String[] args) {
		/*
		 * double next = 0; for (int level = 2; level <= 100; level++) { next =
		 * (level * level * level) - 5 * (level * level) + 23 * level;
		 * System.out.println(level + " = " + next); }
		 * 
		 * System.exit(0);
		 */

		AppGameContainer game;
		try {
			// We load the LJWGL natives.
			System.setProperty("org.lwjgl.librarypath", new File("./lib/native").getAbsolutePath());
			game = new AppGameContainer(new Poulpicious("Poulpicious Platformer"));

			// We resize the window to cover 80% of the screen
			Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
			game.setDisplayMode((int) (dm.getWidth() * 0.7), (int) (dm.getHeight() * 0.7), false);
			game.setShowFPS(true);
			game.setVSync(true);
			// game.setTargetFrameRate(60); // Simulate VSync (the setVSync
			// option exists but it's a bit laggy)
			game.setAlwaysRender(true); // The game renders even if it hasn't
										// the focus.
			game.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
