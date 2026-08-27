package de.trc.trccustomitems.actionbar;

import de.trc.trccustomitems.TRCCustomItems;
import de.trc.trccustomitems.ability.ThunderBladeAbility;
import de.trc.trccustomitems.ability.util.ThunderBladeStorage;
import de.trc.trccustomitems.cooldown.ThunderBladeCooldown;
import de.trc.trccustomitems.item.ThunderBladeItem;
import de.trc.trccustomitems.key.ThunderBladeKeys;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class ThunderBladeActionBar {
    public static void actionBar() {
        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(TRCCustomItems.class), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (!ThunderBladeItem.isThunderBlade(item)) continue;
                if (!ThunderBladeStorage.hasPlayerUlt(player)) player.sendActionBar(Component.text("Uses: ").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD)
                        .append(Component.text(getUses(item) + "  ").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE))
                        .append(Component.text("\uE000").font(Key.key("trc", "icons")).color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).decoration(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(Component.text(" " + ThunderBladeCooldown.getRemainingSeconds(player, ThunderBladeAbility.DASH) + "s  ").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE))
                        .append(Component.text("\uE001").font(Key.key("trc", "icons")).color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).decoration(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(Component.text(" " + ThunderBladeCooldown.getRemainingSeconds(player, ThunderBladeAbility.SWAP) + "s  ").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE))
                        .append(Component.text("\uE002").font(Key.key("trc", "icons")).color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).decoration(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(Component.text(" " + ThunderBladeCooldown.getRemainingSeconds(player, ThunderBladeAbility.SLAM) + "s  ").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE))
                        .append(Component.text("\uE003").font(Key.key("trc", "icons")).color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).decoration(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(Component.text(" " + ThunderBladeCooldown.getRemainingSeconds(player, ThunderBladeAbility.ULTIMATE) + "s  ").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE))
                );
                else player.sendActionBar(Component.text("1 ").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.OBFUSCATED)
                        .append(Component.text("Uses: ").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decoration(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(Component.text(getUses(item) + "  ").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).decoration(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(Component.text("\uE000").font(Key.key("trc", "icons")).color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).decoration(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(Component.text(" " + ThunderBladeCooldown.getRemainingSeconds(player, ThunderBladeAbility.DASH) + "s  ").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).decoration(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(Component.text("\uE001").font(Key.key("trc", "icons")).color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).decoration(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(Component.text(" " + ThunderBladeCooldown.getRemainingSeconds(player, ThunderBladeAbility.SWAP) + "s  ").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).decoration(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(Component.text("\uE002").font(Key.key("trc", "icons")).color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).decoration(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(Component.text(" " + ThunderBladeCooldown.getRemainingSeconds(player, ThunderBladeAbility.SLAM) + "s  ").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).decoration(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(Component.text("\uE003").font(Key.key("trc", "icons")).color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).decoration(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(Component.text(" " + ThunderBladeCooldown.getRemainingSeconds(player, ThunderBladeAbility.ULTIMATE) + "s ").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).decoration(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(Component.text(" 1").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.OBFUSCATED))
                );
            }
        }, 0L, 5L);
    }

    private static int getUses(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return 0;

        return meta.getPersistentDataContainer().getOrDefault(
                ThunderBladeKeys.THUNDER_BLADE_USES,
                PersistentDataType.INTEGER,
                0
        );
    }
}
