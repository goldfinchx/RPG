package ru.artorium.rpg.utils.time;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Countdown {

    @Getter
    @Setter
    private long timeSecs;
    @Getter
    @Setter
    private long fullTime;
    @Getter
    private BukkitTask runnable;

    private final JavaPlugin plugin;

    private final Runnable onTimerEnd;
    private Runnable onSecond;

    public Countdown(JavaPlugin plugin, long time, Runnable onTimerEnd) {
        this.plugin = plugin;

        this.timeSecs = time;
        this.fullTime = time;
        this.onTimerEnd = onTimerEnd;
    }

    public void setOnSecond(Runnable onSecond) {
        this.onSecond = onSecond;
    }

    public void run() {
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (timeSecs == 0) {
                    cancel();
                    onTimerEnd.run();
                    return;
                }

                timeSecs--;
                onSecond.run();
            }
        }.runTaskTimerAsynchronously(plugin, 20, 20);
    }

    public int getPercentOfFullTime() {
        return (int) (timeSecs * 100 / fullTime);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        @Getter private long time;
        @Getter private Runnable onTimerEnd;
        @Getter private Runnable onSecond;
        @Getter private JavaPlugin plugin;

        public Builder setTime(long time) {
            this.time = time;
            return this;
        }

        public Builder onTimerEnd(Runnable runnable) {
            this.onTimerEnd = runnable;
            return this;
        }

        public Builder onSecond(Runnable runnable) {
            this.onSecond = runnable;
            return this;
        }

        public Builder setPlugin(JavaPlugin plugin) {
            this.plugin = plugin;
            return this;
        }

        public Countdown build() {
            Countdown countdown = new Countdown(plugin, time, onTimerEnd);

            if (onSecond != null)
                countdown.setOnSecond(onSecond);

            return countdown;
        }
    }


}
