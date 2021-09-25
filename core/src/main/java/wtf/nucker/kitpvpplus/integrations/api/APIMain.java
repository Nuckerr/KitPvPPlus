package wtf.nucker.kitpvpplus.integrations.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.api.managers.ConfigManager;
import wtf.nucker.kitpvpplus.api.managers.KitManager;
import wtf.nucker.kitpvpplus.api.managers.LeaderboardManager;
import wtf.nucker.kitpvpplus.api.managers.LocationsManager;
import wtf.nucker.kitpvpplus.api.objects.*;
import wtf.nucker.kitpvpplus.managers.LeaderBoardManager;
import wtf.nucker.kitpvpplus.managers.Locations;
import wtf.nucker.kitpvpplus.utils.APIConversion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Nucker
 * @project KitPvPPlus
 * @date 25/09/2021
 */
public class APIMain extends wtf.nucker.kitpvpplus.api.KitPvPPlusAPI {

    private final KitPvPPlus plugin;

    public APIMain(KitPvPPlus plugin) {
        this.plugin = plugin;
        this.setInstance(this);
    }

    @Override
    public void registerAbility(Ability ability) {
        plugin.getAbilityManager().registerAbility(APIConversion.toInstanceAbility(ability));
    }

    @Override
    public KitManager getKitManager() {
        return new KitManager() {
            @Override
            public Kit getKitById(String id) {
                return APIConversion.fromInstanceKit(plugin.getKitManager().getKit(id));
            }

            @Override
            public List<Kit> getKits() {
                List<wtf.nucker.kitpvpplus.api.objects.Kit> res = new ArrayList<>();
                plugin.getKitManager().getKits().forEach(k -> res.add(APIConversion.fromInstanceKit(k)));
                return res;
            }

            @Override
            public Kit createKit(String id) {
                plugin.getKitManager().createKit(id);
                return APIConversion.fromInstanceKit(plugin.getKitManager().getKit(id));
            }
        };
    }

    @Override
    public LocationsManager getLocationsManager() {
        return new LocationsManager() {
            @Override
            public Location getSpawn() {
                return Locations.SPAWN.get();
            }

            @Override
            public Location getArena() {
                return Locations.ARENA.get();
            }
        };
    }

    @Override
    public LeaderboardManager getLeaderboardManager() {
        return new LeaderboardManager() {

            final LeaderBoardManager manager = plugin.getLeaderBoardManager();

            @Override
            public Leaderboard getDeathsLeaderboard() {
                return APIConversion.fromInstanceLeaderboard(manager.getDeathsLeaderboard());
            }

            @Override
            public Leaderboard getBalLeaderboard() {
                return APIConversion.fromInstanceLeaderboard(manager.getBalLeaderboard());
            }

            @Override
            public Leaderboard getExpLeaderboard() {
                return APIConversion.fromInstanceLeaderboard(manager.getExpLeaderboard());
            }

            @Override
            public Leaderboard getKdrLeaderboard() {
                return APIConversion.fromInstanceLeaderboard(manager.getKdrLeaderboard());
            }

            @Override
            public Leaderboard getKillsLeaderboard() {
                return APIConversion.fromInstanceLeaderboard(manager.getKillsLeaderboard());
            }

            @Override
            public Leaderboard getKillStreakLeaderboard() {
                return APIConversion.fromInstanceLeaderboard(manager.getKsLeaderboard());
            }

            @Override
            public Leaderboard getLevelLeaderboard() {
                return APIConversion.fromInstanceLeaderboard(manager.getLevelLeaderboard());
            }
        };
    }

    @Override
    public ConfigManager getConfigManager() {
        return new ConfigManager() {
            @Override
            public ConfigValue getMessage(String path) {
                return new ConfigValue(plugin.getMessages(), path);
            }

            @Override
            public ConfigValue getFromConfig(String path) {
                return new ConfigValue(plugin.getConfig(), path);
            }

            @Override
            public ConfigValue getDataRaw(String path) {
                return new ConfigValue(plugin.getDataManager().getDataYaml(), path);
            }

            @Override
            public ConfigValue getSignDataRaw(String path) {
                return new ConfigValue(plugin.getSignManager().getConfig(), path);
            }

            @Override
            public ConfigValue getKitDataRaw(String path) {
                return new ConfigValue(wtf.nucker.kitpvpplus.managers.KitManager.getConfig(), path);
            }
        };
    }

    @Override
    public PlayerData getPlayerData(UUID uuid) {
        return APIConversion.fromInstanceData(plugin.getDataManager().getPlayerData(Bukkit.getPlayer(uuid)));
    }

    @Override
    public List<PlayerData> getAllPlayerData() {
        List<PlayerData> playerData = new ArrayList<>();
        plugin.getDataManager().getAllPlayerData().forEach(data -> playerData.add(APIConversion.fromInstanceData(data)));

        return playerData;
    }

    @Override
    public PlayerData getPlayerData(OfflinePlayer player) {
        return APIConversion.fromInstanceData(plugin.getDataManager().getPlayerData(player));
    }

}
