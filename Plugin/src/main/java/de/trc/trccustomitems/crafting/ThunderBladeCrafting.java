package de.trc.trccustomitems.crafting;

import de.trc.trccustomitems.item.DiamondLightningRodItem;
import de.trc.trccustomitems.item.LightningBottleItem;
import de.trc.trccustomitems.item.ThunderBladeItem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class ThunderBladeCrafting implements Listener {
    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        ItemStack[] matrix = inventory.getMatrix();

        if (matrix.length < 9) {
            return;
        }

        boolean hasDiamondLightningRod1 = matrix[0] != null
                && matrix[0].getAmount() == 1
                && DiamondLightningRodItem.isDiamondLightningRod(matrix[0]);

        boolean hasTrident = matrix[1] != null
                && matrix[1].getType() == Material.TRIDENT
                && matrix[1].containsEnchantment(Enchantment.CHANNELING)
                && matrix[1].getAmount() == 1;

        boolean hasDiamondLightningRod2 = matrix[2] != null
                && matrix[2].getAmount() == 1
                && DiamondLightningRodItem.isDiamondLightningRod(matrix[2]);

        boolean hasNethIngot1 = matrix[3] != null
                && matrix[3].getType() == Material.NETHERITE_INGOT
                && matrix[3].getAmount() == 1;

        boolean hasNethSword = matrix[4] != null
                && matrix[4].getType() == Material.NETHERITE_SWORD
                && matrix[4].getAmount() == 1;

        boolean hasNethIngot2 = matrix[5] != null
                && matrix[5].getType() == Material.NETHERITE_INGOT
                && matrix[5].getAmount() == 1;

        boolean hasLightningBottle1 = matrix[6] != null
                && matrix[6].getAmount() == 1
                && LightningBottleItem.isLightningBottle(matrix[6]);

        boolean hasBeacon = matrix[7] != null
                && matrix[7].getType() == Material.BEACON
                && matrix[7].getAmount() == 1;

        boolean hasLightningBottle2 = matrix[8] != null
                && matrix[8].getAmount() == 1
                && LightningBottleItem.isLightningBottle(matrix[8]);

        if (hasDiamondLightningRod1
                && hasTrident
                && hasDiamondLightningRod2
                && hasNethIngot1
                && hasNethSword
                && hasNethIngot2
                && hasLightningBottle1
                && hasBeacon
                && hasLightningBottle2)
            inventory.setResult(ThunderBladeItem.getThunderBladeItem());
    }
}
