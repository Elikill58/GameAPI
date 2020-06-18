package com.elikill58.api.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.elikill58.api.Version;
import com.elikill58.api.utils.PacketUtils;

/*
 * This file is part of SamaGamesAPI.
 *
 * SamaGamesAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SamaGamesAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SamaGamesAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
public class VObjective {
	
	protected String name;
	protected String displayName;
	protected ObjectiveLocation location = ObjectiveLocation.SIDEBAR;
	protected List<OfflinePlayer> receivers;
	protected ConcurrentLinkedQueue<VScore> scores;

	/**
	 * Constructor
	 *
	 * @param name
	 *            Objective's name
	 * @param displayName
	 *            Objective's display name
	 */
	public VObjective(String name, String displayName) {
		this.receivers = new ArrayList<>();
		this.scores = new ConcurrentLinkedQueue<>();
		this.name = name;
		this.displayName = displayName;
	}

	/**
	 * Set objective location
	 *
	 * @param location
	 *            Location
	 */
	public void setLocation(ObjectiveLocation location) {
		this.location = location;
	}

	/**
	 * Update objective's name
	 *
	 * @return Old name
	 */
	protected String toggleName() {
		String old = this.name;

		if (this.name.endsWith("1"))
			this.name = this.name.substring(0, this.name.length() - 1);
		else
			this.name += "1";

		return old;
	}

	/**
	 * Add a receiver to the objective
	 *
	 * @param offlinePlayer
	 *            Player
	 */
	public boolean addReceiver(OfflinePlayer offlinePlayer) {
		if (!offlinePlayer.isOnline())
			return false;

		this.receivers.add(offlinePlayer);

		Player p = offlinePlayer.getPlayer();

		this.init(p);
		this.updateScore(p);

		return true;
	}

	/**
	 * Remove a receiver from the objective
	 *
	 * @param offlinePlayer
	 *            Player
	 */
	public void removeReceiver(OfflinePlayer offlinePlayer) {
		this.receivers.remove(offlinePlayer);

		if (offlinePlayer.isOnline())
			this.remove(offlinePlayer.getPlayer());
	}

	/**
	 * Initialize the objective for a given player
	 *
	 * @param receiver
	 *            Player
	 */
	public void init(Player receiver) {
		this.create(receiver);
		this.displayTo(receiver, this.location.getLocation());
	}

	/**
	 * Create the objective of a given player
	 *
	 * @param receiver
	 *            Player
	 */
	protected void create(Player receiver) {
		RawObjective.createObjective(receiver, this);
	}

	/**
	 * Send the objective to a given player at the given location
	 *
	 * @param receiver
	 *            Player
	 * @param location
	 *            Location
	 */
	protected void displayTo(Player receiver, int location) {
		RawObjective.displayObjective(receiver, getName(), location);
	}

	/**
	 * Remove a given receiver from the objective
	 *
	 * @param receiver
	 *            Player
	 */
	protected void remove(Player receiver) {
		RawObjective.removeObjective(receiver, this);
	}

	/**
	 * Update a given score
	 *
	 * @param score
	 *            Score
	 */
	protected void updateScore(VScore score) {
		this.receivers.stream().filter(OfflinePlayer::isOnline)
				.forEach(op -> RawObjective.updateScoreObjective(op.getPlayer(), this, score));
	}

	/**
	 * Get objective location
	 *
	 * @return Location
	 */
	public ObjectiveLocation getLocation() {
		return this.location;
	}

	/**
	 * Update given players score
	 *
	 * @param p
	 *            Player
	 */
	protected void updateScore(Player p) {
		RawObjective.updateScoreObjective(p, this, false);
	}

	/**
	 * Update given players score
	 *
	 * @param p
	 *            Player
	 * @param inverse
	 *            Inverse lines
	 */
	protected void updateScore(Player p, boolean inverse) {
		RawObjective.updateScoreObjective(p, this, inverse);
	}

	/**
	 * Remove a given score from the objective
	 *
	 * @param score
	 *            Score
	 */
	public void removeScore(VScore score) {
		this.scores.remove(score);
		this.receivers.stream().filter(OfflinePlayer::isOnline)
				.forEach(op -> RawObjective.removeScoreObjective(op.getPlayer(), this, score));
	}

	/**
	 * Get objective's score of a given player
	 *
	 * @param player
	 *            Player
	 *
	 * @return Player's score
	 */
	public VScore getScore(String player) {
		for (VScore score : this.scores)
			if (score.getPlayerName().equals(player))
				return score;

		VScore score = new VScore(player, 0);
		this.scores.add(score);

		return score;
	}
	
	public void resetScoreboard() {
		this.scores.forEach((vs) -> {
			removeScore(vs);
		});
	}

	/**
	 * Get objective's scores
	 *
	 * @return Scores
	 */
	public ConcurrentLinkedQueue<VScore> getScores() {
		return this.scores;
	}

	/**
	 * Get objective's name
	 *
	 * @return Name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get objective's display name
	 *
	 * @return Display name
	 */
	public String getDisplayName() {
		return this.displayName;
	}

	/**
	 * Set objective's display name
	 *
	 * @param displayName
	 *            Display name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public enum ObjectiveLocation {
		LIST(0), SIDEBAR(1), BELOWNAME(2);

		private final int location;

		ObjectiveLocation(int location) {
			this.location = location;
		}

		public int getLocation() {
			return this.location;
		}
	}

	public static class RawObjective {
		/* Objective Management */

		public static void createObjective(Player p, VObjective objective) {
			PacketUtils.sendPacket(p, makeScoreboardObjectivePacket(0, objective.getName(), objective.getDisplayName(),
					PacketUtils.SCOREBOARD_HEALTH_DISPLAY));
		}

		public static void updateObjective(Player p, VObjective objective) {
			PacketUtils.sendPacket(p, makeScoreboardObjectivePacket(2, objective.getName(), objective.getDisplayName(),
					PacketUtils.SCOREBOARD_HEALTH_DISPLAY));
		}

		public static void removeObjective(Player p, VObjective objective) {
			PacketUtils.sendPacket(p, makeScoreboardObjectivePacket(1, objective.getName(), objective.getDisplayName(),
					PacketUtils.SCOREBOARD_HEALTH_DISPLAY));
		}

		public static void removeObjective(Player p, String name) {
			PacketUtils.sendPacket(p, makeScoreboardObjectivePacket(1, name, "", PacketUtils.SCOREBOARD_HEALTH_DISPLAY));
		}

		/* Objective Display */

		public static void displayObjective(Player p, String name, int location) {
			PacketUtils.sendPacket(p, makeScoreboardDisplayPacket(name, location));
		}

		public static void updateScoreObjective(Player p, VObjective objective, boolean inverse) {
			if (!inverse) {
				for (VScore score : objective.getScores())
					updateScoreObjective(p, objective, score);

				return;
			}

			for (VScore score : objective.getScores())
				updateScoreObjective(p, objective, score, objective.getScores().size() - score.getScore() - 1);
		}

		public static void updateScoreObjective(Player p, VObjective objective, VScore score) {
			PacketUtils.sendPacket(p, makeScoreboardScorePacket(objective.getName(),
					PacketUtils.SCOREBOARD_ACTION_CHANGE, score.getPlayerName(), score.getScore()));
		}

		public static void updateScoreObjective(Player p, VObjective objective, VScore score, int scoreValue) {
			PacketUtils.sendPacket(p, makeScoreboardScorePacket(objective.getName(),
					PacketUtils.SCOREBOARD_ACTION_CHANGE, score.getPlayerName(), scoreValue));
		}

		public static void removeScoreObjective(Player p, VObjective objective, VScore score) {
			PacketUtils.sendPacket(p, makeScoreboardScorePacket(objective.getName(),
					PacketUtils.SCOREBOARD_ACTION_REMOVE, score.getPlayerName(), 0));
		}

		public static Object makeScoreboardScorePacket(String objectiveName, Object action,
				String scoreName, int scoreValue) {
			if (objectiveName == null)
				objectiveName = "";

			try {
				Object packet = PacketUtils.createPacket("PacketPlayOutScoreboardScore");

				Reflection.setValue(packet, "a", scoreName); // Nom du joueur
				Reflection.setValue(packet, "b", objectiveName); // Nom de l'objective
				Reflection.setValue(packet, "c", scoreValue); // Valeur du score
				Reflection.setValue(packet, "d", action); // Action du packet

				return packet;
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}

			return null;
		}

		public static Object makeScoreboardObjectivePacket(int action, String objectiveName,
				String objectiveDisplayName, Object format) {
			try {
				Object packet = PacketUtils.createPacket("PacketPlayOutScoreboardObjective");
				
				Object displayObjectivePacket = null;
				if(Version.getVersion().isNewerOrEquals(Version.V1_13)) {
					displayObjectivePacket = PacketUtils.createChatBaseComponent(objectiveDisplayName);
				} else
					displayObjectivePacket = objectiveDisplayName;
				Reflection.setValue(packet, "a", objectiveName); // Objective name
				Reflection.setValue(packet, "b", displayObjectivePacket); // Showed objective name
				Reflection.setValue(packet, "c", format); // Show data
				Reflection.setValue(packet, "d", action); // Action to do - 0: Create 1: Remove 2: Update

				return packet;
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}

			return null;
		}

		public static Object makeScoreboardDisplayPacket(String objectiveName,
				int location) {
			try {
				Object packet = PacketUtils.createPacket("PacketPlayOutScoreboardDisplayObjective");
				// Emplacement de l'objective - 0 = list, 1 = sidebar, 2 = belowName
				Reflection.setValue(packet, "a", location);
				Reflection.setValue(packet, "b", objectiveName); // Nom de l'objective

				return packet;
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}

			return null;
		}
	}

	public class VScore {
		private final String playerName;
		private int score;

		public VScore(String player, int score) {
			this.playerName = player;
			this.score = score;
		}

		public int getScore() {
			return this.score;
		}

		public void setScore(int score) {
			this.score = score;
		}

		public String getPlayerName() {
			return this.playerName;
		}
	}
}