package com.poulpicious.states;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.poulpicious.entity.Player;
import com.poulpicious.game.Camera;
import com.poulpicious.game.Map;
import com.poulpicious.game.MapManager;
import com.poulpicious.game.Scoreboard;
import com.poulpicious.network.PClient;
import com.poulpicious.network.ServerPlayer;
import com.poulpicious.pui.PUIManager;
import com.poulpicious.pui.PUIProgressBar;
import com.poulpicious.util.States;

public class GameState extends BasicGameState {

	private Camera camera;
	private Player player;
	private Map map;

	private PUIProgressBar healthbar;

	private Scoreboard scoreboard;
	private ArrayList<ServerPlayer> playerList;

	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		player = PClient.get().getLocalPlayer();
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame game) throws SlickException {
		map = MapManager.get().getCurrentMap();

		float mapWidth = map.getTiledMap().getWidth() * map.getTiledMap().getTileWidth();
		float mapHeight = map.getTiledMap().getHeight() * map.getTiledMap().getTileHeight();

		player = PClient.get().getLocalPlayer();
		player.spawn();

		float health = player.getStats().getHealth().getValue();
		player.setMaxHealth(health);
		this.healthbar = new PUIProgressBar(Color.red, Color.green, health, health, 1f, 0f, 30f, 400f, 30f);

		camera = new Camera(gc, player, new Rectangle(0, 0, mapWidth, mapHeight));
		player.setCamera(camera);

		PUIManager.get().addView(healthbar);
		PUIManager.get().getScreen().centerContentOnX();

		this.scoreboard = new Scoreboard();
		this.scoreboard.addToScreen();

		for (ServerPlayer sp : PClient.get().getRoom().getPlayers().values()) {
			sp.getPlayerEntity().setup();
		}
		
		playerList = new ArrayList<ServerPlayer>(PClient.get().getRoom().getPlayers().values());
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		camera.moveGraphics(g);
		map.render(g);

		for (int i = 0; i < playerList.size(); i++) {
			ServerPlayer sp = playerList.get(i);
			sp.getPlayerEntity().render(g);
		}

		player.render(g);

		g.translate(camera.getPosition().x, camera.getPosition().y);

		float healthRatio = player.getStats().getHealth().getValue() / healthbar.getMaxValue();
		healthbar.setProgress(healthRatio);
		PUIManager.get().render(g);
		camera.restoreGraphics(g);

	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		for (ServerPlayer sp : PClient.get().getRoom().getPlayers().values())
			sp.getPlayerEntity().update(gc.getInput(), delta);
		player.update(gc.getInput(), delta);

		this.scoreboard.setVisible(gc.getInput().isKeyDown(Input.KEY_TAB));
		this.scoreboard.update(gc.getInput(), delta);

		PUIManager.get().update(gc.getInput(), delta);
	}

	@Override
	public void leave(GameContainer gc, StateBasedGame game) throws SlickException {
		PUIManager.get().clearScreen();
	}

	@Override
	public int getID() {
		return States.GAME_STATE;
	}

}