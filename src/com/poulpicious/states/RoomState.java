package com.poulpicious.states;


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.poulpicious.network.PClient;
import com.poulpicious.network.ServerListener;
import com.poulpicious.network.ServerPlayer;
import com.poulpicious.pui.MouseActionListener;
import com.poulpicious.pui.PUI;
import com.poulpicious.pui.PUIButton;
import com.poulpicious.pui.PUIManager;
import com.poulpicious.pui.PUIPanel;
import com.poulpicious.pui.PUITable;
import com.poulpicious.util.Fonts;
import com.poulpicious.util.States;

public class RoomState extends BasicGameState {
	
	private PUI pui;
	private PUITable playerList;
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		pui = new PUI();
		
		PUIPanel leftPanel = new PUIPanel(0, 0, 0.3f * gc.getWidth(), gc.getHeight());
		
		PUIPanel menu = new PUIPanel(0, 200, 0.3f * gc.getWidth(), gc.getHeight() - 200);
		menu.setBackgroundTransparent();
		
		PUIButton readyButton = new PUIButton("Ready !", Fonts.mediumFont, 0, 0);
		readyButton.setToggledColor(Color.green);
		readyButton.addMouseActionListener(new MouseActionListener() {
			@Override
			public void onMouseClick(int button) {
				PClient.get().sendPlayerReady(readyButton.isToggled());
			}
		});
		menu.addComponent(readyButton);
		menu.centerContentOnX();
		
		leftPanel.addComponent(menu);
		
		playerList = new PUITable(600, 200, 400, 400);
		
		PClient.get().addServerListener(new ServerListener() {
			@Override
			public void onPlayerJoinRoom(ServerPlayer player) {
				playerList.addCell(player.getUsername(), player.getUsername());
			}
			
			@Override
			public void onPlayerQuitRoom(ServerPlayer player) {
				playerList.removeCell(player.getUsername());
			}
			
			@Override
			public void onStartGame() {
				game.enterState(States.GAME_STATE);
			}
		});
		
		pui.addComponent(leftPanel);
		pui.addComponent(playerList);
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame game) throws SlickException {
		PUIManager.get().addView(pui);
		
		playerList.addCell(PClient.get().getUsername(), PClient.get().getUsername());
		for (ServerPlayer player : PClient.get().getRoom().getPlayers().values()) {
			playerList.addCell(player.getUsername(), player.getUsername());
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		PUIManager.get().render(g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		PUIManager.get().update(gc.getInput(), delta);
	}
	
	@Override
	public void leave(GameContainer gc, StateBasedGame game) throws SlickException {
		PUIManager.get().clearScreen();
	}

	@Override
	public int getID() {
		return States.ROOM_STATE;
	}

}
