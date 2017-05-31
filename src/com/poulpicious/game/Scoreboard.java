package com.poulpicious.game;

import java.util.ArrayList;

import org.newdawn.slick.Input;

import com.poulpicious.entity.Entity;
import com.poulpicious.network.PClient;
import com.poulpicious.network.ServerPlayer;
import com.poulpicious.pui.PUIManager;
import com.poulpicious.pui.PUITable;

/**
 * 
 * @author yann
 *
 * This class represents the tableView we can see while playing (when TAB is down).
 */
public class Scoreboard {

	private PUITable scoreboardNames;
	private PUITable scoreboardScores;

	public Scoreboard() {
		this.scoreboardNames = new PUITable(0, 300, 300, 500);
		this.scoreboardScores = new PUITable(0, 300, 40, 500);
	}

	public void addToScreen() {
		ArrayList<Entity> playerList = new ArrayList<Entity>();
		for (ServerPlayer sp : PClient.get().getRoom().getPlayers().values()) {
			playerList.add(sp.getPlayerEntity());
		}

		playerList.add(PClient.get().getLocalPlayer());

		for (Entity sp : playerList) {
			this.scoreboardNames.addCell(sp.getName(), sp.getName());
			this.scoreboardScores.addCell(sp.getName(), sp.getScore() + "");
		}

		PUIManager.get().getScreen().addComponent(scoreboardNames);
		PUIManager.get().getScreen().addComponent(scoreboardScores);
		PUIManager.get().getScreen().centerComponentOnX(scoreboardNames);
		scoreboardScores.getBounds().setX(scoreboardNames.getBounds().getMaxX() + 2);
	}

	public void setVisible(boolean visible) {
		this.scoreboardNames.setVisible(visible);
		this.scoreboardScores.setVisible(visible);
	}

	/**
	 * This method updates the list (items are put in order).
	 * @param input
	 * @param delta
	 */
	public void update(Input input, int delta) {
		int inserted = 0;
		int size = this.scoreboardNames.getCellCount();
		this.scoreboardNames.clearCells();
		this.scoreboardScores.clearCells();

		ArrayList<Entity> playerList = new ArrayList<Entity>();
		for (ServerPlayer sp : PClient.get().getRoom().getPlayers().values()) {
			playerList.add(sp.getPlayerEntity());
		}

		playerList.add(PClient.get().getLocalPlayer());

		while (inserted < size) {
			Entity higher = null;
			for (Entity sp : playerList) {
				if (higher == null || sp.getScore() >= higher.getScore())
					higher = sp;
			}
			if (higher != null) {
				playerList.remove(higher);
				this.scoreboardNames.addCell(higher.getName(), higher.getName());
				this.scoreboardScores.addCell(higher.getName(), higher.getScore() + "");
			}
			inserted++;
		}
	}

}