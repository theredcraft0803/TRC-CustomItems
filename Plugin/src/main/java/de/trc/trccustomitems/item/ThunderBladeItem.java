package de.trc.trccustomitems.item;

import de.trc.trccustomitems.key.ThunderBladeKeys;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

@SuppressWarnings({"UnstableApiUsage", "BooleanMethodIsAlwaysInverted"})
public class ThunderBladeItem {
    public static ItemStack getThunderBladeItem() {
        ItemStack THUNDER_BLADE_ITEM = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta THUNDER_BLADE_META = THUNDER_BLADE_ITEM.getItemMeta();
        CustomModelDataComponent THUNDER_BLADE_MODEL = THUNDER_BLADE_META.getCustomModelDataComponent();

        THUNDER_BLADE_META.customName(Component.text("Thunder Blade", NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD)
                .decorate(TextDecoration.UNDERLINED)
                .decorate(TextDecoration.ITALIC));
        THUNDER_BLADE_META.lore(List.of(
                Component.keybind("key.use", NamedTextColor.YELLOW)
                        .append(Component.text(" to dash", NamedTextColor.GOLD)),
                Component.keybind("key.sneak", NamedTextColor.YELLOW)
                        .append(Component.text(" - ", NamedTextColor.YELLOW))
                        .append(Component.keybind("key.use", NamedTextColor.YELLOW))
                        .append(Component.text(" to to perform a Thunder-Slam", NamedTextColor.GOLD)),
                Component.keybind("key.drop", NamedTextColor.YELLOW)
                        .append(Component.text(" to perform a Thunder-Swap", NamedTextColor.GOLD)),
                Component.keybind("key.swapOffhand", NamedTextColor.YELLOW)
                        .append(Component.text(" to use your Ultimate", NamedTextColor.GOLD))
        ));

        THUNDER_BLADE_MODEL.setStrings(List.of("thunder_blade_0"));

        THUNDER_BLADE_META.setCustomModelDataComponent(THUNDER_BLADE_MODEL);
        THUNDER_BLADE_META.setUnbreakable(true);
        THUNDER_BLADE_META.getPersistentDataContainer().set(ThunderBladeKeys.THUNDER_BLADE_IS_ITEM, PersistentDataType.BOOLEAN, true);
        THUNDER_BLADE_ITEM.setItemMeta(THUNDER_BLADE_META);
        return THUNDER_BLADE_ITEM;
    }

    public static boolean isThunderBlade(ItemStack item) {
        if (item == null || item.getType().isAir()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        return meta.getPersistentDataContainer().has(
                ThunderBladeKeys.THUNDER_BLADE_IS_ITEM,
                PersistentDataType.BOOLEAN
        );
    }
}
