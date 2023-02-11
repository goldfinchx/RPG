package ru.artorium.rpg;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MerchantListener implements Listener {

    @EventHandler
    public void onNpcClick(NPCRightClickEvent e) {
        Player player = e.getClicker();

        if (Merchant.get(e.getNPC()) != null) {
            MerchantManager.buildMerchantInventory(Merchant.get(e.getNPC())).open(player);
        }
    }
}
