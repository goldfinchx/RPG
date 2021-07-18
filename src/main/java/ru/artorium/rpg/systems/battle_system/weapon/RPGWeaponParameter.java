package ru.artorium.rpg.systems.battle_system.weapon;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor@Getter
public enum RPGWeaponParameter {

    BLEEDING("Кровотечение", "к кровотечению"),
    STUNNING("Оглушение", "к оглушению"),
    CRITICAL_HIT("Критический удар", "к критическому удару"),
    POISING("Отравление", "к отравлению"),
    BURNING("Поджог", "к поджиганию");

    private final String title;
    private final String loreString;

    public static RPGWeaponParameter getByString(String string) {
        for (RPGWeaponParameter parameter : RPGWeaponParameter.values())
            if (parameter.name().equalsIgnoreCase(string) || parameter.getTitle().equalsIgnoreCase(string))
                return parameter;

        return null;
    }
}
