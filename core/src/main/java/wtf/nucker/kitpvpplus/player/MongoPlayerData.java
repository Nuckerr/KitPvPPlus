package wtf.nucker.kitpvpplus.player;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.utils.MongoDatabase;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class MongoPlayerData {

    private final Player p;
    private static MongoDatabase database;
    private final KitPvPPlus core = KitPvPPlus.getInstance();
    private final MongoCollection<Document> collection;
    private Document doc;

    //Don't do IO from the constructor, also it's clever to keep this closable.
    public MongoPlayerData(Player player) {
        this.p = player;
        this.collection = database.getCollection("data");
        if (collection.find(new Document("uuid", p.getUniqueId())).first() == null) {
            collection.insertOne(new Document("uuid", p.getUniqueId())
                    .append("deaths", 0)
                    .append("kills", 0)
                    .append("exp", 0)
                    .append("level", 0));
        }
        this.doc = collection.find(new Document("uuid", p.getUniqueId())).first();
    }

    public int getKills() {
        return doc.getInteger("kills");
    }

    public int getDeaths() {
        return doc.getInteger("deaths");
    }

    public int getExp() {
        return doc.getInteger("exp");
    }

    public int getLevel() {
        return doc.getInteger("level");
    }

    public int setKills(int newAmount) {
        database.save("data", new Document("uuid", p.getUniqueId()), "kills", newAmount);
        this.updateDoc();
        return doc.getInteger("kills");
    }

    public int setDeaths(int newAmount) {
        database.save("data", new Document("uuid", p.getUniqueId()), "deaths", newAmount);
        this.updateDoc();
        return doc.getInteger("deaths");
    }

    public int setExp(int newAmount) {
        database.save("data", new Document("uuid", p.getUniqueId()), "exp", newAmount);
        this.updateDoc();
        return doc.getInteger("exp");
    }

    public int setLevel(int newAmount) {
        database.save("data", new Document("uuid", p.getUniqueId()), "level", newAmount);
        this.updateDoc();
        return doc.getInteger("level");
    }


    public void updateDoc() {
        this.doc = collection.find(new Document("uuid", p.getUniqueId())).first();
    }

    public Document getDoc() {
        return this.doc;
    }

    public static MongoDatabase getClient() {
        return MongoPlayerData.database;
    }

    public static void setup(String host, int port) {
        MongoPlayerData.database = new MongoDatabase(MongoDatabase.buildURI(host, port));
    }

    public static void setup(String host, int port, String username, String password) {
        MongoPlayerData.database = new MongoDatabase(MongoDatabase.buildURI(host, port, username, password));
    }
}
