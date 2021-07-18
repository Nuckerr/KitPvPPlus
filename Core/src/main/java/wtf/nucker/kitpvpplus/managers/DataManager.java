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

            Logger.debug(plugin.getConfig().getString("data-storage.host"));
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
        return new PlayerData() {
            @Override
            public void updateExp(int newAmount) {
                switch (storageType) {
                    case FLAT:
                        new FlatFilePlayerData(player).setExp(new FlatFilePlayerData(player).getExp() + newAmount);
                        break;
                    case MONGO:
                        new MongoPlayerData(player).setExp(new MongoPlayerData(player).getExp() + newAmount);
                        break;
                    case SQL:
                        new SQLPlayerData(player).setExp(new SQLPlayerData(player).getExp() + newAmount);
                        break;
                }
                this.updateLevel();
                this.updateExpBar();
            }

            @Override
            public void incrementDeaths() {
                switch (storageType) {
                    case FLAT:
                        new FlatFilePlayerData(player).setDeaths(new FlatFilePlayerData(player).getDeaths() + 1);
                        break;
                    case MONGO:
                        new MongoPlayerData(player).setDeaths(new MongoPlayerData(player).getDeaths() + 1);
                        break;
                    case SQL:
                        new SQLPlayerData(player).setDeaths(new SQLPlayerData(player).getDeaths() + 1);
                        break;
                }
            }

            @Override
            public void incrementKills() {
                switch (storageType) {
                    case FLAT:
                        new FlatFilePlayerData(player).setKills(new FlatFilePlayerData(player).getKills() + 1);
                        break;
                    case MONGO:
                        new MongoPlayerData(player).setKills(new MongoPlayerData(player).getKills() + 1);
                        break;
                    case SQL:
                        new SQLPlayerData(player).setKills(new SQLPlayerData(player).getKills() + 1);
                        break;
                }
            }

            @Override
            public void updateLevel() {
                int exp = this.getExp();
                String levelAsString = String.valueOf(exp);
                if(!levelAsString.equals("0")) {
                    levelAsString = levelAsString.substring(0, levelAsString.length() - 2);
                }
                if(levelAsString.equals("")) levelAsString = "0";
                int level = Integer.parseInt(levelAsString);
                switch (storageType) {
                    case FLAT:
                        new FlatFilePlayerData(player).setLevel(level);
                        break;
                    case MONGO:
                        new MongoPlayerData(player).setLevel(level);
                        break;
                    case SQL:
                        new SQLPlayerData(player).setLevel(level);
                        break;
                }
                this.updateExpBar();
            }

            @Override
            public void resetData() {
                switch (storageType) {
                    case FLAT:
                        FlatFilePlayerData flatFile = new FlatFilePlayerData(player);
                        flatFile.setKills(0);
                        flatFile.setDeaths(0);
                        flatFile.setExp(0);
                        flatFile.setLevel(0);
                        break;
                    case MONGO:
                        MongoPlayerData data = new MongoPlayerData(player);
                        data.setKills(0);
                        data.setDeaths(0);
                        data.setExp(0);
                        data.setLevel(0);
                        break;
                    case SQL:
                        SQLPlayerData sql = new SQLPlayerData(player);
                        sql.setKills(0);
                        sql.setDeaths(0);
                        sql.setExp(0);
                        sql.setLevel(0);
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
                        return new FlatFilePlayerData(player).getExp();
                    case MONGO:
                        return new MongoPlayerData(player).getExp();
                    case SQL:
                        return new SQLPlayerData(player).getExp();
                }
                return 0;
            }

            @Override
            public int getDeaths() {
                switch (storageType) {
                    case FLAT:
                        return new FlatFilePlayerData(player).getDeaths();
                    case MONGO:
                        return new MongoPlayerData(player).getDeaths();
                    case SQL:
                        return new SQLPlayerData(player).getDeaths();
                }
                return 0;
            }

            @Override
            public int getKills() {
                switch (storageType) {
                    case FLAT:
                        return new FlatFilePlayerData(player).getKills();
                    case MONGO:
                        return new MongoPlayerData(player).getKills();
                    case SQL:
                        return new SQLPlayerData(player).getKills();
                }
                return 0;
            }

            @Override
            public int getLevel() {
                switch (storageType) {
                    case FLAT:
                        return new FlatFilePlayerData(player).getLevel();
                    case MONGO:
                        return new MongoPlayerData(player).getLevel();
                    case SQL:
                        return new SQLPlayerData(player).getLevel();
                }
                return 0;
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
                if(kit.getPermission().equals("")) return true;
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
