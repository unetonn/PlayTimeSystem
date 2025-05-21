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
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorUtils.colorize("&cSorry, mortal console. This command is for living players only!"));
            return true;
        }

        UUID playerId = player.getUniqueId();
        int playtime = playTimes.getOrDefault(playerId, 0);

        if (args.length == 0) {
            player.sendMessage(ColorUtils.colorize("&eYour total playtime &a" + ConfigManager.formatPlaytime(playtime)));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target != null && target.isOnline()) {
            UUID targetId = target.getUniqueId();
            int targetPlaytime = playTimes.getOrDefault(targetId, 0);
            player.sendMessage(ColorUtils.colorize("&e" + target.getName() + "'s &aplaytime " + ConfigManager.formatPlaytime(targetPlaytime)));
            return true;

        } else {
            player.sendMessage(ColorUtils.colorize("&c&lWHO?! &7Couldn't find username '" + args[0] + "' such username!"));
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
