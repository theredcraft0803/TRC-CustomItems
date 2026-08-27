package de.trc.trccustomitems.cooldown;

import de.trc.trccustomitems.ability.ThunderBladeAbility;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ThunderBladeCooldown {
    private static final Map<UUID, Map<ThunderBladeAbility, Long>> cooldowns = new HashMap<>();

    public static boolean isOnCooldown(Player player, ThunderBladeAbility ability) {
        Map<ThunderBladeAbility, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) return false;

        Long endTime = playerCooldowns.get(ability);
        if (endTime == null) return false;

        return System.currentTimeMillis() < endTime;
    }

    public static double getRemainingSeconds(Player player, ThunderBladeAbility ability) {
        Map<ThunderBladeAbility, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) return 0.0;

        Long endTime = playerCooldowns.get(ability);
        if (endTime == null) return 0.0;

        long remaining = endTime - System.currentTimeMillis();
        return Math.max(0, Math.round(remaining / 100.0) / 10.0);
    }

    public static void setCooldown(Player player, ThunderBladeAbility ability, long durationMillis) {
        cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>())
                .put(ability, System.currentTimeMillis() + durationMillis);
    }
}
