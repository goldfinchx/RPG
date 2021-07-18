package ru.artorium.rpg.systems.battle_system.weapon;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor@Getter
public enum RPGWeaponType {

    ONE_HANDED("Одноручное оружие"),
    TWO_HANDED("Двуручное оружие"),
    POLE_ARM("Древковое оружие"),
    SHOOTING("Стрелковое оружие");

    private final String title;

    public static RPGWeaponType getByString(String string) {
        for (RPGWeaponType rpgWeaponType : RPGWeaponType.values())
            if (rpgWeaponType.name().equalsIgnoreCase(string) || rpgWeaponType.getTitle().equalsIgnoreCase(string))
                return rpgWeaponType;

        return null;
    }
}
