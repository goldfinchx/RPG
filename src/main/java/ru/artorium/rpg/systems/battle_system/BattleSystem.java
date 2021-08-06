package ru.artorium.rpg.systems.battle_system;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.artorium.core.utils.design.Colors;
import ru.artorium.rpg.RPG;
import ru.artorium.rpg.player.obj.PlayerData;

public class BattleSystem {

    public static void runRegeneration() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(RPG.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerData playerData = PlayerData.get(player.getUniqueId());

                if (playerData.getHealth() != playerData.getMaxHealth()) {
                    if ((playerData.getMaxHealth() - playerData.getHealth()) >= playerData.getMaxHealth()/50)
                        playerData.setHealth(playerData.getHealth()+(playerData.getMaxHealth()/50));
                    else
                        playerData.setHealth(playerData.getMaxHealth());

                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Colors.parseColors("&c ❤ " + Math.round(playerData.getHealth()) + "/" + Math.round(playerData.getMaxHealth()) + " ⇧ ")));
                }
            }

        }, 0, 60);
    }

}
