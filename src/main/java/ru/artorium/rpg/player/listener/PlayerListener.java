package ru.artorium.rpg.player.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.artorium.rpg.PlayerManager;
import ru.artorium.rpg.RPG;
import ru.artorium.rpg.menus.JoinMenu;
import ru.artorium.rpg.player.obj.PlayerData;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerData playerData = PlayerData.get(player.getUniqueId());
        player.setGameMode(GameMode.ADVENTURE);

        if (playerData == null)
            playerData = new PlayerData(player.getUniqueId());
        else
            new PlayerManager(playerData).runRPGInterface();

        Bukkit.getScheduler().runTaskLater(RPG.getInstance(), () -> JoinMenu.chooseTexturePackMenu.open(player), 20);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PlayerData playerData = PlayerData.get(player.getUniqueId());

        new PlayerManager(playerData).cancelRPGInterface();

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        if (!player.isOp())
            e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        if (!player.isOp())
            e.setCancelled(true);
    }
}
