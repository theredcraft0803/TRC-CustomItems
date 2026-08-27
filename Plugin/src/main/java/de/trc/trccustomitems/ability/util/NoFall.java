package de.trc.trccustomitems.ability.util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NoFall implements Listener {
    private static final Map<UUID, Long> players = new HashMap<>();
    private static final long MAX_DURATION = 15000L;

    public static void start(Player player) {
        players.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public static void stop(Player player) {
        players.remove(player.getUniqueId());
    }

    public static boolean isActive(Player player) {
        Long startTime = players.get(player.getUniqueId());

        if (startTime == null) return false;

        if (System.currentTimeMillis() - startTime >= MAX_DURATION) {
            players.remove(player.getUniqueId());
            return false;
        }

        return true;
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        Long startTime = players.get(player.getUniqueId());
        if (startTime == null) return;

        if (System.currentTimeMillis() - startTime >= MAX_DURATION) {
            players.remove(player.getUniqueId());
            return;
        }

        event.setCancelled(true);
        players.remove(player.getUniqueId());
    }
}
