package ru.artorium.rpg.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import ru.artorium.rpg.RPG;

public class ConfigUtils {

    private static final RPG plugin = RPG.getInstance();

    public static Location getLocationFromConfig(String locationName) {
        String path = "locations." + locationName + ".";

        World world = Bukkit.getWorld(plugin.getConfig().getString(path + "world"));
        double x = plugin.getConfig().getDouble(path + "x");
        double y = plugin.getConfig().getDouble(path + "y");
        double z = plugin.getConfig().getDouble(path + "z");
        float yaw = plugin.getConfig().getInt(path + "yaw");
        float pitch = plugin.getConfig().getInt(path + "pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }
}