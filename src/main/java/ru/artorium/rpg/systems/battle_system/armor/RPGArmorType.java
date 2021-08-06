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
}
