package ru.artorium.rpg;

import net.citizensnpcs.api.npc.NPC;
import ru.artorium.rpg.community.obj.RPGCommunity;
import ru.artorium.rpg.systems.battle_system.obj.RPGAbstractItem;

import java.util.HashMap;

public class MerchantBuilder {

    private NPC npc;
    private RPGCommunity community = RPGCommunity.PEASANTS;
    private HashMap<RPGAbstractItem, Integer> goods = new HashMap<>();

    public MerchantBuilder(NPC npc) {
        this.npc = npc;
    }

    public MerchantBuilder npc(NPC npc) {
        this.npc = npc;
        return this;
    }

    public MerchantBuilder community(RPGCommunity community) {
        this.community = community;
        return this;
    }

    public MerchantBuilder addGood(RPGAbstractItem good, int price) {
        this.goods.put(good, price);
        return this;
    }

    public Merchant build() {
        return new Merchant(npc, community, goods);
    }
}
