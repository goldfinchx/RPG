package ru.artorium.rpg.systems.battle_system.obj;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.artorium.core.utils.ItemBuilder;
import ru.artorium.rpg.RPG;
import ru.artorium.rpg.systems.battle_system.armor.RPGArmor;
import ru.artorium.rpg.systems.battle_system.item.RPGItem;
import ru.artorium.rpg.systems.battle_system.weapon.RPGWeapon;
import ru.artorium.rpg.systems.battle_system.parameters.RPGItemType;
import ru.artorium.rpg.systems.battle_system.parameters.RPGRarity;
import ru.artorium.rpg.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class RPGAbstractItem {

    @Getter private static final MongoCollection<Document> itemsCollection =
            RPG.getInstance().getMongo().getCollection("items");

    @Getter@Setter private String _id;
    @Getter@Setter private String title;
    @Getter@Setter private String description;
    @Getter@Setter private RPGItemType itemType;
    @Getter@Setter private RPGRarity rarity;
    @Getter@Setter private Material material;
    @Getter@Setter private String nbtTexture;
    @Getter@Setter private int price;


    public String getLastID() {
        MongoCursor<Document> cursor = getItemsCollection().find().iterator();
        int _id = 0;

        while (cursor.hasNext()) {
            Document document = cursor.next();
            if (RPGItemType.valueOf(document.getString("itemType")).equals(this.itemType))
                _id = document.getString("_id").toCharArray()[1];
        }

        _id++;
        switch (this.itemType) {
            case FOOD:
            case QUEST:
            default:
                return "I" + _id;
            case ARMOR:
                return "A" + _id;
            case WEAPON:
                return "W" + _id;
        }
    }

    public static RPGAbstractItem getFromItemStack(ItemStack itemStack) {
        if (itemStack == null)
            return null;
        
        if (itemStack.getType().equals(Material.AIR))
            return null;

        NBTItem nbtItem = new NBTItem(itemStack);
        if (!nbtItem.hasKey("_id"))
            throw new NullPointerException("ID предмета не установлено! Видимо, предмет не относится к плагину");

        switch (nbtItem.getString("_id").toCharArray()[0]) {
            case 'A':
                return RPGArmor.get(nbtItem.getString("_id"));
            case 'W':
                return RPGWeapon.get(nbtItem.getString("_id"));
            case 'I':
            default:
                return RPGItem.get(nbtItem.getString("_id"));
        }
    }

    public ItemStack getItemStack() {
        ItemStack rpgItem = new ItemBuilder(this.getMaterial())
                .setDisplayName(this.getRarity().getColor() + this.getTitle())
                .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addItemFlag(ItemFlag.HIDE_UNBREAKABLE)
                .setUnbreakable(true)
                .build();

        ItemMeta meta = rpgItem.getItemMeta();

        List<String> lore = new ArrayList<>(StringUtils.splitForLore(this.getDescription()));
        lore.add("");
        lore.add("§fТип предмета: " + this.getRarity().getColor() + itemType.getTitle());
        lore.add("§fКачество: " + this.getRarity().getColor() + this.getRarity().getTitle());
        lore.add("");

        meta.setLore(lore);
        rpgItem.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(rpgItem);
        nbtItem.setString("_id", this.get_id());
        nbtItem.setString("type", this.getItemType().name());
        try {
            nbtItem.setString("texture", this.getNbtTexture());
        } catch (NullPointerException ignore) {}
        nbtItem.setInteger("TooltipFlag", 0);

        return nbtItem.getItem();
    }



}
