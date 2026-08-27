package de.trc.trccustomitems.trigger;

import de.trc.trccustomitems.Config;
import de.trc.trccustomitems.ability.ThunderBladeAbilities;
import de.trc.trccustomitems.ability.ThunderBladeAbility;
import de.trc.trccustomitems.ability.util.ThunderBladeStorage;
import de.trc.trccustomitems.cooldown.ThunderBladeCooldown;
import de.trc.trccustomitems.item.ThunderBladeItem;
import de.trc.trccustomitems.key.ThunderBladeKeys;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class ThunderBladeTrigger implements Listener {
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!ThunderBladeItem.isThunderBlade(item)) return;

        if (player.isSneaking()) return;

        if (!ThunderBladeStorage.hasPlayerSlam(player)) {
            if (ThunderBladeStorage.hasPlayerUlt(player)) {
                if (ThunderBladeCooldown.isOnCooldown(player, ThunderBladeAbility.DASH)) return;

                if(canUse(item, Config.THUNDER_BLADE_COST_DASH)) {
                    if (Math.random() > 0.2) removeUses(item, Config.THUNDER_BLADE_COST_DASH);
                    ThunderBladeCooldown.setCooldown(player, ThunderBladeAbility.DASH, Config.THUNDER_BLADE_COOLDOWN_DASH / 2);
                    ThunderBladeAbilities.dash(player);
                }
            } else {
                if (ThunderBladeCooldown.isOnCooldown(player, ThunderBladeAbility.DASH)) return;

                if(canUse(item, Config.THUNDER_BLADE_COST_DASH)) {
                    ThunderBladeCooldown.setCooldown(player, ThunderBladeAbility.DASH, Config.THUNDER_BLADE_COOLDOWN_DASH);
                    removeUses(item, Config.THUNDER_BLADE_COST_DASH);
                    ThunderBladeAbilities.dash(player);
                }
            }
        } else {
            if (ThunderBladeStorage.hasPlayerSlamPair(player)) {
                Player target = ThunderBladeStorage.getPlayerSlamPair(player);
                if (target == null) return;
                ThunderBladeStorage.removePlayerSlamPair(player);
                ThunderBladeAbilities.performSlam(player, target);
            }
        }
    }

    @EventHandler
    public void onSneakRightClick(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!ThunderBladeItem.isThunderBlade(item)) return;

        if (!player.isSneaking()) return;

        if (ThunderBladeStorage.hasPlayerSlam(player)) return;


        if (ThunderBladeCooldown.isOnCooldown(player, ThunderBladeAbility.SLAM)) return;

        if(canUse(item, Config.THUNDER_BLADE_COST_SLAM)) {
            ThunderBladeCooldown.setCooldown(player, ThunderBladeAbility.SLAM, Config.THUNDER_BLADE_COOLDOWN_SLAM);
            removeUses(item, Config.THUNDER_BLADE_COST_SLAM);
            ThunderBladeAbilities.startSlam(player);
        }
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) return;

        ItemStack item = attacker.getInventory().getItemInMainHand();

        if (event.getEntity() instanceof Creeper creeper && ThunderBladeItem.isThunderBlade(item)) {
            creeper.kill(event.getDamageSource());
            return;
        }

        if (event.getEntity() instanceof Phantom phantom && ThunderBladeItem.isThunderBlade(item)) {
            phantom.kill(event.getDamageSource());
            return;
        }

        if (!(event.getEntity() instanceof Player victim)) return;

        ThunderBladeStorage.resetCombo(victim);

        if (!ThunderBladeItem.isThunderBlade(item)) {
            ThunderBladeStorage.resetCombo(attacker);
            return;
        }

        int combo = ThunderBladeStorage.addComboHit(attacker);

        if (combo >= Config.THUNDER_BLADE_COMBO_GOAL) {
            ThunderBladeStorage.resetCombo(attacker);
            ThunderBladeAbilities.combo(attacker, victim);
        }

        if (ThunderBladeStorage.hasPlayerUlt(attacker)) {
            addUses(item, randomRecharge() * 2);
        } else addUses(item, randomRecharge());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();

        if (!ThunderBladeItem.isThunderBlade(item)) return;

        if (player.isSneaking()) return;

        if (ThunderBladeStorage.hasPlayerSlam(player)) return;

        event.setCancelled(true);

        if (ThunderBladeCooldown.isOnCooldown(player, ThunderBladeAbility.SWAP)) return;

        if (canUse(item, Config.THUNDER_BLADE_COST_SWAP)) {
            ThunderBladeCooldown.setCooldown(player, ThunderBladeAbility.SWAP, Config.THUNDER_BLADE_COOLDOWN_SWAP);
            removeUses(item, Config.THUNDER_BLADE_COST_SWAP);
            ThunderBladeAbilities.swap(player);
        }
    }

    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!ThunderBladeItem.isThunderBlade(item)) return;

        if (ThunderBladeStorage.hasPlayerSlam(player)) return;

        event.setCancelled(true);

        if (ThunderBladeCooldown.isOnCooldown(player, ThunderBladeAbility.ULTIMATE)) return;

        if (canUse(item, Config.THUNDER_BLADE_COST_ULTIMATE)) {
            ThunderBladeCooldown.setCooldown(player, ThunderBladeAbility.ULTIMATE, Config.THUNDER_BLADE_COOLDOWN_ULTIMATE);
            removeUses(item, Config.THUNDER_BLADE_COST_ULTIMATE);
            ThunderBladeAbilities.ultimate(player);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ThunderBladeItem.isThunderBlade(item)) return;
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, SoundCategory.PLAYERS, 1, 1);
    }

    private int getUses(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return 0;

        return meta.getPersistentDataContainer().getOrDefault(
                ThunderBladeKeys.THUNDER_BLADE_USES,
                PersistentDataType.INTEGER,
                0
        );
    }

    private boolean canUse(ItemStack item, int uses) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        return getUses(item) >= uses;
    }

    private void removeUses(ItemStack item, int uses) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        if (!canUse(item, uses)) return;

        meta.getPersistentDataContainer().set(
                ThunderBladeKeys.THUNDER_BLADE_USES,
                PersistentDataType.INTEGER,
                getUses(item) - uses
        );

        item.setItemMeta(meta);
        updateTexture(item);
    }

    private void addUses(ItemStack item, int uses) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        int newUses = Math.min(getUses(item) + uses, Config.THUNDER_BLADE_MAX_USES);

        meta.getPersistentDataContainer().set(
                ThunderBladeKeys.THUNDER_BLADE_USES,
                PersistentDataType.INTEGER,
                newUses
        );

        item.setItemMeta(meta);
        updateTexture(item);
    }

    private int randomRecharge() {
        int r = (int) (Math.random() * 100);

        if (r < 20) return 2;
        if (r < 80) return 1;
        return 0;
    }

    private void updateTexture(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        CustomModelDataComponent component = meta.getCustomModelDataComponent();
        component.setStrings(List.of("thunder_blade_" + getUses(item)));

        meta.setCustomModelDataComponent(component);
        item.setItemMeta(meta);
    }
}
