package ru.artorium.rpg.systems.skills_system.menu;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.artorium.core.inv.ClickableItem;
import ru.artorium.core.inv.InventoryService;
import ru.artorium.core.inv.content.InventoryContents;
import ru.artorium.core.inv.content.InventoryProvider;
import ru.artorium.rpg.RPG;
import ru.artorium.rpg.player.obj.PlayerData;
import ru.artorium.rpg.systems.skills_system.ability.obj.RPGAbility;
import ru.artorium.rpg.systems.skills_system.skill.icon.RPGSkillIcon;
import ru.artorium.rpg.systems.skills_system.skill.obj.RPGSkill;
import ru.artorium.rpg.utils.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SkillsMenu {

    private static final ItemStack back = new ItemBuilder(Material.YELLOW_WOOL, (short) 4)
            .setDisplayName("§eВЕРНУТЬСЯ НАЗАД")
            .setLore(new String[]{
                    "",
                    "§fНажмите, чтобы вернуться в меню всех способностей",
                    ""
            })
            .build();

    public static final InventoryService mainSkillsMenu = InventoryService.builder()
            .title("§0Меню навыков")
            .manager(RPG.getInstance().getInventoryManager())
            .size(6, 9)
            .provider(new InventoryProvider() {
                @Override
                public void init(Player player, InventoryContents inventoryContents) {
                    PlayerData playerData = RPG.getPlayerData(player.getUniqueId());

                    for (RPGSkill rpgSkill : RPGSkill.values()) {
                        RPGSkillIcon rpgSkillIcon = rpgSkill.getIcon();
                        ItemStack skillIcon = rpgSkillIcon.getItemIcon();

                        ItemMeta skillIconMeta = skillIcon.getItemMeta();
                        skillIconMeta.setLore(new ArrayList<>());
                        List<String> lore = new ArrayList<>(rpgSkillIcon.getDescription());

                        lore.add("&fУровень &8» &c" + playerData.getSkillLevel(rpgSkill));
                        lore.add("&fПрогресс уровня &8» " + StringUtils.getProgressString(IntegerUtils.getProcentFromInteger((int) Math.round(playerData.getSkillExperience(rpgSkill)), rpgSkill.getLevels().get(playerData.getSkillLevel(rpgSkill)))));

                        skillIconMeta.setLore(Colors.parseColors(lore));
                        skillIcon.setItemMeta(skillIconMeta);

                        skillIcon.setAmount(Math.min(playerData.getSkillLevel(rpgSkill), 64));

                        if (rpgSkillIcon.getNBTString() != null) {
                            NBTItem nbtItem = new NBTItem(skillIcon);
                            nbtItem.setString("texture", rpgSkillIcon.getNBTString());
                            nbtItem.applyNBT(skillIcon);
                        }

                        inventoryContents.set(rpgSkillIcon.getSlotInMenu(), ClickableItem.of(skillIcon, event ->
                                InventoryService.builder()
                                .title(Colors.parseColors("&0Меню способностей " + rpgSkillIcon.getTitle()))
                                .manager(RPG.getInstance().getInventoryManager())
                                .parent(mainSkillsMenu)
                                .size(6, 9)
                                .provider(new InventoryProvider() {
                                    @Override
                                    public void init(Player player, InventoryContents inventoryContents) {
                                        PlayerData playerData = RPG.getPlayerData(player.getUniqueId());

                                        inventoryContents.set(5, 3, ClickableItem.of(back, event1 -> mainSkillsMenu.open(player)));
                                        inventoryContents.set(5, 5, ClickableItem.empty(skillIcon));

                                        int raw = 0;
                                        for (RPGAbility rpgAbility : rpgSkill.getAbilities()) {
                                            LinkedHashMap<Integer, Integer> levelMap = rpgAbility.getLevels();
                                            int pointsToFirstLvl = new ArrayList<>(levelMap.keySet()).get(0);

                                            ItemStack abilityMainIcon = new ItemBuilder(Material.GLASS_PANE)
                                                    .setDisplayName("§6" + rpgAbility.getTitle())
                                                    .setData((short) (playerData.getSkillLevel(rpgSkill) > pointsToFirstLvl ? 5 : 14))
                                                    .setLore(rpgAbility.getDescription())
                                                    .build();

                                            inventoryContents.set(raw, 1, ClickableItem.empty(abilityMainIcon));
                                            AtomicInteger abilityLevel = new AtomicInteger(1);

                                            int finalRaw = raw;
                                            levelMap.forEach((pointsToOpen, levelValue) -> {
                                                boolean isUpgradable = false;
                                                boolean isOpen;

                                                if (playerData.getSkillLevel(rpgSkill) > pointsToOpen) {
                                                    isOpen = true;
                                                    isUpgradable = playerData.getAbilityLevel(rpgAbility) < abilityLevel.get();
                                                } else { isOpen = false; }

                                                ItemStack abilityLevelIcon = new ItemBuilder(Material.GLASS_PANE)
                                                        .setDisplayName("§6" + rpgAbility.getTitle() + " " + StringUtils.toRoman(abilityLevel.get()))
                                                        .setData((short) (isOpen ? (isUpgradable ? 0 : 5) : 14))
                                                        .setLore(new String[]{
                                                                "",
                                                                "§fОчков для открытия: " + pointsToOpen,
                                                                "§fЗначение уровня: " + levelValue,
                                                                ""
                                                        })
                                                        .build();

                                                inventoryContents.set(finalRaw, abilityLevel.get()+1, isOpen ? (isUpgradable ? ClickableItem.of(abilityLevelIcon, event1 -> {
                                                    if (playerData.getSkillPoints() >= 1) {
                                                        playerData.setSkillPoints(playerData.getSkillPoints() - 1);
                                                        playerData.setAbilityLevel(rpgAbility, playerData.getAbilityLevel(rpgAbility) + 1);
                                                        update(player, inventoryContents);

                                                        player.sendMessage("§aУровень " + rpgAbility.getTitle() + " успешно поднят!");
                                                        SuperSound.FINE.play(player);

                                                    } else {
                                                        player.sendMessage("§cУ вас не хватает очков умений, для улучшения данного навыка!");
                                                        SuperSound.ERROR.play(player);
                                                    }


                                                }) : ClickableItem.empty(abilityLevelIcon)) : ClickableItem.empty(abilityLevelIcon));

                                                abilityLevel.getAndIncrement();

                                            });
                                            raw++;
                                        }



                                    }

                                    @Override
                                    public void update(Player player, InventoryContents contents) {
                                        init(player, contents);
                                    }

                                }).build().open(player)));

                    }
                }

                @Override
                public void update(Player player, InventoryContents contents) { }
            })
            .build();

}
