package ru.artorium.rpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.artorium.rpg.menus.TestMenu;

public class TestCommand extends Command {
    public TestCommand() {
        super("test");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player player = (Player) sender;

        TestMenu.testInventory.open(player);
        return false;
    }
}
