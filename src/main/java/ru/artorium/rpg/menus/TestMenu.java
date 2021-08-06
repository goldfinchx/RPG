package ru.artorium.rpg.menus;

import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.artorium.core.services.inventoryservice.ClickableItem;
import ru.artorium.core.services.inventoryservice.InventoryService;
import ru.artorium.core.services.inventoryservice.content.InventoryContents;
import ru.artorium.core.services.inventoryservice.content.InventoryProvider;
import ru.artorium.core.services.inventoryservice.content.SlotIterator;
import ru.artorium.core.utils.ItemBuilder;
import ru.artorium.core.utils.design.Colors;
import ru.artorium.rpg.player.obj.PlayerData;
import ru.artorium.rpg.systems.battle_system.ItemsManager;
import ru.artorium.rpg.systems.battle_system.armor.RPGArmor;
import ru.artorium.rpg.systems.battle_system.item.RPGItem;
import ru.artorium.rpg.systems.battle_system.weapon.RPGWeapon;
import ru.artorium.rpg.systems.battle_system.obj.RPGAbstractItem;
import ru.artorium.rpg.systems.battle_system.parameters.RPGItemType;
import ru.artorium.rpg.systems.skills_system.menu.SkillsMenu;

import java.util.ArrayList;
import java.util.List;

public class TestMenu {

    private static final ItemStack back = new ItemBuilder(Material.ARROW).setDisplayName(Colors.parseColors("&cНАЗАД")).build();
    private static final ItemStack forward = new ItemBuilder(Material.ARROW).setDisplayName(Colors.parseColors("&cВПЕРЁД")).build();

    public final static InventoryService weaponsMenu = InventoryService.builder()
            .provider(new InventoryProvider() {
                @Override
                public void init(Player player, InventoryContents contents) {
                    List<ClickableItem> icons = new ArrayList<>();
                    List<RPGWeapon> weapons = new ArrayList<>();

                    BasicDBObject query = new BasicDBObject();
                    query.put("itemType", RPGItemType.WEAPON.name());

                    for (Document weaponDocument : RPGAbstractItem.getItemsCollection().find(query)) {
                        RPGWeapon rpgWeapon = RPGWeapon.get(weaponDocument.getString("_id"));
                        weapons.add(rpgWeapon);
                    }


                    for (RPGWeapon rpgWeapon : weapons)
                        if (rpgWeapon != null)
                            icons.add(ClickableItem.of(rpgWeapon.getItemStack(), event -> player.getInventory().addItem(rpgWeapon.getItemStack())));

                    ClickableItem[] iconsArray = new ClickableItem[icons.size()+1];
                    int id = 0;
                    for (ClickableItem clickableItem : icons) {
                        iconsArray[id] = clickableItem;
                        id++;
                    }
                    contents.pagination().setItems(iconsArray);
                    contents.pagination().setItemsPerPage(36);

                    contents.pagination().addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

                    if (!contents.pagination().isFirst())
                        contents.set(5, 3, ClickableItem.of(back, event1 -> weaponsMenu.open(player, contents.pagination().previous().getPage())));

                    if (!contents.pagination().isLast())
                        contents.set(5, 5, ClickableItem.of(forward, event1 -> weaponsMenu.open(player, contents.pagination().next().getPage())));
                }

                @Override
                public void update(Player player, InventoryContents contents) {
                }

            }).build();

    public final static InventoryService armorsMenu = InventoryService.builder()
            .provider(new InventoryProvider() {
                @Override
                public void init(Player player, InventoryContents contents) {
                    List<ClickableItem> icons = new ArrayList<>();
                    List<RPGArmor> armors = new ArrayList<>();

                    BasicDBObject query = new BasicDBObject();
                    query.put("itemType", RPGItemType.ARMOR.name());

                    for (Document weaponDocument : RPGAbstractItem.getItemsCollection().find(query)) {
                        armors.add(RPGArmor.get(weaponDocument.getString("_id")));
                    }

                    ClickableItem[] iconsArray = new ClickableItem[icons.size()+1];
                    int id = 0;
                    for (ClickableItem clickableItem : icons) {
                        iconsArray[id] = clickableItem;
                        id++;
                    }
                    contents.pagination().setItems(iconsArray);
                    contents.pagination().setItemsPerPage(36);

                    contents.pagination().addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

                    if (!contents.pagination().isFirst())
                        contents.set(5, 3, ClickableItem.of(back, event1 -> armorsMenu.open(player, contents.pagination().previous().getPage())));

                    if (!contents.pagination().isLast())
                        contents.set(5, 5, ClickableItem.of(forward, event1 -> armorsMenu.open(player, contents.pagination().next().getPage())));
                }

                @Override
                public void update(Player player, InventoryContents contents) {
                }

            }).build();

    public final static InventoryService itemsMenu = InventoryService.builder()
            .provider(new InventoryProvider() {
                @Override
                public void init(Player player, InventoryContents contents) {
                    List<ClickableItem> icons = new ArrayList<>();
                    List<RPGItem> items = new ArrayList<>();

                    for (Document itemDocument : RPGAbstractItem.getItemsCollection().find()) {
                        if (itemDocument.getString("itemType").equalsIgnoreCase("ARMOR"))
                            continue;

                        if (itemDocument.getString("itemType").equalsIgnoreCase("WEAPON"))
                            continue;

                        items.add(RPGItem.get(itemDocument.getString("_id")));
                    }

                    for (RPGItem rpgItem : items)
                        icons.add(ClickableItem.of(rpgItem.getItemStack(), event -> player.getInventory().addItem(rpgItem.getItemStack())));

                    ClickableItem[] iconsArray = new ClickableItem[icons.size()+1];
                    int id = 0;
                    for (ClickableItem clickableItem : icons) {
                        iconsArray[id] = clickableItem;
                        id++;
                    }
                    contents.pagination().setItems(iconsArray);
                    contents.pagination().setItemsPerPage(36);

                    contents.pagination().addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

                    if (!contents.pagination().isFirst())
                        contents.set(5, 3, ClickableItem.of(back, event1 -> itemsMenu.open(player, contents.pagination().previous().getPage())));

                    if (!contents.pagination().isLast())
                        contents.set(5, 5, ClickableItem.of(forward, event1 -> itemsMenu.open(player, contents.pagination().next().getPage())));

                }

                @Override
                public void update(Player player, InventoryContents contents) {
                }

            }).build();

    public final static InventoryService testInventory = InventoryService.builder()
            .title("TEST")
            .size(5, 9)
            .provider(new InventoryProvider() {
                @Override
                public void init(Player player, InventoryContents contents) {
                    contents.add(ClickableItem.of(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Colors.parseColors("&cМеню оружия")).build(), event -> weaponsMenu.open((Player) event.getWhoClicked())));
                    contents.add(ClickableItem.of(new ItemBuilder(Material.IRON_HELMET).setDisplayName(Colors.parseColors("&cМеню брони")).build(), event -> armorsMenu.open((Player) event.getWhoClicked())));
                    contents.add(ClickableItem.of(new ItemBuilder(Material.POTATO).setDisplayName(Colors.parseColors("&cМеню предметов")).build(), event -> itemsMenu.open((Player) event.getWhoClicked())));
                    contents.add(ClickableItem.of(new ItemBuilder(Material.REDSTONE).setDisplayName(Colors.parseColors("&cМеню навыков")).build(), event -> new SkillsMenu(PlayerData.get(player.getUniqueId())).mainSkillsMenu.open(player)));

                }

                @Override
                public void update(Player player, InventoryContents contents) {

                }
            })
            .build();
}
