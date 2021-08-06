package ru.artorium.rpg.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.artorium.core.services.inventoryservice.ClickableItem;
import ru.artorium.core.services.inventoryservice.InventoryService;
import ru.artorium.core.services.inventoryservice.content.InventoryContents;
import ru.artorium.core.services.inventoryservice.content.InventoryProvider;
import ru.artorium.core.utils.ItemBuilder;

public class JoinMenu {

    private static final ItemStack withoutRP = new ItemBuilder(Material.BARRIER)
            .setDisplayName("§cНЕ УСТАНАВЛИВАТЬ ТЕКСТУР-ПАК")
            .setLore(new String[]{
                    "",
                    "§fНе устанавливать текстур-пак"
            })
            .build();

    private static final ItemStack lightRP = new ItemBuilder(Material.IRON_BLOCK)
            .setDisplayName("§cЛЕГКИЙ ТЕКСТУР-ПАК")
            .setLore(new String[]{
                    "",
                    "§fУстановить текстур-пак, для слабых ПК"
            })
            .build();

    private static final ItemStack ultimateRP = new ItemBuilder(Material.GOLD_BLOCK)
            .setDisplayName("§cМАКСИМАЛЬНЫЙ ТЕКСТУР-ПАК")
            .setLore(new String[]{
                    "",
                    "§fУстановить текстур-пак со всеми плюхами"
            })
            .build();

    public static final InventoryService chooseTexturePackMenu = InventoryService.builder()
            .title("Выберите текстур-пак")
            .size(3, 9)
            .provider(new InventoryProvider() {
                @Override
                public void init(Player player, InventoryContents inventoryContents) {
                    inventoryContents.set(1, 2, ClickableItem.of(withoutRP, event -> player.closeInventory()));

                    inventoryContents.set(1, 4, ClickableItem.of(lightRP, event -> {
                        player.setResourcePack("static.artorium.me/artquestlight.rar");
                        player.closeInventory();
                    }));

                    inventoryContents.set(1, 6, ClickableItem.of(ultimateRP, event -> {
                        player.setResourcePack("static.artorium.me/artquestultimate.rar");
                        player.closeInventory();
                    }));
                }

                @Override
                public void update(Player player, InventoryContents contents) { }
            })
            .build();
}
