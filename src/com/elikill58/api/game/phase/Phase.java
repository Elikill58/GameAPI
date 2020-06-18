package com.elikill58.api.game.phase;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.elikill58.api.scoreboard.ObjectiveSign;

/**
 * A {@link Phase} represent a part of the game. ex: Lobby, In game, End of the game.<br>
 * These examples are default phases
 * <p>
 * EventHandlers ({@link StartHandler}, {@link JoinHandler}, etc...) are used to player action during the phase
 * To register a new eventHandler, you must to add the method with the @Override annotation
 */
public abstract class Phase {

    /**
     * the phase id and display
     */
    public final String id, display;
    /**
     * True if the current phase affect the booster
     */
    public boolean affectBooster;
    /**
     * True if player can join the game during the phase
     */
    public boolean canJoin;

    public Phase(String id, String displayName, boolean affectBooster, boolean canJoin) {
        this.id = id;
        if(displayName == "")
        	this.display = id;
        else
        	this.display = displayName;
        this.affectBooster = affectBooster;
        this.canJoin = canJoin;
    }

    public void onStart() {}

    public void onEnd() {}
    
    public abstract void setScoreboardLines(Player p, ObjectiveSign sign);

    public void onJoin(PlayerJoinEvent event) {}

    public void onLeft(PlayerQuitEvent event) {}

    public void onBlockBreak(BlockBreakEvent event) {}

    public void onBlockPlace(BlockPlaceEvent event) {}

    public void onInventoryClick(InventoryClickEvent event) {}

    public void onEntityDamage(EntityDamageEvent event) {}

    public void onInteract(PlayerInteractEvent event) {}

    public void onPlayerDeath(PlayerDeathEvent event) {}

    public void onEntityDeath(EntityDeathEvent event) {}
}
