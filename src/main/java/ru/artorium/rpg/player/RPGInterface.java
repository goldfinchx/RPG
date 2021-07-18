package ru.artorium.rpg.player;

import fr.skytasul.quests.api.QuestsAPI;
import fr.skytasul.quests.players.PlayerAccount;
import fr.skytasul.quests.players.accounts.UUIDAccount;
import fr.skytasul.quests.structure.QuestBranch;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import ru.artorium.rpg.RPG;
import ru.artorium.rpg.player.obj.PlayerData;
import ru.artorium.rpg.utils.Colors;
import ru.artorium.rpg.utils.ScoreboardCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RPGInterface implements Listener {

    private int runnable;

    public RPGInterface(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        PlayerData playerData = RPG.getPlayerData(uuid);
        PlayerAccount playerAccount = new PlayerAccount(new UUIDAccount(uuid), 0);

        ScoreboardCreator escapistScoreboard = ScoreboardCreator.createScore(player);

        List<String> scores = new ArrayList<>();
        scores.add("   ");
        scores.add("Квесты:");
        try {
            if (QuestsAPI.getQuestsStarteds(playerAccount).isEmpty()) {
                scores.add(" Начатых квестов нет");
            } else {
                scores.add(Colors.stripColors(QuestsAPI.getQuestsStarteds(playerAccount).get(QuestsAPI.getQuestsStarteds(playerAccount).size()-1).getName()));
                scores.add(Colors.stripColors(QuestsAPI.getQuestsStarteds(playerAccount).get(QuestsAPI.getQuestsStarteds(playerAccount).size()-1).getBranchesManager().getPlayerBranch(playerAccount).getDescriptionLine(playerAccount, QuestBranch.Source.SCOREBOARD)));
            }
        } catch (NullPointerException ignore) {
            scores.add(" Начатых квестов нет");
        }

        scores.add("    ");
        scores.add("Статистика:");
        scores.add(" Монеты &8» &c" + playerData.getCoins());
        scores.add(" Убийств &8» &c" + playerData.getPlayerKills() + "&8/&c" + playerData.getMobKills());
        scores.add(" Смертей &8» &c" + playerData.getDeaths());
        scores.add("     ");
        scores.add("   &cartorium.me");

        escapistScoreboard.setTitle("&cRPG");
        escapistScoreboard.setSlotsFromList(scores);

        new BukkitRunnable() {
            @Override
            public void run() {
                runnable = this.getTaskId();

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Colors.parseColors("&c ❤ " + Math.round(playerData.getHealth()) + "/" + Math.round(playerData.getMaxHealth()) + "  ")));

                List<String> updateScores = new ArrayList<>();
                updateScores.add("   ");
                updateScores.add("Квесты:");

                boolean isAnyQuest = true;

                try {
                    if (QuestsAPI.getQuestsStarteds(playerAccount).isEmpty())
                        isAnyQuest = false;
                } catch (NullPointerException ignore) {
                    isAnyQuest = false;
                }


                if (isAnyQuest) {
                    updateScores.add(Colors.stripColors(QuestsAPI.getQuestsStarteds(playerAccount).get(QuestsAPI.getQuestsStarteds(playerAccount).size() - 1).getName()));
                    updateScores.add(Colors.stripColors(QuestsAPI.getQuestsStarteds(playerAccount).get(QuestsAPI.getQuestsStarteds(playerAccount).size() - 1).getBranchesManager().getPlayerBranch(playerAccount).getDescriptionLine(playerAccount, QuestBranch.Source.SCOREBOARD)));
                } else {
                    updateScores.add(" Начатых квестов нет");
                }

                updateScores.add("    ");
                updateScores.add("Статистика:");
                updateScores.add(" Монеты &8» &c" + playerData.getCoins());
                updateScores.add(" Убийств &8» &c" + playerData.getPlayerKills() + "&8/&c" + playerData.getMobKills());
                updateScores.add(" Смертей &8» &c" + playerData.getDeaths());
                updateScores.add("     ");
                updateScores.add("   &cartorium.me");

                if (ScoreboardCreator.hasScore(player)) ScoreboardCreator.getByPlayer(player).setSlotsFromList(updateScores);

            }
        }.runTaskTimerAsynchronously(RPG.getInstance(), 20, 20);
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(runnable);
    }

}
