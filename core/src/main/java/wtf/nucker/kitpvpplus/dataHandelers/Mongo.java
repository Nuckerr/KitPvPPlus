package wtf.nucker.kitpvpplus.dataHandelers;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.api.events.PlayerDataCreationEvent;
import wtf.nucker.kitpvpplus.exceptions.InsufficientBalance;
import wtf.nucker.kitpvpplus.listeners.custom.PlayerStateChangeEvent;
import wtf.nucker.kitpvpplus.managers.DataManager;
import wtf.nucker.kitpvpplus.managers.PlayerBank;
import wtf.nucker.kitpvpplus.objects.Kit;
import wtf.nucker.kitpvpplus.utils.APIConversion;
import wtf.nucker.kitpvpplus.utils.Config;
import wtf.nucker.kitpvpplus.utils.MongoDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 03/08/2021
 */
public class Mongo implements PlayerData {

    private final OfflinePlayer p;
    private static MongoDatabase database;
    private final KitPvPPlus core = KitPvPPlus.getInstance();
    private final MongoCollection<Document> collection;
    private Document doc;

    //Don't do IO from the constructor, also it's clever to keep this closable.
    public Mongo(final OfflinePlayer player) {
        this.p = player;
        this.collection = database.getCollection("data");
        if (collection.find(new Document("uuid", p.getUniqueId())).first() == null) {
            collection.insertOne(new Document("uuid", p.getUniqueId())
                    .append("deaths", 0)
                    .append("kills", 0)
                    .append("exp", 0)
                    .append("level", 0)
                    .append("killstreak", 0)
                    .append("highest-killstreak", 0));
            Bukkit.getPluginManager().callEvent(new PlayerDataCreationEvent(APIConversion.fromInstanceData(this)));
        }
        this.doc = collection.find(new Document("uuid", p.getUniqueId())).first();
    }

    @Override
    public void updateExp(int newAmount) {
        this.set("exp", newAmount);
    }

    @Override
    public void incrementDeaths() {
        this.set("deaths", this.getDeaths() + 1);
    }

    @Override
    public void incrementKills() {
        this.set("kills", this.getKills() + 1);
    }

    @Override
    public void updateLevel() {
        if(!p.isOnline()) return;
        float exp = this.getExp() - (this.getLevel() * 100);
        String modifiedExp = String.valueOf(exp).replace(".", "");
        modifiedExp = "0."+modifiedExp;
        p.getPlayer().setExp(Float.parseFloat(modifiedExp));
        p.getPlayer().setLevel(this.getLevel());
    }

    @Override
    public void incrementKillStreak() {
        this.set("killstreak", this.getKillStreak() + 1);
        if(this.getKillStreak() > this.getTopKillStreak()) {
            this.set("highest-killstreak", this.getKillStreak());
        }
    }

    @Override
    public void resetKillStreak() {
        this.set("killstreak", 0);
    }

    @Override
    public void resetData() {
        this.set("kills", 0);
        this.set("deaths", 0);
        this.set("exp", 0);
        this.set("level", 0);
        this.set("killstreak", 0);
        this.set("highest-killstreak", 0);
    }

    @Override
    public void deleteData() {
        Mongo.getClient().getCollection("data").findOneAndDelete(new Document("uuid", getPlayer().getUniqueId()));
    }

    @Override
    public void setState(PlayerState newState) {
        if(!p.isOnline()) return;
        PlayerState oldState = this.getState();
        DataManager.getPlayerStates().remove(p);
        DataManager.getPlayerStates().put(p, newState);
        Bukkit.getPluginManager().callEvent(new PlayerStateChangeEvent(p.getPlayer(), oldState, newState));
    }

    @Override
    public int getExp() {
        return doc.getInteger("exp");
    }

    @Override
    public int getDeaths() {
        return doc.getInteger("deaths");
    }

    @Override
    public int getKills() {
        return doc.getInteger("kills");
    }

    @Override
    public int getLevel() {
        return doc.getInteger("level");
    }

    @Override
    public int getKillStreak() {
        return doc.getInteger("killstreak");
    }

    @Override
    public int getTopKillStreak() {
        return doc.getInteger("highest-killstreak");
    }

    @Override
    public double getKDR() {
        if(this.getDeaths() <= 0 || this.getKills() <= 0) return 0;
        return ((double) this.getKills()) / ((double) this.getDeaths());    }

    @Override
    public PlayerState getState() {
        return DataManager.getPlayerStates().get(p);
    }

    @Override
    public void updateExpBar() {
        if(!p.isOnline()) return;
        float exp = this.getExp() - (this.getLevel() * 100);
        String modifiedExp = String.valueOf(exp).replace(".", "");
        modifiedExp = "0."+modifiedExp;
        p.getPlayer().setExp(Float.parseFloat(modifiedExp));
        p.getPlayer().setLevel(this.getLevel());
    }

    @Override
    public List<Kit> getOwnedKits() {
        List<Kit> res = new ArrayList<>();
        for (Kit kit : KitPvPPlus.getInstance().getKitManager().getKits()) {
            if (this.ownsKit(kit)) {
                res.add(kit);
            }
        }
        return res;
    }

    @Override
    public boolean ownsKit(Kit kit) {
        if(!p.isOnline()) return false;
        YamlConfiguration dataYaml = KitPvPPlus.getInstance().getDataManager().getDataYaml();
        if (p.isOp()) return true;
        if (p.getPlayer().hasPermission(kit.getPermission())) return true;
        return dataYaml.getStringList("playerdata." + p.getUniqueId() + ".owned-kits").contains(kit.getId());
    }

    @Override
    public List<Kit> purchaseKit(Kit kit) {
        Config dataConfig = KitPvPPlus.getInstance().getDataManager().getDataConfig();
        PlayerBank bank = new PlayerBank(p);
        if (bank.getBal() < kit.getPrice()) {
            throw new InsufficientBalance();
        }
        bank.setBal(bank.getBal() - kit.getPrice());
        List<String> res = dataConfig.getConfig().getStringList("playerdata." + p.getUniqueId() + ".owned-kits");
        res.add(kit.getId());
        dataConfig.getConfig().set("playerdata." + p.getUniqueId() + ".owned-kits", res);
        dataConfig.save();
        return this.getOwnedKits();
    }

    @Override
    public OfflinePlayer getPlayer() {
        return p;
    }

    public void updateDoc() {
        this.doc = collection.find(new Document("uuid", p.getUniqueId())).first();
    }

    public Document getDoc() {
        return this.doc;
    }

    public static MongoDatabase getClient() {
        return Mongo.database;
    }


    private void set(String key, int value) {
        database.save("data", new Document("uuid", p.getUniqueId()), key, value);
        this.updateDoc();
    }

    public static void setup(String host, int port) {
        Mongo.database = new MongoDatabase(MongoDatabase.buildURI(host, port));
    }

    public static void setup(String host, int port, String username, String password) {
        Mongo.database = new MongoDatabase(MongoDatabase.buildURI(host, port, username, password));
    }
}
