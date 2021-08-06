package ru.artorium.rpg.systems.battle_system.armor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor@Getter
public enum RPGArmorParameter {

    BLEEDING_PROTECTION("Защита от кровотечения", "к защите от кровотечения"),
    STUNNING_PROTECTION("Защита от оглушения", "к защите от оглушения"),
    ADDITIONAL_HEALTH("Дополнительное здоровье", "к здоровью"),
    ;

    private final String title;
    private final String loreString;

    public static RPGArmorParameter getByString(String string) {
        for (RPGArmorParameter parameter : RPGArmorParameter.values())
            if (parameter.name().equalsIgnoreCase(string) || parameter.getTitle().equalsIgnoreCase(string))
                return parameter;

        return null;
    }
}
