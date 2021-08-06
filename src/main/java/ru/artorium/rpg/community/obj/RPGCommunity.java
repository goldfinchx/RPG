package ru.artorium.rpg.community.obj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor@Getter
public enum RPGCommunity {

    NORTHUMBRIA("Нортумбрия"),
    NORMANS("Норманны"),
    CATHOLIC_CHURCH("Католическая церковь"),
    PEASANTS("Люди");

    private final String title;
}
