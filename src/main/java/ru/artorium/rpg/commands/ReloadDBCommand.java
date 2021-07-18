package ru.artorium.rpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.artorium.rpg.systems.battle_system.obj.RPGItem;

import java.util.ArrayList;

public class ReloadDBCommand extends Command {
    public ReloadDBCommand() {
        super("reloadItems", "Перезагрузка дата-базы предметов", "§aБаза предметов успешно перезагружена!", new ArrayList<String>(){{
           add("ri");
           add("reloaditems");
           add("reloaddb");
        }});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player player = (Player) sender;
        if (!player.isOp())
            return false;

        RPGItem.loadUpItems();

        return false;
    }
}