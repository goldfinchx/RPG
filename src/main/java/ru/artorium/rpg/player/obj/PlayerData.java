package ru.artorium.rpg.player.obj;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import ru.artorium.rpg.PlayerManager;
import ru.artorium.rpg.RPG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
public class PlayerData {

    private final static MongoCollection<PlayerData> playersCollection =
            RPG.getInstance().getMongo().getDb().withCodecRegistry(CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())))
                    .getCollection("rpg_players", PlayerData.class);

    private String id;

    private int level;
    private int experience;
    private int skillPoints;
    private int coins;
    private int glory;
    private int playerKills;
    private int mobKills;
    private int deaths;

    private double health;
    private double maxHealth;
    private long createdDate;
    private long lastJoinDate;

    private HashMap<String, Integer> communitiesReputation;
    private List<Document> skills;
    private HashMap<String, Integer> abilities;

    public PlayerData(UUID id) {
        this.id = id.toString();

        this.level = 1;
        this.experience = 0;
        this.skillPoints = 3;
        this.coins = 0;
        this.glory = 0;
        this.playerKills = 0;
        this.mobKills = 0;
        this.deaths = 0;
        this.health = 50;
        this.maxHealth = 50;
        this.createdDate = System.currentTimeMillis();
        this.lastJoinDate = System.currentTimeMillis();

        this.communitiesReputation = new HashMap<>();;
        this.skills = new ArrayList<>();;
        this.abilities = new HashMap<>();

        playersCollection.insertOne(this);
        new PlayerManager(this).startGame();
    }

    public static PlayerData get(UUID id) {
        BasicDBObject playerQuery = new BasicDBObject();
        playerQuery.put("_id", id.toString());

        if (playersCollection.find(playerQuery).first() != null) {
            return playersCollection.find(playerQuery).first();
        }
        return null;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;

        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.set("_id", id);

        playersCollection.updateOne(filter, update);
    }

    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;

        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.set("level", level);

        playersCollection.updateOne(filter, update);
    }

    public int getExperience() {
        return experience;
    }
    public void setExperience(int experience) {
        this.experience = experience;

        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.set("experience", experience);

        playersCollection.updateOne(filter, update);
    }

    public int getSkillPoints() {
        return skillPoints;
    }
    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;

        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.set("skillPoints", skillPoints);

        playersCollection.updateOne(filter, update);
    }

    public int getCoins() {
        return coins;
    }
    public void setCoins(int coins) {
        this.coins = coins;

        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.set("coins", coins);

        playersCollection.updateOne(filter, update);
    }

    public int getGlory() {
        return glory;
    }
    public void setGlory(int glory) {
        this.glory = glory;

        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.set("glory", glory);

        playersCollection.updateOne(filter, update);
    }

    public int getPlayerKills() {
        return playerKills;
    }
    public void setPlayerKills(int playerKills) {
        this.playerKills = playerKills;

        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.set("playerKills", playerKills);

        playersCollection.updateOne(filter, update);
    }

    public int getMobKills() {
        return mobKills;
    }
    public void setMobKills(int mobKills) {
        this.mobKills = mobKills;

        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.set("mobKills", mobKills);

        playersCollection.updateOne(filter, update);
    }

    public int getDeaths() {
        return deaths;
    }
    public void setDeaths(int deaths) {
        this.deaths = deaths;

        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.set("deaths", deaths);

        playersCollection.updateOne(filter, update);
    }

    public double getHealth() {
        return health;
    }
    public void setHealth(double health) {
        this.health = health;

        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.set("health", health);

        playersCollection.updateOne(filter, update);
    }

    public double getMaxHealth() {
        return maxHealth;
    }
    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;

        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.set("maxHealth", maxHealth);

        playersCollection.updateOne(filter, update);
    }

    public long getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;

        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.set("createdDate", createdDate);

        playersCollection.updateOne(filter, update);
    }

    public long getLastJoinDate() {
        return lastJoinDate;
    }
    public void setLastJoinDate(long lastJoinDate) {
        this.lastJoinDate = lastJoinDate;

        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.set("lastJoinDate", lastJoinDate);

        playersCollection.updateOne(filter, update);
    }

    public HashMap<String, Integer> getCommunitiesReputation() {
        return communitiesReputation;
    }
    public void setCommunitiesReputation(HashMap<String, Integer> communitiesReputation) {
        this.communitiesReputation = communitiesReputation;

        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.set("communitiesReputation", communitiesReputation);

        playersCollection.updateOne(filter, update);
    }

    public List<Document> getSkills() {
        return skills;
    }
    public void setSkills(List<Document> skills) {
        this.skills = skills;

        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.set("skills", skills);

        playersCollection.updateOne(filter, update);
    }

    public HashMap<String, Integer> getAbilities() {
        return abilities;
    }
    public void setAbilities(HashMap<String, Integer> abilities) {
        this.abilities = abilities;

        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.set("abilities", abilities);

        playersCollection.updateOne(filter, update);
    }
}
