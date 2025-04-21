package org.main.playtime.model;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.main.playtime.Playtime;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigManager {

    public static Map<UUID, Integer> playTimes = new ConcurrentHashMap<>();
    private static FileConfiguration config;
    private static File configFile;

    public static void setup(Playtime plugin) {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false); // Make sure this file is in the correct location
        }
        reload(); // this class reload method
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static FileConfiguration get() {
        if (config == null) {
            reload();
        }
        return config;
    }

    public static void save() {
        if (config == null || configFile == null) {
            throw new IllegalArgumentException("File or configuration cannot be null. Ensure the configuration is properly initialized.");
        }
        try {
            config.save(configFile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void savePlaytime(FileConfiguration config, UUID uuid) {
        if (playTimes.containsKey(uuid)) {
            config.set("player-playtime." + uuid.toString(), playTimes.get(uuid));
        } else {
            Bukkit.getLogger().warning("Playtime for UUID " + uuid + " not found. Skipping save.");
        }
    }

    public static void loadAll() {
        FileConfiguration config = ConfigManager.get();
        if (config == null) {
            Bukkit.getLogger().warning("Failed to load configuration. Configuration is null.");
            return;
        }

        loadPlayerData(config, playTimes);
        saveSpawnLocation(config);
    }

    private static void loadPlayerData(FileConfiguration config, Map<UUID, Integer> dataMap) {
        ConfigurationSection section = config.getConfigurationSection("player-playtime");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(key);
                    int value = section.getInt(key);
                    dataMap.put(uuid, value);
                } catch (IllegalArgumentException e) {
                    Bukkit.getLogger().warning("Invalid UUID format for key: " + key);
                }
            }
        } else {
            Bukkit.getLogger().info("No player playtime data found.");
        }
    }

    public static String formatPlaytime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return String.format("&e%dh %dm %ds", hours, minutes, seconds);
    }

    public static void saveSpawnLocation(FileConfiguration config) {
        Location spawnLocation = getSpawnLocation();
        if (spawnLocation == null || spawnLocation.getWorld() == null) {
            Bukkit.getLogger().warning("spawn location or world is null. Skipping save.");
            return;
        }

        config.set("spawn-location.world", spawnLocation.getWorld().getName());
        config.set("spawn-location.x", spawnLocation.getX());
        config.set("spawn-location.y", spawnLocation.getY());
        config.set("spawn-location.z", spawnLocation.getZ());
        config.set("spawn-location.yaw", spawnLocation.getYaw());
        config.set("spawn-location.pitch", spawnLocation.getPitch());

        try {
            ConfigManager.save();
        } catch (Exception e) {
            Bukkit.getLogger().severe("failed to save spawn location :(");
            e.printStackTrace();
        }
    }

    public static Location getSpawnLocation() {
        FileConfiguration config = ConfigManager.get();
        if (!config.contains("spawn-location")) {
            return null;
        }
        World world = Bukkit.getWorld("world");
        if (world == null) {
            return null;
        }
        double x = config.getDouble("spawn-location.x");
        double y = config.getDouble("spawn-location.y");
        double z = config.getDouble("spawn-location.z");
        float yaw = (float) config.getDouble("spawn-location.yaw");
        float pitch = (float) config.getDouble("spawn-location.pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }
}
