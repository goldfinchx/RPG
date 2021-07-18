package ru.artorium.rpg.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.artorium.rpg.RPG;

public class Titles {

    public static void sendDefaultTitle(Player player, TitleType titleType, String title, String subtitle) {

        switch (titleType) {
            case FAST:
                player.sendTitle(Colors.parseColors(title), Colors.parseColors(subtitle), 0, 40, 0);
                break;

            case CUTTED:
                player.sendTitle(Colors.parseColors(title), Colors.parseColors(subtitle), 0, 15, 0);
                break;

            case VERY_CUTTED:
                player.sendTitle(Colors.parseColors(title), Colors.parseColors(subtitle), 0, 5, 0);
                break;

            case LONG:
                player.sendTitle(Colors.parseColors(title), Colors.parseColors(subtitle), 25, 100, 30);
                break;

            case NORMAL:
                player.sendTitle(Colors.parseColors(title), Colors.parseColors(subtitle), 10, 70, 20);
                break;

            case TEST:
                player.sendTitle(Colors.parseColors("&c-"), Colors.parseColors(""), 10, 20, 20);
                Bukkit.getScheduler().runTaskLaterAsynchronously(RPG.getInstance(), () -> player.sendTitle(Colors.parseColors("&c--"), Colors.parseColors(""), 10, 20, 20),10);

                Bukkit.getScheduler().runTaskLaterAsynchronously(RPG.getInstance(), () -> player.sendTitle(Colors.parseColors("&c---"), Colors.parseColors(""), 10, 20, 20),20);
                break;

            default:
                break;
        }

    }
}
