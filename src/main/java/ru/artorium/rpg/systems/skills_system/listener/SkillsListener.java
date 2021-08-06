package ru.artorium.rpg.systems.skills_system.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ru.artorium.rpg.RPG;
import ru.artorium.rpg.systems.battle_system.armor.RPGArmor;
import ru.artorium.rpg.systems.battle_system.weapon.RPGWeapon;
import ru.artorium.rpg.systems.battle_system.obj.RPGAbstractItem;
import ru.artorium.rpg.player.obj.PlayerData;
import ru.artorium.rpg.systems.skills_system.skill.obj.RPGSkill;

public class SkillsListener implements Listener {

    /* TODO: Добавить листенеры:
        - катание на коне
        - красноречие
        - шахтерство
        - взлом
        - кузнечество
        - кулинария
     */

    @EventHandler (priority = EventPriority.LOW)
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getDamage() == 0.0)
            return;

        if (e.getDamager().getType().equals(EntityType.PLAYER)) {
            Player damager = (Player) e.getDamager();
            PlayerData damagerCharacter = PlayerData.get(damager.getUniqueId());

            if (RPGAbstractItem.getFromItemStack(damager.getInventory().getItemInMainHand()) != null) {
                if (RPGAbstractItem.getFromItemStack(damager.getInventory().getItemInMainHand()) instanceof RPGWeapon) {
                    RPGWeapon weapon = (RPGWeapon) RPGAbstractItem.getFromItemStack(damager.getInventory().getItemInMainHand());
                    RPGSkill skill = RPGSkill.valueOf(weapon.getWeaponType().name() + "_WEAPON");
                   // damagerCharacter.addSkillExperience(skill, 1);
                }
             }
        }

        if (e.getEntity().getType().equals(EntityType.PLAYER)) {
            Player target = (Player) e.getDamager();
            PlayerData targetCharacter = PlayerData.get(target.getUniqueId());

            double lightExperienceMultiplier = 0.0;
            double mediumExperienceMultiplier = 0.0;
            double heavyExperienceMultiplier = 0.0;

            if (RPGAbstractItem.getFromItemStack(target.getInventory().getHelmet()) != null) {
                if (RPGAbstractItem.getFromItemStack(target.getInventory().getHelmet()) instanceof RPGArmor) {
                    switch (((RPGArmor) RPGAbstractItem.getFromItemStack(target.getInventory().getHelmet())).getArmorType()) {
                        case LIGHT:
                            lightExperienceMultiplier = +0.25;
                            break;
                        case MEDIUM:
                            mediumExperienceMultiplier = +0.25;
                            break;

                        case HEAVY:
                            heavyExperienceMultiplier = +0.25;
                            break;
                    }
                }

            }
            if (RPGAbstractItem.getFromItemStack(target.getInventory().getChestplate()) != null) {
                if (RPGAbstractItem.getFromItemStack(target.getInventory().getChestplate()) instanceof RPGArmor) {
                    switch (((RPGArmor) RPGAbstractItem.getFromItemStack(target.getInventory().getChestplate())).getArmorType()) {
                        case LIGHT:
                            lightExperienceMultiplier = +0.25;
                            break;
                        case MEDIUM:
                            mediumExperienceMultiplier = +0.25;
                            break;

                        case HEAVY:
                            heavyExperienceMultiplier = +0.25;
                            break;
                    }
                }
            }

            if (RPGAbstractItem.getFromItemStack(target.getInventory().getLeggings()) != null) {
                if (RPGAbstractItem.getFromItemStack(target.getInventory().getLeggings()) instanceof RPGArmor) {
                    switch (((RPGArmor) RPGAbstractItem.getFromItemStack(target.getInventory().getLeggings())).getArmorType()) {
                        case LIGHT:
                            lightExperienceMultiplier = +0.25;
                            break;
                        case MEDIUM:
                            mediumExperienceMultiplier = +0.25;
                            break;

                        case HEAVY:
                            heavyExperienceMultiplier = +0.25;
                            break;
                    }
                }
            }
            if (RPGAbstractItem.getFromItemStack(target.getInventory().getBoots()) != null) {
                if (RPGAbstractItem.getFromItemStack(target.getInventory().getBoots()) instanceof RPGArmor) {
                    switch (((RPGArmor) RPGAbstractItem.getFromItemStack(target.getInventory().getBoots())).getArmorType()) {
                        case LIGHT:
                            lightExperienceMultiplier = +0.25;
                            break;
                        case MEDIUM:
                            mediumExperienceMultiplier = +0.25;
                            break;

                        case HEAVY:
                            heavyExperienceMultiplier = +0.25;
                            break;
                    }
                }
            }

            if (lightExperienceMultiplier != 0.0)
              //  targetCharacter.addSkillExperience(RPGSkill.LIGHT_ARMOR, lightExperienceMultiplier*1);

            if (mediumExperienceMultiplier != 0.0)
              //  targetCharacter.addSkillExperience(RPGSkill.MEDIUM_ARMOR, mediumExperienceMultiplier*1);

            if (heavyExperienceMultiplier != 0.0) {
                //  targetCharacter.addSkillExperience(RPGSkill.HEAVY_ARMOR, heavyExperienceMultiplier*1);
            }




        }
    }
    
}
