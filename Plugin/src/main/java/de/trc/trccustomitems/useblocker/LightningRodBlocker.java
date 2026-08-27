package de.trc.trccustomitems.useblocker;

import de.trc.trccustomitems.item.DiamondLightningRodItem;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class LightningRodBlocker implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();

        if (DiamondLightningRodItem.isDiamondLightningRod(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        boolean hasDiamondLightningRod = false;
        boolean hasHoneyComb = false;

        for (ItemStack item : event.getInventory().getMatrix()) {
            if (item == null) continue;

            if (DiamondLightningRodItem.isDiamondLightningRod(item)) {
                hasDiamondLightningRod = true;
            }

            if (item.getType() == Material.HONEYCOMB) {
                hasHoneyComb = true;
            }
        }

        if (hasDiamondLightningRod && hasHoneyComb) {
            event.getInventory().setResult(null);
        }
    }
}
