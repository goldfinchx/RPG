package ru.artorium.rpg.systems.battle_system.item;

import com.mongodb.BasicDBObject;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.artorium.rpg.systems.battle_system.obj.RPGAbstractItem;
import ru.artorium.rpg.systems.battle_system.parameters.RPGItemType;
import ru.artorium.rpg.systems.battle_system.parameters.RPGRarity;

@AllArgsConstructor
public class RPGItem extends RPGAbstractItem {
    public RPGItem(String title, String description, RPGItemType itemType, RPGRarity rarity, Material material, String nbtTexture, int price) {
        Document itemDocument = new Document();

        itemDocument.put("_id", getLastID());
        itemDocument.put("title", title);
        itemDocument.put("description", description);
        itemDocument.put("rarity", rarity.name());
        itemDocument.put("material", material.name());
        itemDocument.put("nbtTexture", nbtTexture);
        itemDocument.put("itemType", itemType.name());
        itemDocument.put("price", price);

        getItemsCollection().insertOne(itemDocument);
    }

    private RPGItem(String _id, String title, String description, RPGItemType itemType, RPGRarity rarity, Material material, String texture, int price) {
        this.set_id(_id);
        this.setTitle(title);
        this.setDescription(description);
        this.setItemType(itemType);
        this.setRarity(rarity);
        this.setMaterial(material);
        this.setNbtTexture(texture);
        this.setPrice(price);
    }

    public static RPGItem get(String _id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", _id);
        Document itemDocument = getItemsCollection().find(query).first();

        if (itemDocument == null)
            throw new NullPointerException("Предмет с таким ID не найден!");

        String title = itemDocument.getString("title");
        String description = itemDocument.getString("description");
        RPGRarity rarity = RPGRarity.valueOf(itemDocument.getString("rarity"));
        RPGItemType itemType = RPGItemType.valueOf(itemDocument.getString("itemType"));
        Material material = Material.valueOf(itemDocument.getString("material"));

        String texture = "default";
        try {
            texture = itemDocument.getString("nbtTexture");
        } catch (NullPointerException ignore) {}

        int price = 10;
        try {
            price = itemDocument.getInteger("price");
        } catch (NullPointerException ignore) {}

        return new RPGItem(_id, title, description, itemType, rarity, material, texture, price);
    }

    @Override
    public ItemStack getItemStack() {
       return super.getItemStack();
    }
}
