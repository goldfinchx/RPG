package ru.artorium.rpg.utils;


import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import ru.artorium.rpg.RPG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Bungee implements PluginMessageListener {

    @Getter@Setter public static List<String> serverList;
    @Getter@Setter public static HashMap<String, List<Player>> serversWithPlayers;

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord"))
            return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        switch (subchannel) {
            case "GetServers":
                String[] servers = in.readUTF().split(", ");
                setServerList(Arrays.asList(servers));

                break;
            case "PlayerCount":
                String server = in.readUTF();
                int playerCount = in.readInt();

                break;
            case "PlayerList":
                String server1 = in.readUTF();
                List<Player> playerList = new ArrayList<>();

                for (String playerName : in.readUTF().split(", "))
                    playerList.add(Bukkit.getPlayer(playerName));

                serversWithPlayers.put(server1, playerList);
                break;
            case "Forward":
            default:
                break;
        }
    }

    public static void sendPlayer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);

        player.sendPluginMessage(RPG.getInstance(), "BungeeCord", out.toByteArray());
    }

    public static void getPlayerCount(String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(server);

        Bukkit.getServer().sendPluginMessage(RPG.getInstance(), "BungeeCord", out.toByteArray());
    }

    public static void sendServers() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServers");
        serverList = new ArrayList<>();

        Bukkit.getServer().sendPluginMessage(RPG.getInstance(), "BungeeCord", out.toByteArray());
    }

    public static void sendPlayerList(String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("PlayerList");
        out.writeUTF(server);

        Bukkit.getServer().sendPluginMessage(RPG.getInstance(), "BungeeCord", out.toByteArray());
    }

    public static void sendPlayersOnServers() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        serversWithPlayers = new HashMap<>();
        serverList.forEach(server -> {
            out.writeUTF("PlayerList");
            out.writeUTF(server);

            Bukkit.getServer().sendPluginMessage(RPG.getInstance(), "BungeeCord", out.toByteArray());
        });

    }


}

