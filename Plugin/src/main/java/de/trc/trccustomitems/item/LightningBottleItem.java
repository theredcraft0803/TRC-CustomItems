package de.trc.trccustomitems.item;

import de.trc.trccustomitems.key.LightningBottleKeys;

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
public class LightningBottleItem {
    public static ItemStack getLightningBottleItem() {
        ItemStack LIGHTNING_BOTTLE_ITEM = new ItemStack(Material.GLASS_BOTTLE);
        ItemMeta LIGHTNING_BOTTLE_META = LIGHTNING_BOTTLE_ITEM.getItemMeta();
        CustomModelDataComponent LIGHTNING_BOTTLE_MODEL = LIGHTNING_BOTTLE_META.getCustomModelDataComponent();

        LIGHTNING_BOTTLE_META.customName(Component.text("Lightning Bottle", TextColor.color(0x11A4C8)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        LIGHTNING_BOTTLE_META.getPersistentDataContainer().set(LightningBottleKeys.LIGHTNING_BOTTLE_IS_ITEM, PersistentDataType.BOOLEAN, true);

        LIGHTNING_BOTTLE_MODEL.setStrings(List.of("lightning_bottle"));

        LIGHTNING_BOTTLE_META.setCustomModelDataComponent(LIGHTNING_BOTTLE_MODEL);
        LIGHTNING_BOTTLE_ITEM.setItemMeta(LIGHTNING_BOTTLE_META);
        return LIGHTNING_BOTTLE_ITEM;
    }

    public static boolean isLightningBottle(ItemStack item) {
        if (item == null || item.getType().isAir()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        return meta.getPersistentDataContainer().has(
                LightningBottleKeys.LIGHTNING_BOTTLE_IS_ITEM,
                PersistentDataType.BOOLEAN
        );
    }
}
