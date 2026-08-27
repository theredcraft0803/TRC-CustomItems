package de.trc.trccustomitems.item;

import de.trc.trccustomitems.key.DiamondLightningRodKeys;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class DiamondLightningRodItem {
    public static ItemStack getDiamondLightningRodItem() {
        ItemStack DIAMOND_LIGHTNING_ROD_ITEM = new ItemStack(Material.LIGHTNING_ROD);
        ItemMeta DIAMOND_LIGHTNING_ROD_META = DIAMOND_LIGHTNING_ROD_ITEM.getItemMeta();
        CustomModelDataComponent DIAMOND_LIGHTNING_ROD_MODEL = DIAMOND_LIGHTNING_ROD_META.getCustomModelDataComponent();

        DIAMOND_LIGHTNING_ROD_META.customName(Component.text("Diamond Lightning Rod", TextColor.color(0xE5FF)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        DIAMOND_LIGHTNING_ROD_META.getPersistentDataContainer().set(DiamondLightningRodKeys.DIAMOND_LIGHTNING_ROD_IS_ITEM, PersistentDataType.BOOLEAN, true);

        DIAMOND_LIGHTNING_ROD_MODEL.setStrings(List.of("diamond_lightning_rod"));

        DIAMOND_LIGHTNING_ROD_META.setCustomModelDataComponent(DIAMOND_LIGHTNING_ROD_MODEL);
        DIAMOND_LIGHTNING_ROD_ITEM.setItemMeta(DIAMOND_LIGHTNING_ROD_META);
        return DIAMOND_LIGHTNING_ROD_ITEM;
    }

    public static boolean isDiamondLightningRod(ItemStack item) {
        if (item == null || item.getType().isAir()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        return meta.getPersistentDataContainer().has(
                DiamondLightningRodKeys.DIAMOND_LIGHTNING_ROD_IS_ITEM,
                PersistentDataType.BOOLEAN
        );
    }
}
