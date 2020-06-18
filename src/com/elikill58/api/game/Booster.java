package com.elikill58.api.game;

import static com.elikill58.api.game.GameAPI.ACTIVE_PHASE;

import org.bukkit.entity.Player;

public class Booster {

    /**
     * The minimum value of booster
     */
    private static final int BOOSTER_FLOOR = 0;
    /**
     * The maximum value of booster
     */
    private static final int BOOSTER_CEIL = 200;

    /**
     * The current value of booster
     */
    public static int BOOSTER = 0;

    /**
     * Add a value to booster is player has perm to do it
     * @param player the player which add a booster
     */
    public static void addBooster(Player player) {
        if(!ACTIVE_PHASE.affectBooster)
            return;

        /*Rank grade = PlayerData.getPlayerData(player).getRank();

        BOOSTER += grade.boost;

        forceBoosterBounds();

        if(grade != Rank.PLAYER)
            GameAPI.broadcast("booster.plus", "%playername%", player.getName(), "%booster%", String.valueOf(BOOSTER));*/
    }

    /**
     * Recalcule the current booster
     */
    public static void recalculateBooster() {
        if(!ACTIVE_PHASE.affectBooster)
            return;

        BOOSTER = 0;

        /*Bukkit.getScheduler().scheduleSyncDelayedTask(GameAPI.GAME_PROVIDER.getPlugin(), () -> {
        	Utils.getOnlinePlayers().forEach(player -> {
                if(player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)
                BOOSTER += PlayerData.getPlayerData(player).getRank().boost - 1;
            });
        }, 20);*/

        forceBoosterBounds();
    }

    /**
     * Check the booster to stay in limit
     */
    private static void forceBoosterBounds() {
        if (BOOSTER < BOOSTER_FLOOR)
            BOOSTER = BOOSTER_FLOOR;

        if(BOOSTER > BOOSTER_CEIL) {
            BOOSTER = BOOSTER_CEIL;
        }
    }

    /**
     * Apply booster to specified number
     *
     * @param coins the coins value on which the booster will be applied
     * @return the coins with booster
     */
    public static int apply(int coins) {
        return coins + ((coins * Booster.BOOSTER) / 100);
    }

    /**
     * Apply booster to specified number
     *
     * @param coins the coins value on which the booster will be applied
     * @return the coins with booster
     */
    public static double apply(double coins) {
        return coins + ((coins * Booster.BOOSTER) / 100);
    }
}
