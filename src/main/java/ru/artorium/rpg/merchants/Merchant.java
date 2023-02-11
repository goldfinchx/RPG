package ru.artorium.rpg;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import net.citizensnpcs.api.npc.NPC;
import org.bson.Document;
import ru.artorium.rpg.community.obj.RPGCommunity;
import ru.artorium.rpg.systems.battle_system.armor.RPGArmor;
import ru.artorium.rpg.systems.battle_system.item.RPGItem;
import ru.artorium.rpg.systems.battle_system.obj.RPGAbstractItem;
import ru.artorium.rpg.systems.battle_system.weapon.RPGWeapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Merchant {

    private static final MongoCollection<Document> merchantsCollection = RPG.getInstance().getMongo().getCollection("merchants");

    @Getter private final NPC npc;
    @Getter private final RPGCommunity community;
    @Getter private final HashMap<RPGAbstractItem, Integer> goods;

    public Merchant(NPC npc, RPGCommunity community, HashMap<RPGAbstractItem, Integer> goods) {
        Document merchantDocument = new Document();
        List<Document> goodsDocument = new ArrayList<>();

        this.npc = npc;
        this.community = community;
        this.goods = goods;

        goods.forEach((rpgItem, price) -> {
            Document goodDocument = new Document();
            goodDocument.put("item", rpgItem.get_id());
            goodDocument.put("price", rpgItem.getPrice());
            goodsDocument.add(goodDocument);
        });

        merchantDocument.put("npc", npc.getId());
        merchantDocument.put("community", community.name());
        merchantDocument.put("goods", goodsDocument);

        merchantsCollection.insertOne(merchantDocument);
    }

    public static Merchant get(NPC npc) {
        Merchant merchant = null;
        BasicDBObject query = new BasicDBObject();
        query.put("npc", npc.getId());

        if (merchantsCollection.find(query).first() != null) {
            Document merchantDocument = merchantsCollection.find(query).first();
            RPGCommunity community = RPGCommunity.valueOf(merchantDocument.getString("community"));
            HashMap<RPGAbstractItem, Integer> goods = new HashMap<>();
            merchantDocument.getList("goods", Document.class).forEach(document -> {
                switch (document.getString("item").split("_")[0]) {
                    case "A":
                        goods.put(RPGArmor.get(document.getString("item")), document.getInteger("price"));
                        break;
                    case "W":
                        goods.put(RPGWeapon.get(document.getString("item")), document.getInteger("price"));
                        break;
                    case "I":
                        goods.put(RPGItem.get(document.getString("item")), document.getInteger("price"));
                        break;
                    default:
                        break;
                }
            });

            merchant = new Merchant(npc, community, goods);
        }

        return merchant;
    }
}
