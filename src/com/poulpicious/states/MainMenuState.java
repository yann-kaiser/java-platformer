package com.poulpicious.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import com.poulpicious.pui.MouseActionListener;
import com.poulpicious.pui.PUI;
import com.poulpicious.pui.PUIButton;
import com.poulpicious.pui.PUILabel;
import com.poulpicious.pui.PUIManager;
import com.poulpicious.pui.PUIPopup;
import com.poulpicious.util.Fonts;
import com.poulpicious.util.States;

public class MainMenuState extends BasicGameState {

	private PUI mainMenuGUI;
	
	private PUIPopup optionsPopup;
	private PUIPopup quitPopup;

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		mainMenuGUI = new PUI(0, 0, 0.8f * gc.getWidth(), gc.getHeight());
		
		PUILabel title = new PUILabel("Main Menu", Fonts.splashTitleFont, 100, 100);
		mainMenuGUI.addComponent(title);
				
		PUIButton quickGame = new PUIButton("Quick Game", Fonts.splashFont, 100, 350);
		mainMenuGUI.addComponent(quickGame);
		quickGame.addMouseActionListener(new MouseActionListener() {
			@Override
			public void onMouseClick(int button) {
				if(button == Input.MOUSE_LEFT_BUTTON)
					sbg.enterState(States.LOBBY);
			}
		});
		

		PUIButton createGame = new PUIButton("My Character", Fonts.splashFont, 100, 425);
		mainMenuGUI.addComponent(createGame);
		createGame.addMouseActionListener(new MouseActionListener() {
			@Override
			public void onMouseClick(int button) {
				sbg.enterState(States.CHARACTER_MENU, new FadeOutTransition(), new FadeInTransition());
			}
		});

		PUIButton options = new PUIButton("Options", Fonts.splashFont, 100, 500);
		mainMenuGUI.addComponent(options);
		options.addMouseActionListener(new MouseActionListener() {
			@Override
			public void onMouseClick(int button) {
				PUIManager.get().showPopup(optionsPopup);
			}
		});

		PUIButton quit = new PUIButton("Quit", Fonts.splashFont, 100, 550);
		mainMenuGUI.addComponent(quit);
		quit.addMouseActionListener(new MouseActionListener() {
			@Override
			public void onMouseClick(int button) {
				PUIManager.get().showPopup(quitPopup);
			}
		});
		
		optionsPopup = new PUIPopup("Options", 0, 0, 0.8f * gc.getWidth(), 0.8f * gc.getHeight(), true);
		PUIManager.get().getScreen().centerComponent(optionsPopup);
		
		quitPopup = new PUIPopup("Quit", 0, 0, 500, 250, false);
		PUIManager.get().getScreen().centerComponent(quitPopup);

		
		PUILabel quitLabel = new PUILabel("Are you sure ?", Fonts.mediumFont, 0, 50);
		quitPopup.addComponent(quitLabel);
		quitPopup.centerComponent(quitLabel);
		
		PUIButton yesButton = new PUIButton("Yes", Fonts.splashFont, 0, 0);
		quitPopup.addComponent(yesButton);
		quitPopup.snap(yesButton, PUI.BOTTOM_LEFT, 10, 10);
		yesButton.addMouseActionListener(new MouseActionListener() {
			@Override
			public void onMouseClick(int button) {
				gc.exit();
			}
		});
		
		PUIButton noButton = new PUIButton("No", Fonts.splashFont, 0, 0);
		quitPopup.addComponent(noButton);
		quitPopup.snap(noButton, PUI.BOTTOM_RIGHT, 10, 10);
		noButton.addMouseActionListener(new MouseActionListener() {
			@Override
			public void onMouseClick(int button) {
				PUIManager.get().closePopup(quitPopup);
			}
		});
		mainMenuGUI.centerContentOnX();
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		PUIManager.get().addView(mainMenuGUI);
		PUIManager.get().getScreen().centerContentOnX();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setBackground(new Color(0, 0.8f, 0.9f, 1f));
		PUIManager.get().render(g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		PUIManager.get().update(gc.getInput(), delta);
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		PUIManager.get().clearScreen();
	}

	@Override
	public int getID() {
		return States.MAIN_MENU;
	}

}
