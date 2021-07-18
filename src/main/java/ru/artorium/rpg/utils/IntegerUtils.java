package ru.artorium.rpg.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

public class IntegerUtils {

    public static int getRandomInteger(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());

        }

        return result;
    }

    public static boolean getRandom(double chance) {
        return Math.random() < chance;
    }

    public static int getProcentFromInteger(double toFind, double from) {
        double procent = toFind/from;
        return (int) Math.round(procent*100);
    }
}
