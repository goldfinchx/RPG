package ru.artorium.rpg.systems.battle_system.armor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RPGArmorType {

    LIGHT("Легкая броня"),
    MEDIUM("Средняя броня"),
    HEAVY("Тяжелая броня");

    private final String title;

    public static RPGArmorType getByString(String string) {
        for (RPGArmorType rpgArmorType : RPGArmorType.values())
            if (rpgArmorType.name().equalsIgnoreCase(string) || rpgArmorType.getTitle().equalsIgnoreCase(string))
                return rpgArmorType;

        return null;
    }
}
