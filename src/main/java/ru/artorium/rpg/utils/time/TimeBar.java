package ru.artorium.rpg.utils.time;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TimeBar implements Listener {

    @Getter private String title;
    @Getter private final BossBar bossBar;
    @Getter private final Countdown countdown;
    @Getter private final Runnable onTimerEnd;

    private final boolean isPersonal;
    private final UUID barOwner;
    private final boolean killable;

    private TimeBar(@NonNull JavaPlugin plugin, String title, long time, BarColor color,
                    BarStyle style, Runnable onTimerEnd, boolean isPersonal, UUID barOwner, boolean listenEvents,
                    boolean reverseProgress, boolean killable) {

        TimeBar instance = this;

        this.title = title;
        this.bossBar = Bukkit.createBossBar(title, color, style);
        this.isPersonal = isPersonal;
        this.barOwner = barOwner;
        this.killable = killable;

        this.onTimerEnd = () -> {
            bossBar.removeAll();

            if (!isPersonal)
                HandlerList.unregisterAll(instance);

            onTimerEnd.run();
        };

        this.countdown = Countdown.builder().setTime(time).setPlugin(plugin).onTimerEnd(this.onTimerEnd).build();
        this.countdown.setOnSecond(() -> {
            double chance = (double) this.countdown.getPercentOfFullTime()/100;
            this.bossBar.setProgress(reverseProgress ? chance : (double)1 - chance);
            this.bossBar.setTitle(this.title.replace("%s", TimeUtils.getTimeToString(TimeUnit.SECONDS, countdown.getTimeSecs())));
        });

        if (listenEvents)
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void run() {
        countdown.run();

        if (!isPersonal)
            Bukkit.getOnlinePlayers().forEach(this.bossBar::addPlayer);
        else
            this.bossBar.addPlayer(Bukkit.getPlayer(barOwner));
    }

    public void stop() {
        getOnTimerEnd().run();
        getCountdown().getRunnable().cancel();
    }

    public void setTitle(String title) {
        this.title = title;
        this.bossBar.setTitle(title.replace("%s", TimeUtils.getTimeToString(TimeUnit.SECONDS, countdown.getTimeSecs())));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        if (isPersonal && event.getPlayer().getUniqueId().equals(barOwner) && countdown.getTimeSecs() <= 0 || (isPersonal && killable))
            return;


        if (isPersonal) {
            if (event.getPlayer().getUniqueId().equals(barOwner))
                bossBar.addPlayer(event.getPlayer());

        } else
            bossBar.addPlayer(event.getPlayer());


    }

    @EventHandler
    public void onPlayerQuits(PlayerQuitEvent event) {
        if (killable) {
            this.stop();
            return;
        }


        if (isPersonal) {
            if (event.getPlayer().getUniqueId().equals(barOwner))
                bossBar.removePlayer(event.getPlayer());

            return;
        }
        bossBar.removePlayer(event.getPlayer());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private JavaPlugin plugin;
        private String title;
        private long time;
        private BarColor color;
        private BarStyle style;
        private Runnable onTimerEnd;

        private boolean reverseProgress;

        private boolean listenEvents;
        private boolean isPersonal;
        private UUID barOwner;
        private boolean killable;

        public Builder() {
            this.listenEvents = true;
            this.isPersonal = false;
            this.barOwner = null;
            this.reverseProgress = false;
            this.killable = false;
        }

        public Builder setPlugin(JavaPlugin plugin) {
            this.plugin = plugin;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTimer(long timeInSeconds) {
            this.time = timeInSeconds;
            return this;
        }

        public Builder setColor(BarColor barColor) {
            this.color = barColor;
            return this;
        }

        public Builder setStyle(BarStyle style) {
            this.style = style;
            return this;
        }

        public Builder setKillable(boolean killable) {
            this.killable = killable;
            return this;
        }

        public Builder setRunnableOnTimerEnd(Runnable runnable) {
            this.onTimerEnd = runnable;
            return this;
        }

        public Builder setPersonal(boolean isPersonal, UUID barOwner) {
            this.isPersonal = isPersonal;
            this.barOwner = barOwner;
            return this;
        }

        public Builder setListeningEvents(boolean listen) {
            this.listenEvents = listen;
            return this;
        }

        public Builder reverseProgress(boolean reverse) {
            this.reverseProgress = reverse;
            return this;
        }

        public TimeBar build() {
            return new TimeBar(plugin, title, time, color, style, onTimerEnd, isPersonal, barOwner, listenEvents, reverseProgress, killable);
        }
    }
}