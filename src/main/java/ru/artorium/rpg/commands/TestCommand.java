package ru.artorium.rpg.commands;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.artorium.rpg.menus.TestMenu;
import ru.artorium.rpg.player.obj.PlayerData;

public class TestCommand extends Command {
    public TestCommand() {
        super("test");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player player = (Player) sender;
        PlayerData playerData = PlayerData.get(player.getUniqueId());
        player.spawnParticle(Particle.BLOCK_CRACK, player.getLocation().add(0, 1.5, 0), 15, Material.REDSTONE_BLOCK.createBlockData());
        TestMenu.testInventory.open(player);
        return false;
    }
}
