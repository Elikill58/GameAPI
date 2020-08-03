package com.elikill58.api.game;

import org.bukkit.GameMode;

import javax.annotation.Nullable;

public class GameProperties {

    public boolean hasPvp = true;
    public boolean foodLoss = true;
    public boolean disableAllDamages = false;
    public boolean canDrop = true;
    public boolean canModifyInventories = true;
    public int minPlayers = 1;
    public int maxPlayers = 12;
    public int maxSpecs = 12;
    public boolean disableBlockPlace = false;
    public boolean disableBlockBreak = false;
    @Nullable
    public GameMode forcedGamemode = null;
    public boolean disableNaturalSpawning = false;
    public boolean disableFallDamages = false;
    public boolean blockReload = false;
    public boolean scoreboardEnabled = false;

    /**
     * If the pvp is enabled
     * <p>
     * <b>default: true</b>
     *
     * @param pvp true if the pvp is enabled
     * @return the instance of {@link GameProperties}
     */
    public GameProperties pvp(boolean pvp) {
        this.hasPvp = pvp;
        return this;
    }

    /**
     * If player can lose food
     * <p>
     * <b>default: true</b>
     *
     * @param foodLoss true if player can lose her food
     * @return the instance of {@link GameProperties}
     */
    public GameProperties foodLoss(boolean foodLoss) {
        this.foodLoss = foodLoss;
        return this;
    }

    /**
     * If player can take damage
     * <p>
     * <b>default: false</b>
     *
     * @param disableAllDamages true is we must to disable all damages
     * @return the instance of {@link GameProperties}
     */
    public GameProperties disableAllDamages(boolean disableAllDamages) {
        this.disableAllDamages = disableAllDamages;
        return this;
    }

    /**
     * If player can drop item
     * <p>
     * <b>default: true</b>
     *
     * @param canDrop true if people can drop item
     * @return the instance of {@link GameProperties}
     */
    public GameProperties canDrop(boolean canDrop) {
        this.canDrop = canDrop;
        return this;
    }

    /**
     * If player can modify is own inventory
     * <p>
     * <b>default: true</b>
     *
     * @param canModifyInventories true is a player can move item in inventory
     * @return the instance of {@link GameProperties}
     */
    public GameProperties canModifyInventories(boolean canModifyInventories) {
        this.canModifyInventories = canModifyInventories;
        return this;
    }

    /**
     * The minimum of player need in game
     * <p>
     * <b>default: 0</b>
     *
     * @param minPlayers the minimum of player to start game
     * @return the instance of {@link GameProperties}
     */
    public GameProperties minPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    /**
     * The maximum of player in the game
     * <p>
     * <b>default: 0</b>
     *
     * @param maxPlayers the maximum of player in the game
     * @return the instance of {@link GameProperties}
     */
    public GameProperties maxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    /**
     * If we can place block
     * <p>
     * <b>default: false</b>
     *
     * @param disableBlockPlace true if we can place block
     * @return the instance of {@link GameProperties}
     */
    public GameProperties disableBlockPlace(boolean disableBlockPlace) {
        this.disableBlockPlace = disableBlockPlace;
        return this;
    }

    /**
     * If we can break block
     * <p>
     * <b>default: false</b>
     *
     * @param disableBlockPlace true if we can break block
     * @return the instance of {@link GameProperties}
     */
    public GameProperties disableBlockBreak(boolean disableBlockBreak) {
        this.disableBlockBreak = disableBlockBreak;
        return this;
    }

    /**
     * Define the default gamemode
     * <p>
     * <b>default: null</b>
     * <p>
     * <b>By default it doesn't have any effect</b>
     *
     * @param gamemode the default gamemode which will be set to the player
     * @return the instance of {@link GameProperties}
     */
    public GameProperties forcedGamemode(@Nullable GameMode gamemode) {
        this.forcedGamemode = gamemode;
        return this;
    }

    /**
     * The maximum number of spectator allowed
     * <p>
     * <b>default: 0</b>
     *
     * @param maxSpecs the maximum of spectator allowed
     * @return the instance of {@link GameProperties}
     */
    public GameProperties maxSpecs(int maxSpecs) {
        this.maxSpecs = maxSpecs;
        return this;
    }

    /**
     * Show if the natural spawn of mobs is enabled
     * <p>
     * <b>default: false</b>
     * 
     * @param disableNaturalSpawning true if the entity as mobs don't spawn naturally
     * @return the instance of {@link GameProperties}
     */
    public GameProperties disableNaturalSpawning(boolean disableNaturalSpawning) {
        this.disableNaturalSpawning = disableNaturalSpawning;
        return this;
    }

    /**
     * If fall damage are disabled
     * <p>
     * <b>default: false</b>
     *
     * @param disableFallDamages true if the fall damage is disabled
     * @return the instance of {@link GameProperties}
     */
    public GameProperties disableFallDamages(boolean disableFallDamages) {
        this.disableFallDamages = disableFallDamages;
        return this;
    }

    /**
     * If the reload command is disabled (by console and player, don't work on 1.7)
     * <p>
     * <b>default: false</b>
     *
     * @param disableFallDamages true if the /reload command in blocked
     * @return the instance of {@link GameProperties}
     */
    public GameProperties blockReload(boolean blockReload) {
        this.blockReload = blockReload;
        return this;
    }
    
    /**
     * If the scoreboard is enabled
     * 
     * @param enabled true if the scoreboard will be enabled
     * @return the instance of {@link GameProperties}
     */
    public GameProperties scoreboardEnable(boolean enabled) {
    	this.scoreboardEnabled = enabled;
    	return this;
    }
}
