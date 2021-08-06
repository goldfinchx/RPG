package ru.artorium.rpg.systems.battle_system.weapon;

import com.mongodb.BasicDBObject;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.artorium.core.utils.ItemBuilder;
import ru.artorium.rpg.systems.battle_system.obj.RPGAbstractItem;
import ru.artorium.rpg.systems.battle_system.parameters.RPGItemType;
import ru.artorium.rpg.systems.battle_system.parameters.RPGRarity;
import ru.artorium.rpg.systems.skills_system.skill.obj.RPGSkill;
import ru.artorium.rpg.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RPGWeapon extends RPGAbstractItem {

    @Getter private RPGWeaponType weaponType;
    @Getter private int stabDamage;
    @Getter private int slashDamage;
    @Getter private int bluntDamage;

    @Getter private Document additionalParameters;
    @Getter private int requiredLevel;
    @Getter private RPGSkill requiredSkill;
    @Getter private int requiredSkillLevel;

    public RPGWeapon(String title, String description, RPGRarity rarity, Material material, String nbtTexture, int price,
                    RPGWeaponType weaponType, int slashDamage, int stabDamage, int bluntDamage, HashMap<RPGWeaponParameter, Integer> additionalParameters,
                    int requiredLevel, RPGSkill requiredSkill, int requiredSkillLevel) {

        Document weaponDocument = new Document();

        weaponDocument.put("_id", getLastID());
        weaponDocument.put("title", title);
        weaponDocument.put("description", description);
        weaponDocument.put("rarity", rarity.name());
        weaponDocument.put("material", material.name());
        weaponDocument.put("nbtTexture", nbtTexture);
        weaponDocument.put("itemType", RPGItemType.ARMOR.name());
        weaponDocument.put("price", price);

        weaponDocument.put("weaponType", weaponType.name());
        weaponDocument.put("stabDamage", stabDamage);
        weaponDocument.put("slashDamage", slashDamage);
        weaponDocument.put("bluntDamage", bluntDamage);

        Document additionalParametersDocument = new Document();
        additionalParameters.forEach((rpgWeaponParameter, level) -> additionalParametersDocument.put(rpgWeaponParameter.name(), level));
        weaponDocument.put("additionalParameters", additionalParametersDocument);

        weaponDocument.put("requiredLevel", requiredLevel);
        weaponDocument.put("requiredSkill", requiredSkill.name() + "#" + requiredSkillLevel);

        getItemsCollection().insertOne(weaponDocument);
    }

    private RPGWeapon(String _id, String title, String description, RPGRarity rarity, Material material, String texture, int price,
                     RPGWeaponType weaponType, int slashDamage, int stabDamage, int bluntDamage, Document additionalParameters,
                     int requiredLevel, RPGSkill requiredSkill, int requiredSkillLevel) {

        this.set_id(_id);
        this.setTitle(title);
        this.setDescription(description);
        this.setItemType(RPGItemType.WEAPON);
        this.setRarity(rarity);
        this.setMaterial(material);
        this.setNbtTexture(texture);
        this.setPrice(price);

        this.weaponType = weaponType;
        this.slashDamage = slashDamage;
        this.stabDamage = stabDamage;
        this.bluntDamage = bluntDamage;
        this.additionalParameters = additionalParameters;
        this.requiredLevel = requiredLevel;
        this.requiredSkill = requiredSkill;
        this.requiredSkillLevel = requiredSkillLevel;
    }


    public static RPGWeapon get(String _id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", _id);
        Document weaponDocument = getItemsCollection().find(query).first();

        if (weaponDocument == null)
            throw new NullPointerException("Оружие с таким ID не найдено!");

        String title = weaponDocument.getString("title");
        String description = weaponDocument.getString("description");
        RPGRarity rarity = RPGRarity.valueOf(weaponDocument.getString("rarity"));
        Material material = Material.valueOf(weaponDocument.getString("material"));

        String texture = "default";
        try {
            texture = weaponDocument.getString("nbtTexture");
        } catch (NullPointerException ignore) {}

        int price = 10;
        try {
            price = weaponDocument.getInteger("price");
        } catch (NullPointerException ignore) {}

        RPGWeaponType weaponType = RPGWeaponType.valueOf(weaponDocument.getString("weaponType"));
        int slashDamage = 0;
        int stabDamage = 0;
        int bluntDamage = 0;

        try {
            slashDamage = weaponDocument.getInteger("slashDamage");
        } catch (NullPointerException ignore) {}

        try {
            stabDamage = weaponDocument.getInteger("stabDamage");
        } catch (NullPointerException ignore) {}

        try {
            bluntDamage = weaponDocument.getInteger("bluntDamage");
        } catch (NullPointerException ignore) {}



        Document additionalParameters = weaponDocument.get("additionalParameters", Document.class);
        int requiredLevel = weaponDocument.getInteger("requiredLevel");

        RPGSkill requiredSkill = null;
        int requiredSkillLevel = 0;

        try {
            requiredSkill = RPGSkill.valueOf(weaponDocument.getString("requiredSkill").split("#")[0]);
        } catch (NullPointerException ignore) {}

        try {
            requiredSkillLevel = Integer.parseInt(weaponDocument.getString("requiredSkill").split("#")[1]);
        } catch (NullPointerException ignore) {}

        return new RPGWeapon(_id, title, description, rarity, material, texture, price, weaponType,
                slashDamage, stabDamage, bluntDamage, additionalParameters, requiredLevel,
                requiredSkill, requiredSkillLevel);
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack rpgWeapon = super.getItemStack();
        ItemMeta meta = rpgWeapon.getItemMeta();

        List<String> lore = meta.getLore();
        lore.add("§fТип оружия: " + "§7" + this.weaponType.getTitle());
        lore.add("§fПрочность: " + "§a" + 100 + "%");
        lore.add("");
        lore.add("§fХарактеристики:");
        if (stabDamage != 0) lore.add("§a" + this.stabDamage + " колющего урона");
        if (slashDamage != 0) lore.add("§a" + this.slashDamage + " режущего урона");
        if (bluntDamage != 0) lore.add("§a" + this.bluntDamage + " дробящего урона");
        try {
            if (additionalParameters.size() != 0) {
                lore.add("");
                this.additionalParameters.forEach(((rpgWeaponParameter, level) -> lore.add("§a+" + level + "% " + RPGWeaponParameter.getByString(rpgWeaponParameter).getLoreString())));
            }
        } catch (NullPointerException ignore) {}

        lore.add("");
        lore.add("§fТребования:");

        if (requiredSkill != null)
            lore.add("§7- " + requiredSkillLevel + " " + requiredSkill.getIcon().getTitle().toLowerCase());

        if (requiredLevel != 0)
            lore.add("§7- " + requiredLevel + " уровень");

        lore.add("");

        meta.setLore(lore);
        rpgWeapon.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(rpgWeapon);
        nbtItem.setInteger("durability", 100);
        nbtItem.setInteger("stabDamage", this.stabDamage);
        nbtItem.setInteger("slashDamage", this.slashDamage);
        nbtItem.setInteger("bluntDamage", this.bluntDamage);

        NBTCompoundList attribute = nbtItem.getCompoundList("AttributeModifiers");
        NBTListCompound attackSpeedMod = attribute.addCompound();
        switch (weaponType) {
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

        return nbtItem.getItem();
    }
}
