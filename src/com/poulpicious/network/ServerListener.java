package com.poulpicious.network;

public interface ServerListener {

	public default void onPlayerJoinRoom(ServerPlayer player) {
	}

	public default void onPlayerQuitRoom(ServerPlayer player) {
	}

	public default void onStartGame() {
	}
}
