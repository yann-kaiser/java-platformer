package com.poulpicious.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import com.poulpicious.entity.Stat;
import com.poulpicious.entity.Stats;
import com.poulpicious.network.PClient;
import com.poulpicious.pui.GaugeListener;
import com.poulpicious.pui.MouseActionListener;
import com.poulpicious.pui.PUI;
import com.poulpicious.pui.PUIButton;
import com.poulpicious.pui.PUIGauge;
import com.poulpicious.pui.PUILabel;
import com.poulpicious.pui.PUIManager;
import com.poulpicious.util.Fonts;
import com.poulpicious.util.States;

public class CharacterState extends BasicGameState {

	private PUILabel title;
	private PUILabel pointsToSpendLabel;

	private PUILabel healthLabel;
	private PUILabel damagesLabel;
	private PUILabel resistanceLabel;

	private PUIGauge healthGauge;
	private PUIGauge damagesGauge;
	private PUIGauge resistanceGauge;
	
	private PUIButton returnButton;

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.title = new PUILabel("Character Edition", Fonts.splashTitleFont, 0, 10);
		
		this.pointsToSpendLabel = new PUILabel("", Fonts.mediumFont, 0, 130);
		
		this.returnButton = new PUIButton("< Return to main menu", Fonts.mediumFont, 20, 20);
		this.returnButton.addMouseActionListener(new MouseActionListener() {
			@Override
			public void onMouseClick(int button) {
				PClient.get().saveCharacterInfos();
				sbg.enterState(States.MAIN_MENU, new FadeOutTransition(), new FadeInTransition());
			}
		});
		PUIManager.get().getScreen().snap(returnButton, PUI.BOTTOM_LEFT, 20, 20);
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gc.getGraphics().setBackground(Color.black);

		Stats stats = PClient.get().getLocalPlayer().getStats();

		Stat health = stats.getHealth();
		this.healthLabel = new PUILabel("Health (" + health.getLevel() + ")", Fonts.mediumFont, 0, 200);
		this.healthGauge = new PUIGauge(Color.white, Color.green, health.getValue(), health.getMaxValue(),
				health.getGrowth(), 0, 250, 250, 40);

		this.healthGauge.addGaugeListener(new GaugeListener() {
			@Override
			public boolean onAdd() {
				if (stats.getPointsToSpend() > 0) {
					stats.incrementStat(health);
					return true;
				}
				return false;
			}

			@Override
			public boolean onSub() {
				if (health.getLevel() > 0) {
					stats.decrementStat(health);
					return true;
				}
				return false;
			}
		});

		Stat damages = stats.getBulletDamage();
		this.damagesLabel = new PUILabel("Damages (" + damages.getLevel() + ")", Fonts.mediumFont, 0, 310);
		this.damagesGauge = new PUIGauge(Color.white, Color.red, damages.getValue(), damages.getMaxValue(),
				damages.getGrowth(), 0, 360, 250, 40);
		
		this.damagesGauge.addGaugeListener(new GaugeListener() {
			@Override
			public boolean onAdd() {
				if (stats.getPointsToSpend() > 0) {
					stats.incrementStat(damages);
					return true;
				}
				return false;
			}

			@Override
			public boolean onSub() {
				if (damages.getLevel() > 0) {
					stats.decrementStat(damages);
					return true;
				}
				return false;
			}
		});

		Stat resistance = stats.getResistance();
		this.resistanceLabel = new PUILabel("Resistance (" + resistance.getLevel() + ")", Fonts.mediumFont, 0, 420);
		this.resistanceGauge = new PUIGauge(Color.white, Color.blue, resistance.getValue(), resistance.getMaxValue(),
				resistance.getGrowth(), 0, 470, 250, 40);
		
		this.resistanceGauge.addGaugeListener(new GaugeListener() {
			@Override
			public boolean onAdd() {
				if (stats.getPointsToSpend() > 0) {
					stats.incrementStat(resistance);
					return true;
				}
				return false;
			}

			@Override
			public boolean onSub() {
				if (resistance.getLevel() > 0) {
					stats.decrementStat(resistance);
					return true;
				}
				return false;
			}
		});

		PUIManager screen = PUIManager.get();
		screen.addView(title);
		screen.addView(pointsToSpendLabel);

		screen.addView(healthLabel);
		screen.addView(healthGauge);

		screen.addView(damagesLabel);
		screen.addView(damagesGauge);

		screen.addView(resistanceLabel);
		screen.addView(resistanceGauge);
		
		screen.addView(returnButton);

		screen.getScreen().centerContentOnX();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		PUIManager.get().render(g);
		pointsToSpendLabel.setText("Points to spend: " + PClient.get().getLocalPlayer().getStats().getPointsToSpend());
		
		Stats stats = PClient.get().getLocalPlayer().getStats();
		this.healthLabel.setText("Health (" + stats.getHealth().getLevel() + ")");
		this.damagesLabel.setText("Damages (" + stats.getBulletDamage().getLevel() + ")");
		this.resistanceLabel.setText("Resistance (" + stats.getResistance().getLevel() + ")");
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		PUIManager.get().update(gc.getInput(), delta);
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		PUIManager.get().clearScreen();
	}

	@Override
	public int getID() {
		return States.CHARACTER_MENU;
	}

}
