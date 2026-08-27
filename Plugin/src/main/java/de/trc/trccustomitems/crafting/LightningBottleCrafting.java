package de.trc.trccustomitems.crafting;

import de.trc.trccustomitems.TRCCustomItems;
import de.trc.trccustomitems.item.LightningBottleItem;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.DecoratedPot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class LightningBottleCrafting implements Listener {

    @EventHandler
    public void onLightningStrike(LightningStrikeEvent event) {

        Location location = event.getLightning().getLocation();
        World world = location.getWorld();

        if (world == null) return;

        Block lightningRod = world.getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        if (lightningRod.getType() != Material.LIGHTNING_ROD && lightningRod.getType() != Material.WAXED_LIGHTNING_ROD) return;

        Block potBlock = lightningRod.getRelative(0, -1, 0);
        if (!(potBlock.getState() instanceof DecoratedPot pot)) return;

        Inventory inventory = pot.getInventory();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);

            if (item != null && item.getType() == Material.GLASS_BOTTLE) {
                item.setAmount(item.getAmount() - 1);

                if (item.getAmount() <= 0) {
                    inventory.setItem(i, null);
                }

                break;
            }
        }

        Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(TRCCustomItems.class), () -> {
            Location dropLocation = pot.getLocation().add(0.5, 0, 0.5);
            pot.getWorld().dropItem(dropLocation, LightningBottleItem.getLightningBottleItem());
        }, 20L);
    }
}
