package ru.artorium.rpg.systems.skills_system.skill.icon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import ru.artorium.core.services.inventoryservice.content.SlotPos;
import ru.artorium.core.utils.ItemBuilder;
import ru.artorium.core.utils.design.Colors;


import java.util.List;

@AllArgsConstructor@Getter
public enum RPGSkillIcon {

    ICON_HEAVY_ARMOR("Ношение тяжелой брони", new ItemBuilder(Material.DIAMOND_CHESTPLATE)
            .setDisplayName(Colors.parseColors("&6Ношение тяжелой брони"))
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .build(), Colors.parseColors(new String[]{
            "",
            "&fДанный навык отвечает за &aиспользование",
            "&aтяжелой брони&f, увелечение её параметров,",
            "&fи открытие способностей, связанных с ней",
            "",
            "&fПовышается за счёт &aблокирования урона",
            "&fданной броней. Каждый элемент брони",
            "&fдаёт +х0.25 к данному навыку",
            ""
    }), "Dark Samurai Armor", new SlotPos(4, 7)),

    ICON_MEDIUM_ARMOR("Ношение средней брони", new ItemBuilder(Material.IRON_CHESTPLATE)
            .setDisplayName(Colors.parseColors("&6Ношение средней брони"))
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .build(), Colors.parseColors(new String[]{
            "",
            "&fДанный навык отвечает за &aиспользование",
            "&aсредней брони&f, увелечение её параметров,",
            "&fи открытие способностей, связанных с ней",
            "",
            "&fПовышается за счёт &aблокирования урона",
            "&fданной броней. Каждый элемент брони",
            "&fдаёт +х0.25 к данному навыку",
            ""
    }), "Norman", new SlotPos(3, 7)),

    ICON_LIGHT_ARMOR("Ношение легкой брони", new ItemBuilder(Material.GOLDEN_CHESTPLATE)
            .setDisplayName(Colors.parseColors("&6Ношение легкой брони"))
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .build(), Colors.parseColors(new String[]{
            "",
            "&fДанный навык отвечает за &aиспользование",
            "&aлёгкой брони&f, увелечение её параметров,",
            "&fи открытие способностей, связанных с ней",
            "",
            "&fПовышается за счёт &aблокирования урона",
            "&fданной броней. Каждый элемент брони",
            "&fдаёт +х0.25 к данному навыку",
            ""
    }), "Longbowarcher", new SlotPos(2, 7)),

    ICON_BLOCKING("Блокирование", new ItemBuilder(Material.SHIELD)
            .setDisplayName(Colors.parseColors("&6Блокирование"))
            .build(), Colors.parseColors(new String[]{
            "",
            "&fДанный навык отвечает за &aиспользование",
            "&aщитов&f, увеличение их параметров, и",
            "&fоткрытие способностей, связанных щитами",
            "",
            "&fПовышается за счёт &aблокирования ударов",
            "&fс помощью щитов",
            ""
    }), "Big Round Red Shield", new SlotPos(1, 7)),

    ICON_ONE_HANDED_WEAPON("Владение одноручным оружием", new ItemBuilder(Material.GOLDEN_SWORD)
            .setDisplayName(Colors.parseColors("&6Владение одноручным оружием"))
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .build(), Colors.parseColors(new String[]{
            "",
            "&fДанный навык отвечает за &aиспользование",
            "&aодноручного оружия&f, увелечение его параметров,",
            "&fи открытие способностей, связанных с ним",
            "",
            "&fПовышается за счёт &aнанесения урона&f,",
            "&fоружием данного типа.",
            ""
    }), "Chinese Jian Sword", new SlotPos(1, 1)),

    ICON_TWO_HANDED_WEAPON("Владение двуручным оружием", new ItemBuilder(Material.STONE_SWORD)
            .setDisplayName(Colors.parseColors("&6Владение двуручным оружием"))
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .build(), Colors.parseColors(new String[]{
            "",
            "&fДанный навык отвечает за &aиспользование",
            "&aдвуручного оружия&f, увелечение его параметров,",
            "&fи открытие способностей, связанных с ним",
            "",
            "&fПовышается за счёт &aнанесения урона&f,",
            "&fоружием данного типа.",
            ""
    }), "Heavy Greatsword", new SlotPos(2, 1)),

    ICON_POLE_WEAPON("Владение древковым оружием", new ItemBuilder(Material.IRON_SWORD)
            .setDisplayName(Colors.parseColors("&6Владение древковым оружием"))
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .build(),Colors.parseColors(new String[]{
            "",
            "&fДанный навык отвечает за &aиспользование",
            "&aдревкового оружия&f, увелечение его параметров,",
            "&fи открытие способностей, связанных с ним",
            "",
            "&fПовышается за счёт &aнанесения урона&f,",
            "&fоружием данного типа.",
            ""
    }), "Light Halberd", new SlotPos(3, 1)),

    ICON_SHOOTING_WEAPON("Владение стрелковым оружием", new ItemBuilder(Material.BOW)
            .setDisplayName(Colors.parseColors("&6Владение стрелковым оружием"))
            .build(),Colors.parseColors(new String[]{
            "",
            "&fДанный навык отвечает за &aиспользование",
            "&aстрелкового оружия&f, увелечение его параметров,",
            "&fи открытие способностей, связанных с ним",
            "",
            "&fПовышается за счёт &aнанесения урона&f,",
            "&fоружием данного типа.",
            ""
    }), "Light Crossbow", new SlotPos(4, 1)),

    ICON_ORATORY("Красноречие", new ItemBuilder(Material.RED_DYE, (byte) 1)
            .setDisplayName(Colors.parseColors("&6Красноречие"))
            .build(), Colors.parseColors(new String[]{
            "",
            "&fНавык позволяет &aвыгодно торговать&f,",
            "&fвыполнять некоторые квесты по-другому,",
            "&fи открывать скрытые квесты и механики",
            "",
            "&fПовышается за счёт &aторговли с торговцами&f,",
            "&fи &aза выполнение квестов",
            ""
    }), null, new SlotPos(5, 4)),

    ICON_BLACKSMITING("Кузнечество", new ItemBuilder(Material.IRON_PICKAXE)
            .setDisplayName(Colors.parseColors("&6Кузнечество"))
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .build(), Colors.parseColors(new String[]{
            "",
            "&fНавык позволяет &aсоздавать уникальное",
            "&aснаряжение&f, и снижать стоимость и кол-во",
            "&fресурсов для его создания",
            "",
            "&fПовышается за счёт &aсоздания снаряжения",
            ""
    }), "Hammer", new SlotPos(4, 5)),

    ICON_MINING("Шахтерство", new ItemBuilder(Material.IRON_PICKAXE)
            .setDisplayName(Colors.parseColors("&6Шахтерство"))
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .build(), Colors.parseColors(new String[]{
            "",
            "&fДанный навык позволяет &aдобывать больше",
            "&aресурсов&f, при ломании руд.",
            "",
            "&fПовышается за счёт &aдобычи руд",
            ""
    }), null, new SlotPos(4, 3)),

    ICON_COOKERY("Кулинария", new ItemBuilder(Material.BAKED_POTATO)
            .setDisplayName(Colors.parseColors("&6Кулинария"))
            .build(),Colors.parseColors(new String[]{
            "",
            "&fДанный навык позволяет &aготовить",
            "&aнаивкуснейшие блюда &fво всей Европе,",
            "&fа также улучшать их эффекты.",
            "",
            "&fПовышается за счёт &aготовки блюд",
            ""
    }), "Dorset Cheese", new SlotPos(3, 3)),

    ICON_HERBALISM("Травничество", new ItemBuilder(Material.CARROT)
            .setDisplayName(Colors.parseColors("&6Травничество"))
            .build(), Colors.parseColors(new String[]{
            "",
            "&fДанный навык позволяет &aсоздавать отвары",
            "&aбальзамы и т.д&f, из растущих трав.",
            "",
            "&fПовышается за счёт &aсоздания отваров,",
            "&fи &aсбора полезных трав",
            ""
    }), "Raw Legumes", new SlotPos(3, 5)),

    ICON_HORSERIDING("Верховая езда", new ItemBuilder(Material.IRON_HORSE_ARMOR)
            .setDisplayName(Colors.parseColors("&6Верховая езда"))
            .build(), Colors.parseColors(new String[]{
            "",
            "&fДанный навык позволяет эффективней",
            "&aкататься на конях &fи снижать",
            "&fпотребность в их уходе",
            "",
            "&fПовышается за &aсчёт езды на конях",
            ""
    }),null, new SlotPos(4, 4)),

    ICON_LOCKPICKING("Взламывание", new ItemBuilder(Material.DARK_OAK_DOOR)
            .setDisplayName(Colors.parseColors("&6Взламывание"))
            .build(), Colors.parseColors(new String[]{
            "",
            "&fДанный навык позволяет эффективней",
            "&aвзламывать сундуки и двери&f.",
            "",
            "&fПовышается за &aсчёт взлома",
            "&aсундуков и дверей",
            ""
    }), null, new SlotPos(3, 4)),

    ICON_CLASS_ABILITIES("Классовые способности", new ItemBuilder(Material.BOOK)
            .setDisplayName(Colors.parseColors("&6Классовые способности"))
            .build(),Colors.parseColors(new String[]{
                    "",
            "&fУникальный набор способностей и навыков,",
            "&fприсутствующих только у &aвашего класса&f.",
            "",
            "&0Повышается за счет &aувеличения уровня персонажа",
            ""
    }), "Tome of Fire", new SlotPos(1, 4));


    private final String title;
    private final ItemStack itemIcon;
    private final List<String> description;
    private final String NBTString;
    private final SlotPos slotInMenu;


}
