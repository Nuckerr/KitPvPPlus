package wtf.nucker.kitpvpplus.dataHandelers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.api.events.PlayerDataCreationEvent;
import wtf.nucker.kitpvpplus.exceptions.InsufficientBalance;
import wtf.nucker.kitpvpplus.exceptions.PermissionException;
import wtf.nucker.kitpvpplus.listeners.custom.PlayerStateChangeEvent;
import wtf.nucker.kitpvpplus.managers.DataManager;
import wtf.nucker.kitpvpplus.managers.PlayerBank;
import wtf.nucker.kitpvpplus.objects.Kit;
import wtf.nucker.kitpvpplus.utils.APIConversion;
import wtf.nucker.kitpvpplus.utils.Config;
import wtf.nucker.kitpvpplus.utils.PlayerUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 03/08/2021
 */
public class SQL implements PlayerData {

    private final OfflinePlayer player;
    private static Connection connection;
    private final String uuid;

    public SQL(final OfflinePlayer player) {
        this.player = player;
        this.uuid = player.getUniqueId().toString();
        try {
            if (!this.containsPlayer(uuid)) {
                connection.prepareStatement("INSERT INTO player_data(UUID, KILLS, DEATHS, EXP, LEVEL) VALUE ('" + uuid + "', DEFAULT, DEFAULT, DEFAULT, DEFAULT)").executeUpdate();
                Bukkit.getPluginManager().callEvent(new PlayerDataCreationEvent(APIConversion.fromInstanceData(this)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setup(String uri, String username, String password) {
        try {
            SQL.connection = DriverManager.getConnection(uri, username, password);

            connection.prepareStatement("CREATE TABLE PLAYER_DATA(" +
                    "    uuid varchar(36) NOT NULL comment 'Used for identifying data'," +
                    "    kills int default 0," +
                    "    deaths int default 0," +
                    "    exp int default 0," +
                    "    level int default 0" +
                    "    killstreak int default 0" +
                    "    highestkillstreak int default 0" +
                    ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateExp(int newAmount) {
        this.setData(uuid, "level", newAmount + this.getExp());
    }

    @Override
    public void incrementDeaths() {
        this.setData(uuid, "deaths", this.getDeaths() + 1);
    }

    @Override
    public void incrementKills() {
        this.setData(uuid, "deaths", this.getDeaths() + 1);
    }

    @Override
    public void updateLevel() {
        int exp = this.getExp();
        String levelAsString = String.valueOf(exp);
        if(levelAsString.length() > 2) {
            levelAsString = levelAsString.substring(0, levelAsString.length() - 2);
        }else {
            levelAsString = "";
        }
        if(levelAsString.isEmpty()) levelAsString = "0";
        int level = Integer.parseInt(levelAsString);

        this.setData(uuid, "level", level);
        this.updateExpBar();
    }

    @Override
    public void incrementKillStreak() {
        this.setData(uuid, "killstreak", this.getKillStreak() + 1);
    }

    @Override
    public void resetKillStreak() {
        this.setData(uuid, "killstreak", 0);
    }

    @Override
    public void resetData() {
        this.setData(uuid, "deaths", 0);
        this.setData(uuid, "kills", 0);
        this.setData(uuid, "killstreak", 0);
        this.setData(uuid, "highestkillstreak", 0);
        this.setData(uuid, "exp", 0);
        this.setData(uuid, "level", 0);
        this.setData(uuid, "level", 0);
    }

    @Override
    public void deleteData() {
        try {
            SQL.getConnection().prepareStatement("DELETE FROM player_data WHERE uuid = '" + player.getUniqueId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setState(PlayerState newState) {
        if(!player.isOnline()) return;
        PlayerState oldState = this.getState();
        DataManager.getPlayerStates().remove(player);
        DataManager.getPlayerStates().put(player, newState);
        Bukkit.getPluginManager().callEvent(new PlayerStateChangeEvent(player.getPlayer(), oldState, newState));
    }

    @Override
    public int getExp() {
        return this.getData(uuid, "exp");
    }

    @Override
    public int getDeaths() {
        return this.getData(uuid, "deaths");
    }

    @Override
    public int getKills() {
        return this.getData(uuid,"kills");
    }

    @Override
    public int getLevel() {
        return this.getData(uuid, "level");
    }

    @Override
    public int getKillStreak() {
        return this.getData(uuid, "killstreak");
    }

    @Override
    public int getTopKillStreak() {
        return this.getData(uuid, "highestkillstreak");
    }

    @Override
    public double getKDR() {
        if(this.getDeaths() <= 0 || this.getKills() <= 0) return 0;
        return ((double) this.getKills()) / ((double) this.getDeaths());
    }

    @Override
    public PlayerState getState() {
        return DataManager.getPlayerStates().get(player);
    }

    @Override
    public void updateExpBar() {
        if(!player.isOnline()) return;
        float exp = this.getExp() - (this.getLevel() * 100);
        String modifiedExp = String.valueOf(exp).replace(".", "");
        modifiedExp = "0."+modifiedExp;
        player.getPlayer().setExp(Float.parseFloat(modifiedExp));
        player.getPlayer().setLevel(this.getLevel());
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
        YamlConfiguration dataYaml = KitPvPPlus.getInstance().getDataManager().getDataYaml();
        if(!kit.getPermission().equals("")) {
            if(PlayerUtils.checkOfflinePermission(player, kit.getPermission())) {
                if(kit.isFree()) return true;
                return dataYaml.getStringList("owned-kits").contains(kit.getId());
            }
            return false;
        }
        if(kit.isFree()) return true;
        return dataYaml.getStringList("owned-kits").contains(kit.getId());
    }

    @Override
    public List<Kit> purchaseKit(Kit kit) {
        if(!kit.getPermission().equals("")) {
            if(!PlayerUtils.checkOfflinePermission(player, kit.getPermission())) throw new PermissionException(player.getName() + " is missing permission " + kit.getPermission());
        }
        Config dataConfig = KitPvPPlus.getInstance().getDataManager().getDataConfig();
        PlayerBank bank = new PlayerBank(player);
        if (bank.getBal() < kit.getPrice()) {
            throw new InsufficientBalance();
        }
        bank.setBal(bank.getBal() - kit.getPrice());
        List<String> res = dataConfig.getConfig().getStringList("playerdata." + player.getUniqueId() + ".owned-kits");
        res.add(kit.getId());
        dataConfig.getConfig().set("playerdata." + player.getUniqueId() + ".owned-kits", res);
        dataConfig.save();
        return this.getOwnedKits();
    }

    @Override
    public OfflinePlayer getPlayer() {
        return this.player;
    }

    public boolean containsPlayer(String uuid) {
        try {
            ResultSet set = connection.prepareStatement("SELECT COUNT(UUID) FROM player_data WHERE UUID = '" + uuid + "';").executeQuery();
            set.next();
            return !(set.getInt(1) == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getData(String uuid, String collum) {
        try {
            if (!(this.containsPlayer(uuid))) return -1;
            ResultSet set = connection.prepareStatement("SELECT * FROM `player_DATA` WHERE UUID = '" + uuid + "';").executeQuery();
            set.next();
            return set.getInt(collum);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int setData(String uuid, String collum, int newValue) {
        try {
            if (!this.containsPlayer(uuid)) return -1;
            connection.prepareStatement("UPDATE player_data SET " + collum + " = " + newValue + " WHERE uuid = '" + uuid + "';").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.getData(uuid, collum);
    }

    public static String buildURL(String host, int port, String database) {
        return "jdbc:mysql://[host]:[port]/[database]".replace("[host]", host).replace("[port]", String.valueOf(port)).replace("[database]", database);
    }

    public static Connection getConnection() {
        return connection;
    }
}
