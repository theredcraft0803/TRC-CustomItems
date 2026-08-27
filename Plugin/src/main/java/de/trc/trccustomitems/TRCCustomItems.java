package de.trc.trccustomitems;

import de.trc.trccustomitems.ability.ThunderBladeAbilities;
import de.trc.trccustomitems.ability.util.NoFall;
import de.trc.trccustomitems.ability.util.NoFallUnderY;
import de.trc.trccustomitems.actionbar.ThunderBladeActionBar;
import de.trc.trccustomitems.command.GiveCustomItemCommand;
import de.trc.trccustomitems.command.tabcompleter.GiveCustomItemTabCompleter;
import de.trc.trccustomitems.crafting.DiamondLightningRodCrafting;
import de.trc.trccustomitems.crafting.LightningBottleCrafting;
import de.trc.trccustomitems.crafting.ThunderBladeCrafting;
import de.trc.trccustomitems.key.DiamondLightningRodKeys;
import de.trc.trccustomitems.key.LightningBottleKeys;
import de.trc.trccustomitems.key.ThunderBladeKeys;
import de.trc.trccustomitems.trigger.ThunderBladeTrigger;
import de.trc.trccustomitems.useblocker.GlassBottleBlocker;

import de.trc.trccustomitems.useblocker.LightningRodBlocker;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class TRCCustomItems extends JavaPlugin {

    @Override
    public void onEnable() {
        Config.load(this);

        getServer().getPluginManager().registerEvents(new NoFallUnderY(), this);
        getServer().getPluginManager().registerEvents(new NoFall(), this);

        ThunderBladeKeys.THUNDER_BLADE_USES = new NamespacedKey(this, "thunder_blade_uses");
        ThunderBladeKeys.THUNDER_BLADE_IS_ITEM = new NamespacedKey(this, "thunder_blade_is_item");
        getServer().getPluginManager().registerEvents(new ThunderBladeTrigger(), this);
        getServer().getPluginManager().registerEvents(new ThunderBladeCrafting(), this);
        ThunderBladeAbilities.init(this);
        ThunderBladeActionBar.actionBar();

        LightningBottleKeys.LIGHTNING_BOTTLE_IS_ITEM = new NamespacedKey(this, "lightning_bottle_is_item");
        getServer().getPluginManager().registerEvents(new LightningBottleCrafting(), this);
        getServer().getPluginManager().registerEvents(new GlassBottleBlocker(), this);

        DiamondLightningRodKeys.DIAMOND_LIGHTNING_ROD_IS_ITEM = new NamespacedKey(this, "diamond_lightning_rod_is_item");
        getServer().getPluginManager().registerEvents(new DiamondLightningRodCrafting(), this);
        getServer().getPluginManager().registerEvents(new LightningRodBlocker(), this);

        Objects.requireNonNull(getCommand("givecustomitem")).setExecutor(new  GiveCustomItemCommand());
        Objects.requireNonNull(getCommand("givecustomitem")).setTabCompleter(new GiveCustomItemTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
