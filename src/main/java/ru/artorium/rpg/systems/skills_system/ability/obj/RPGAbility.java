package ru.artorium.rpg.systems.skills_system.ability.obj;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashMap;

@AllArgsConstructor@Getter
public enum RPGAbility {

    LIGHT_PROTECTION("Защита тыла", new String[]{
            "",
            "§fУвеличивает параметр защиты",
            "§fдля легкой брони.",
            "",
            "§f1 ур. -> §6+10%",
            "§f2 ур. -> §6+20%",
            "§f3 ур. -> §6+30%",
            "§f4 ур. -> §6+40%",
            "§f5 ур. -> §6+50%",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(10, 10);
        put(25, 20);
        put(50, 30);
        put(75, 40);
        put(100, 50);
    }}),

    LIGHT_SPEED("Гонец", new String[]{
            "",
            "§fУвеличивает скорость перемещения",
            "§fв ПОЛНОМ КОМПЛЕКТЕ легкой брони.",
            "",
            "§f1 ур. -> §6+5%",
            "§f2 ур. -> §6+10%",
            "§f3 ур. -> §6+15%",
            "§f4 ур. -> §6+20%",
            "§f5 ур. -> §6+25%",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(20, 5);
        put(40, 10);
        put(60, 15);
        put(80, 20);
        put(100, 25);
    }}),

    LIGHT_SPEED_EFFECT("Погоня", new String[]{
            "",
            "§fСпособность, дающая на некоторое",
            "§fвремя Скорость II.",
            "",
            "§f1 ур. -> §6действие 15 сек.",
            "§f2 ур. -> §6действие 30 сек.",
            "§f3 ур. -> §6действие 60 сек.",
            "",
            "§fПерезарядка -> §6120 сек.",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(5, 15);
        put(25, 30);
        put(75, 60);
    }}),

    LIGHT_DODGING("Ловкач", new String[]{
            "",
            "§fУвеличивает шанс увернуться от удара,",
            "§fпри ношении ПОЛНОГО КОМПЛЕКТА лег.брони",
            "",
            "§f1 ур. -> §6+5%",
            "§f2 ур. -> §6+10%",
            "§f3 ур. -> §6+15%",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(30, 5);
        put(60, 10);
        put(90, 15);
    }}),

    MEDIUM_PROTECTION("Поддержка флангов", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(10, 10);
        put(25, 20);
        put(50, 30);
        put(75, 40);
        put(100, 50);
    }}),

    MEDIUM_BLEEDING_PROTECTION("Комплексное крепление", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(25, 5);
        put(50, 15);
        put(75, 25);
        put(100, 50);
    }}),

    MEDIUM_EFFECTS_SKILL("Неравная дуэль", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(5, 15);
        put(25, 30);
        put(75, 60);
    }}),

    MEDIUN_REGEN_SKILL("Чувство крови", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(70, 1);
    }}),
    
    HEAVY_PROTECTION("Первая шеренга", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(10, 10);
        put(25, 20);
        put(50, 30);
        put(75, 40);
        put(100, 50);
    }}),

    HEAVY_STUNNING_PROTECTION("Ватная обивка", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(25, 5);
        put(50, 15);
        put(75, 25);
        put(100, 50);
    }}),

    HEAVY_RESISTANCE_EFFECT("Приказ к атаке", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(5, 20);
        put(25, 40);
        put(75, 60);
    }}),

    HEAVY_WEIGHT_REMOVER("Здоровяк", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(100, 1);
    }}),

    ONE_HANDED_ATTACKING("Базовая тренировка", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(20, 10);
        put(40, 20);
        put(60, 30);
        put(80, 40);
        put(100, 50);
    }}),

    ONE_HANDED_ATTACKING_SPEED("Град ударов", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(10, 5);
        put(35, 15);
        put(65, 30);
    }}),

    ONE_HANDED_KNOCKBACK_SKILL("Удержание на расстоянии", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(15, 5);
        put(45, 15);
        put(75, 30);
    }}),

    ONE_HANDED_MAIN_SKILL("Напрыгивание", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(15, 1);
    }}),

    TWO_HANDED_ATTACKING("Импульсный удар", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(20, 10);
        put(40, 20);
        put(60, 30);
        put(80, 40);
        put(100, 50);
    }}),

    TWO_HANDED_ATTACKING_SPEED("Град ударов", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(10, 5);
        put(35, 15);
        put(65, 30);
    }}),

    TWO_HANDED_REACH_SKILL("Атакующая стойка", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(20, 5);
        put(45, 15);
        put(75, 30);
    }}),

    TWO_HANDED_MAIN_SKILL("", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(15, 1);
    }}),

    POLE_ATTACKING("Натиск", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(20, 10);
        put(40, 20);
        put(60, 30);
        put(80, 40);
        put(100, 50);
    }}),

    POLE_KNOCKBACK_SKILL("Удержание на расстоянии", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(15, 5);
        put(45, 15);
        put(75, 30);
    }}),

    POLE_REACH_SKILL("Атакующая стойка", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(20, 5);
        put(45, 15);
        put(75, 30);
    }}),

    POLE_MAIN_SKILL("Выпад", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(15, 1);
    }}),

    SHOOTING_ATTACKING("Максимальная натяжка", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(20, 10);
        put(40, 20);
        put(60, 30);
        put(80, 40);
        put(100, 50);
    }}),

    SHOOTING_ARROWS_ECONOMY("Повторное использование", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(35, 10);
        put(55, 20);
        put(95, 35);
    }}),

    SHOOTING_FIRE_ARROWS_SKILL("Огненный ливень", new String[]{
            "",
            "",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(5, 15);
        put(35, 30);
        put(80, 60);
    }}),

    SHOOTING_TRIPLESHOOT_SKILL("Тройной выстрел", new String[]{
            "",
            "",
            ""
    },   new LinkedHashMap<Integer, Integer>() {{
        put(50, 1);
    }}),

    CARE("Своевременный уход", new String[]{
            "",
            "§fУвеличивает прочность снаряжения.",
            "",
            "§f1 ур. -> §6+10%",
            "§f2 ур. -> §6+25%",
            "§f3 ур. -> §6+50%",
            "§f4 ур. -> §6+75%",
            "§f5 ур. -> §6+100%",
            ""
    }, new LinkedHashMap<Integer, Integer>() {{
        put(20, 10);
        put(40, 20);
        put(60, 30);
        put(80, 40);
        put(100, 50);
    }}),;

    private final String title;
    private final String[] description;
    private final LinkedHashMap<Integer, Integer> levels;
    /*
    TODO
    private final Consumer<Player> onActivate;
     */
}
