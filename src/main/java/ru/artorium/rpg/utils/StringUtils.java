package ru.artorium.rpg.utils;

import java.util.TreeMap;

public class StringUtils {

    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>(){{
        put(1000, "M");
        put(900, "CM");
        put(500, "D");
        put(400, "CD");
        put(100, "C");
        put(90, "XC");
        put(50, "L");
        put(40, "XL");
        put(10, "X");
        put(9, "IX");
        put(5, "V");
        put(4, "IV");
        put(1, "I");
        put(0, "×");
    }};

    public static String toRoman(int number) {
        int l =  map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number-l);
    }

    public static String getProgressString(int percent) {
        int scoresToPaint = percent/10;

        StringBuilder progressLineBuilder = new StringBuilder("§f" + percent + "% " + (scoresToPaint != 0 ? "§a" : "§7"));

        for (int i = 0; i < 10; i++) {
            progressLineBuilder.append("-");

            if (i+1 == scoresToPaint)
                progressLineBuilder.append("§7");
        }

        return progressLineBuilder.toString();
    }
}

