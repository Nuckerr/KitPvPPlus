package wtf.nucker.kitpvpplus.player;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class SQLPlayerData {

    private final Player player;
    private static Connection connection;
    private final String uuid;

    public SQLPlayerData(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId().toString();
        try {
            if (!this.containsPlayer(uuid)) {
                connection.prepareStatement("INSERT INTO player_data(UUID, KILLS, DEATHS, EXP, LEVEL) VALUE ('" + uuid + "', DEFAULT, DEFAULT, DEFAULT, DEFAULT)").executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setup(String uri, String username, String password) {
        try {
            SQLPlayerData.connection = DriverManager.getConnection(uri, username, password);

            connection.prepareStatement("CREATE TABLE PLAYER_DATA(" +
                    "    uuid varchar(36) NOT NULL comment 'Used for identifying data'," +
                    "    kills int default 0," +
                    "    deaths int default 0," +
                    "    exp int default 0," +
                    "    level int default 0" +
                    ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getKills() {
        return this.getData(this.uuid, "kills");
    }

    public int getDeaths() {
        return this.getData(this.uuid, "deaths");
    }

    public int getExp() {
        return this.getData(this.uuid, "exp");
    }

    public int getLevel() {
        return this.getData(this.uuid, "level");
    }

    public int setKills(int newValue) {
        return this.setData(uuid, "kills", newValue);
    }

    public int setDeaths(int newValue) {
        return this.setData(uuid, "deaths", newValue);
    }

    public int setExp(int newValue) {
        return this.setData(uuid, "exp", newValue);
    }

    public int setLevel(int newValue) {
        return this.setData(uuid, "level", newValue);
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
