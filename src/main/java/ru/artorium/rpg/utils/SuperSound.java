package ru.artorium.rpg.utils;

import lombok.AllArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@AllArgsConstructor
public enum SuperSound {
    FINE(Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F),
    ERROR(Sound.ENCHANT_THORNS_HIT, 1.0F, 0.5F),
    INFO(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.5F),
    OPEN_MENU(Sound.ENTITY_CHICKEN_EGG, 1.0F, 1.0F),
    CLOSE_MENU(Sound.ENTITY_HORSE_STEP, 1.0F, 1.0F),
    OPEN_LIST(Sound.ENTITY_SPIDER_STEP, 1.0F, 1.0F),
    EPIC(Sound.ITEM_TOTEM_USE, 1.0F, 0.5F),
    LEGENDARY(Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0F, 0.5F);

    private final Sound sound;
    private final float playbackSpeed;
    private final float volume;

    public void play(Player player) {
        player.playSound(player.getLocation(), this.sound, this.playbackSpeed, this.volume);
    }

}
