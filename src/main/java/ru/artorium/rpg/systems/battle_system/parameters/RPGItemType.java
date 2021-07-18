package ru.artorium.rpg.systems.battle_system.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor@Getter
public enum RPGItemType {

    QUEST("Квест-предмет"),
    WEAPON("Оружие"),
    ARMOR("Броня"),
    FOOD("Еда");

    public String title;
}
