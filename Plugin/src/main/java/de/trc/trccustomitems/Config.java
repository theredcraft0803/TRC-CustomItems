package de.trc.trccustomitems;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
    public static int THUNDER_BLADE_MAX_USES;
    public static int THUNDER_BLADE_COMBO_GOAL;

    public static int THUNDER_BLADE_COST_DASH;
    public static int THUNDER_BLADE_COST_SLAM;
    public static int THUNDER_BLADE_COST_SWAP;
    public static int THUNDER_BLADE_COST_ULTIMATE;

    public static long THUNDER_BLADE_COOLDOWN_DASH;
    public static long THUNDER_BLADE_COOLDOWN_SLAM;
    public static long THUNDER_BLADE_COOLDOWN_SWAP;
    public static long THUNDER_BLADE_COOLDOWN_ULTIMATE;

    public static float THUNDER_BLADE_ABILITY_DASH_MULTIPLICATOR;
    public static double THUNDER_BLADE_ABILITY_SWAP_DISTANCE;
    public static double THUNDER_BLADE_ABILITY_SWAP_DAMAGE;
    public static double THUNDER_BLADE_ABILITY_SLAM_DISTANCE;
    public static int THUNDER_BLADE_ABILITY_ULTIMATE_SEC;

    public static void load(JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        THUNDER_BLADE_MAX_USES = config.getInt("thunder-blade.max-uses", 6);
        THUNDER_BLADE_COMBO_GOAL = config.getInt("thunder-blade.combo-goal", 3);

        THUNDER_BLADE_COST_DASH = config.getInt("thunder-blade.cost.dash", 2);
        THUNDER_BLADE_COST_SLAM = config.getInt("thunder-blade.cost.slam", 2);
        THUNDER_BLADE_COST_SWAP = config.getInt("thunder-blade.cost.swap", 2);
        THUNDER_BLADE_COST_ULTIMATE = config.getInt("thunder-blade.cost.ultimate", 2);

        THUNDER_BLADE_COOLDOWN_DASH = config.getLong("thunder-blade.cooldown.dash", 2000L);
        THUNDER_BLADE_COOLDOWN_SLAM = config.getLong("thunder-blade.cooldown.slam", 2000L);
        THUNDER_BLADE_COOLDOWN_SWAP = config.getLong("thunder-blade.cooldown.swap", 2000L);
        THUNDER_BLADE_COOLDOWN_ULTIMATE = config.getLong("thunder-blade.cooldown.ultimate", 2000L);

        THUNDER_BLADE_ABILITY_DASH_MULTIPLICATOR = (float) config.getDouble("thunder-blade.ability.dash-multiplicator", 2.0);
        THUNDER_BLADE_ABILITY_SWAP_DISTANCE = config.getDouble("thunder-blade.ability.swap-distance", 20.0);
        THUNDER_BLADE_ABILITY_SWAP_DAMAGE = config.getDouble("thunder-blade.ability.swap-damage", 8.0);
        THUNDER_BLADE_ABILITY_SLAM_DISTANCE = config.getDouble("thunder-blade.ability.slam-distance", 30.0);
        THUNDER_BLADE_ABILITY_ULTIMATE_SEC = config.getInt("thunder-blade.ability.ultimate-sec", 20);
    }
}
