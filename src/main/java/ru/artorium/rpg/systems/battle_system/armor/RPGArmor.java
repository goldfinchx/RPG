package ru.artorium.rpg.systems.battle_system.armor;

import com.mongodb.BasicDBObject;
import de.tr7zw.nbtapi.NBTItem;
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

public class RPGArmor extends RPGAbstractItem {

    @Getter private RPGArmorType armorType;
    @Getter private int slashProtection;
    @Getter private int stabProtection;
    @Getter private int bluntProtection;

    @Getter private Document additionalParameters;
    @Getter private int requiredLevel;
    @Getter private RPGSkill requiredSkill;
    @Getter private int requiredSkillLevel;

    public RPGArmor(String title, String description, RPGRarity rarity, Material material, String nbtTexture, int price,
                    RPGArmorType armorType, int slashProtection, int stabProtection, int bluntProtection, HashMap<RPGArmorParameter, Integer> additionalParameters,
                    int requiredLevel, RPGSkill requiredSkill, int requiredSkillLevel) {
        Document armorDocument = new Document();


        armorDocument.put("_id", getLastID());
        armorDocument.put("title", title);
        armorDocument.put("description", description);
        armorDocument.put("rarity", rarity.name());
        armorDocument.put("material", material.name());
        armorDocument.put("nbtTexture", nbtTexture);
        armorDocument.put("itemType", RPGItemType.ARMOR.name());
        armorDocument.put("price", price);

        armorDocument.put("armorType", armorType.name());
        armorDocument.put("stabProtection", stabProtection);
        armorDocument.put("slashProtection", slashProtection);
        armorDocument.put("bluntProtection", bluntProtection);

        Document additionalParametersDocument = new Document();
        additionalParameters.forEach((rpgArmorParameter, level) -> additionalParametersDocument.put(rpgArmorParameter.name(), level));
        armorDocument.put("additionalParameters", additionalParametersDocument);

        armorDocument.put("requiredLevel", requiredLevel);
        armorDocument.put("requiredSkill", requiredSkill.name() + "#" + requiredSkillLevel);

        getItemsCollection().insertOne(armorDocument);
    }

    private RPGArmor(String _id, String title, String description, RPGRarity rarity, Material material, String texture, int price,
                     RPGArmorType armorType, int slashProtection, int stabProtection, int bluntProtection, Document additionalParameters,
                     int requiredLevel, RPGSkill requiredSkill, int requiredSkillLevel) {

        this.set_id(_id);
        this.setTitle(title);
        this.setDescription(description);
        this.setItemType(RPGItemType.ARMOR);
        this.setRarity(rarity);
        this.setMaterial(material);
        this.setNbtTexture(texture);
        this.setPrice(price);

        this.armorType = armorType;
        this.slashProtection = slashProtection;
        this.stabProtection = stabProtection;
        this.bluntProtection = bluntProtection;
        this.additionalParameters = additionalParameters;
        this.requiredLevel = requiredLevel;
        this.requiredSkill = requiredSkill;
        this.requiredSkillLevel = requiredSkillLevel;
    }

    public static RPGArmor get(String _id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", _id);
        Document armorDocument = getItemsCollection().find(query).first();

        if (armorDocument == null)
            throw new NullPointerException("Броня с таким ID не найдена!");

        String title = armorDocument.getString("title");
        String description = armorDocument.getString("description");
        RPGRarity rarity = RPGRarity.valueOf(armorDocument.getString("rarity"));
        Material material = Material.valueOf(armorDocument.getString("material"));
        String texture = "default";
        try {
            texture = armorDocument.getString("nbtTexture");
        } catch (NullPointerException ignore) {}

        int price = 10;
        try {
            price = armorDocument.getInteger("price");
        } catch (NullPointerException ignore) {}

        RPGArmorType armorType = RPGArmorType.valueOf(armorDocument.getString("armorType"));
        int slashProtection = 0;
        int stabProtection = 0;
        int bluntProtection = 0;

        try {
            slashProtection = armorDocument.getInteger("slashProtection");
        } catch (NullPointerException ignore) {}

        try {
            stabProtection = armorDocument.getInteger("stabProtection");
        } catch (NullPointerException ignore) {}

        try {
            bluntProtection = armorDocument.getInteger("bluntProtection");
        } catch (NullPointerException ignore) {}

        Document additionalParameters = armorDocument.get("additionalParameters", Document.class);
        int requiredLevel = armorDocument.getInteger("requiredLevel");
        RPGSkill requiredSkill = RPGSkill.valueOf(armorDocument.getString("requiredSkill").split("#")[0]);
        int requiredSkillLevel = Integer.parseInt(armorDocument.getString("requiredSkill").split("#")[1]);

        return new RPGArmor(_id, title, description, rarity, material, texture, price, armorType,
                slashProtection, stabProtection, bluntProtection, additionalParameters, requiredLevel,
                requiredSkill, requiredSkillLevel);
    }


    @Override
    public ItemStack getItemStack() {
        ItemStack rpgArmor = super.getItemStack();
        ItemMeta meta = rpgArmor.getItemMeta();
        List<String> lore = meta.getLore();

        lore.add("§fТип: " + "§7" + this.armorType.getTitle());
        lore.add("§fПрочность: " + "§a" + 100 + "%");
        lore.add("");
        lore.add("§fХарактеристики:");
        if (stabProtection != 0) lore.add("§a" + this.stabProtection + " защиты от колющего урона");
        if (slashProtection != 0) lore.add("§a" + this.slashProtection + " защиты от режущего урона");
        if (bluntProtection != 0) lore.add("§a" + this.bluntProtection + " защиты от дробящего урона");
        try {
            if (additionalParameters.size() != 0) {
                lore.add("");
                this.additionalParameters.forEach(((rpgArmorParameter, level) -> lore.add("§a+" + level + "% " + RPGArmorParameter.getByString(rpgArmorParameter).getLoreString())));
            }
                 } catch (NullPointerException ignore) {}

        lore.add("");
        lore.add("§fТребования:");
        lore.add("§7- " + requiredSkillLevel + " " + requiredSkill.getIcon().getTitle().toLowerCase());
        lore.add("§7- " + requiredLevel + " уровень");
        lore.add("");

        meta.setLore(lore);
        rpgArmor.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(rpgArmor);
        nbtItem.setInteger("durability", 100);
        nbtItem.setInteger("stabProtection", this.stabProtection);
        nbtItem.setInteger("slashProtection", this.slashProtection);
        nbtItem.setInteger("bluntProtection", this.bluntProtection);

        return nbtItem.getItem();
    }
}
