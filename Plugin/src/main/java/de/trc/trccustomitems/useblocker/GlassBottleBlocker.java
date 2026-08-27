package de.trc.trccustomitems.useblocker;

import de.trc.trccustomitems.item.LightningBottleItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GlassBottleBlocker implements Listener {
    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (LightningBottleItem.isLightningBottle(player.getInventory().getItemInMainHand())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        boolean hasLightningBottle = false;
        boolean hasHoneyBlock = false;

        for (ItemStack item : event.getInventory().getMatrix()) {
            if (item == null) continue;

            if (LightningBottleItem.isLightningBottle(item)) {
                hasLightningBottle = true;
            }

            if (item.getType() == Material.HONEY_BLOCK) {
                hasHoneyBlock = true;
            }
        }

        if (hasLightningBottle && hasHoneyBlock) {
            event.getInventory().setResult(null);
        }
    }
}
