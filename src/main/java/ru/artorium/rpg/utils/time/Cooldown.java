package ru.artorium.rpg.utils.time;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Cooldown {

    private final Map<UUID, Long> cooldownMap = new HashMap<>();

    private final long time;

    public Cooldown(TimeUnit timeUnit, long count) {
        this.time = timeUnit.toMillis(count);
    }

    public void put(UUID user) {
        cooldownMap.put(user, System.currentTimeMillis()+time);
    }

    public boolean hasCooldown(UUID user) {
        if(!cooldownMap.containsKey(user)) return false;
        return cooldownMap.get(user) > System.currentTimeMillis();
    }

    public String getFormattedTime(UUID user) {
        if(!hasCooldown(user)) return "0сек.";
        long m = cooldownMap.get(user)-System.currentTimeMillis();
        long mins = TimeUnit.MILLISECONDS.toMinutes(m);
        if(mins == 0) {
            return TimeUnit.MILLISECONDS.toSeconds(m) + "сек.";
        }
        return mins + "мин.";
    }

}
