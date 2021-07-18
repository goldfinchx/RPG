package ru.artorium.rpg.systems.battle_system.listener;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.artorium.rpg.RPG;
import ru.artorium.rpg.systems.battle_system.obj.RPGItem;
import ru.artorium.rpg.player.obj.PlayerData;
import ru.artorium.rpg.utils.Colors;
import ru.artorium.rpg.utils.IntegerUtils;
import ru.artorium.rpg.utils.RandomCollection;

import java.util.Objects;


public class BattleListener implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {

        // TARGET STATS
        RPGItem rpgHelmet = null;
        RPGItem rpgChestplate = null;
        RPGItem rpgLeggings = null;
        RPGItem rpgBoots = null;

        Player playerDamager = null;
        LivingEntity entityDamager = null;

        Player playerTarget = null;
        LivingEntity entityTarget = null;

        if (e.getDamager() instanceof Player)
            playerDamager = (Player) e.getDamager();

        if (e.getDamager() instanceof LivingEntity)
            entityDamager = (Player) e.getDamager();

        if (e.getEntity() instanceof Player)
            playerTarget = (Player) e.getEntity();

        if (e.getEntity() instanceof LivingEntity)
            entityTarget = (LivingEntity) e.getEntity();

        if (entityTarget.getEquipment() != null) {
            try { rpgHelmet = RPGItem.getFromItemStack(entityTarget.getEquipment().getHelmet());
            } catch (NullPointerException ignore) {}

            try { rpgChestplate = RPGItem.getFromItemStack(Objects.requireNonNull(entityTarget.getEquipment().getChestplate()));
            } catch (NullPointerException ignore) {}

            try { rpgLeggings = RPGItem.getFromItemStack(Objects.requireNonNull(entityTarget.getEquipment().getLeggings()));
            } catch (NullPointerException ignore) {}

            try { rpgBoots = RPGItem.getFromItemStack(Objects.requireNonNull(entityTarget.getEquipment().getBoots()));
            } catch (NullPointerException ignore) {}
        }

        int stabProtectionHelmet = rpgHelmet == null ? 0 : rpgHelmet.getStabProtection();
        int stabProtectionChestplate = rpgChestplate == null ? 0 : rpgChestplate.getStabProtection();
        int stabProtectionLeggings = rpgLeggings == null ? 0 : rpgLeggings.getStabProtection();
        int stabProtectionBoots = rpgBoots == null ? 0 : rpgBoots.getStabProtection();

        int slashProtectionHelmet = rpgHelmet == null ? 0 : rpgHelmet.getSlashProtection();
        int slashProtectionChestplate = rpgChestplate == null ? 0 : rpgChestplate.getSlashProtection();
        int slashProtectionLeggings = rpgLeggings == null ? 0 : rpgLeggings.getSlashProtection();
        int slashProtectionBoots = rpgBoots == null ? 0 : rpgBoots.getSlashProtection();

        int bluntProtectionHelmet = rpgHelmet == null ? 0 : rpgHelmet.getBluntProtection();
        int bluntProtectionChestplate = rpgChestplate == null ? 0 : rpgChestplate.getBluntProtection();
        int bluntProtectionLeggings = rpgLeggings == null ? 0 : rpgLeggings.getBluntProtection();
        int bluntProtectionBoots = rpgBoots == null ? 0 : rpgBoots.getBluntProtection();

        int totalStabProtection = stabProtectionHelmet + stabProtectionChestplate + stabProtectionLeggings + stabProtectionBoots;
        int totalSlashProtection = slashProtectionHelmet + slashProtectionLeggings + slashProtectionChestplate + slashProtectionBoots;
        int totalBluntProtection = bluntProtectionHelmet + bluntProtectionChestplate + bluntProtectionLeggings + bluntProtectionBoots;

        // ATTACKER STATS
        RPGItem rpgWeapon = null;

        if (entityDamager.getEquipment() != null) {
            try { rpgWeapon = RPGItem.getFromItemStack(entityDamager.getEquipment().getItemInMainHand());
            } catch (NullPointerException ignore) {}
        }

        if (rpgWeapon == null)
            return;

        int stabDamage = rpgWeapon.getStabDamage();
        int slashDamage = rpgWeapon.getSlashDamage();
        int bluntDamage = rpgWeapon.getBluntDamage();

        RandomCollection<String> damageVariants = new RandomCollection<>();

        if (stabDamage != 0) damageVariants.add(0.32, "STAB");
        if (slashDamage != 0) damageVariants.add(0.32, "SLASH");
        if (bluntDamage != 0) damageVariants.add(0.32, "BLUNT");
        damageVariants.add(0.04, "MISS");

        int finalDamage = 0;

        Hologram hologram = HologramsAPI.createHologram(RPG.getInstance(), entityTarget.getLocation().add(IntegerUtils.getRandomInteger(1, 5) * 0.1, IntegerUtils.getRandomInteger(1, 2) * 1.0, IntegerUtils.getRandomInteger(1, 5) * 0.1));

        if (playerDamager != null) {
            if (playerDamager.getAttackCooldown() != 1.0) {
                e.setCancelled(true);
                return;
            }
        }

        // ПОНИЖЕНИЕ УРОНА ДЛЯ СТОЯЩИХ РЯДОМ ВРАГОВ ПРИ СВИП АТАКЕ
        if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
            switch (damageVariants.next()) {
                case "STAB":
                    finalDamage = (int) (Math.round((stabDamage - (totalStabProtection * 0.5)) * (IntegerUtils.getRandomInteger(7, 10) * 0.1)))/IntegerUtils.getRandomInteger(2,3);
                    hologram.appendTextLine(Colors.parseColors("&a-" + finalDamage));

                    break;

                case "SLASH":
                    finalDamage = (int) (Math.round((slashDamage - (totalSlashProtection * 0.5)) * (IntegerUtils.getRandomInteger(7, 10) * 0.1)))/IntegerUtils.getRandomInteger(2,3);

                    if (IntegerUtils.getRandom(0.02)) {
                        entityTarget.sendMessage(Colors.parseColors("&cУ вас началось кровотечение!"));
                        entityDamager.sendMessage(Colors.parseColors("&cВы вызвали кровотечение у врага!"));

                        entityTarget.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 10, 1, false, false));
                    }

                    hologram.appendTextLine(Colors.parseColors("&a-" + finalDamage));
                    break;

                case "BLUNT":
                    finalDamage = (int) (Math.round((bluntDamage - (totalBluntProtection * 0.5)) * (IntegerUtils.getRandomInteger(7, 10) * 0.1)))/IntegerUtils.getRandomInteger(2,3);

                    if (IntegerUtils.getRandom(0.01)) {
                        entityTarget.sendMessage(Colors.parseColors("&cВы были оглушены!"));
                        entityDamager.sendMessage(Colors.parseColors("&cВы оглушили врага!"));

                        entityTarget.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*3, 1, false, false));
                        entityTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*3, 2, false, false));
                    }

                    hologram.appendTextLine(Colors.parseColors("&a-" + finalDamage));
                    break;

                case "MISS":
                    hologram.appendTextLine(Colors.parseColors("&7*промах*"));
                    e.setCancelled(true);
                    Bukkit.getScheduler().runTaskLaterAsynchronously(RPG.getInstance(), hologram::delete, 8);
            }
        } else {
            switch (damageVariants.next()) {
                case "STAB":
                    finalDamage = (int) Math.round((stabDamage - (totalStabProtection * 0.5)));

                    if (IntegerUtils.getRandom(0.02)) {
                        finalDamage = finalDamage * 2;
                        hologram.appendTextLine(Colors.parseColors("&6&l-" + finalDamage));
                        return;
                    }

                    hologram.appendTextLine(Colors.parseColors("&d-" + finalDamage));
                    break;

                case "SLASH":
                    finalDamage = (int) Math.round((slashDamage - (totalSlashProtection * 0.5)));

                    if (IntegerUtils.getRandom(0.02)) {
                        finalDamage = finalDamage * 2;
                        hologram.appendTextLine(Colors.parseColors("&6&l-" + finalDamage));
                        return;
                    }

                    hologram.appendTextLine(Colors.parseColors("&d-" + finalDamage));

                    if (IntegerUtils.getRandom(0.02)) {
                        entityTarget.sendMessage(Colors.parseColors("&cУ вас началось кровотечение!"));
                        entityDamager.sendMessage(Colors.parseColors("&cВы вызвали кровотечение у врага!"));

                        entityTarget.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 10, 1, false, false));
                    }
                    break;

                case "BLUNT":
                    finalDamage = (int) Math.round((bluntDamage - (totalBluntProtection * 0.5)));
                    hologram.appendTextLine(Colors.parseColors("&b-" + finalDamage));

                    if (IntegerUtils.getRandom(0.01)) {
                        entityTarget.sendMessage(Colors.parseColors("&cВы были оглушены!"));
                        entityDamager.sendMessage(Colors.parseColors("&cВы оглушили врага!"));

                        entityTarget.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 1, false, false));
                        entityTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 2, false, false));
                    }
                    break;

                case "MISS":
                    hologram.appendTextLine(Colors.parseColors("&7*промах*"));
                    e.setCancelled(true);
                    Bukkit.getScheduler().runTaskLaterAsynchronously(RPG.getInstance(), hologram::delete, 8);
                    return;
            }
        }

        if (playerDamager != null) {
            hologram.getVisibilityManager().setVisibleByDefault(false);
            hologram.getVisibilityManager().showTo((Player) e.getDamager());
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(RPG.getInstance(), hologram::delete, 8);

        finalDamage = Math.abs(finalDamage);

        if (playerTarget != null) {
            PlayerData targetCharacter = RPG.getPlayerData(playerTarget.getUniqueId());

            if (finalDamage >= targetCharacter.getHealth()) {
                Bukkit.getPlayer(targetCharacter.get_id()).setHealth(0.0);

                if (playerDamager != null)
                    RPG.getPlayerData(e.getDamager().getUniqueId()).setPlayerKills(RPG.getPlayerData(e.getDamager().getUniqueId()).getPlayerKills()+1);

            } else {
                RPG.getPlayerData(e.getEntity().getUniqueId()).setHealth(RPG.getPlayerData(e.getEntity().getUniqueId()).getHealth() - finalDamage);
                e.setDamage(0.0);
            }

        } else { e.setDamage(finalDamage); }



        Player player = (Player) e.getDamager();
        int finalDamage1 = finalDamage;

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            onlinePlayer.sendMessage("егор гей");
            onlinePlayer.sendMessage("");
            onlinePlayer.sendMessage("кд -> " + player.getAttackCooldown());
            onlinePlayer.sendMessage("финальный урон -> " + finalDamage1);
            onlinePlayer.sendMessage("");
            onlinePlayer.sendMessage("режущий урон -> " + slashDamage);
            onlinePlayer.sendMessage("колющий урон -> " + stabDamage);
            onlinePlayer.sendMessage("дробящий урон -> " + bluntDamage);
            onlinePlayer.sendMessage("");
            onlinePlayer.sendMessage("режущая броня -> " + totalSlashProtection);
            onlinePlayer.sendMessage("колющая броня -> " + totalStabProtection);
            onlinePlayer.sendMessage("дробящая броня -> " + totalBluntProtection);
        });

    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity().getType().equals(EntityType.PLAYER)) {
            Player player = (Player) e.getEntity();
            PlayerData playerData = RPG.getPlayerData(player.getUniqueId());

            if (e.getDamage() >= playerData.getHealth()) {
                player.setHealth(0.0);
            } else {
                playerData.setHealth(playerData.getHealth()-e.getDamage());
                e.setDamage(0.0);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        PlayerData playerData = RPG.getPlayerData(e.getEntity().getUniqueId());

        playerData.setDeaths(playerData.getDeaths()+1);
        e.setDeathMessage(null);

        player.setHealth(20.0);
        playerData.setHealth(playerData.getMaxHealth()/2);
    }

}
