package de.trc.trccustomitems.crafting;

import de.trc.trccustomitems.item.DiamondLightningRodItem;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class DiamondLightningRodCrafting implements Listener {
    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        ItemStack[] matrix = inventory.getMatrix();

        if (matrix.length < 9) {
            return;
        }

        boolean hasDiamondBlock1 = matrix[0] != null
                && matrix[0].getType() == Material.DIAMOND_BLOCK
                && matrix[0].getAmount() == 1;

        boolean hasNetherStar = matrix[1] != null
                && matrix[1].getType() == Material.NETHER_STAR
                && matrix[1].getAmount() == 1;

        boolean hasDiamondBlock2 = matrix[2] != null
                && matrix[2].getType() == Material.DIAMOND_BLOCK
                && matrix[2].getAmount() == 1;

        boolean hasNethIngot1 = matrix[3] != null
                && matrix[3].getType() == Material.NETHERITE_INGOT
                && matrix[3].getAmount() == 1;

        boolean hasLightningRod1 = matrix[4] != null
                && matrix[4].getType() == Material.LIGHTNING_ROD
                && matrix[4].getAmount() == 1;

        boolean hasNethIngot2 = matrix[5] != null
                && matrix[5].getType() == Material.NETHERITE_INGOT
                && matrix[5].getAmount() == 1;

        boolean hasDiamondBlock3 = matrix[6] != null
                && matrix[6].getType() == Material.DIAMOND_BLOCK
                && matrix[6].getAmount() == 1;

        boolean hasLightningRod2 = matrix[7] != null
                && matrix[7].getType() == Material.LIGHTNING_ROD
                && matrix[7].getAmount() == 1;

        boolean hasDiamondBlock4 = matrix[8] != null
                && matrix[8].getType() == Material.DIAMOND_BLOCK
                && matrix[8].getAmount() == 1;

        if (hasDiamondBlock1
                && hasNetherStar
                && hasDiamondBlock2
                && hasNethIngot1
                && hasLightningRod1
                && hasNethIngot2
                && hasDiamondBlock3
                && hasLightningRod2
                && hasDiamondBlock4)
            inventory.setResult(DiamondLightningRodItem.getDiamondLightningRodItem());
    }
}
