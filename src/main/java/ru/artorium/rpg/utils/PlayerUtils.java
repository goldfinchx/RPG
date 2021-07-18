package ru.artorium.rpg.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import ru.artorium.rpg.RPG;

public class PlayerUtils {

    public static void removeAllPotionEffects(Player player) {
        for (PotionEffect potionEffect : player.getActivePotionEffects())
            player.removePotionEffect(potionEffect.getType());
    }

    public static boolean isInventoryHaveEmptySlots(Player player) {
        Inventory inventory = player.getInventory();

        return inventory.firstEmpty() != -1;
    }

    public static void hideAllPlayers(Player player) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> player.hidePlayer(RPG.getInstance(), onlinePlayer));
    }

    public static void hidePlayerFromOthers(Player player) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.hidePlayer(RPG.getInstance(), player));
    }

    public static void showAllPlayers(Player player) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> player.showPlayer(RPG.getInstance(), onlinePlayer));
    }

    public static void showPlayerForOthers(Player player) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.showPlayer(RPG.getInstance(), player));
    }

    public static boolean isSurfaceUnderPlayer(Location location) {
        location.add(0, 1, 0);
        Location location2 = location.add(0, 1, 0);

        return (location.getBlock().getType().equals(Material.AIR) && location2.getBlock().getType().equals(Material.AIR))
                || (location.getBlock().getType().equals(Material.WATER) && location2.getBlock().getType().equals(Material.WATER))
                || (location.getBlock().getType().equals(Material.WATER) && location2.getBlock().getType().equals(Material.AIR));
    }

    public static Location getRandomLocation(World world) {
        double x = IntegerUtils.getRandomInteger(-3000, -3000);
        double y = IntegerUtils.getRandomInteger(20, 30);
        double z = IntegerUtils.getRandomInteger(-3000, -3000);

        Location randomLocation = new Location(world, x, y, z);

        while (!isSurfaceUnderPlayer(randomLocation))
            randomLocation = new Location(world, x, y+1, z);

        return randomLocation;
    }
}
