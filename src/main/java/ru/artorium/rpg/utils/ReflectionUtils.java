package ru.artorium.rpg.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionUtils {

    private static final Map<String, Class<?>> classCache = new HashMap<>();
    private static final String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

    public static Class<?> getMinecraftClass(final String pathToClass) throws ClassNotFoundException {
        Class<?> cached = classCache.get(pathToClass);

        if (cached == null) {
            final String className = String.format(pathToClass, version);
            cached = Class.forName(className);
            classCache.put(pathToClass, cached);
        }

        return cached;
    }

    public static int getPing(final Player player) {
        try {
            final Object entityPlayer = getMinecraftClass("org.bukkit.craftbukkit.%s.entity.CraftPlayer").getMethod("getHandle").invoke(player);
            final Object ping = getMinecraftClass("net.minecraft.server.%s.EntityPlayer").getField("ping").get(entityPlayer);
            return (int) ping;
        } catch (final ClassNotFoundException | NoSuchMethodException | SecurityException |
                IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static ItemStack addCanPlaceOn(final ItemStack item, final String... minecraftItemNames) {
        try {
            final Class<?> craftItemStackClass = getMinecraftClass("org.bukkit.craftbukkit.%s.inventory.CraftItemStack");
            final Object nmsItemStack = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
            final Class<?> nbtClass = getMinecraftClass("net.minecraft.server.%s.NBTTagCompound");
            Object nbt = nmsItemStack.getClass().getMethod("getTag").invoke(nmsItemStack);
            if (nbt == null) {
                nbt = nbtClass.getConstructor().newInstance();
            }

            final Object nbtList = getMinecraftClass("net.minecraft.server.%s.NBTTagList").getConstructor().newInstance();
            for (String minecraftItemName : minecraftItemNames) {
                if (!minecraftItemName.contains("minecraft")) {
                    minecraftItemName = "minecraft:" + minecraftItemName;
                }

                final Constructor<?> constr = getMinecraftClass("net.minecraft.server.%s.NBTTagString").getConstructor(String.class);
                constr.setAccessible(true);
                final Object nbtString = constr.newInstance(minecraftItemName);
                nbtList.getClass().getMethod("add", Object.class).invoke(nbtList, nbtString);
            }

            nbtClass.getMethod("set", String.class, getMinecraftClass("net.minecraft.server.%s.NBTBase")).invoke(nbt, "CanPlaceOn", nbtList);
            nmsItemStack.getClass().getMethod("setTag", nbtClass).invoke(nmsItemStack, nbt);
            final Object bukkitItemStack = craftItemStackClass.getMethod("asBukkitCopy", nmsItemStack.getClass()).invoke(null, nmsItemStack);
            return (ItemStack) bukkitItemStack;
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException |
                InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException e) {
            e.printStackTrace();
            return item;
        }
    }

    public static ItemStack addCanDestroy(final ItemStack item, final String... minecraftItemNames) {
        try {
            final Class<?> craftItemStackClass = getMinecraftClass("org.bukkit.craftbukkit.%s.inventory.CraftItemStack");
            final Object nmsItemStack = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
            final Class<?> nbtClass = getMinecraftClass("net.minecraft.server.%s.NBTTagCompound");
            Object nbt = nmsItemStack.getClass().getMethod("getTag").invoke(nmsItemStack);
            if (nbt == null) {
                nbt = nbtClass.getConstructor().newInstance();
            }
            final Object nbtList = getMinecraftClass("net.minecraft.server.%s.NBTTagList").getConstructor().newInstance();
            for (String minecraftItemName : minecraftItemNames) {
                if (!minecraftItemName.contains("minecraft")) {
                    minecraftItemName = "minecraft:" + minecraftItemName;
                }

                final Constructor<?> constr = getMinecraftClass("net.minecraft.server.%s.NBTTagString").getConstructor(String.class);
                constr.setAccessible(true);
                final Object nbtString = constr.newInstance(minecraftItemName);
                nbtList.getClass().getMethod("add", Object.class).invoke(nbtList, nbtString);
            }
            nbtClass.getMethod("set", String.class, getMinecraftClass("net.minecraft.server.%s.NBTBase")).invoke(nbt, "CanDestroy", nbtList);
            nmsItemStack.getClass().getMethod("setTag", nbtClass).invoke(nmsItemStack, nbt);
            final Object bukkitItemStack = craftItemStackClass.getMethod("asBukkitCopy", nmsItemStack.getClass()).invoke(null, nmsItemStack);
            return (ItemStack) bukkitItemStack;
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException |
                InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException e) {
            e.printStackTrace();
            return item;
        }
    }

    public static CommandMap getCommandMap() {
        try {
            final Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            return (CommandMap) field.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerCommand(final String name, final Command command) {
        getCommandMap().register(name, command);
    }

    public static Map<String, Command> getKnownCommands() {
        try {
            final CommandMap map = getCommandMap();
            return (Map<String, Command>) map.getClass().getMethod("getKnownCommands").invoke(map);
        } catch (final InvocationTargetException | IllegalAccessException | SecurityException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unregisterCommand(final Command command) {
        final List<String> names = new ArrayList<>();
        names.add(command.getName());
        names.addAll(command.getAliases());
        command.unregister(getCommandMap());
        names.forEach(getKnownCommands()::remove);
    }


    public static List<String> materialToMinecraftName(final Material... materials) {
        final List<String> itemNames = new ArrayList<>();
        try {
            final Class<?> magicNumbersClass = ReflectionUtils.getMinecraftClass("org.bukkit.craftbukkit.%s.util.CraftMagicNumbers");
            final Class<?> nmsItemClass = ReflectionUtils.getMinecraftClass("net.minecraft.server.%s.Item");
            final Method getItemMethod = magicNumbersClass.getMethod("getItem", Material.class);
            final Method getNameMethod = nmsItemClass.getMethod("getName");
            for (final Material material : materials) {
                final Object nmsItem = getItemMethod.invoke(null, material);
                String minecraftName = (String) getNameMethod.invoke(nmsItem);
                if (minecraftName.startsWith("item.minecraft.")) {
                    minecraftName = minecraftName.replace("item.minecraft.", "minecraft:");
                }
                if (minecraftName.startsWith("block.minecraft.")) {
                    minecraftName = minecraftName.replace("block.minecraft.", "minecraft:");
                }
                itemNames.add(minecraftName);
            }
        } catch (final ClassNotFoundException | NoSuchMethodException | SecurityException | ClassCastException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return itemNames;
    }

    private static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    private static Class<?> getOBClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
        } catch (ClassNotFoundException var3) {
            var3.printStackTrace();
            return null;
        }
    }


    public static String itemToStringBlob(ItemStack itemStack) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("i", itemStack);
        return config.saveToString();
    }

    public static ItemStack stringBlobToItem(String stringBlob) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(stringBlob);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return config.getItemStack("i", null);
    }



}

