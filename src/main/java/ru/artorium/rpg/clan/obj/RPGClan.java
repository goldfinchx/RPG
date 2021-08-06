package ru.artorium.rpg.clan.obj;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import ru.artorium.rpg.RPG;

import java.util.List;
import java.util.UUID;

public class RPGClan {

    private final static MongoCollection<Document> clansCollection =
            RPG.getInstance().getMongo().getCollection("clans");

    private Document clanDocument;
    private static String name;
    private static String tag;
    private static UUID leader;
    private static List<UUID> members;


    public RPGClan() {

    }

}
