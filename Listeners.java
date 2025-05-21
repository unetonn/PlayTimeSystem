package org.main.playtime.other;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.main.playtime.Playtime;
import org.main.playtime.model.ConfigManager;

import java.util.UUID;

import static org.main.playtime.model.ConfigManager.playTimes;

public class Listeners implements Listener {

    public static Playtime plugin;
    public Listeners(Playtime plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        ConfigManager.reload();

        UUID uuid = player.getUniqueId();
        int playtime = playTimes.getOrDefault(uuid, 0);
        playTimes.put(uuid, playtime);
        ConfigManager.get().set("player-playtime." + uuid, playtime);
        ConfigManager.save();
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        UUID uuid = player.getUniqueId();
        int playtime = ConfigManager.get().getInt("player-playtime." + uuid, 0);
        playTimes.put(uuid, playtime);
    }
}


    /*
    
    public static void updateScoreboard(Player player, JavaPlugin plugin) {
        if (ScoreboardUtils.titleTask == null) {
            ScoreboardUtils.startTitleScheduler();
        }
    }

    // ScoreboardUtils.createScoreboard(player);
    // Update scoreboard with the correct playtime from config
    // updateScoreboard(player, Playtime.getInstance());

    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getConfig().contains("hour." + player.getUniqueId())) {
            plugin.getConfig().set("hour." + player.getUniqueId(), 0);
            plugin.getConfig().set("minute." + player.getUniqueId(), 0);
            plugin.saveConfig();
        }
    }
    
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        int playtime = playTimes.getOrDefault(uuid, 0);
        playTimes.put(uuid, playtime);
        ConfigManager.get().set("player-playtime." + uuid, playtime);
    }
    
     */
