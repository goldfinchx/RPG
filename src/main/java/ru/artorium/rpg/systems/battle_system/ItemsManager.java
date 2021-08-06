package ru.artorium.rpg.systems.battle_system;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.artorium.core.utils.ItemBuilder;
import ru.artorium.rpg.systems.battle_system.item.RPGItem;
import ru.artorium.rpg.systems.battle_system.weapon.RPGWeapon;
import ru.artorium.rpg.systems.battle_system.armor.RPGArmor;
import ru.artorium.rpg.systems.battle_system.armor.RPGArmorParameter;
import ru.artorium.rpg.systems.battle_system.obj.RPGAbstractItem;
import ru.artorium.rpg.systems.battle_system.parameters.RPGItemType;
import ru.artorium.rpg.systems.battle_system.weapon.RPGWeaponParameter;
import ru.artorium.rpg.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemsManager {

    public static ItemStack getAsItem(RPGAbstractItem rpgItem) {
        ItemStack item = new ItemBuilder(rpgItem.getMaterial())
                .setDisplayName(rpgItem.getRarity().getColor() + rpgItem.getTitle())
                .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addItemFlag(ItemFlag.HIDE_UNBREAKABLE)
                .setUnbreakable(true)
                .build();

        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>(StringUtils.splitForLore(rpgItem.getDescription()));
        lore.add("");
        lore.add("§fТип предмета: " + rpgItem.getRarity().getColor() + rpgItem.getItemType().getTitle());
        lore.add("§fКачество: " + rpgItem.getRarity().getColor() + rpgItem.getRarity().getTitle());
        lore.add("");

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("_id", rpgItem.get_id());
        nbtItem.setString("type", rpgItem.getItemType().name());
        try {
            nbtItem.setString("texture", rpgItem.getNbtTexture());
        } catch (NullPointerException ignore) {}

        if (rpgItem instanceof RPGItem) {
            meta.setLore(lore);
            item.setItemMeta(meta);
        } else if (rpgItem instanceof RPGArmor) {
            RPGArmor rpgArmor = (RPGArmor) rpgItem;

            lore.add("§fТип: " + "§7" + rpgArmor.getArmorType().getTitle());
            lore.add("§fПрочность: " + "§a" + 100 + "%");
            lore.add("");
            lore.add("§fХарактеристики:");
            if (rpgArmor.getStabProtection() != 0) lore.add("§a" + rpgArmor.getStabProtection() + " защиты от колющего урона");
            if (rpgArmor.getSlashProtection() != 0) lore.add("§a" + rpgArmor.getSlashProtection() + " защиты от режущего урона");
            if (rpgArmor.getBluntProtection() != 0) lore.add("§a" + rpgArmor.getBluntProtection() + " защиты от дробящего урона");
            lore.add("");
            try {
                if (rpgArmor.getAdditionalParameters().size() != 0)
                    rpgArmor.getAdditionalParameters().forEach(((rpgArmorParameter, level) -> lore.add("§a+" + level + "% " + RPGArmorParameter.getByString(rpgArmorParameter).getLoreString())));
            } catch (NullPointerException ignore) {}

            lore.add("");
            lore.add("§fТребования:");
            lore.add("§7- " + rpgArmor.getRequiredSkillLevel() + " " + rpgArmor.getRequiredSkill().getIcon().getTitle().toLowerCase());
            lore.add("§7- " + rpgArmor.getRequiredLevel() + " уровень");
            lore.add("");

            meta.setLore(lore);
            item.setItemMeta(meta);

            nbtItem.setInteger("durability", 100);
            nbtItem.setInteger("stabProtection", rpgArmor.getStabProtection());
            nbtItem.setInteger("slashProtection", rpgArmor.getSlashProtection());
            nbtItem.setInteger("bluntProtection", rpgArmor.getBluntProtection());

        } else if (rpgItem instanceof RPGWeapon) {
            RPGWeapon rpgWeapon = (RPGWeapon) rpgItem;

            lore.add("§fТип оружия: " + "§7" + rpgWeapon.getWeaponType().getTitle());
            lore.add("§fПрочность: " + "§a" + 100 + "%");
            lore.add("");
            lore.add("§fХарактеристики:");
            if (rpgWeapon.getStabDamage() != 0) lore.add("§a" + rpgWeapon.getStabDamage() + " колющего урона");
            if (rpgWeapon.getSlashDamage() != 0) lore.add("§a" + rpgWeapon.getSlashDamage() + " режущего урона");
            if (rpgWeapon.getBluntDamage() != 0) lore.add("§a" + rpgWeapon.getBluntDamage() + " дробящего урона");
            lore.add("");
            try {
                if (rpgWeapon.getAdditionalParameters().size() != 0)
                    rpgWeapon.getAdditionalParameters().forEach(((rpgWeaponParameter, level) -> lore.add("§a+" + level + "% " + RPGWeaponParameter.getByString(rpgWeaponParameter).getLoreString())));
            } catch (NullPointerException ignore) {}

            lore.add("");
            lore.add("§fТребования:");
            lore.add("§7- " + rpgWeapon.getRequiredSkillLevel() + " " + rpgWeapon.getRequiredSkill().getIcon().getTitle().toLowerCase());
            lore.add("§7- " + rpgWeapon.getRequiredLevel() + " уровень");
            lore.add("");

            meta.setLore(lore);
            item.setItemMeta(meta);

            nbtItem.setInteger("durability", 100);
            nbtItem.setInteger("stabDamage", rpgWeapon.getStabDamage());
            nbtItem.setInteger("slashDamage", rpgWeapon.getSlashDamage());
            nbtItem.setInteger("bluntDamage", rpgWeapon.getBluntDamage());

            NBTCompoundList attribute = nbtItem.getCompoundList("AttributeModifiers");
            NBTListCompound attackSpeedMod = attribute.addCompound();
            switch (rpgWeapon.getWeaponType()) {
                case ONE_HANDED:
                    // nbtItem.setInteger("attackReach", 2);
                    attackSpeedMod.setDouble("Amount", -0.2);
                    break;
                case TWO_HANDED:
                    // nbtItem.setInteger("attackReach", 3);
                    attackSpeedMod.setDouble("Amount", -2.5);
                    break;
                case POLE_ARM:
                    // nbtItem.setInteger("attackReach", 5);
                    attackSpeedMod.setDouble("Amount", -3.3);
                    break;
                default:
                    break;
            }

            attackSpeedMod.setString("AttributeName", "generic.attackSpeed");
            attackSpeedMod.setString("Name", "generic.attackSpeed");
            attackSpeedMod.setInteger("Operation", 0);
            attackSpeedMod.setInteger("UUIDLeast", 59764);
            attackSpeedMod.setInteger("UUIDMost", 31483);
            attackSpeedMod.setString("Slot", "mainhand");
        }

        return nbtItem.getItem();
    }




    public static int getItemDurability(ItemStack itemStack) {
        int durability = 0;
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("durability"))
            durability = nbtItem.getInteger("durability");

        return durability;
    }
    public static void setItemDurability(ItemStack itemStack, int value) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("durability"))
            nbtItem.removeKey("durability");

        nbtItem.setInteger("durability", value);
        nbtItem.applyNBT(itemStack);

        updateItemStack(itemStack);
    }

    public static int getItemStabDamage(ItemStack itemStack) {
        int stabDamage = 0;
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("stabDamage"))
            stabDamage = nbtItem.getInteger("stabDamage");

        return stabDamage;
    }
    public static int getItemSlashDamage(ItemStack itemStack) {
        int slashDamage = 0;
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("slashDamage"))
            slashDamage = nbtItem.getInteger("slashDamage");

        return slashDamage;
    }
    public static int getItemBluntDamage(ItemStack itemStack) {
        int bluntDamage = 0;
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("bluntDamage"))
            bluntDamage = nbtItem.getInteger("bluntDamage");

        return bluntDamage;
    }

    public static void setItemStabDamage(ItemStack itemStack, int value) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("stabDamage"))
            nbtItem.removeKey("stabDamage");

        nbtItem.setInteger("stabDamage", value);
        nbtItem.applyNBT(itemStack);

        updateItemStack(itemStack);
    }
    public static void setItemSlashDamage(ItemStack itemStack, int value) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("slashDamage"))
            nbtItem.removeKey("slashDamage");

        nbtItem.setInteger("slashDamage", value);
        nbtItem.applyNBT(itemStack);

        updateItemStack(itemStack);
    }
    public static void setItemBluntDamage(ItemStack itemStack, int value) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("bluntDamage"))
            nbtItem.removeKey("bluntDamage");

        nbtItem.setInteger("bluntDamage", value);
        nbtItem.applyNBT(itemStack);

        updateItemStack(itemStack);
    }

    public static int getItemStabProtection(ItemStack itemStack) {
        int stabProtection = 0;
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("stabProtection"))
            stabProtection = nbtItem.getInteger("stabProtection");

        return stabProtection;
    }
    public static int getItemSlashProtection(ItemStack itemStack) {
        int slashProtection = 0;
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("slashProtection"))
            slashProtection = nbtItem.getInteger("slashProtection");

        return slashProtection;
    }
    public static int getItemBluntProtection(ItemStack itemStack) {
        int bluntProtection = 0;
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("bluntProtection"))
            bluntProtection = nbtItem.getInteger("bluntProtection");

        return bluntProtection;
    }

    public static void setItemStabProtection(ItemStack itemStack, int value) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("stabProtection"))
            nbtItem.removeKey("stabProtection");

        nbtItem.setInteger("stabProtection", value);
        nbtItem.applyNBT(itemStack);

        updateItemStack(itemStack);
    }
    public static void setItemSlashProtection(ItemStack itemStack, int value) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("slashProtection"))
            nbtItem.removeKey("slashProtection");

        nbtItem.setInteger("slashProtection", value);
        nbtItem.applyNBT(itemStack);

        updateItemStack(itemStack);
    }
    public static void setItemBluntProtection(ItemStack itemStack, int value) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("bluntProtection"))
            nbtItem.removeKey("bluntProtection");

        nbtItem.setInteger("bluntProtection", value);
        nbtItem.applyNBT(itemStack);

        updateItemStack(itemStack);
    }

    public static void updateItemStack(ItemStack itemStack) {
        RPGAbstractItem rpgItem = RPGAbstractItem.getFromItemStack(itemStack);
        NBTItem nbtItem = new NBTItem(itemStack);
        RPGItemType rpgItemType = RPGItemType.valueOf(nbtItem.getString("type"));

        ItemMeta meta = itemStack.getItemMeta();

        int durability;

        List<String> lore = new ArrayList<>(StringUtils.splitForLore(rpgItem.getDescription()));
        lore.add("");
        lore.add("§fТип предмета: " + rpgItem.getRarity().getColor() + rpgItemType.getTitle());
        lore.add("§fКачество: " + rpgItem.getRarity().getColor() + rpgItem.getRarity().getTitle());
        lore.add("");

        switch (rpgItemType) {
            case FOOD:
            case QUEST:
                break;

            case ARMOR:
                durability = getItemDurability(itemStack);
                int stabProtection = getItemStabProtection(itemStack);
                int slashProtection = getItemSlashProtection(itemStack);
                int bluntProtection = getItemBluntProtection(itemStack);

                lore.add("§fТип: " + "§7" + ((RPGArmor) rpgItem).getArmorType().getTitle());

                if (durability > 50) lore.add("§fПрочность: " + "§a" + durability + "%");
                else if (durability < 25) lore.add("§fПрочность: " + "§c" + durability + "%");
                else lore.add("§fПрочность: " + "§e" + durability + "%");

                lore.add("");
                lore.add("§fХарактеристики:");
                if (stabProtection != 0) lore.add("§a" + stabProtection + " защиты от колющего урона");
                if (slashProtection != 0) lore.add("§a" + slashProtection + " защиты от режущего урона");
                if (bluntProtection != 0) lore.add("§a" + bluntProtection + " защиты от дробящего урона");
                lore.add("");
                if (((RPGArmor) rpgItem).getAdditionalParameters().size() != 0)
                    ((RPGArmor) rpgItem).getAdditionalParameters().forEach(((rpgArmorParameter, level) -> lore.add("§a+" + level + "% " + RPGArmorParameter.getByString(rpgArmorParameter).getLoreString())));

                lore.add("");
                lore.add("§fТребования:");
                lore.add("§7- " + ((RPGArmor) rpgItem).getRequiredSkillLevel() + " " + ((RPGArmor) rpgItem).getRequiredSkill().getIcon().getTitle().toLowerCase());
                lore.add("§7- " + ((RPGArmor) rpgItem).getRequiredLevel() + " уровень");
                lore.add("");
                break;
            case WEAPON:
                durability = getItemDurability(itemStack);
                int stabDamage = getItemStabDamage(itemStack);
                int slashDamage = getItemSlashDamage(itemStack);
                int bluntDamage = getItemBluntDamage(itemStack);

                lore.add("§fТип: " + "§7" + ((RPGWeapon) rpgItem).getWeaponType().getTitle());

                if (durability > 50) lore.add("§fПрочность: " + "§a" + durability + "%");
                else if (durability < 25) lore.add("§fПрочность: " + "§c" + durability + "%");
                else lore.add("§fПрочность: " + "§e" + durability + "%");

                lore.add("");
                lore.add("§fХарактеристики:");
                if (stabDamage != 0) lore.add("§a" + stabDamage + " колющего урона");
                if (slashDamage != 0) lore.add("§a" + slashDamage + " режущего урона");
                if (bluntDamage != 0) lore.add("§a" + bluntDamage + " дробящего урона");
                lore.add("");
                if (((RPGWeapon) rpgItem).getAdditionalParameters().size() != 0)
                    ((RPGWeapon) rpgItem).getAdditionalParameters().forEach(((rpgWeaponParameter, level) -> lore.add("§a+" + level + "% " + RPGWeaponParameter.getByString(rpgWeaponParameter).getLoreString())));
                lore.add("");
                lore.add("§fТребования:");
                lore.add("§7- " + ((RPGWeapon) rpgItem).getRequiredSkillLevel() + " " + ((RPGWeapon) rpgItem).getRequiredSkill().getIcon().getTitle().toLowerCase());
                lore.add("§7- " + ((RPGWeapon) rpgItem).getRequiredLevel() + " уровень");
                lore.add("");
                break;

            default:
                break;
        }

        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }
}
