package org.main.playtime.other;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.main.playtime.model.ConfigManager;
import org.main.playtime.model.ColorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlaytimeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();
        int playtime = ConfigManager.playTimes.getOrDefault(playerId, 0);

        if (args.length == 0) {
            player.sendMessage(ColorUtils.colorize("&aYour total playtime is: " + ConfigManager.formatPlaytime(playtime)));

        } else if (args.length == 1) {

            Player target = Bukkit.getPlayer(args[0]);
            if (target != null && target.isOnline()) {
                UUID targetId = target.getUniqueId();
                int targetPlaytime = ConfigManager.playTimes.getOrDefault(targetId, 0);
                player.sendMessage(ColorUtils.colorize("&6" + target.getName() + "'s &7total playtime is: " + ConfigManager.formatPlaytime(targetPlaytime)));

            } else {
                player.sendMessage(ColorUtils.colorize("&c&lERROR: &7Player not found or is offline."));
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> onlinePlayers = new ArrayList<>();
            for (Player looped : Bukkit.getOnlinePlayers()) {
                onlinePlayers.add(looped.getName());
            }
            return onlinePlayers;
        }
        return Collections.emptyList();
    }
}
