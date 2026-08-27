package de.trc.trccustomitems.ability.util;

import de.trc.trccustomitems.Config;
import de.trc.trccustomitems.TRCCustomItems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ThunderBladeStorage {
    private static final Map<UUID, Long> ULTIMATE_PLAYERS = new HashMap<>();
    private static final Map<UUID, Long> SLAM_PLAYERS = new HashMap<>();
    private static final Map<UUID, UUID> SLAM_PAIRS = new HashMap<>();
    private static final Map<UUID, Integer> COMBO_COUNTS = new HashMap<>();
    private static final int OVERDRIVE_TIME = Config.THUNDER_BLADE_ABILITY_ULTIMATE_SEC;

    public static void addPlayerUlt(Player player) {
        UUID uuid = player.getUniqueId();

        ULTIMATE_PLAYERS.put(uuid, System.currentTimeMillis());

        Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(TRCCustomItems.class), () -> {
            ULTIMATE_PLAYERS.remove(uuid);
        }, OVERDRIVE_TIME * 20L);
    }

    public static boolean hasPlayerUlt(Player player) {
        UUID uuid = player.getUniqueId();
        return ULTIMATE_PLAYERS.containsKey(uuid);
    }


    public static void addPlayerSlam(Player player) {
        UUID uuid = player.getUniqueId();
        SLAM_PLAYERS.put(uuid, System.currentTimeMillis());
    }

    public static void removePlayerSlam(Player player) {
        UUID uuid = player.getUniqueId();
        SLAM_PLAYERS.remove(uuid);
    }

    public static boolean hasPlayerSlam(Player player) {
        UUID uuid = player.getUniqueId();
        return SLAM_PLAYERS.containsKey(uuid);
    }


    public static void addPlayerSlamPair(Player player, Player target) {
        UUID uuidPlayer = player.getUniqueId();
        UUID uuidTarget = target.getUniqueId();
        SLAM_PAIRS.put(uuidPlayer, uuidTarget);
    }

    public static void removePlayerSlamPair(Player player) {
        UUID uuidPlayer = player.getUniqueId();
        SLAM_PAIRS.remove(uuidPlayer);
    }

    public static boolean hasPlayerSlamPair(Player player) {
        UUID uuidPlayer = player.getUniqueId();
        return SLAM_PAIRS.containsKey(uuidPlayer);
    }

    public static Player getPlayerSlamPair(Player player) {
        UUID uuid = player.getUniqueId();
        UUID uuidTarget = SLAM_PAIRS.get(uuid);
        if (uuidTarget == null) return null;
        return Bukkit.getPlayer(uuidTarget);
    }


    public static int addComboHit(Player player) {
        UUID uuid = player.getUniqueId();
        int newCount = COMBO_COUNTS.getOrDefault(uuid, 0) + 1;
        COMBO_COUNTS.put(uuid, newCount);
        return newCount;
    }

    public static void resetCombo(Player player) {
        UUID uuid = player.getUniqueId();
        COMBO_COUNTS.put(uuid, 0);
    }

    public static void clearCombo(Player player) {
        UUID uuid = player.getUniqueId();
        COMBO_COUNTS.remove(uuid);
    }
}
