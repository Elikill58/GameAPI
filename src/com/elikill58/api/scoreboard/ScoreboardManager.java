package com.elikill58.api.scoreboard;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.elikill58.api.game.GameAPI;
import com.elikill58.api.utils.Utils;

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
public class ScoreboardManager implements Listener {

	private final ScheduledExecutorService executorMonoThread;
	private final ScheduledExecutorService scheduledExecutorService;
	private final HashMap<UUID, PersonalScoreboard> scoreboards = new HashMap<>();

	public ScoreboardManager(Plugin pl) {
		executorMonoThread = Executors.newScheduledThreadPool(16);
		scheduledExecutorService = Executors.newScheduledThreadPool(1);
		
		pl.getServer().getPluginManager().registerEvents(this, pl);
		
		getScheduledExecutorService().scheduleAtFixedRate(() -> {
			for (PersonalScoreboard scoreboard : scoreboards.values()) {
				getExecutorMonoThread().execute(() -> scoreboard.setLines());
			}
		}, 80, 80, TimeUnit.MILLISECONDS);
		
		Utils.getOnlinePlayers().forEach((p) -> onLogin(new PlayerJoinEvent(p, null)));
	}

	public void onDisable() {
		scoreboards.values().forEach(PersonalScoreboard::onLogout);
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent e) {
		if (scoreboards.containsKey(e.getPlayer().getUniqueId()) || !GameAPI.ACTIVE_GAME.properties.scoreboardEnabled) {
			return;
		}
		scoreboards.put(e.getPlayer().getUniqueId(), new PersonalScoreboard(e.getPlayer()));
	}

	@EventHandler
	public void onLogout(PlayerQuitEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		if (scoreboards.containsKey(uuid)) {
			scoreboards.get(uuid).onLogout();
			scoreboards.remove(uuid);
		}
	}
	
	public ScheduledExecutorService getExecutorMonoThread() {
		return executorMonoThread;
	}

	public ScheduledExecutorService getScheduledExecutorService() {
		return scheduledExecutorService;
	}
	
	public HashMap<UUID, PersonalScoreboard> getScoreboards(){
		return scoreboards;
	}
}