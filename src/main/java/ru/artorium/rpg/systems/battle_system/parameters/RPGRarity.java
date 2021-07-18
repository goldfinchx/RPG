package ru.artorium.rpg.systems.battle_system.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor@Getter
public enum RPGRarity {

    COMMON("Обычное", "§7"),
    RARE("Редкое", "§6"),
    EPIC("Эпическое", "§d"),
    LEGENDARY("Легендарное", "§c");


    private final String title;
    private final String color;


    public static RPGRarity getByString(String string) {
        for (RPGRarity rpgRarity : RPGRarity.values())
            if (rpgRarity.name().equalsIgnoreCase(string) || rpgRarity.getTitle().equalsIgnoreCase(string))
                return rpgRarity;

        return null;
    }

}
