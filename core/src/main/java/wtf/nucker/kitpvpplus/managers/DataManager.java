package wtf.nucker.kitpvpplus.managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.dataHandelers.*;
import wtf.nucker.kitpvpplus.utils.Config;
import wtf.nucker.kitpvpplus.utils.Logger;
import wtf.nucker.kitpvpplus.utils.StorageType;

import java.sql.SQLException;
import java.util.*;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class DataManager {

    private StorageType storageType;
    private static Map<OfflinePlayer, PlayerState> playerStates;

    private Config dataConfig;

    public DataManager(KitPvPPlus plugin) {
        this.dataConfig = new Config("data.yml");
        if (plugin.getConfig().getString("data-storage.type").equalsIgnoreCase("flat")) {
            this.storageType = StorageType.FLAT;
        } else if (plugin.getConfig().getString("data-storage.type").equalsIgnoreCase("mongo")) {
            this.storageType = StorageType.MONGO;

            if (plugin.getConfig().getBoolean("data-storage.authentication.enabled")) {
                Mongo.setup(plugin.getConfig().getString("data-storage.host"),
                        plugin.getConfig().getInt("data-storage.port"),
                        plugin.getConfig().getString("data-storage.authentication.username"),
                        plugin.getConfig().getString("data-storage.authentication.password"));
            } else {
                Mongo.setup(plugin.getConfig().getString("data-storage.host"),
                        plugin.getConfig().getInt("data-storage.port"));
            }
        } else if (plugin.getConfig().getString("data-storage.type").equalsIgnoreCase("mysql")) {
            this.storageType = StorageType.SQL;
            if (plugin.getConfig().getBoolean("data-storage.authentication.enabled")) {
                ConfigurationSection section = plugin.getConfig().getConfigurationSection("data-storage");
                SQL.setup(SQL.buildURL(section.getString("host"), section.getInt("port"), section.getString("database")),
                        section.getString("authentication.username"), section.getString("authentication.password"));
            } else {
                Logger.error("Authentication must be enabled for MySQL");
                plugin.getPluginLoader().disablePlugin(plugin);
            }
        }

        DataManager.playerStates = new HashMap<>();
        if (plugin.getServer().getOnlinePlayers().size() > 0) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                player.teleport(Locations.SPAWN.get());
                playerStates.put(player, PlayerState.SPAWN);
            }
        }
    }


    public static Map<OfflinePlayer, PlayerState> getPlayerStates() {
        return playerStates;
    }

    public List<PlayerData> getAllPlayerData() {
        List<PlayerData> res = new ArrayList<>();

        if(this.getDataYaml().getConfigurationSection("playerdata") == null) return res;
        this.getDataYaml().getConfigurationSection("playerdata").getKeys(false).forEach(key -> {
            res.add(this.getPlayerData(Bukkit.getOfflinePlayer(UUID.fromString(key))));
        });

        return res;
    }

    public PlayerData getPlayerData(OfflinePlayer player) {
        if(player == null) return null;
       switch (this.storageType) {
           case MONGO:
               return new Mongo(player);
           case SQL:
               return new SQL(player);
           default:
               return new FlatFile(player);
       }
    }

    public Config getDataConfig() {
        return this.dataConfig;
    }
    public YamlConfiguration getDataYaml() {
        return this.getDataConfig().getConfig();
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public boolean isConnected() {
        switch (this.getStorageType()) {
            case FLAT:
                return !(this.getDataConfig() == null);
            case SQL:
                try {
                    return !SQL.getConnection().isClosed();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            case MONGO:
                return !(Mongo.getClient() == null);
            default:
                return false;
        }
    }
}
