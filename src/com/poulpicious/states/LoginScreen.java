package com.poulpicious.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import com.poulpicious.network.PClient;
import com.poulpicious.pui.MouseActionListener;
import com.poulpicious.pui.PUI;
import com.poulpicious.pui.PUIButton;
import com.poulpicious.pui.PUILabel;
import com.poulpicious.pui.PUIManager;
import com.poulpicious.pui.PUITextField;
import com.poulpicious.util.Fonts;
import com.poulpicious.util.States;

public class LoginScreen extends BasicGameState {

	private PUI pui;
	
	private PUITextField address;
	private PUITextField username;
	private PUITextField password;
	
//	private String text;
	private final Color color = new Color(Color.white);
	private float timer;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		pui = new PUI(0, 0, gc.getWidth(), gc.getHeight());
		
		PUILabel title = new PUILabel("Poulpicious Platformer", Fonts.splashTitleFont, 10, 0.1f * gc.getHeight());
		pui.addComponent(title);
		
		pui.centerContentOnX();
		
		PUILabel addressLabel = new PUILabel("Server address:", Fonts.mediumFont, 200, 0.3f * gc.getHeight());
		address = new PUITextField("", gc.getInput(), 200, addressLabel.getBounds().getY() + 40, 300, 40);
		
		PUILabel usernameLabel = new PUILabel("Username:", Fonts.mediumFont, 200, address.getBounds().getY() + 50);
		username = new PUITextField("", gc.getInput(), 200, usernameLabel.getBounds().getY() + 40, 300, 40);
		
		PUILabel passwordLabel = new PUILabel("Password:", Fonts.mediumFont, 200, username.getBounds().getY() + 50);
		password = new PUITextField("", gc.getInput(), 200, passwordLabel.getBounds().getY() + 40, 300, 40);
		password.setMasked(true);
		
		PUIButton loginButton = new PUIButton("Login", Fonts.mediumFont, 200, password.getBounds().getY() + 80);
		loginButton.addMouseActionListener(new MouseActionListener() {
			public void onMouseClick(int button) {
				PClient.get().connectToMaster(address.getText(), username.getText(), password.getText(),
					() -> sbg.enterState(States.MAIN_MENU, new FadeOutTransition(Color.black, 400), new FadeInTransition(Color.black, 400)));
			}
		});
		
		pui.addComponent(addressLabel);
		pui.addComponent(address);
		pui.addComponent(usernameLabel);
		pui.addComponent(username);
		pui.addComponent(passwordLabel);
		pui.addComponent(password);
		
		pui.addComponent(loginButton);
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		timer = 0;
		color.a = 0;
		gc.getGraphics().setBackground(Color.black);
		
		PUIManager.get().addView(pui);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		PUIManager.get().render(g);
		//text = "Press enter to Play !";
		//Fonts.drawXCentered(text, Fonts.splashFont, gc.getWidth(), 0, 0.8f * gc.getHeight(), color);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {	
		color.a = (float) Math.abs(Math.sin(timer / 500f));
		timer += delta;
		
		PUIManager.get().update(gc.getInput(), delta);
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		PUIManager.get().clearScreen();
	}

	@Override
	public int getID() {
		return States.LOGIN;
	}

}
