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

    @Getter@Setter private Document clanDocument;
    @Getter@Setter private static String name;
    @Getter@Setter private static String tag;
    @Getter@Setter private static UUID leader;
    @Getter@Setter private static List<UUID> members;


    public RPGClan() {

    }

}
