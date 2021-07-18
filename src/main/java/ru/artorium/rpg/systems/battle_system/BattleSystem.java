package ru.artorium.rpg.systems.battle_system;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import ru.artorium.rpg.RPG;
import ru.artorium.rpg.player.obj.PlayerData;
import ru.artorium.rpg.utils.Colors;

public class BattleSystem {

    public static void runRegeneration() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(RPG.getInstance(), () -> {
            for (PlayerData playerData : RPG.getInstance().getPlayers().values()) {
                if (playerData.getHealth() != playerData.getMaxHealth()) {
                    if ((playerData.getMaxHealth()- playerData.getHealth()) >= playerData.getMaxHealth()/25)
                        playerData.setHealth(playerData.getHealth()+(playerData.getMaxHealth()/25));
                    else
                        playerData.setHealth(playerData.getMaxHealth());

                    Bukkit.getPlayer(playerData.get_id()).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Colors.parseColors("&c ❤ " + Math.round(playerData.getHealth()) + "/" + Math.round(playerData.getMaxHealth()) + " ⇧ ")));
                }
            }



        }, 0, 60);


    }
}
