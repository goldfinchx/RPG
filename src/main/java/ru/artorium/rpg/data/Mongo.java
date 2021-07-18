package ru.artorium.rpg.data;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Mongo {

    private final JavaPlugin plugin;

    @Getter private final MongoClient mongoClient;
    @Getter private final MongoDatabase db;

    public Mongo(JavaPlugin plugin) {
        this.plugin = plugin;

        String host = plugin.getConfig().getString("mongo.host");
        String login = plugin.getConfig().getString("mongo.login");
        String dbName = plugin.getConfig().getString("mongo.dbName");
        String password = plugin.getConfig().getString("mongo.password");
        String mongoClientURI = "mongodb://" + login + ":" + password + "@" + host + ":" + 27017;

        this.mongoClient = new MongoClient(new MongoClientURI(mongoClientURI));

        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        this.db = mongoClient.getDatabase(dbName).withCodecRegistry(codecRegistry);
    }

    public MongoCollection<Document> getCollection(String name) { return db.getCollection(plugin.getConfig().getString("mongo.prefix") + name); }
}

