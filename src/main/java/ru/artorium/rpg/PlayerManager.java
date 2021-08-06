package ru.artorium.rpg;

import io.lumine.xikage.mythicmobs.drops.droppables.MythicItemDrop;
import io.lumine.xikage.mythicmobs.io.MythicConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import ru.artorium.core.services.ScoreboardService;
import ru.artorium.core.utils.design.Colors;
import ru.artorium.core.utils.design.SuperSound;
import ru.artorium.rpg.community.obj.RPGCommunity;
import ru.artorium.rpg.player.obj.PlayerData;
import ru.artorium.rpg.systems.skills_system.ability.obj.RPGAbility;
import ru.artorium.rpg.systems.skills_system.skill.obj.RPGSkill;
import ru.artorium.rpg.utils.ConfigUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerManager {

     private final PlayerData playerData;
     private final List<Document> skills;
     private final HashMap<String, Integer> abilities;
     private final HashMap<String, Integer> communitiesReputation;

     public static HashMap<UUID, Integer> interfaces = new HashMap<>();

    public PlayerManager(PlayerData playerData) {
        this.playerData = playerData;
        this.skills = playerData.getSkills();
        this.abilities = playerData.getAbilities();
        this.communitiesReputation = playerData.getCommunitiesReputation();
    }

    public void setSkillLevel(RPGSkill rpgSkill, int level) {
        for (Document document : skills) {
            if (!document.getString("name").equals(rpgSkill.name()))
                continue;

            document.replace("level", level);
            playerData.setSkills(skills);
            return;
        }

        Document document = new Document();

        document.put("name", rpgSkill.name());
        document.put("level", level);
        document.put("experience", (double) rpgSkill.getLevels().get(level-1));

        skills.add(document);
        playerData.setSkills(skills);
    }
    public void addSkillExperience(RPGSkill rpgSkill, double experience) {
        for (Document document : skills) {
            if (!document.getString("name").equals(rpgSkill.name()))
                continue;

            document.replace("experience", document.getDouble("experience") + experience);

            int skillLevel = document.getInteger("level");
            if (document.getDouble("experience") >= rpgSkill.getLevels().get(skillLevel)) {
                setSkillLevel(rpgSkill, skillLevel + 1);

                Player player = Bukkit.getPlayer(playerData.getId());

                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 0.5F);
                player.sendTitle(Colors.parseColors("&6 ⇧ " + rpgSkill.getIcon().getTitle().toUpperCase() + " ⇧ "), Colors.parseColors("&f" + skillLevel + " ➠ " + (skillLevel + 1)), 10, 30, 20);
                player.sendMessage("");
                player.sendMessage("");
                player.sendMessage(Colors.parseColors("&fУровень &6" + rpgSkill.getIcon().getTitle() + " &fповышен!"));
                player.sendMessage(Colors.parseColors("&fНовый уровень » &6" + (skillLevel + 1)));
                player.sendMessage("");
                player.sendMessage("");
            }

            playerData.setSkills(skills);
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
        this.playerData.setAbilities(abilities);
    }
    public void addAbilityLevel(RPGAbility rpgAbility, int amount) {
        int levelBefore = this.abilities.getOrDefault(rpgAbility.name(), 0);

        this.abilities.remove(rpgAbility.name());
        this.abilities.put(rpgAbility.name(), levelBefore+amount);
        this.playerData.setAbilities(abilities);
    }
    public int getAbilityLevel(RPGAbility rpgAbility) {
        int level = 0;
        if (abilities.containsKey(rpgAbility.name()))
            level = this.abilities.get(rpgAbility.name());

        return level;
    }

    public int getCommunityReputation(RPGCommunity community) {
        return this.communitiesReputation.get(community.name());
    }
    public void setCommunityReputation(RPGCommunity community, int value) {
        this.communitiesReputation.remove(community.name());
        this.communitiesReputation.put(community.name(), value);
        this.playerData.setCommunitiesReputation(communitiesReputation);
    }

    public void startGame() {
        Player player = Bukkit.getPlayer(UUID.fromString(playerData.getId()));

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

        runRPGInterface();

        // ТУТОРИАЛ
    }

    public void runRPGInterface() {
        Player player = Bukkit.getPlayer(UUID.fromString(playerData.getId()));

        ScoreboardService scoreboard = ScoreboardService.createScore(player);

        List<String> scores = new ArrayList<>();
        scores.add("Игрок:");
        scores.add(" Монеты &8» &c" + playerData.getCoins());
        scores.add(" Очки навыков &8» &c" + playerData.getSkillPoints());
        scores.add("    ");
        scores.add("Статистика:");
        scores.add(" Убийств &8» &c" + playerData.getPlayerKills() + "&8/&c" + playerData.getMobKills());
        scores.add(" Смертей &8» &c" + playerData.getDeaths());
        scores.add("     ");
        scores.add("   &cartorium.me");

        scoreboard.setTitle("&cRPG");
        scoreboard.setSlotsFromList(scores);
        BukkitRunnable runnable = new BukkitRunnable() {

            @Override
            public void run() {
                PlayerData updateData = PlayerData.get(UUID.fromString(playerData.getId()));
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Colors.parseColors("&c ❤ " + Math.round(updateData.getHealth()) + "/" + Math.round(updateData.getMaxHealth()) + "  ")));

                List<String> updateScores = new ArrayList<>();

                updateScores.add("Игрок:");
                updateScores.add(" Монеты &8» &c" + updateData.getCoins());
                updateScores.add(" Очки навыков &8» &c" + updateData.getSkillPoints());
                updateScores.add("    ");
                updateScores.add("Статистика:");
                updateScores.add(" Убийств &8» &c" + updateData.getPlayerKills() + "&8/&c" + updateData.getMobKills());
                updateScores.add(" Смертей &8» &c" + updateData.getDeaths());
                updateScores.add("     ");
                updateScores.add("   &cartorium.me");

                if (ScoreboardService.hasScore(player)) ScoreboardService.getByPlayer(player).setSlotsFromList(updateScores);
            }
        };

        runnable.runTaskTimerAsynchronously(RPG.getInstance(), 20, 10);

        interfaces.put(UUID.fromString(this.playerData.getId()), runnable.getTaskId());
    }

    // TODO: 01.08.2021 сделать реген

    public void cancelRPGInterface() {
        Bukkit.getScheduler().cancelTask(interfaces.get(UUID.fromString(playerData.getId())));
        interfaces.remove(UUID.fromString(playerData.getId()));
    }

}
