package ru.artorium.rpg;

import org.bukkit.Bukkit;
import ru.artorium.core.services.inventoryservice.ClickableItem;
import ru.artorium.core.services.inventoryservice.InventoryService;
import ru.artorium.core.services.inventoryservice.content.Pagination;
import ru.artorium.core.services.inventoryservice.content.SlotIterator;
import ru.artorium.core.utils.PlayerUtils;
import ru.artorium.core.utils.design.Colors;
import ru.artorium.rpg.player.obj.PlayerData;
import ru.artorium.rpg.systems.battle_system.obj.RPGAbstractItem;

import java.util.Map;

public class MerchantManager {

    public static InventoryService buildMerchantInventory(Merchant merchant) {
        return InventoryService.builder()
                .title(merchant.getNpc().getFullName())
                .size(6, 9)
                .provider((player, inventoryContents) -> {
                    Pagination pagination = inventoryContents.pagination();
                    SlotIterator slotIterator = inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0);

                    ClickableItem[] craftItems = new ClickableItem[merchant.getGoods().size()+1];
                    int index = 0;
                    for (Map.Entry<RPGAbstractItem, Integer> entry : merchant.getGoods().entrySet()) {
                        RPGAbstractItem key = entry.getKey();
                        Integer value = entry.getValue();
                        craftItems[index] = ClickableItem.of(key.getItemStack(), event -> {
                            if (tryTransaction(PlayerData.get(player.getUniqueId()), value)) {
                                player.getInventory().addItem(key.getItemStack());
                            }
                        });

                        index++;
                    }

                    pagination.setItems(craftItems);
                    pagination.setItemsPerPage(27);

                    slotIterator.blacklist(6, 0);
                    slotIterator.blacklist(6, 1);
                    slotIterator.blacklist(6, 2);
                    slotIterator.blacklist(6, 3);
                    slotIterator.blacklist(6, 4);
                    slotIterator.blacklist(6, 5);
                    slotIterator.blacklist(6, 6);
                    slotIterator.blacklist(6, 7);
                    slotIterator.blacklist(6, 8);

                    pagination.addToIterator(slotIterator);


                })
                .build();
    }


    private static boolean tryTransaction(PlayerData playerData, int price) {
        if (!PlayerUtils.isInventoryHaveEmptySlots(Bukkit.getPlayer(playerData.getId())))
            return false;

        if (playerData.getCoins() >= price) {
            Bukkit.getPlayer(playerData.getId()).sendMessage("");
            Bukkit.getPlayer(playerData.getId()).sendMessage(Colors.parseColors("&aСпасибо за покупку!"));
            Bukkit.getPlayer(playerData.getId()).sendMessage("");
            return true;
        } else {
            Bukkit.getPlayer(playerData.getId()).sendMessage("");
            Bukkit.getPlayer(playerData.getId()).sendMessage(Colors.parseColors("&cУ тебя не хватает монет, чтобы это купить!"));
            Bukkit.getPlayer(playerData.getId()).sendMessage("");
            return false;
        }


    }
}
