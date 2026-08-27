package de.trc.trccustomitems.command.tabcompleter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GiveCustomItemTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This Command is only for Players").color(NamedTextColor.RED));
            return List.of();
        }

        if (!player.hasPermission("customitems.give")) return List.of();

        if (args.length == 1) {
            return List.of(
                    "lightning-bottle",
                    "thunder-blade",
                    "diamond_lightning_rod"
            );
        }
        return List.of();
    }
}
