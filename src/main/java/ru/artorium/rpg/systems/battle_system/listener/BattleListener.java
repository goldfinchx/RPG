package ru.artorium.rpg.systems.battle_system.listener;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import io.lumine.xikage.mythicmobs.utils.events.extra.ArmorEquipEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.artorium.core.utils.IntegerUtils;
import ru.artorium.core.utils.RandomCollection;
import ru.artorium.core.utils.design.Colors;
import ru.artorium.rpg.RPG;
import ru.artorium.rpg.systems.battle_system.armor.RPGArmor;
import ru.artorium.rpg.systems.battle_system.parameters.RPGItemType;
import ru.artorium.rpg.systems.battle_system.weapon.RPGWeapon;
import ru.artorium.rpg.systems.battle_system.obj.RPGAbstractItem;
import ru.artorium.rpg.player.obj.PlayerData;


public class BattleListener implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {

        // TARGET STATS
        RPGArmor rpgHelmet = null;
        RPGArmor rpgChestplate = null;
        RPGArmor rpgLeggings = null;
        RPGArmor rpgBoots = null;

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

        if (entityTarget != null && entityTarget.getEquipment() != null) {
            if (RPGAbstractItem.getFromItemStack(entityTarget.getEquipment().getHelmet()) != null  && RPGAbstractItem.getFromItemStack(entityTarget.getEquipment().getHelmet()).getItemType().equals(RPGItemType.ARMOR))
                rpgHelmet = (RPGArmor) RPGAbstractItem.getFromItemStack(entityTarget.getEquipment().getHelmet());

            if (RPGAbstractItem.getFromItemStack(entityTarget.getEquipment().getChestplate()) != null  && RPGAbstractItem.getFromItemStack(entityTarget.getEquipment().getChestplate()).getItemType().equals(RPGItemType.ARMOR))
                rpgChestplate = (RPGArmor) RPGAbstractItem.getFromItemStack(entityTarget.getEquipment().getChestplate());

            if (RPGAbstractItem.getFromItemStack(entityTarget.getEquipment().getLeggings()) != null  && RPGAbstractItem.getFromItemStack(entityTarget.getEquipment().getLeggings()).getItemType().equals(RPGItemType.ARMOR))
                rpgLeggings = (RPGArmor) RPGAbstractItem.getFromItemStack(entityTarget.getEquipment().getLeggings());

            if (RPGAbstractItem.getFromItemStack(entityTarget.getEquipment().getBoots()) != null  && RPGAbstractItem.getFromItemStack(entityTarget.getEquipment().getBoots()).getItemType().equals(RPGItemType.ARMOR))
                rpgBoots = (RPGArmor) RPGAbstractItem.getFromItemStack(entityTarget.getEquipment().getBoots());
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
        RPGWeapon rpgWeapon = null;

        if (entityDamager != null && entityDamager.getEquipment() != null) {
            if (RPGAbstractItem.getFromItemStack(entityDamager.getEquipment().getItemInMainHand()) == null) {
                e.setDamage(1.0);
                return;
            }

            if (RPGAbstractItem.getFromItemStack(entityDamager.getEquipment().getItemInMainHand()).getItemType().equals(RPGItemType.WEAPON)) {
                rpgWeapon = (RPGWeapon) RPGAbstractItem.getFromItemStack(entityDamager.getEquipment().getItemInMainHand());

            }
        }

        if (rpgWeapon == null) {
            e.setDamage(1.0);
            return;
        }

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

        if (rpgWeapon.getRequiredLevel() > playerDamager.getLevel()) {
            playerDamager.sendMessage(Colors.parseColors("&cЭто оружие требует более высокий уровень для использования!"));
            e.setDamage(1.0);
            return;
        }

        // ПОНИЖЕНИЕ УРОНА ДЛЯ СТОЯЩИХ РЯДОМ ВРАГОВ ПРИ СВИП АТАКЕ И ОТСУТСТВИИ ЭНЕРГИИ
        if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) || playerDamager.getAttackCooldown() != 1.0) {
            switch (damageVariants.next()) {
                case "STAB":
                    finalDamage = (int) (Math.round((stabDamage - (totalStabProtection * 0.5)) * (IntegerUtils.getRandomInteger(7, 10) * 0.1))) / IntegerUtils.getRandomInteger(5, 8);
                    hologram.appendTextLine(Colors.parseColors("&a-" + finalDamage));

                    break;

                case "SLASH":
                    finalDamage = (int) (Math.round((slashDamage - (totalSlashProtection * 0.5)) * (IntegerUtils.getRandomInteger(7, 10) * 0.1))) / IntegerUtils.getRandomInteger(5, 8);
                    hologram.appendTextLine(Colors.parseColors("&a-" + finalDamage));

                    break;

                case "BLUNT":
                    finalDamage = (int) (Math.round((bluntDamage - (totalBluntProtection * 0.5)) * (IntegerUtils.getRandomInteger(7, 10) * 0.1))) / IntegerUtils.getRandomInteger(5, 8);
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
                        playerDamager.spawnParticle(Particle.BLOCK_CRACK, entityTarget.getLocation().add(0, 1.5, 0), 15, Material.REDSTONE_BLOCK.createBlockData());
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

        finalDamage = Math.abs(finalDamage);

        hologram.getVisibilityManager().setVisibleByDefault(false);
        hologram.getVisibilityManager().showTo(playerDamager);
        Bukkit.getScheduler().runTaskLaterAsynchronously(RPG.getInstance(), hologram::delete, 8);

        if (playerTarget != null) {
            PlayerData targetData = PlayerData.get(playerTarget.getUniqueId());

            if (finalDamage >= targetData.getHealth()) {
                Bukkit.getPlayer(targetData.getId()).setHealth(0.0);

                if (playerDamager != null)
                    PlayerData.get(playerDamager.getUniqueId()).setPlayerKills(PlayerData.get(playerDamager.getUniqueId()).getPlayerKills() + 1);

            } else {
                PlayerData.get(playerTarget.getUniqueId()).setHealth(PlayerData.get(playerTarget.getUniqueId()).getHealth() - finalDamage);
                e.setDamage(0.0);
            }

        } else {
            e.setDamage(finalDamage);
        }

        /*
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

         */
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (PlayerData.get(e.getEntity().getUniqueId()) == null)
            return;

        if (e.getEntity().getType().equals(EntityType.PLAYER)) {
            Player player = (Player) e.getEntity();
            PlayerData playerData = PlayerData.get(player.getUniqueId());

            if (e.getDamage() >= playerData.getHealth()) {
                player.setHealth(0.0);
                playerData.setDeaths(playerData.getDeaths()+1);
            } else {
                if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL) || e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING))
                    playerData.setHealth(playerData.getHealth()-(e.getDamage()*(playerData.getMaxHealth()/25)));
                else if (e.getCause().equals(EntityDamageEvent.DamageCause.POISON) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK))
                    playerData.setHealth(playerData.getHealth()-(e.getDamage()*(playerData.getMaxHealth()/100)));
                else
                    playerData.setHealth(playerData.getHealth()-e.getDamage());

                e.setDamage(0.0);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        PlayerData playerData = PlayerData.get(e.getEntity().getUniqueId());

        e.setDeathMessage(null);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        PlayerData playerData = PlayerData.get(e.getPlayer().getUniqueId());

        player.setHealth(20.0);
        playerData.setHealth(playerData.getMaxHealth()/2);
    }

    @EventHandler
    public void onEquip(ArmorEquipEvent e) {
        Player player = e.getPlayer();
        PlayerData playerData = PlayerData.get(e.getPlayer().getUniqueId());

        if (RPGAbstractItem.getFromItemStack(e.getNewArmorPiece()) != null && (RPGAbstractItem.getFromItemStack(e.getNewArmorPiece()) instanceof RPGArmor)) {
            RPGArmor rpgArmor = (RPGArmor) RPGAbstractItem.getFromItemStack(e.getNewArmorPiece());

            if (rpgArmor.getRequiredLevel() > playerData.getLevel()) {
                e.setCancelled(true);
                player.sendMessage(Colors.parseColors("&cЭта броня требует более высокий уровень для использования!"));
                return;
            }




        }
    }

}
