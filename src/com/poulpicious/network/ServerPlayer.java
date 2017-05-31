package com.poulpicious.network;

import com.poulpicious.entity.ServerPlayerEntity;

/**
 * This class represents a player on the network (not the local one).
 * It contains fields that are used to send packets to specific players (for example private messaging would use it).
 * and contains the entity of this player.
 * @author yann
 *
 */
public class ServerPlayer {

	private int connectionID;
	private String username;
	private ServerPlayerEntity playerEntity;
	
	public ServerPlayer(int id, String username) {
		this.connectionID = id;
		this.username = username;
		this.playerEntity = new ServerPlayerEntity(username, 0, 0, 32, 62);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

	public ServerPlayerEntity getPlayerEntity() {
		return playerEntity;
	}

}