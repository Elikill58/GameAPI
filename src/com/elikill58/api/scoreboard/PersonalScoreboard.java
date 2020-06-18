package com.elikill58.api.scoreboard;

import static com.elikill58.api.game.GameAPI.ACTIVE_PHASE;

import org.bukkit.entity.Player;

import com.elikill58.api.Messages;;

public class PersonalScoreboard {
	
	private final Player player;
	private final ObjectiveSign objectiveSign;

	public PersonalScoreboard(Player player) {
		this.player = player;
		objectiveSign = new ObjectiveSign("sidebar", "EliGameAPI");

		objectiveSign.addReceiver(player);
		objectiveSign.setDisplayName(Messages.getMessage("scoreboard.name"));
	}

	public void setLines() {
		if(ACTIVE_PHASE != null)
			ACTIVE_PHASE.setScoreboardLines(player, objectiveSign);
		
		objectiveSign.updateLines();
	}

	public void onLogout() {
		objectiveSign.removeReceiver(player);
	}

	public Player getPlayer() {
		return player;
	}

	public void resetScoreboard() {
		objectiveSign.resetScoreboard();
	}
}