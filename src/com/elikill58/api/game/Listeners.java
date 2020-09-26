package com.elikill58.api.game;

import static com.elikill58.api.game.GameAPI.ACTIVE_PHASE;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.elikill58.api.Messages;
import com.elikill58.api.PlayerData;
import com.elikill58.api.data.DataManager;

class Listeners implements Listener {

	private final Game<?> game;
	
    public Listeners(Game<?> game) {
    	this.game = game;
	}

	@EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(game.properties.forcedGamemode != null)
            event.getPlayer().setGameMode(game.properties.forcedGamemode);

        if(ACTIVE_PHASE != null)
        	ACTIVE_PHASE.onJoin(event);
        Booster.addBooster(event.getPlayer());
    }

    @EventHandler
    public void handleLeft(PlayerQuitEvent event) {
        if(ACTIVE_PHASE != null)
            ACTIVE_PHASE.onLeft(event);

        Booster.recalculateBooster();
		DataManager.getData().saveData(PlayerData.getPlayerData(event.getPlayer()));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(game.properties.disableBlockBreak) {
            event.setCancelled(true);
        } else if (ACTIVE_PHASE != null) {
            ACTIVE_PHASE.onBlockBreak(event);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(game.properties.disableBlockPlace) {
            event.setCancelled(true);
        } else if(ACTIVE_PHASE != null) {
            ACTIVE_PHASE.onBlockPlace(event);
        }
    }

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent event) {
        if((event.getDamager() instanceof Player) && (event.getEntity() instanceof Player) && !game.properties.hasPvp)
            event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLoss(FoodLevelChangeEvent event) {
        if(!game.properties.foodLoss)
            event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL
                && (game.properties.disableFallDamages || game.properties.disableAllDamages)) {
            event.setCancelled(true);
        } else {
            ACTIVE_PHASE.onEntityDamage(event);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(!game.properties.canDrop)
            event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!game.properties.canModifyInventories) {
            event.setCancelled(true);
        } else {
            ACTIVE_PHASE.onInventoryClick(event);
        }
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if(game.properties.disableNaturalSpawning && event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM)
            event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
    	if(ACTIVE_PHASE != null)
    		ACTIVE_PHASE.onInteract(event);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
    	if(ACTIVE_PHASE != null)
    		ACTIVE_PHASE.onPlayerDeath(event);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
    	if(ACTIVE_PHASE != null)
    		ACTIVE_PHASE.onEntityDeath(event);
    }
    
    @EventHandler
    public void onPing(ServerListPingEvent e) {
    	if(game.motd() != "")
    		e.setMotd(game.motd());
    }
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
    	e.setFormat(Messages.getMessage("chat", "%name%", e.getPlayer().getName(), "%message%", e.getMessage()));
    }
    
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
    	if(game.properties.blockReload && e.getMessage().startsWith("/reload")) {
    		e.setCancelled(true);
    		e.setMessage(ChatColor.RED + "The reload command is disabled by ColorGame ! Please, restart your server and NOT use reload.");
    	}
    }
    
    @EventHandler
    public void onConsoleCommand(ServerCommandEvent e) {
    	if(game.properties.blockReload && e.getCommand().startsWith("reload")) {
    		e.setCancelled(true);
    		e.getSender().sendMessage(ChatColor.RED + "The reload command is disabled by ColorGame ! Please, restart your server and NOT use reload.");
    	}
    }
    
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (game.properties.clearWeather && event.toWeatherState()) {
            event.setCancelled(true);
            World world = event.getWorld();
            world.setStorm(false);
            world.setThundering(false);
            world.setWeatherDuration(0);
        }
    }
}
