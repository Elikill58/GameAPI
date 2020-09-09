package com.elikill58.api.game;

import org.bukkit.Bukkit;

import com.elikill58.api.Messages;
import com.elikill58.api.UniversalUtils;
import com.elikill58.api.data.DataManager;
import com.elikill58.api.game.phase.Phase;
import com.elikill58.api.inventories.InventoryManager;
import com.elikill58.api.scoreboard.ScoreboardManager;
import com.elikill58.api.utils.Utils;

public final class GameAPI {
	
	public static GameProvider GAME_PROVIDER;

	public static Phase ACTIVE_PHASE;
	public static Game<?> ACTIVE_GAME;
	private static ScoreboardManager scoreboardManager;

	public static void enable(GameProvider gameProvider, Game<?> game) {

		ACTIVE_GAME = game;
		GAME_PROVIDER = gameProvider;
		InventoryManager.INV.clear();
		Bukkit.getServer().getPluginManager().registerEvents(new InventoryManager(), gameProvider.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new Listeners(game), gameProvider.getPlugin());
		scoreboardManager = new ScoreboardManager(gameProvider.getPlugin());

		try {
			String dir = Phase.class.getProtectionDomain().getCodeSource().getLocation().getFile().replaceAll("%20", " ");
			if (dir.endsWith(".class"))
				dir = dir.substring(0, dir.lastIndexOf('!'));

			if (dir.startsWith("file:/"))
				dir = dir.substring(UniversalUtils.getOs() == UniversalUtils.OS.LINUX ? 5 : 6);

			for (Object classDir : UniversalUtils.getClasseNamesInPackage(dir, "com.elikill58." + game.name().toLowerCase() + ".phases")) {
				try {
					Phase phase = (Phase) Class.forName(classDir.toString().replaceAll(".class", "")).newInstance();
					game.phases.register(phase);
				} catch (Exception temp) {}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		DataManager.init(GAME_PROVIDER);
		Messages.load(GAME_PROVIDER.getPlugin());
		startPhase(ACTIVE_GAME.lobbyPhase());
	}

	public static void startPhase(Phase phase) {
		if (ACTIVE_PHASE != null)
			ACTIVE_PHASE.onEnd();

		(ACTIVE_PHASE = phase).onStart();
		scoreboardManager.getScoreboards().forEach((uuid, ps) -> ps.resetScoreboard());
		
	}

	public static void broadcast(String key, Object... placeholders) {
		Bukkit.broadcastMessage(Utils.applyColorCodes(ACTIVE_GAME.prefix() + " " + Messages.getMessage(key, placeholders)));
	}

	public static void broadcastList(String key, Object... placeholders) {
		for(String s : Messages.getMessageList(key, placeholders))
			Bukkit.broadcastMessage(Utils.applyColorCodes(ACTIVE_GAME.prefix() + " " + s));
	}

	public static void broadcastListWithoutPrefix(String key, Object... placeholders) {
		for(String s : Messages.getMessageList(key, placeholders))
			Bukkit.broadcastMessage(Utils.applyColorCodes(s));
	}

	public static void disable() {
		scoreboardManager.onDisable();
	}
	
}
