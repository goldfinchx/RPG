package ru.artorium.rpg.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.artorium.core.inv.ClickableItem;
import ru.artorium.core.inv.InventoryService;
import ru.artorium.core.inv.content.InventoryContents;
import ru.artorium.core.inv.content.InventoryProvider;
import ru.artorium.core.inv.content.SlotIterator;
import ru.artorium.rpg.RPG;
import ru.artorium.rpg.systems.battle_system.obj.RPGItem;
import ru.artorium.rpg.systems.battle_system.parameters.RPGItemType;
import ru.artorium.rpg.systems.skills_system.menu.SkillsMenu;
import ru.artorium.rpg.utils.Colors;
import ru.artorium.rpg.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class TestMenu {

    private static final ItemStack back = new ItemBuilder(Material.ARROW).setDisplayName(Colors.parseColors("&cНАЗАД")).build();
    private static final ItemStack forward = new ItemBuilder(Material.ARROW).setDisplayName(Colors.parseColors("&cВПЕРЁД")).build();

    public final static InventoryService weaponsMenu = InventoryService.builder()
            .manager(RPG.getInstance().getInventoryManager())
            .provider(new InventoryProvider() {
                @Override
                public void init(Player player, InventoryContents contents) {
                    RPGItem.loadUpItems();

                    List<ClickableItem> icons = new ArrayList<>();

                    for (RPGItem rpgWeapon : RPG.getInstance().getWeapons())
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
            .manager(RPG.getInstance().getInventoryManager())
            .provider(new InventoryProvider() {
                @Override
                public void init(Player player, InventoryContents contents) {
                    RPGItem.loadUpItems();

                    List<ClickableItem> icons = new ArrayList<>();

                    for (RPGItem rpgArmor : RPG.getInstance().getArmors())
                        if (rpgArmor != null)
                            icons.add(ClickableItem.of(rpgArmor.getItemStack(), event -> player.getInventory().addItem(rpgArmor.getItemStack())));


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

    public final static InventoryService foodMenu = InventoryService.builder()
            .manager(RPG.getInstance().getInventoryManager())
            .provider(new InventoryProvider() {
                @Override
                public void init(Player player, InventoryContents contents) {
                    RPGItem.loadUpItems();

                    List<ClickableItem> icons = new ArrayList<>();

                    for (RPGItem rpgFood : RPG.getInstance().getRpgItems())
                        if (rpgFood != null && rpgFood.getItemType().equals(RPGItemType.FOOD))
                            icons.add(ClickableItem.of(rpgFood.getItemStack(), event -> player.getInventory().addItem(rpgFood.getItemStack())));

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
                        contents.set(5, 3, ClickableItem.of(back, event1 -> foodMenu.open(player, contents.pagination().previous().getPage())));

                    if (!contents.pagination().isLast())
                        contents.set(5, 5, ClickableItem.of(forward, event1 -> foodMenu.open(player, contents.pagination().next().getPage())));

                }

                @Override
                public void update(Player player, InventoryContents contents) {
                }

            }).build();

    public final static InventoryService testInventory = InventoryService.builder()
            .title("TEST")
            .manager(RPG.getInstance().getInventoryManager())
            .size(5, 9)
            .provider(new InventoryProvider() {
                @Override
                public void init(Player player, InventoryContents contents) {

                    contents.add(ClickableItem.of(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Colors.parseColors("&cМеню оружия")).build(), event -> weaponsMenu.open((Player) event.getWhoClicked())));
                    contents.add(ClickableItem.of(new ItemBuilder(Material.IRON_HELMET).setDisplayName(Colors.parseColors("&cМеню брони")).build(), event -> armorsMenu.open((Player) event.getWhoClicked())));
                    contents.add(ClickableItem.of(new ItemBuilder(Material.POTATO).setDisplayName(Colors.parseColors("&cМеню навыков")).build(), event -> foodMenu.open(player)));
                    contents.add(ClickableItem.of(new ItemBuilder(Material.REDSTONE).setDisplayName(Colors.parseColors("&cМеню навыков")).build(), event -> SkillsMenu.mainSkillsMenu.open(player)));

                }

                @Override
                public void update(Player player, InventoryContents contents) {

                }
            })
            .build();
}
