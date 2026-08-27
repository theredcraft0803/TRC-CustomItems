package de.trc.trccustomitems.ability;

import de.trc.trccustomitems.Config;
import de.trc.trccustomitems.TRCCustomItems;
import de.trc.trccustomitems.ability.util.NoFall;
import de.trc.trccustomitems.ability.util.NoFallUnderY;
import de.trc.trccustomitems.ability.util.ThunderBladeStorage;

import fr.skytasul.glowingentities.GlowingEntities;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Collection;

public class ThunderBladeAbilities {
    static GlowingEntities glowingEntities;

    public static void init(JavaPlugin plugin) {
        glowingEntities = new GlowingEntities(plugin);
    }

    public static void dash(Player player) {
        player.getWorld().spawnParticle(
                Particle.CLOUD,
                player.getLocation(),
                20,
                0.3, 0.1, 0.3,
                0.05
        );
        NoFallUnderY.start(player);
        player.playSound(player, Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 1.0F);
        Vector direction = player.getLocation().getDirection().normalize();
        player.setVelocity(direction.multiply(Config.THUNDER_BLADE_ABILITY_DASH_MULTIPLICATOR));
    }

    public static void swap(Player player) {
        RayTraceResult result = player.getWorld().rayTraceEntities(
                player.getEyeLocation(),
                player.getEyeLocation().getDirection(),
                Config.THUNDER_BLADE_ABILITY_SWAP_DISTANCE,
                0.5,
                entity -> entity instanceof Player && entity != player
        );

        if (result != null && result.getHitEntity() instanceof Player targetPlayer) {
            Location targetLocation = targetPlayer.getLocation();
            Location playerLocation = player.getLocation();
            World world = playerLocation.getWorld();

            spawnBeam(player, playerLocation.distance(targetLocation), 0.2, Color.GREEN);

            targetPlayer.teleport(playerLocation);
            player.teleport(targetLocation);

            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 120, 1, true, false));

            world.strikeLightningEffect(playerLocation);
            world.strikeLightningEffect(targetLocation).isEffect();

            Vector knockback = playerLocation.toVector().subtract(targetLocation.toVector()).normalize();
            targetPlayer.setVelocity(knockback.multiply(1.2));

            targetPlayer.playSound(targetPlayer, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

            targetPlayer.damage(Config.THUNDER_BLADE_ABILITY_SWAP_DAMAGE);
            targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 0));

            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
        } else spawnBeam(player, Config.THUNDER_BLADE_ABILITY_SWAP_DISTANCE, 0.21, Color.RED);
    }

    private static void spawnBeam(Player player, double length, double step, Color color) {
        Location start = player.getEyeLocation();
        Vector direction = start.getDirection().normalize();

        for (double d = 0; d <= length; d += step) {
            Location point = start.clone().add(direction.clone().multiply(d));
            player.getWorld().spawnParticle(Particle.DUST,
                    point,
                    1,
                    0,
                    0,
                    0,
                    0,
                    new Particle.DustOptions(color, 2));
        }
    }

    @SuppressWarnings("deprecation")
    public static void startSlam(Player player) {
        ThunderBladeStorage.addPlayerSlam(player);

        player.getWorld().spawnParticle(
                Particle.CLOUD,
                player.getLocation(),
                20,
                0.3, 0.1, 0.3,
                0.05
        );
        player.setVelocity(new Vector(0, 2.5, 0));
        player.playSound(player, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 2, true, false));

        BukkitTask[] task = new BukkitTask[1];

        task[0] = Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(TRCCustomItems.class), () -> {
            Player oldTarget = ThunderBladeStorage.getPlayerSlamPair(player);

            if (!player.isOnline() || !ThunderBladeStorage.hasPlayerSlam(player)) {
                task[0].cancel();
                return;
            }

            RayTraceResult result = player.getWorld().rayTraceEntities(
                    player.getEyeLocation(),
                    player.getEyeLocation().getDirection(),
                    Config.THUNDER_BLADE_ABILITY_SLAM_DISTANCE,
                    0.5,
                    entity -> entity instanceof Player && entity != player
            );

            if (result != null && result.getHitEntity() != null) {
                Player target = (Player) result.getHitEntity();

                try {
                    if (oldTarget != target) {
                        if (oldTarget != null) {
                            glowingEntities.unsetGlowing(oldTarget, player);
                        }

                        glowingEntities.setGlowing(target, player, ChatColor.GOLD);
                    }
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }

                ThunderBladeStorage.addPlayerSlamPair(player, target);

            } else {
                if (oldTarget != null) {
                    try {
                        glowingEntities.unsetGlowing(oldTarget, player);
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }

                    ThunderBladeStorage.removePlayerSlamPair(player);
                }
            }

            if (player.isOnGround() && player.getVelocity().getY() <= 0) {
                Player target = ThunderBladeStorage.getPlayerSlamPair(player);

                if (target != null) {
                    try {
                        glowingEntities.unsetGlowing(target, player);
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                }

                ThunderBladeStorage.removePlayerSlamPair(player);
                ThunderBladeStorage.removePlayerSlam(player);
                task[0].cancel();
            }
        }, 0L, 1L);

        Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(TRCCustomItems.class), () -> {
            if (player.isOnGround()) {
                Player target = ThunderBladeStorage.getPlayerSlamPair(player);

                if (target != null) {
                    try {
                        glowingEntities.unsetGlowing(target, player);
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                }

                ThunderBladeStorage.removePlayerSlamPair(player);
                ThunderBladeStorage.removePlayerSlam(player);
                task[0].cancel();
            }
        }, 10 * 20);
    }

    @SuppressWarnings("deprecation")
    public static void performSlam(Player player, Player target) {
        Location playerLocation = player.getLocation();
        Location targetLocation = target.getLocation().clone();

        double targetActualY = targetLocation.getY(); // untere Grenze: Gegner-Y-Position

        targetLocation.setY(playerLocation.getY());

        World world = targetLocation.getWorld();

        while (targetLocation.getY() > targetActualY) {
            Block feetBlock = world.getBlockAt(targetLocation.getBlockX(), targetLocation.getBlockY(), targetLocation.getBlockZ());
            Block headBlock = world.getBlockAt(targetLocation.getBlockX(), targetLocation.getBlockY() + 1, targetLocation.getBlockZ());

            if (feetBlock.isPassable() && headBlock.isPassable()) {
                player.teleport(targetLocation);
                player.playSound(targetLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                player.removePotionEffect(PotionEffectType.SLOW_FALLING);
                player.setVelocity(new Vector(0, -1, 0));

                try {
                    glowingEntities.unsetGlowing(target, player);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
                ThunderBladeStorage.removePlayerSlamPair(player);
                ThunderBladeStorage.removePlayerSlam(player);

                NoFall.start(player);

                BukkitTask[] task = new BukkitTask[1];

                task[0] = Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(TRCCustomItems.class), () -> {
                    if (!player.isOnGround()) return;
                    Collection<Entity> entities = player.getNearbyEntities(5, 5, 5);
                    for (Entity entity : entities) {
                        if ((entity instanceof Player p)) {
                            if (p == player) continue;

                            Location impactLocation = player.getLocation();
                            double distance = p.getLocation().distance(impactLocation);
                            double radius = 5.0;
                            double strength = 1.0 - Math.min(distance / radius, 1.0);
                            strength = Math.max(strength, 0.25);
                            Vector direction = p.getLocation().toVector().subtract(impactLocation.toVector());

                            if (direction.lengthSquared() < 0.01) {
                                double angle = Math.random() * Math.PI * 2;
                                direction = new Vector(Math.cos(angle), 0, Math.sin(angle));
                            } else {
                                direction.normalize();
                            }

                            double horizontalStrength = 0.7 * strength;
                            double verticalStrength = 0.75 + (0.35 * strength);

                            Vector knockback = direction.multiply(horizontalStrength);
                            knockback.setY(verticalStrength);

                            double damage = 25.0 + (20.0 * strength);

                            p.damage(damage, player);
                            p.setVelocity(knockback);
                        } else {
                            Location impactLocation = player.getLocation();
                            double distance = entity.getLocation().distance(impactLocation);
                            double radius = 5.0;
                            double strength = 1.0 - Math.min(distance / radius, 1.0);
                            strength = Math.max(strength, 0.25);
                            Vector direction = entity.getLocation().toVector().subtract(impactLocation.toVector());

                            if (direction.lengthSquared() < 0.01) {
                                double angle = Math.random() * Math.PI * 2;
                                direction = new Vector(Math.cos(angle), 0, Math.sin(angle));
                            } else {
                                direction.normalize();
                            }

                            double horizontalStrength = 0.8 * strength;
                            double verticalStrength = 0.95 + (0.35 * strength);

                            Vector knockback = direction.multiply(horizontalStrength);
                            knockback.setY(verticalStrength);

                            entity.setVelocity(knockback);
                        }
                    }
                    createImpact(player.getLocation());
                    task[0].cancel();
                }, 0L, 1L);

                return;
            }

            targetLocation.subtract(0, 1, 0);
        }

        targetLocation.setY(targetActualY);

        player.teleport(targetLocation);
        player.playSound(targetLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        player.removePotionEffect(PotionEffectType.SLOW_FALLING);
        player.setVelocity(new Vector(0, -1, 0));

        try {
            glowingEntities.unsetGlowing(target, player);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        ThunderBladeStorage.removePlayerSlamPair(player);
        ThunderBladeStorage.removePlayerSlam(player);

        NoFall.start(player);

        BukkitTask[] task = new BukkitTask[1];

        task[0] = Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(TRCCustomItems.class), () -> {
            if (!player.isOnGround()) return;
            Collection<Entity> entities = player.getNearbyEntities(5, 5, 5);
            for (Entity entity : entities) {
                if ((entity instanceof Player p)) {
                    if (p == player) continue;

                    Location impactLocation = player.getLocation();
                    double distance = p.getLocation().distance(impactLocation);
                    double radius = 5.0;
                    double strength = 1.0 - Math.min(distance / radius, 1.0);
                    strength = Math.max(strength, 0.25);
                    Vector direction = p.getLocation().toVector().subtract(impactLocation.toVector());

                    if (direction.lengthSquared() < 0.01) {
                        double angle = Math.random() * Math.PI * 2;
                        direction = new Vector(Math.cos(angle), 0, Math.sin(angle));
                    } else {
                        direction.normalize();
                    }

                    double horizontalStrength = 0.7 * strength;
                    double verticalStrength = 0.75 + (0.35 * strength);

                    Vector knockback = direction.multiply(horizontalStrength);
                    knockback.setY(verticalStrength);

                    double damage = 25.0 + (20.0 * strength);

                    p.damage(damage, player);
                    p.setVelocity(knockback);
                } else {
                    Location impactLocation = player.getLocation();
                    double distance = entity.getLocation().distance(impactLocation);
                    double radius = 5.0;
                    double strength = 1.0 - Math.min(distance / radius, 1.0);
                    strength = Math.max(strength, 0.25);
                    Vector direction = entity.getLocation().toVector().subtract(impactLocation.toVector());

                    if (direction.lengthSquared() < 0.01) {
                        double angle = Math.random() * Math.PI * 2;
                        direction = new Vector(Math.cos(angle), 0, Math.sin(angle));
                    } else {
                        direction.normalize();
                    }

                    double horizontalStrength = 0.8 * strength;
                    double verticalStrength = 0.95 + (0.35 * strength);

                    Vector knockback = direction.multiply(horizontalStrength);
                    knockback.setY(verticalStrength);

                    entity.setVelocity(knockback);
                }
            }
            createImpact(player.getLocation());
            task[0].cancel();
        }, 0L, 1L);
    }

    private static void createImpact(Location center) {
        World world = center.getWorld();
        if (world == null) return;

        for (int i = 0; i < 180; i++) {
            double angle = Math.random() * Math.PI * 2;
            double radius = Math.random() * 2.8;

            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            double y = Math.random() * 1.8;

            Location loc = center.clone().add(x, y, z);

            world.spawnParticle(
                    Particle.DUST,
                    loc,
                    1,
                    0.15,
                    0.25,
                    0.15,
                    0,
                    new Particle.DustOptions(Color.WHITE, 2.0f)
            );

            world.spawnParticle(
                    Particle.EXPLOSION,
                    loc,
                    1,
                    0.15,
                    0.25,
                    0.15,
                    0
            );
        }

        world.spawnParticle(
                Particle.CLOUD,
                center.clone().add(0, 0.3, 0),
                80,
                1.5,
                0.3,
                1.5,
                0.12
        );

        world.spawnParticle(
                Particle.WHITE_ASH,
                center.clone().add(0, 0.2, 0),
                120,
                1.8,
                1.0,
                1.8,
                0.08
        );

        world.playSound(
                center,
                "trc:thunder_slam",
                1.0f,
                1.2f
        );
    }

    public static void ultimate(Player player) {
        ThunderBladeStorage.addPlayerUlt(player);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Config.THUNDER_BLADE_ABILITY_ULTIMATE_SEC * 20, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Config.THUNDER_BLADE_ABILITY_ULTIMATE_SEC * 20, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Config.THUNDER_BLADE_ABILITY_ULTIMATE_SEC * 20 / 4, 0, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Config.THUNDER_BLADE_ABILITY_ULTIMATE_SEC * 20, 4, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Config.THUNDER_BLADE_ABILITY_ULTIMATE_SEC * 20, 0, true, false));
    }

    public static void combo(Player attacker, Player victim) {
        Collection<Entity> entities = attacker.getNearbyEntities(10, 10, 10);
        for (Entity entity : entities) {
            if (!(entity instanceof Player player)) continue;
            player.getWorld().strikeLightningEffect(player.getLocation());
            if (player != victim) player.damage(6.0);
        }
        victim.damage(10.0);
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 40 , 0, true, false));
    }
}
