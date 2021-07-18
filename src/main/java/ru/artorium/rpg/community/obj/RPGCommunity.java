package ru.artorium.rpg.community.obj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor@Getter
public enum RPGCommunity {

    NORTHUMBRIA("Нортумбрия"),
    NORMANS("Норманны"),
    CATHOLIC_CHURCH("Католическая церковь");

    private final String title;

    public static RPGCommunity getByString(String title) {
        for (RPGCommunity rpgCommunity : values())
            if (rpgCommunity.getTitle().equalsIgnoreCase(title))
                return rpgCommunity;

        return null;
    }

}
