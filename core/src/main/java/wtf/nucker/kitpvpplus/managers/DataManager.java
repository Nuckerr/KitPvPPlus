package wtf.nucker.kitpvpplus.managers;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.exceptions.InsufficientBalance;
import wtf.nucker.kitpvpplus.listeners.custom.PlayerStateChangeEvent;
import wtf.nucker.kitpvpplus.objects.Kit;
import wtf.nucker.kitpvpplus.player.*;
import wtf.nucker.kitpvpplus.utils.Config;
import wtf.nucker.kitpvpplus.utils.Logger;
import wtf.nucker.kitpvpplus.utils.StorageType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class DataManager {

    private YamlConfiguration dataYaml = null;
    private StorageType storageType;
    private static HashMap<Player, PlayerState> playerStates;

    private Config dataConfig = null;

    public DataManager(KitPvPPlus plugin) {
        if (plugin.getConfig().getString("data-storage.type").equalsIgnoreCase("flat")) {
            this.dataConfig = new Config("data.yml");
            this.storageType = StorageType.FLAT;
            this.dataYaml = this.dataConfig.getConfig();
        } else if (plugin.getConfig().getString("data-storage.type").equalsIgnoreCase("mongo")) {
            this.storageType = StorageType.MONGO;

            if (plugin.getConfig().getBoolean("data-storage.authentication.enabled")) {
                MongoPlayerData.setup(plugin.getConfig().getString("data-storage.host"),
                        plugin.getConfig().getInt("data-storage.port"),
                        plugin.getConfig().getString("data-storage.authentication.username"),
                        plugin.getConfig().getString("data-storage.authentication.password"));
            } else { ;
                MongoPlayerData.setup(plugin.getConfig().getString("data-storage.host"),
                        plugin.getConfig().getInt("data-storage.port"));
            }
        } else if (plugin.getConfig().getString("data-storage.type").equalsIgnoreCase("mysql")) {
            this.storageType = StorageType.SQL;
            if (plugin.getConfig().getBoolean("data-storage.authentication.enabled")) {
                ConfigurationSection section = plugin.getConfig().getConfigurationSection("data-storage");
                SQLPlayerData.setup(SQLPlayerData.buildURL(section.getString("host"), section.getInt("port"), section.getString("database")),
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
        this.dataConfig = new Config("data.yml");
        this.dataYaml = this.dataConfig.getConfig();
    }

    public static HashMap<Player, PlayerState> getPlayerStates() {
        return playerStates;
    }

    public PlayerData getPlayerData(Player player) {
        
        FlatFilePlayerData flatFile = null;
        MongoPlayerData mongo = null;
        SQLPlayerData sql = null;
        
        switch (storageType) {
            case FLAT:
                flatFile = new FlatFilePlayerData(player);
                break;
            case MONGO:
                mongo = new MongoPlayerData(player);
            case SQL:
                sql = new SQLPlayerData(player);
        }

        FlatFilePlayerData finalFlatFile = flatFile;
        MongoPlayerData finalMongo = mongo;
        SQLPlayerData finalSql = sql;
        return new PlayerData() {
            @Override
            public void updateExp(int newAmount) {
                switch (storageType) {
                    case FLAT:
                        finalFlatFile.setExp(finalFlatFile.getExp() + newAmount);
                        break;
                    case MONGO:
                        finalMongo.setExp(finalMongo.getExp() + newAmount);
                        break;
                    case SQL:
                        finalSql.setExp(finalSql.getExp() + newAmount);
                        break;
                }
                this.updateLevel();
                this.updateExpBar();
            }

            @Override
            public void incrementDeaths() {
                switch (storageType) {
                    case FLAT:
                        finalFlatFile.setDeaths(finalFlatFile.getDeaths() + 1);
                        break;
                    case MONGO:
                        finalMongo.setDeaths(finalMongo.getDeaths() + 1);
                        break;
                    case SQL:
                        finalSql.setDeaths(finalSql.getDeaths() + 1);
                        break;
                }
            }

            @Override
            public void incrementKills() {
                switch (storageType) {
                    case FLAT:
                        finalFlatFile.setKills(finalFlatFile.getKills() + 1);
                        break;
                    case MONGO:
                        finalMongo.setKills(finalMongo.getKills() + 1);
                        break;
                    case SQL:
                        finalSql.setKills(finalSql.getKills() + 1);
                        break;
                }
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
                if(levelAsString.equals("")) levelAsString = "0";
                int level = Integer.parseInt(levelAsString);
                switch (storageType) {
                    case FLAT:
                        finalFlatFile.setLevel(level);
                        break;
                    case MONGO:
                        finalMongo.setLevel(level);
                        break;
                    case SQL:
                        finalSql.setLevel(level);
                        break;
                }
                this.updateExpBar();
            }

            @Override
            public void incrementKillStreak() {
                  switch (storageType) {
                      case FLAT:
                          finalFlatFile.setKillStreak(finalFlatFile.getKillStreak() + 1);
                          break;
                      case MONGO:
                          finalMongo.setKillStreak(finalMongo.getKillStreak() + 1);
                          break;
                      case SQL:
                          finalSql.setKillStreak(finalSql.getKillStreak() + 1);
                  }
            }

            @Override
            public void resetKillStreak() {
                switch (storageType) {
                    case FLAT:
                        finalFlatFile.setKillStreak(0);
                        break;
                    case MONGO:
                        finalMongo.setKillStreak(0);
                        break;
                    case SQL:
                        finalSql.setKillStreak(0);
                }
            }

            @Override
            public void resetData() {
                switch (storageType) {
                    case FLAT:
                        finalFlatFile.setKills(0);
                        finalFlatFile.setDeaths(0);
                        finalFlatFile.setExp(0);
                        finalFlatFile.setLevel(0);
                        break;
                    case MONGO:
                        finalMongo.setKills(0);
                        finalMongo.setDeaths(0);
                        finalMongo.setExp(0);
                        finalMongo.setLevel(0);
                        break;
                    case SQL:
                        finalSql.setKills(0);
                        finalSql.setDeaths(0);
                        finalSql.setExp(0);
                        finalSql.setLevel(0);
                        break;
                }
                this.updateLevel();
            }

            @Override
            public void deleteData() {
                switch (storageType) {
                    case FLAT:
                        getDataYaml().set("playerdata." + player.getUniqueId(), null);
                        break;
                    case MONGO:
                        MongoPlayerData.getClient().getCollection("data").findOneAndDelete(new Document("uuid", player.getUniqueId()));
                        break;
                    case SQL:
                        try {
                            SQLPlayerData.getConnection().prepareStatement("DELETE FROM player_data WHERE uuid = '" + player.getUniqueId() + "'");
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        break;
                }
                this.updateLevel();
            }

            @Override
            public void setState(PlayerState newState) {
                PlayerState oldState = this.getState();
                DataManager.playerStates.remove(player);
                DataManager.playerStates.put(player, newState);
                Bukkit.getPluginManager().callEvent(new PlayerStateChangeEvent(player, oldState, newState));
            }

            @Override
            public int getExp() {
                switch (storageType) {
                    case FLAT:
                        return finalFlatFile.getExp();
                    case MONGO:
                        return finalMongo.getExp();
                    case SQL:
                        return finalSql.getExp();
                }
                return 0;
            }

            @Override
            public int getDeaths() {
                switch (storageType) {
                    case FLAT:
                        return finalFlatFile.getDeaths();
                    case MONGO:
                        return finalMongo.getDeaths();
                    case SQL:
                        return finalSql.getDeaths();
                }
                return 0;
            }

            @Override
            public int getKills() {
                switch (storageType) {
                    case FLAT:
                        return finalFlatFile.getKills();
                    case MONGO:
                        return finalMongo.getKills();
                    case SQL:
                        return finalSql.getKills();
                }
                return 0;
            }

            @Override
            public int getLevel() {
                switch (storageType) {
                    case FLAT:
                        return finalFlatFile.getLevel();
                    case MONGO:
                        return finalMongo.getLevel();
                    case SQL:
                        return finalSql.getLevel();
                }
                return 0;
            }

            @Override
            public int getKillStreak() {
                switch (storageType) {
                    case FLAT:
                        return finalFlatFile.getKillStreak();
                    case MONGO:
                        return finalMongo.getKillStreak();
                    case SQL:
                        return finalSql.getKillStreak();
                }

                return 0;
            }

            @Override
            public int getTopKillStreak() {
                switch (storageType) {
                    case FLAT:
                        return finalFlatFile.getHighestKillStreak();
                    case MONGO:
                        finalMongo.getHighestKillStreak();
                    case SQL:
                        return finalSql.getHighestKillStreak();
                }
                return 0;
            }

            @Override
            public double getKDR() {
                if(this.getDeaths() <= 0 || this.getKills() <= 0) return 0;
                return ((double) this.getKills()) / ((double) this.getDeaths());
            }

            @Override
            public PlayerState getState() {
                return DataManager.playerStates.get(player);
            }

            public void updateExpBar() {
                float exp = this.getExp() - (this.getLevel() * 100);
                String modifiedExp = String.valueOf(exp).replace(".", "");
                modifiedExp = "0."+modifiedExp;
                player.setExp(Float.parseFloat(modifiedExp));
                player.setLevel(this.getLevel());
            }

            public List<Kit> getOwnedKits() {
                List<Kit> res = new ArrayList<>();
                for (Kit kit : KitPvPPlus.getInstance().getKitManager().getKits()) {
                    if (this.ownsKit(kit)) {
                        res.add(kit);
                    }
                }
                return res;
            }

            public boolean ownsKit(Kit kit) {
                if (player.isOp()) return true;
                if (player.hasPermission(kit.getPermission())) return true;
                return dataYaml.getStringList("playerdata." + player.getUniqueId() + ".owned-kits").contains(kit.getId());
            }

            @Override
            public List<Kit> purchaseKit(Kit kit) {
                PlayerBank bank = new PlayerBank(player);
                if (bank.getBal() < kit.getPrice()) {
                    throw new InsufficientBalance();
                }
                bank.setBal(bank.getBal() - kit.getPrice());
                List<String> res = dataConfig.getConfig().getStringList("playerdata." + player.getUniqueId() + ".owned-kits");
                res.add(kit.getId());
                dataYaml.set("playerdata." + player.getUniqueId() + ".owned-kits", res);
                dataConfig.save();
                return this.getOwnedKits();
            }

            @Override
            public Player getPlayer() {
                return player;
            }
        };
    }

    public Config getDataConfig() {
        return dataConfig;
    }

    public YamlConfiguration getDataYaml() {
        return dataYaml;
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public boolean isConnected() {
        switch (this.getStorageType()) {
            case FLAT:
                return !(getDataYaml() == null);
            case SQL:
                try {
                    return !SQLPlayerData.getConnection().isClosed();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            case MONGO:
                return !(MongoPlayerData.getClient() == null);
            default:
                return false;
        }
    }
}
