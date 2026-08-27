package de.trc.trccustomitems.command;

import de.trc.trccustomitems.item.DiamondLightningRodItem;
import de.trc.trccustomitems.item.LightningBottleItem;
import de.trc.trccustomitems.item.ThunderBladeItem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveCustomItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This Command is only for Players").color(NamedTextColor.RED));
            return true;
        }

        if (!player.hasPermission("customitems.give")) {
            sender.sendMessage(Component.text("No Permission").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /" + label + " <item name>").color(NamedTextColor.RED));
            return true;
        }

        switch(args[0]) {
            case "lightning-bottle": {
                player.give(LightningBottleItem.getLightningBottleItem());
                break;
            }

            case "thunder-blade": {
                player.give(ThunderBladeItem.getThunderBladeItem());
                break;
            }

            case "diamond_lightning_rod": {
                player.give(DiamondLightningRodItem.getDiamondLightningRodItem());
                break;
            }
        }

        return true;
    }
}
