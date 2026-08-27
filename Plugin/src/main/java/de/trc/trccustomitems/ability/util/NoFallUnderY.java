package de.trc.trccustomitems.ability.util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NoFallUnderY implements Listener {
    private static final Map<UUID, FallData> players = new HashMap<>();
    private static final long MAX_DURATION = 15000L;

    public static void start(Player player) {
        players.put(player.getUniqueId(), new FallData(player.getY() - 1, System.currentTimeMillis()));
    }

    public static void stop(Player player) {
        players.remove(player.getUniqueId());
    }

    public static boolean isActive(Player player) {
        FallData data = players.get(player.getUniqueId());

        if (data == null) return false;

        if (System.currentTimeMillis() - data.startTime() >= MAX_DURATION) {
            players.remove(player.getUniqueId());
            return false;
        }

        return true;
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        FallData data = players.get(player.getUniqueId());
        if (data == null) return;

        if (System.currentTimeMillis() - data.startTime() >= MAX_DURATION) {
            players.remove(player.getUniqueId());
            return;
        }

        event.setCancelled(true);

        double startY = data.startY();
        double endY = player.getY();

        if (endY < startY) {
            double fallDistance = startY - endY;

            player.damage(fallDistance);
        }

        players.remove(player.getUniqueId());
    }

    private record FallData(double startY, long startTime) {

    }
}
