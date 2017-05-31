package com.poulpicious.network;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Room {
	
	private LinkedHashMap<Integer, ServerPlayer> players;
	
	public Room () {
		this.players = new LinkedHashMap<Integer, ServerPlayer>();
	}
	
	public void addPlayer(ServerPlayer player) {
		this.players.put(player.getConnectionID(), player);
	}
	
	public ServerPlayer getPlayer(String username) {
		ServerPlayer res = null;
		for (ServerPlayer sp : this.players.values()) {
			if (sp.getUsername().equals(username)) {
				res = sp;
				break;
			}
		}
		
		return res;
	}
	
	public HashMap<Integer, ServerPlayer> getPlayers() {
		return players;
	}

}
