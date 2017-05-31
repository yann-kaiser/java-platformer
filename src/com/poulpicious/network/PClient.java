package com.poulpicious.network;

import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.poulpicious.entity.Player;
import com.poulpicious.entity.Stats;
import com.poulpicious.network.packets.Packet00Login;
import com.poulpicious.network.packets.Packet01LoginAcknowledge;
import com.poulpicious.network.packets.Packet02CharacterInfos;
import com.poulpicious.network.packets.Packet03CharacterInfosResponse;
import com.poulpicious.network.packets.Packet04RequestMatchmaking;
import com.poulpicious.network.packets.Packet05MatchmakingResponse;
import com.poulpicious.network.packets.Packet06StopMatchmaking;
import com.poulpicious.network.packets.Packet07PlayerJoin;
import com.poulpicious.network.packets.Packet08PlayerReady;
import com.poulpicious.network.packets.Packet09StartGame;
import com.poulpicious.network.packets.Packet10PlayerDisconnect;
import com.poulpicious.network.packets.Packet11PlayerUpdate;
import com.poulpicious.network.packets.Packet12PlayerShoot;
import com.poulpicious.network.packets.Packet13PlayerDeath;

/**
 * 
 * @author yann
 *
 * This is the class responsible for the network communication.
 */
public class PClient {

	private static class PClientHolder {
		private static final PClient _instance = new PClient();
	}

	public static PClient get() {
		return PClientHolder._instance;
	}
	
	public interface NetworkActionListener {
		void onAction();
	}

	private Client client;
	
	private NetworkActionListener authenticationListener;

	private boolean searchingGame;
	private boolean authenticated;
	private boolean canStartGame;

	private Room room;

	private String username;

	private ArrayList<ServerListener> listeners;
	
	private Player localPlayer;
	private Stats selectedStats;

	private PClient() {
		
	}
	
	public void init() {
		this.client = new Client();
		this.client.addListener(new PNetworkListener(this));

		this.listeners = new ArrayList<ServerListener>();
		
		this.localPlayer = new Player("", 0, 0, 20, 84);
		
		this.searchingGame = false;
		this.authenticated = false;

		Kryo k = this.client.getKryo();
		k.register(Packet00Login.class);
		k.register(Packet01LoginAcknowledge.class);
		k.register(Packet02CharacterInfos.class);
		k.register(Packet03CharacterInfosResponse.class);
		k.register(Packet04RequestMatchmaking.class);
		k.register(Packet05MatchmakingResponse.class);
		k.register(Packet06StopMatchmaking.class);
		k.register(Packet07PlayerJoin.class);
		k.register(Packet08PlayerReady.class);
		k.register(Packet09StartGame.class);
		k.register(Packet10PlayerDisconnect.class);
		k.register(Packet11PlayerUpdate.class);
		k.register(Packet12PlayerShoot.class);
		k.register(Packet13PlayerDeath.class);
	}

	public void connectToMaster(String address, String username, String password, NetworkActionListener netActionListener) {
		try {
			this.client.start();
			this.client.connect(5000, address.equals("") ? "localhost" : address, 25565, 25565);

			// The next lines show how to send a packet through network. It is really made easy with KryoNet.
			Packet00Login pl = new Packet00Login();
			pl.username = username;
			pl.password = password; // This password could be encrypted here.

			this.client.sendTCP(pl);
			this.authenticationListener = netActionListener;
		} catch (IOException e) {
			System.out.println("Couldn't connect to master server.");
		}
	}

	public void startGameSearchState() {
		this.searchingGame = true;

		Packet04RequestMatchmaking prm = new Packet04RequestMatchmaking();
		// TODO: Setup requests.
		this.client.sendTCP(prm);
	}

	public void stopGameSearchState() {
		Packet06StopMatchmaking psm = new Packet06StopMatchmaking();
		this.client.sendTCP(psm);
	}

	public void sendPlayerReady(boolean ready) {
		Packet08PlayerReady ppr = new Packet08PlayerReady();
		ppr.ready = ready;

		this.client.sendTCP(ppr);
	}

	public void addServerListener(ServerListener sl) {
		this.listeners.add(sl);
	}

	public void removeServerListener(ServerListener sl) {
		this.listeners.remove(sl);
	}

	public void callOnPlayerJoined(ServerPlayer player) {
		for (ServerListener sl : listeners) {
			sl.onPlayerJoinRoom(player);
		}
	}
	

	public void callOnPlayerQuit(ServerPlayer player) {
		for (ServerListener sl : listeners) {
			sl.onPlayerQuitRoom(player);
		}
	}


	public boolean isConnected() {
		return client.isConnected();
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void authenticate(String username) {
		this.authenticated = true;
		this.username = username;
		this.localPlayer.setName(username);
		this.authenticationListener.onAction();
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public boolean isSearchingGame() {
		return searchingGame;
	}

	public void setSearchingGame(boolean searchingGame) {
		this.searchingGame = searchingGame;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isCanStartGame() {
		return canStartGame;
	}

	public void setCanStartGame(boolean canStartGame) {
		this.canStartGame = canStartGame;

		if (canStartGame) {
			for (ServerListener sl : listeners)
				sl.onStartGame();
		}
	}

	public void sendPlayerPosition(float x, float y) {
		Packet11PlayerUpdate ppp = new Packet11PlayerUpdate();
		ppp.id = 0;
		ppp.health = localPlayer.getStats().getHealth().getValue();
		ppp.x = x;
		ppp.y = y;
		
		client.sendUDP(ppp);
	}

	public ServerPlayer getPlayer(int id) {
		return this.getRoom().getPlayers().get(id);
	}

	public void sendPlayerShoot(Vector2f centerPos, Vector2f direction) {
		Packet12PlayerShoot pps = new Packet12PlayerShoot();
		pps.id = client.getID();
		pps.originX = centerPos.x;
		pps.originY = centerPos.y;
		pps.directionX = direction.x;
		pps.directionY = direction.y;
		
		client.sendTCP(pps);
	}

	public Player getLocalPlayer() {
		return localPlayer;
	}

	public void setLocalPlayer(Player localPlayer) {
		this.localPlayer = localPlayer;
	}

	public Stats getSelectedStats() {
		return selectedStats;
	}

	public void setSelectedStats(Stats selectedStats) {
		this.selectedStats = selectedStats;
	}

	public void saveCharacterInfos() {
		Packet02CharacterInfos pci = new Packet02CharacterInfos();
		pci.level = localPlayer.getStats().getLevel();
		pci.experience = localPlayer.getStats().getExperience();
		pci.healthPoints = localPlayer.getStats().getHealth().getLevel();
		pci.damagePoints = localPlayer.getStats().getBulletDamage().getLevel();
		pci.resistancePoints = localPlayer.getStats().getResistance().getLevel();
		
		this.client.sendTCP(pci);
	}
}
