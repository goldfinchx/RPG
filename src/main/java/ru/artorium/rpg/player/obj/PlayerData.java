package ru.artorium.rpg.player.obj;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.artorium.rpg.RPG;
import ru.artorium.rpg.player.RPGInterface;
import ru.artorium.rpg.classes.obj.RPGClass;
import ru.artorium.rpg.community.obj.RPGCommunity;
import ru.artorium.rpg.fraction.obj.RPGFraction;
import ru.artorium.rpg.systems.skills_system.ability.obj.RPGAbility;
import ru.artorium.rpg.systems.skills_system.skill.obj.RPGSkill;
import ru.artorium.rpg.utils.Colors;
import ru.artorium.rpg.utils.ConfigUtils;
import ru.artorium.rpg.utils.SuperSound;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerData {

    private final static MongoCollection<Document> playersCollection =
            RPG.getInstance().getMongo().getCollection("players");

    private final BasicDBObject playerQuery;
    @Getter private Document playerDocument;

    @Getter private final UUID _id;

    @Getter@Setter private RPGFraction rpgFraction;
    @Getter@Setter private RPGClass rpgClass;

    @Getter@Setter private int level;
    @Getter@Setter private int experience;
    @Getter@Setter private int skillPoints;
    @Getter@Setter private int coins;
    @Getter@Setter private int glory;
    @Getter@Setter private int playerKills;
    @Getter@Setter private int mobKills;
    @Getter@Setter private int deaths;

    @Getter@Setter private double health;
    @Getter@Setter private double maxHealth;
    @Getter@Setter private long createdDate;
    @Getter@Setter private long lastJoinDate;

    @Getter private Document communitiesReputation;
    @Getter private List<Document> skills;
    @Getter private Document abilities;

    @Getter@Setter private RPGInterface rpgInterface;

    public PlayerData(UUID _id) {
        this._id = _id;

        this.playerQuery = new BasicDBObject();
        this.playerQuery.put("_id", this._id.toString());

        this.playerDocument = playersCollection.find(playerQuery).first();

        if (playerDocument == null) {
            playerDocument = new Document();

            playerDocument.put("_id", _id.toString());
            playerDocument.put("fraction", RPGFraction.ANGLO_SAXONS.name());
            playerDocument.put("class", RPGClass.FOOTMAN.name());
            playerDocument.put("level", 1);
            playerDocument.put("experience", 0);
            playerDocument.put("skillPoints", 0);
            playerDocument.put("coins", 0);
            playerDocument.put("glory", 0);
            playerDocument.put("playerKills", 0);
            playerDocument.put("mobKills", 0);
            playerDocument.put("deaths", 0);
            playerDocument.put("maxHealth", 50.0);
            playerDocument.put("health", 50.0);
            playerDocument.put("createdDate", System.currentTimeMillis());
            playerDocument.put("lastJoinDate", System.currentTimeMillis());
            playerDocument.put("communitiesReputation", new Document());
            playerDocument.put("skills", new ArrayList<>());
            playerDocument.put("abilities", new Document());

            playersCollection.insertOne(playerDocument);
            PlayerData createdData = new PlayerData(this._id);

            Bukkit.getScheduler().runTaskLater(RPG.getInstance(), createdData::startGame, 60);
        } else {
            this.rpgFraction = RPGFraction.getByString(playerDocument.getString("fraction"));
            this.rpgClass = RPGClass.getByString(playerDocument.getString("class"));
            this.level = playerDocument.getInteger("level");
            this.experience = playerDocument.getInteger("experience");
            this.skillPoints = playerDocument.getInteger("skillPoints");
            this.coins = playerDocument.getInteger("coins");
            this.glory = playerDocument.getInteger("glory");
            this.playerKills = playerDocument.getInteger("playerKills");
            this.mobKills = playerDocument.getInteger("mobKills");
            this.deaths = playerDocument.getInteger("deaths");
            this.maxHealth = playerDocument.getDouble("maxHealth");
            this.health = playerDocument.getDouble("health");
            this.createdDate = playerDocument.getLong("createdDate");
            this.lastJoinDate = playerDocument.getLong("lastJoinDate");

            this.communitiesReputation = (Document) playerDocument.get("communitiesReputation");
            this.skills = playerDocument.getList("skills", Document.class);
            this.abilities = (Document) playerDocument.get("abilities");

            RPG.getInstance().getPlayers().put(_id, this);
        }

    }

    public void saveData() {
        this.playerDocument.put("_id", this._id.toString());
        this.playerDocument.put("fraction", this.rpgFraction.name());
        this.playerDocument.put("class", this.rpgClass.name());
        this.playerDocument.put("level", this.level);
        this.playerDocument.put("experience", this.experience);
        this.playerDocument.put("skillPoints", this.skillPoints);
        this.playerDocument.put("coins", this.coins);
        this.playerDocument.put("glory", this.glory);
        this.playerDocument.put("playerKills", this.playerKills);
        this.playerDocument.put("mobKills", this.mobKills);
        this.playerDocument.put("deaths", this.deaths);
        this.playerDocument.put("maxHealth", this.maxHealth);
        this.playerDocument.put("health", this.health);
        this.playerDocument.put("createdDate", this.createdDate);
        this.playerDocument.put("lastJoinDate", this.lastJoinDate);
        this.playerDocument.put("communitiesReputation", this.communitiesReputation);
        this.playerDocument.put("skills", this.skills);
        this.playerDocument.put("abilities", this.abilities);

        playersCollection.replaceOne(this.playerQuery, this.playerDocument, new UpdateOptions().upsert(true));
        this.playerDocument = playersCollection.find(playerQuery).first();
    }

    public void setSkillLevel(RPGSkill rpgSkill, int level) {
            if (!skills.isEmpty())
                for (Document document : skills) {
                    if (!document.getString("name").equals(rpgSkill.name()))
                        continue;

                    document.replace("level", level);
                    return;
                }

        Document document = new Document();

        document.put("name", rpgSkill.name());
        document.put("level", level);
        document.put("experience", (double) rpgSkill.getLevels().get(level-1));

        skills.add(document);
    }
    public void addSkillExperience(RPGSkill rpgSkill, double experience) {
            for (Document document : skills) {
                if (!document.getString("name").equals(rpgSkill.name()))
                    continue;

                document.replace("experience", document.getDouble("experience")+experience);

                int skillLevel = document.getInteger("level");
                if (document.getDouble("experience") >= rpgSkill.getLevels().get(skillLevel)) {
                    setSkillLevel(rpgSkill, skillLevel+1);

                    Player player = Bukkit.getPlayer(_id);

                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 0.5F);
                    player.sendTitle(Colors.parseColors("&6 ⇧ " + rpgSkill.getIcon().getTitle().toUpperCase() + " ⇧ "), Colors.parseColors("&f" + skillLevel + " ➠ " + (skillLevel+1)), 10, 30, 20);
                    player.sendMessage("");
                    player.sendMessage("");
                    player.sendMessage(Colors.parseColors("&fУровень &6" + rpgSkill.getIcon().getTitle() + " &fповышен!"));
                    player.sendMessage(Colors.parseColors("&fНовый уровень » &6" + (skillLevel+1)));
                    player.sendMessage("");
                    player.sendMessage("");
                }

                return;
            }
    }
    public int getSkillLevel(RPGSkill rpgSkill) {
        int skillLevel = 0;
        for (Document document : skills) {
            if (!document.getString("name").equals(rpgSkill.name()))
                continue;

            skillLevel = document.getInteger("level");
        }

        return skillLevel;
    }
    public double getSkillExperience(RPGSkill rpgSkill) {
        double skillExperience = 0;
        for (Document document : skills) {
            if (!document.get("name").equals(rpgSkill.name()))
                continue;

            skillExperience = document.getDouble("experience");
        }

        return skillExperience;
    }

    public void setAbilityLevel(RPGAbility rpgAbility, int level) {
        this.abilities.remove(rpgAbility.name());
        this.abilities.put(rpgAbility.name(), level);
    }
    public void addAbilityLevel(RPGAbility rpgAbility, int amount) {
        int levelBefore = this.abilities.containsKey(rpgAbility.name()) ? this.abilities.getInteger(rpgAbility.name()) : 0;

        this.abilities.remove(rpgAbility.name());
        this.abilities.put(rpgAbility.name(), levelBefore+amount);
    }
    public int getAbilityLevel(RPGAbility rpgAbility) {
        int level = 0;
        if (abilities.containsKey(rpgAbility.name()))
            level = this.abilities.getInteger(rpgAbility.name());

        return level;
    }

    public int getCommunityReputation(RPGCommunity communityName) {
        return this.communitiesReputation.getInteger(communityName.name());
    }
    public void setCommunityReputation(RPGCommunity communityName, int value) {
        this.communitiesReputation.remove(communityName.name());
        this.communitiesReputation.put(communityName.name(), value);
    }

    public void startGame() {
        Player player = Bukkit.getPlayer(this._id);

        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 2, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 10, false, false));
        player.teleport(ConfigUtils.getLocationFromConfig("join_location"));
        player.sendTitle(Colors.parseColors("&cArtorium"), "", 20*6, 20*2, 20);
        SuperSound.EPIC.play(player);

        player.setLevel(1);
        player.setExp(0.0F);

        for (RPGSkill rpgSkill : RPGSkill.values())
            this.setSkillLevel(rpgSkill, 1);

        for (RPGCommunity rpgCommunity : RPGCommunity.values())
            this.setCommunityReputation(rpgCommunity, 0);

        /*
        TODO туториал
          - выбор класса в конце
          - рассказ о боевке
          - рассказ о фракциях
          - рассказ о квестах
          - рассказ о навыках
        */

        this.saveData();
    }

    public static void loadUpPlayers() {
        MongoCursor<Document> cursor = playersCollection.find().iterator();

        RPG.getInstance().getLogger().log(Level.INFO, "Загрузка игроков.");

        int players = 0;
        while (cursor.hasNext()) {
            Document document = cursor.next();
            UUID _id = UUID.fromString(document.getString("_id"));

            new PlayerData(_id);

            players++;
        }
        RPG.getInstance().getLogger().log(Level.INFO, "Готово. Загруженно " + players + " игроков.");
    }

}
