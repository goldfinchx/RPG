package ru.artorium.rpg.classes.obj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum RPGClass {

    // Классы англо-саксов
    FOOTMAN("Пехотинец", new String[]{
            "",
            "§fОсновная единица армии, в бою",
            "§fиспользует мечи, топоры, щиты",
            "§fи средне-тяжелую броню"
            }, ru.artorium.rpg.fraction.obj.RPGFraction.ANGLO_SAXONS),

    BOWMAN("Лучник", new String[]{
            "",
            "§fКласс отличающийся высокой мобильностью.",
            "§fВ бою использует лук или короткий меч,",
            "§fносит лёгкую броню"
            }, ru.artorium.rpg.fraction.obj.RPGFraction.ANGLO_SAXONS),

    SPEARMAN("Копейщик", new String[]{
            "",
            "§fТяжело вооруженный воин, имеет малую",
            "§fмобильность, но высокий уровень защиты",
            "§fи большую дальность атаки"
            }, ru.artorium.rpg.fraction.obj.RPGFraction.ANGLO_SAXONS),

    // Классы викингов
    RAIDER("Налётчик", new String[]{
            "",
            "§fОсновной боец севереных племён,",
            "§fносит лёгкую броню, использует",
            "§fтопоры и мечи"
            }, ru.artorium.rpg.fraction.obj.RPGFraction.VIKINGS),

    BERSERK("Берсерк", new String[]{
            "",
            "§fУникальный воин севера, использующий",
            "§fособые отвары, повыщающие их мощь"
            }, ru.artorium.rpg.fraction.obj.RPGFraction.VIKINGS),

    JAEGER("Егерь", new String[]{
            "",
            "§fОхотничьи отряды севера, использующие",
            "§fлуки и легкую броню в борьбе с врагами"
            }, ru.artorium.rpg.fraction.obj.RPGFraction.VIKINGS);

    private final String title;
    private final String[] description;
    private final ru.artorium.rpg.fraction.obj.RPGFraction RPGFraction;

    public static RPGClass getByString(String title) {
        for (RPGClass rpgClass : values())
            if (rpgClass.name().equalsIgnoreCase(title))
                return rpgClass;

            return null;
    }

}
