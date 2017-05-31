package com.poulpicious.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import com.poulpicious.game.MapManager;
import com.poulpicious.network.PClient;
import com.poulpicious.pui.MouseActionListener;
import com.poulpicious.pui.PUI;
import com.poulpicious.pui.PUIButton;
import com.poulpicious.pui.PUILabel;
import com.poulpicious.pui.PUIManager;
import com.poulpicious.pui.PUIPanel;
import com.poulpicious.util.Fonts;
import com.poulpicious.util.States;

public class LobbyState extends BasicGameState {

	private PUI lobbyGUI;

	private PUIPanel searchPanel;
	private PUIPanel searchMapPanel;

	private PUIPanel matchmakingPanel;

	private PUILabel loadingLabel;
	private int nbOfDots = 3;
	private int count = 0;
	private int nextCount = 1000;

	private boolean wasSearchingGame;

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		lobbyGUI = new PUI(0, 0, gc.getWidth(), gc.getHeight());

		PUILabel title = new PUILabel("Lobby", Fonts.splashTitleFont, 20, 20);
		lobbyGUI.addComponent(title);

		searchPanel = new PUIPanel(0, 0, 0.7f * lobbyGUI.getBounds().getWidth(),
				0.7f * lobbyGUI.getBounds().getHeight());
		lobbyGUI.addComponent(searchPanel);
		lobbyGUI.centerComponent(searchPanel);

		MapManager.get().loadMap(0);
		searchMapPanel = new PUIPanel(0, 0, searchPanel.getBounds().getWidth() - 40, 250);

		PUILabel mapName = new PUILabel("Map 01", Fonts.mediumFont, 20, 20);
		searchMapPanel.addComponent(mapName);
		searchMapPanel.snap(mapName, PUI.TOP, 0, 10);
		searchMapPanel.centerComponentOnX(mapName);

		searchPanel.addComponent(searchMapPanel);
		searchPanel.snap(searchMapPanel, PUI.TOP_LEFT, 20, 20);

		PUIButton startSearch = new PUIButton("Start Searching", Fonts.mediumFont, 100,
				searchPanel.getBounds().getHeight() - 50);
		startSearch.addMouseActionListener(new MouseActionListener() {
			@Override
			public void onMouseClick(int button) {
				searchPanel.setVisible(false);
				matchmakingPanel.setVisible(true);
				PClient.get().startGameSearchState();
				wasSearchingGame = true;
				System.out.println("Started searching");
			}
		});
		searchPanel.addComponent(startSearch);
		searchPanel.snap(startSearch, PUI.BOTTOM_RIGHT, 20, 20);

		PUIButton returnButton = new PUIButton("< Return to Menu", Fonts.mediumFont, 0, 0);
		returnButton.addMouseActionListener(new MouseActionListener() {
			@Override
			public void onMouseClick(int button) {
				searchPanel.setVisible(true);
				matchmakingPanel.setVisible(false);

				if (wasSearchingGame) {
					System.out.println("LEAVING");
					PClient.get().stopGameSearchState();
				} else
					sbg.enterState(States.MAIN_MENU);
			}
		});
		lobbyGUI.addComponent(returnButton);
		lobbyGUI.snap(returnButton, PUI.BOTTOM_LEFT, 20, 20);

		matchmakingPanel = new PUIPanel(10, 10, 0.8f * gc.getWidth(), 0.8f * gc.getHeight());
		matchmakingPanel.setBackgroundTransparent();

		PUILabel matchmakingLabel = new PUILabel("Searching for a Game", Fonts.splashTitleFont, 0, 200);
		matchmakingPanel.addComponent(matchmakingLabel);

		loadingLabel = new PUILabel(". . .", Fonts.splashTitleFont, 0, 300);
		matchmakingPanel.addComponent(loadingLabel);

		matchmakingPanel.centerContentOnX();

		lobbyGUI.addComponent(matchmakingPanel);
		lobbyGUI.centerComponentOnX(matchmakingPanel);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		PUIManager.get().addView(lobbyGUI);
		PUIManager.get().getScreen().centerContentOnX();
		matchmakingPanel.setVisible(false);

		this.wasSearchingGame = false;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		PUIManager.get().render(g);

		if (!PClient.get().isSearchingGame()) {
			TiledMap map = MapManager.get().getCurrentMap().getTiledMap();
			g.scale(0.125f, 0.125f);
			map.render((int) searchMapPanel.getBounds().getCenterX() * 8 - map.getWidth() * map.getTileWidth() / 2,
					(int) searchMapPanel.getBounds().getCenterY() * 8 - map.getHeight() * map.getTileHeight() / 2 + 40,
					0, 0, 20, 20);
			g.scale(1f, 1f);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		PUIManager.get().update(gc.getInput(), delta);

		count += delta;

		if (count - nextCount >= 0) {
			nbOfDots = (nbOfDots + 1) % 3;

			String res = ". ";
			for (int i = 0; i < nbOfDots; i++)
				res += ". ";
			this.loadingLabel.setText(res.substring(0, res.length() - 1));

			nextCount += 1000;
		}

		if (PClient.get().getRoom() != null) {
			sbg.enterState(States.ROOM_STATE);
		}

		if (wasSearchingGame && !PClient.get().isSearchingGame()) {
			wasSearchingGame = false;
			sbg.enterState(States.MAIN_MENU);
		}
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		PUIManager.get().clearScreen();
	}

	@Override
	public int getID() {
		return States.LOBBY;
	}

}
