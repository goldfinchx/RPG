package ru.artorium.rpg.systems.battle_system.obj;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.artorium.rpg.RPG;
import ru.artorium.rpg.systems.battle_system.parameters.RPGItemType;
import ru.artorium.rpg.systems.battle_system.armor.RPGArmorParameter;
import ru.artorium.rpg.systems.battle_system.armor.RPGArmorType;
import ru.artorium.rpg.systems.battle_system.parameters.RPGRarity;
import ru.artorium.rpg.systems.battle_system.weapon.RPGWeaponParameter;
import ru.artorium.rpg.systems.battle_system.weapon.RPGWeaponType;
import ru.artorium.rpg.systems.skills_system.skill.obj.RPGSkill;
import ru.artorium.rpg.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class RPGItem {

    private final static MongoCollection<Document> itemsCollection =
            RPG.getInstance().getMongo().getCollection("items");

    @Getter private final Document itemDocument;

    @Getter@Setter private String _id;
    @Getter@Setter private String title;
    @Getter@Setter private String description;
    @Getter@Setter private RPGItemType itemType;
    @Getter@Setter private RPGRarity rarity;
    @Getter@Setter private Material material;
    @Getter@Setter private String nbtTexture;

    @Getter private RPGWeaponType weaponType;
    @Getter private int stabDamage;
    @Getter private int slashDamage;
    @Getter private int bluntDamage;

    @Getter private RPGArmorType armorType;
    @Getter private int slashProtection;
    @Getter private int stabProtection;
    @Getter private int bluntProtection;

    @Getter private Document additionalParameters;
    @Getter private int requiredLevel;
    @Getter private RPGSkill requiredSkill;
    @Getter private int requiredSkillLevel;

    public RPGItem(String _id) {
        this.set_id(_id);

        BasicDBObject itemQuery = new BasicDBObject();
        itemQuery.put("_id", _id);
        this.itemDocument = itemsCollection.find(itemQuery).first();

        this.setItemType(RPGItemType.valueOf(itemDocument.getString("itemType")));
        this.setTitle(itemDocument.getString("title"));
        this.setDescription(itemDocument.getString("description"));
        this.setRarity(RPGRarity.getByString(itemDocument.getString("rarity")));
        this.setMaterial(Material.valueOf(itemDocument.getString("material")));
        this.setNbtTexture(itemDocument.getString("nbtTexture"));

        switch (this.getItemType()) {
            case WEAPON:
                this.weaponType = RPGWeaponType.getByString(itemDocument.getString("weaponType"));
                this.stabDamage = itemDocument.getInteger("stabDamage");
                this.slashDamage = itemDocument.getInteger("slashDamage");
                this.bluntDamage = itemDocument.getInteger("bluntDamage");

                this.additionalParameters = (Document) itemDocument.get("additionalParameters");
                this.requiredLevel = itemDocument.getInteger("requiredLevel");
                this.requiredSkill = RPGSkill.valueOf(itemDocument.getString("requiredSkill").split("#")[0]);
                this.requiredSkillLevel = Integer.parseInt(itemDocument.getString("requiredSkill").split("#")[1]);
                break;
            case ARMOR:
                this.armorType = RPGArmorType.getByString(itemDocument.getString("armorType"));
                this.stabProtection = itemDocument.getInteger("stabProtection");
                this.slashProtection = itemDocument.getInteger("slashProtection");
                this.bluntProtection = itemDocument.getInteger("bluntProtection");

                this.additionalParameters = (Document) itemDocument.get("additionalParameters");
                this.requiredLevel = itemDocument.getInteger("requiredLevel");
                this.requiredSkill = RPGSkill.valueOf(itemDocument.getString("requiredSkill").split("#")[0]);
                this.requiredSkillLevel = Integer.parseInt(itemDocument.getString("requiredSkill").split("#")[1]);
                break;
            case FOOD:
            case QUEST:
            default:
                break;
        }

    }

    public static void createArmor(String title, String description, RPGRarity rarity, Material material, String nbtTexture,
                              RPGArmorType armorType, int slashProtection, int stabProtection, int bluntProtection, HashMap<RPGArmorParameter, Integer> additionalParameters,
                              int requiredLevel, RPGSkill requiredSkill, int requiredSkillLevel) {

        Document armorDocument = new Document();

        String lastArmorId = getLastArmorID();
        armorDocument.put("_id", lastArmorId);
        armorDocument.put("title", title);
        armorDocument.put("description", description);
        armorDocument.put("rarity", rarity.name());
        armorDocument.put("material", material.name());
        armorDocument.put("nbtTexture", nbtTexture);
        armorDocument.put("itemType", RPGItemType.ARMOR.name());

        armorDocument.put("armorType", armorType.name());
        armorDocument.put("stabProtection", stabProtection);
        armorDocument.put("slashProtection", slashProtection);
        armorDocument.put("bluntProtection", bluntProtection);

        Document additionalParametersDocument = new Document();
        additionalParameters.forEach((rpgArmorParameter, level) -> additionalParametersDocument.put(rpgArmorParameter.name(), level));
        armorDocument.put("additionalParameters", additionalParametersDocument);

        armorDocument.put("requiredLevel", requiredLevel);
        armorDocument.put("requiredSkill", requiredSkill.name() + "#" + requiredSkillLevel);

        itemsCollection.insertOne(armorDocument);
        RPGItem createdArmor = new RPGItem(lastArmorId);
        RPG.getInstance().getRpgItems().add(createdArmor);
    }

    public static void createWeapon(String title, String description, RPGRarity rarity, Material material, String nbtTexture,
                              RPGWeaponType weaponType, int stabDamage, int slashDamage, int bluntDamage, HashMap<RPGWeaponParameter, Integer> additionalParameters,
                              int requiredLevel, RPGSkill requiredSkill, int requiredSkillLevel) {

        Document weaponDocument = new Document();

        String lastWeaponId = getLastWeaponID();

        weaponDocument.put("_id", lastWeaponId);
        weaponDocument.put("title", title);
        weaponDocument.put("description", description);
        weaponDocument.put("rarity", rarity.name());
        weaponDocument.put("material", material.name());
        weaponDocument.put("nbtTexture", nbtTexture);
        weaponDocument.put("weaponType", weaponType.name());
        weaponDocument.put("itemType", RPGItemType.WEAPON.name());

        weaponDocument.put("stabDamage", stabDamage);
        weaponDocument.put("slashDamage", slashDamage);
        weaponDocument.put("bluntDamage", bluntDamage);

        Document additionalParametersDocument = new Document();
        additionalParameters.forEach((rpgWeaponParameter, level) -> additionalParametersDocument.put(rpgWeaponParameter.name(), level));
        weaponDocument.put("additionalParameters", additionalParametersDocument);

        weaponDocument.put("requiredLevel", requiredLevel);
        weaponDocument.put("requiredSkill", requiredSkill.name() + "#" + requiredSkillLevel);

        itemsCollection.insertOne(weaponDocument);
        RPGItem createdWeapon = new RPGItem(lastWeaponId);
        RPG.getInstance().getRpgItems().add(createdWeapon);
    }

    public static void createItem(String title, String description, RPGRarity rarity, Material material, String nbtTexture, RPGItemType itemType) {

        Document itemDocument = new Document();

        String lastItemId = getLastItemID();

        itemDocument.put("_id", lastItemId);
        itemDocument.put("title", title);
        itemDocument.put("description", description);
        itemDocument.put("rarity", rarity.name());
        itemDocument.put("material", material.name());
        itemDocument.put("nbtTexture", nbtTexture);
        itemDocument.put("itemType", itemType.name());

        itemsCollection.insertOne(itemDocument);
        RPGItem createItem = new RPGItem(lastItemId);
        RPG.getInstance().getRpgItems().add(createItem);
    }

    private static String getLastArmorID() {
        MongoCursor<Document> cursor = itemsCollection.find().iterator();
        int _id = 0;

        while (cursor.hasNext()) {
            Document document = cursor.next();
            if (RPGItemType.valueOf(document.getString("itemType")).equals(RPGItemType.ARMOR))
                _id = Integer.parseInt(document.getString("_id").replace("A", ""));
        }

        return "A" + (_id+1);
    }
    private static String getLastWeaponID() {
        MongoCursor<Document> cursor = itemsCollection.find().iterator();
        int _id = 0;

        while (cursor.hasNext()) {
            Document document = cursor.next();
            if (RPGItemType.valueOf(document.getString("itemType")).equals(RPGItemType.WEAPON))
                _id = Integer.parseInt(document.getString("_id").replace("W", ""));
        }

        return "W" + (_id+1);
    }
    private static String getLastItemID() {
        MongoCursor<Document> cursor = itemsCollection.find().iterator();
        int _id = 0;

        while (cursor.hasNext()) {
            Document document = cursor.next();
            if (RPGItemType.valueOf(document.getString("itemType")).equals(RPGItemType.WEAPON))
                continue;

            if (RPGItemType.valueOf(document.getString("itemType")).equals(RPGItemType.ARMOR))
                continue;

            _id = Integer.parseInt(document.getString("_id").replace("I", ""));

        }

        return "I" + (_id+1);
    }

    public static void loadUpItems() {
        MongoCursor<Document> cursor = itemsCollection.find().iterator();

        RPG.getInstance().getLogger().log(Level.INFO, "Загрузка предметов.");
        RPG.getInstance().getRpgItems().clear();
        int items = 0;
        while (cursor.hasNext()) {
            Document document = cursor.next();
            String _id = document.getString("_id");
            RPGItemType itemType = RPGItemType.valueOf(document.getString("itemType"));

            RPGItem loadedItem = new RPGItem(_id);
            RPG.getInstance().getRpgItems().add(loadedItem);

            switch (itemType) {
                case ARMOR:
                    RPG.getInstance().getArmors().add(loadedItem);
                    break;
                case WEAPON:
                    RPG.getInstance().getWeapons().add(loadedItem);
                    break;
                default:
                    break;
            }

            items++;
        }

        RPG.getInstance().getLogger().log(Level.INFO, "Готово. Загруженно " + items + " предметов.");
    }

    public static RPGItem getFromItemStack(ItemStack itemStack) {
        RPGItem rpgItem = null;
        
        if (itemStack == null)
            return null;
        
        if (itemStack.getType().equals(Material.AIR))
            return null;

        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("_id"))
            rpgItem = new RPGItem(nbtItem.getString("_id"));

        return rpgItem;
    }
    public ItemStack getItemStack() {
        ItemStack rpgItem = new ItemBuilder(this.getMaterial())
                .setDisplayName(this.getRarity().getColor() + this.getTitle())
                .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addItemFlag(ItemFlag.HIDE_UNBREAKABLE)
                .setUnbreakable(true)
                .build();

        ItemMeta meta = rpgItem.getItemMeta();
        List<String> lore = new ArrayList<>();

        Arrays.asList(this.getDescription().split("#")).forEach(s -> lore.add("§f" + s));
        lore.add("");
        lore.add("§fТип предмета: " + this.getRarity().getColor() + itemType.getTitle());
        lore.add("§fКачество: " + this.getRarity().getColor() + this.getRarity().getTitle());
        lore.add("");


        switch (this.itemType) {
            case ARMOR:
                lore.add("§fТип: " + "§7" + this.armorType.getTitle());
                lore.add("§fПрочность: " + "§a" + 100 + "%");
                lore.add("");
                lore.add("§fХарактеристики:");
                if (stabProtection != 0) lore.add("§a" + this.stabProtection + " защиты от колющего урона");
                if (slashProtection != 0) lore.add("§a" + this.slashProtection + " защиты от режущего урона");
                if (bluntProtection != 0) lore.add("§a" + this.bluntProtection + " защиты от дробящего урона");
                lore.add("");
                try {
                    if (additionalParameters.size() != 0)
                        this.additionalParameters.forEach(((rpgArmorParameter, level) -> lore.add("§a+" + level + "% " + RPGArmorParameter.getByString(rpgArmorParameter).getLoreString())));
                } catch (NullPointerException ignore) {}

                lore.add("");
                lore.add("§fТребования:");
                lore.add("§7- " + requiredSkillLevel + " " + requiredSkill.getIcon().getTitle().toLowerCase());
                lore.add("§7- " + requiredLevel + " уровень");
                lore.add("");
                break;

            case WEAPON:
                lore.add("§fТип оружия: " + "§7" + this.weaponType.getTitle());
                lore.add("§fПрочность: " + "§a" + 100 + "%");
                lore.add("");
                lore.add("§fХарактеристики:");
                if (stabDamage != 0) lore.add("§a" + this.stabDamage + " колющего урона");
                if (slashDamage != 0) lore.add("§a" + this.slashDamage + " режущего урона");
                if (bluntDamage != 0) lore.add("§a" + this.bluntDamage + " дробящего урона");
                lore.add("");
                try {
                    if (additionalParameters.size() != 0)
                        this.additionalParameters.forEach(((rpgWeaponParameter, level) -> lore.add("§a+" + level + "% " + RPGWeaponParameter.getByString(rpgWeaponParameter).getLoreString())));
                } catch (NullPointerException ignore) {}

                lore.add("");
                lore.add("§fТребования:");
                lore.add("§7- " + requiredSkillLevel + " " + requiredSkill.getIcon().getTitle().toLowerCase());
                lore.add("§7- " + requiredLevel + " уровень");
                lore.add("");
                break;

            default:
                break;
        }

        meta.setLore(lore);
        rpgItem.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(rpgItem);
        nbtItem.setString("_id", this.get_id());
        try {
            nbtItem.setString("texture", this.getNbtTexture());
        } catch (NullPointerException ignore) {}


        switch (this.itemType) {
            case ARMOR:
                nbtItem.setInteger("durability", 100);
                nbtItem.setInteger("stabProtection", this.stabProtection);
                nbtItem.setInteger("slashProtection", this.slashProtection);
                nbtItem.setInteger("bluntProtection", this.bluntProtection);
                break;

            case WEAPON:
                nbtItem.setInteger("durability", 100);
                nbtItem.setInteger("stabDamage", this.stabDamage);
                nbtItem.setInteger("slashDamage", this.slashDamage);
                nbtItem.setInteger("bluntDamage", this.bluntDamage);

                NBTCompoundList attribute = nbtItem.getCompoundList("AttributeModifiers");
                NBTListCompound attackSpeedMod = attribute.addCompound();
                switch (weaponType) {
                    case ONE_HANDED:
                        //    nbtItem.setInteger("attackReach", 2);
                        attackSpeedMod.setDouble("Amount", -0.2);
                        break;
                    case TWO_HANDED:
                        //     nbtItem.setInteger("attackReach", 3);
                        attackSpeedMod.setDouble("Amount", -2.5);
                        break;
                    case POLE_ARM:
                        //     nbtItem.setInteger("attackReach", 5);
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
                break;

            default:
                break;
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
    }
    public static void setItemSlashDamage(ItemStack itemStack, int value) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("slashDamage"))
            nbtItem.removeKey("slashDamage");

        nbtItem.setInteger("slashDamage", value);
        nbtItem.applyNBT(itemStack);
    }
    public static void setItemBluntDamage(ItemStack itemStack, int value) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("bluntDamage"))
            nbtItem.removeKey("bluntDamage");

        nbtItem.setInteger("bluntDamage", value);
        nbtItem.applyNBT(itemStack);
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
    }
    public static void setItemSlashProtection(ItemStack itemStack, int value) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("slashProtection"))
            nbtItem.removeKey("slashProtection");

        nbtItem.setInteger("slashProtection", value);
        nbtItem.applyNBT(itemStack);
    }
    public static void setItemBluntProtection(ItemStack itemStack, int value) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("bluntProtection"))
            nbtItem.removeKey("bluntProtection");

        nbtItem.setInteger("bluntProtection", value);
        nbtItem.applyNBT(itemStack);
    }

    public static void updateItemStack(ItemStack itemStack, RPGItemType itemType) {
        RPGItem rpgItem = getFromItemStack(itemStack);

        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();

        int durability;

        Arrays.asList(rpgItem.getDescription().split("#")).forEach(s -> lore.add("§f" + s));
        lore.add("");
        lore.add("§fТип предмета: " + rpgItem.getRarity().getColor() + itemType.getTitle());
        lore.add("§fКачество: " + rpgItem.getRarity().getColor() + rpgItem.getRarity().getTitle());
        lore.add("");

        switch (itemType) {
            case FOOD:
            case QUEST:

            case ARMOR:
                durability = getItemDurability(itemStack);
                int stabProtection = getItemStabProtection(itemStack);
                int slashProtection = getItemSlashProtection(itemStack);
                int bluntProtection = getItemBluntProtection(itemStack);

                lore.add("§fТип: " + "§7" + rpgItem.armorType.getTitle());

                if (durability > 50) lore.add("§fПрочность: " + "§a" + durability + "%");
                else if (durability < 25) lore.add("§fПрочность: " + "§c" + durability + "%");
                else lore.add("§fПрочность: " + "§e" + durability + "%");

                lore.add("");
                lore.add("§fХарактеристики:");
                if (stabProtection != 0) lore.add("§a" + stabProtection + " защиты от колющего урона");
                if (slashProtection != 0) lore.add("§a" + slashProtection + " защиты от режущего урона");
                if (bluntProtection != 0) lore.add("§a" + bluntProtection + " защиты от дробящего урона");
                lore.add("");
                if (rpgItem.additionalParameters.size() != 0)
                    rpgItem.additionalParameters.forEach(((rpgArmorParameter, level) -> lore.add("§a+" + level + "% " + RPGArmorParameter.getByString(rpgArmorParameter).getLoreString())));

                lore.add("");
                lore.add("§fТребования:");
                lore.add("§7- " + rpgItem.getRequiredSkillLevel() + " " + rpgItem.getRequiredSkill().getIcon().getTitle().toLowerCase());
                lore.add("§7- " + rpgItem.getRequiredLevel() + " уровень");
                lore.add("");
                break;
            case WEAPON:
                durability = getItemDurability(itemStack);
                int stabDamage = getItemStabDamage(itemStack);
                int slashDamage = getItemSlashDamage(itemStack);
                int bluntDamage = getItemBluntDamage(itemStack);

                lore.add("§fТип: " + "§7" + rpgItem.weaponType.getTitle());

                if (durability > 50) lore.add("§fПрочность: " + "§a" + durability + "%");
                else if (durability < 25) lore.add("§fПрочность: " + "§c" + durability + "%");
                else lore.add("§fПрочность: " + "§e" + durability + "%");

                lore.add("");
                lore.add("§fХарактеристики:");
                if (stabDamage != 0) lore.add("§a" + stabDamage + " колющего урона");
                if (slashDamage != 0) lore.add("§a" + slashDamage + " режущего урона");
                if (bluntDamage != 0) lore.add("§a" + bluntDamage + " дробящего урона");
                lore.add("");
                if (rpgItem.additionalParameters.size() != 0)
                    rpgItem.additionalParameters.forEach(((rpgWeaponParameter, level) -> lore.add("§a+" + level + "% " + RPGWeaponParameter.getByString(rpgWeaponParameter).getLoreString())));
                lore.add("");
                lore.add("§fТребования:");
                lore.add("§7- " + rpgItem.getRequiredSkillLevel() + " " + rpgItem.getRequiredSkill().getIcon().getTitle().toLowerCase());
                lore.add("§7- " + rpgItem.getRequiredLevel() + " уровень");
                lore.add("");
                break;

            default:
                break;
        }

        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }


}
