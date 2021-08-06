package ru.artorium.rpg.player.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import ru.artorium.core.utils.design.Colors;
import ru.artorium.rpg.RPG;
import ru.artorium.rpg.player.obj.PlayerData;

public class LevelsListener implements Listener {

    @EventHandler
    public void onLevelUp(PlayerLevelChangeEvent e) {
        Player player = e.getPlayer();
        PlayerData playerData = PlayerData.get(player.getUniqueId());

        if (e.getOldLevel() == 0)
            return;

        playerData.setLevel(player.getLevel());
        playerData.setSkillPoints(playerData.getSkillPoints()+(e.getNewLevel()-e.getOldLevel()));

        playerData.setMaxHealth(playerData.getMaxHealth()+(10*(e.getNewLevel()-e.getOldLevel())));
        player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.5F, 1.0F);
        player.sendTitle(Colors.parseColors("&6НОВЫЙ УРОВЕНЬ"), Colors.parseColors("Вам доступно &6" + playerData.getSkillPoints() + "&F очков умений"), 20*3, 20*3, 20*3);

        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage(Colors.parseColors("&6Ваш уровень повышен!"));
        player.sendMessage("");
        player.sendMessage("");
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent e) {
        Player player = e.getPlayer();
        PlayerData playerData = PlayerData.get(player.getUniqueId());

        playerData.setExperience(playerData.getExperience()+e.getAmount());

        player.sendMessage(Colors.parseColors("&a+" + e.getAmount() + " единиц опыта"));
    }

}
