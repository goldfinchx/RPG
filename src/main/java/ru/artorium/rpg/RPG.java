package ru.artorium.rpg;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.lumine.xikage.mythicmobs.MythicMobs;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.artorium.core.inv.InventoryManager;
import ru.artorium.rpg.commands.ReloadDBCommand;
import ru.artorium.rpg.commands.TestCommand;
import ru.artorium.rpg.data.Mongo;
import ru.artorium.rpg.player.listener.LevelsListener;
import ru.artorium.rpg.player.listener.PlayerListener;
import ru.artorium.rpg.player.obj.PlayerData;
import ru.artorium.rpg.systems.battle_system.BattleSystem;
import ru.artorium.rpg.systems.battle_system.listener.BattleListener;
import ru.artorium.rpg.systems.battle_system.obj.RPGItem;
import ru.artorium.rpg.systems.skills_system.listener.SkillsListener;
import ru.artorium.rpg.utils.Bungee;
import ru.artorium.rpg.utils.ReflectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class RPG extends JavaPlugin {

    @Getter private static RPG instance;
    @Getter private Mongo mongo;

    @Getter private InventoryManager inventoryManager;
    @Getter private ProtocolManager protocolManager;
    @Getter private MythicMobs mythicMobs;

    @Getter private HashMap<UUID, PlayerData> players;

    @Getter private List<RPGItem> rpgItems;
    @Getter private List<RPGItem> weapons;
    @Getter private List<RPGItem> armors;

    @Override
    public void onEnable() {
        instance = this;

        // Подключение внутреннего конфига
        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();

        // Подключение MongoDB
        this.mongo = new Mongo(this);
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        // Подключение Bungee каналов
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new Bungee());

        // Подключение сторонних плагинов
        this.inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        protocolManager = ProtocolLibrary.getProtocolManager();
        mythicMobs = MythicMobs.inst();

        // Загрузка всех персонажей
        players = new HashMap<>();
        PlayerData.loadUpPlayers();

        // Автоматическое сохранение данных игроков
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers())
                getPlayerData(onlinePlayer.getUniqueId()).saveData();
        }, 1200*5, 1200*5);

        // Загрузка всех предметов
        rpgItems = new ArrayList<>();
        armors = new ArrayList<>();
        weapons = new ArrayList<>();
        RPGItem.loadUpItems();

        // Регистрация лисенеров
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new SkillsListener(), this);
        Bukkit.getPluginManager().registerEvents(new LevelsListener(), this);
        Bukkit.getPluginManager().registerEvents(new BattleListener(), this);
        BattleSystem.runRegeneration();

        // Регистрация команд
        ReflectionUtils.registerCommand("test", new TestCommand());
        ReflectionUtils.registerCommand("reloadItems", new ReloadDBCommand());
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers())
            try { getPlayerData(player.getUniqueId()).saveData();
            } catch (NullPointerException ignored) {}

    }

    public static PlayerData getPlayerData(UUID uuid) { return getInstance().getPlayers().get(uuid); }
}
