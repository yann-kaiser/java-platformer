package com.poulpicious.network;

import org.newdawn.slick.geom.Vector2f;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.poulpicious.entity.ServerPlayerEntity;
import com.poulpicious.entity.Stats;
import com.poulpicious.network.packets.Packet01LoginAcknowledge;
import com.poulpicious.network.packets.Packet02CharacterInfos;
import com.poulpicious.network.packets.Packet05MatchmakingResponse;
import com.poulpicious.network.packets.Packet06StopMatchmaking;
import com.poulpicious.network.packets.Packet07PlayerJoin;
import com.poulpicious.network.packets.Packet09StartGame;
import com.poulpicious.network.packets.Packet10PlayerDisconnect;
import com.poulpicious.network.packets.Packet11PlayerUpdate;
import com.poulpicious.network.packets.Packet12PlayerShoot;
import com.poulpicious.network.packets.Packet13PlayerDeath;

public class PNetworkListener extends Listener {

	private PClient client;

	public PNetworkListener(PClient client) {
		this.client = client;
	}

	@Override
	public void connected(Connection c) {
		super.connected(c);
		client.toString();
	}

	/**
	 * This big method handles the packet reception.
	 * It seems really messy, but it is very straight forward. It is just a big 'switch', except with checks of type.
	 */
	@Override
	public void received(Connection c, Object o) {
		if (o instanceof Packet01LoginAcknowledge) {
			Packet01LoginAcknowledge pla = (Packet01LoginAcknowledge) o;

			if (pla.accepted) {
				PClient.get().authenticate(pla.username);
				System.out.println("LOGIN ACCEPTED.");
			} else {
				System.out.println("LOGIN REFUSED.");
			}
		} else if (o instanceof Packet02CharacterInfos) {
			Packet02CharacterInfos pci = (Packet02CharacterInfos) o;

			Stats stats = new Stats(pci.level, pci.experience, pci.healthPoints, pci.damagePoints,
					pci.resistancePoints);

			if (pci.playerID == client.getClient().getID()) {
				PClient.get().setSelectedStats(stats);
				PClient.get().getLocalPlayer().setStats(stats);
			} else {
				ServerPlayer player = this.client.getRoom().getPlayers().get(pci.playerID);
				player.getPlayerEntity().setupHealth(stats.getHealth().getValue());
				player.getPlayerEntity().setStats(stats);
			}
		} else if (o instanceof Packet05MatchmakingResponse) {
			System.out.println("Received response for matchmaking");
			client.setRoom(new Room());
		} else if (o instanceof Packet06StopMatchmaking) {
			this.client.setSearchingGame(false);
		} else if (o instanceof Packet07PlayerJoin) {
			Packet07PlayerJoin ppj = (Packet07PlayerJoin) o;

			if (ppj.id == this.client.getClient().getID())
				return;
			ServerPlayer player = new ServerPlayer(ppj.id, ppj.username);

			this.client.getRoom().addPlayer(player);
			client.callOnPlayerJoined(player);
		} else if (o instanceof Packet09StartGame) {
			Packet02CharacterInfos pci = new Packet02CharacterInfos();
			Stats stats = client.getLocalPlayer().getStats();
			pci.level = stats.getLevel();
			pci.experience = stats.getExperience();
			pci.healthPoints = stats.getHealth().getLevel();
			pci.damagePoints = stats.getBulletDamage().getLevel();
			pci.resistancePoints = stats.getResistance().getLevel();
			pci.playerID = client.getClient().getID();
			pci.save = false;

			c.sendTCP(pci);
			this.client.setCanStartGame(true);
		} else if (o instanceof Packet10PlayerDisconnect) {
			Packet10PlayerDisconnect ppd = (Packet10PlayerDisconnect) o;
			ServerPlayer player = this.client.getRoom().getPlayers().get(ppd.id);
			this.client.getRoom().getPlayers().remove(ppd.id);
			this.client.callOnPlayerQuit(player);
		} else if (o instanceof Packet11PlayerUpdate) {
			Packet11PlayerUpdate ppp = (Packet11PlayerUpdate) o;
			ServerPlayerEntity spe = PClient.get().getPlayer(ppp.id).getPlayerEntity();
			spe.setNextDestination(ppp.x, ppp.y);
			spe.getStats().getHealth().set(ppp.health);
		} else if (o instanceof Packet12PlayerShoot) {
			Packet12PlayerShoot pps = (Packet12PlayerShoot) o;
			Vector2f origin = new Vector2f(pps.originX, pps.originY);
			Vector2f direction = new Vector2f(pps.directionX, pps.directionY);

			PClient.get().getPlayer(pps.id).getPlayerEntity().shoot(origin, direction);
		} else if (o instanceof Packet13PlayerDeath) {
			Packet13PlayerDeath ppd = (Packet13PlayerDeath) o;

			if (ppd.killerID == this.client.getClient().getID()) {
				ServerPlayer player = this.client.getPlayer(ppd.killedID);
				if (player != null && player.getPlayerEntity().getStats() != null)
					this.client.getLocalPlayer().onKill(player.getPlayerEntity().getStats().getLevel());
				else
					this.client.getLocalPlayer().onKill(1);
				this.client.getLocalPlayer().incrementScore();
				return;
			}

			this.client.getPlayer(ppd.killerID).getPlayerEntity().incrementScore();
		}
	}

	@Override
	public void disconnected(Connection c) {
	}

	@Override
	public void idle(Connection c) {
		super.idle(c);
	}
}
