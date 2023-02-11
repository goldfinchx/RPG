package ru.artorium.rpg;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.lumine.xikage.mythicmobs.MythicMobs;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.artorium.rpg.commands.TestCommand;
import ru.artorium.rpg.data.Mongo;
import ru.artorium.rpg.player.listener.LevelsListener;
import ru.artorium.rpg.player.listener.PlayerListener;
import ru.artorium.rpg.systems.battle_system.BattleSystem;
import ru.artorium.rpg.systems.battle_system.listener.BattleListener;
import ru.artorium.rpg.systems.skills_system.listener.SkillsListener;
import ru.artorium.rpg.utils.Bungee;
import ru.artorium.rpg.utils.ReflectionUtils;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class RPG extends JavaPlugin {

    @Getter private static RPG instance;
    @Getter private Mongo mongo;

    @Getter private ProtocolManager protocolManager;
    @Getter private MythicMobs mythicMobs;

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
        protocolManager = ProtocolLibrary.getProtocolManager();
        mythicMobs = MythicMobs.inst();

        // Регистрация лисенеров
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new SkillsListener(), this);
        Bukkit.getPluginManager().registerEvents(new LevelsListener(), this);
        Bukkit.getPluginManager().registerEvents(new BattleListener(), this);
        Bukkit.getPluginManager().registerEvents(new MerchantListener(), this);
        BattleSystem.runRegeneration();

        // Регистрация команд
        ReflectionUtils.registerCommand("test", new TestCommand());
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        this.mongo.getMongoClient().close();
    }
}
