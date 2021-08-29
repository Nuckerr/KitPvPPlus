package wtf.nucker.kitpvpplus.api;

import org.bukkit.OfflinePlayer;
import wtf.nucker.kitpvpplus.api.managers.ConfigManager;
import wtf.nucker.kitpvpplus.api.managers.KitManager;
import wtf.nucker.kitpvpplus.api.managers.LeaderboardManager;
import wtf.nucker.kitpvpplus.api.managers.LocationsManager;
import wtf.nucker.kitpvpplus.api.objects.Ability;
import wtf.nucker.kitpvpplus.api.objects.PlayerData;

import java.util.List;
import java.util.UUID;

/**
 * @author Nucker
 * @project KitPvpCore  
 * @date 21/07/2021
 */
public abstract class KitPvPPlusAPI {

    private static KitPvPPlusAPI instance;

    private final KitManager kitManager;
    private final LocationsManager locationsManager;
    private final ConfigManager configManager;
    private final LeaderboardManager leaderboardManager;

    public KitPvPPlusAPI(KitManager kitManager, LocationsManager locationsManager, ConfigManager configManager, LeaderboardManager leaderboardManager) {
        instance = this;

        this.kitManager = kitManager;
        this.locationsManager = locationsManager;
        this.configManager = configManager;
        this.leaderboardManager = leaderboardManager;
    }

    /**
     * Registers a custom ability to the server
     * @param ability API instance of the ability you are registering
     * **MAKE SURE YOUR ID DOES NOT CONFLICT WITH ANY OTHER ABILITIES
     */
    public abstract void registerAbility(Ability ability);

    /**
     * @return the servers kit manager
     */
    public KitManager getKitManager() {
        return this.kitManager;
    }

    /**
     * @return the servers locations manager
     */
    public LocationsManager getLocationsManager() {
        return this.locationsManager;
    }

    /**
     * @return the server's leaderboard manager
     */
    public LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }

    /**
     * @return the servers config manager
     */
    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    /**
     * @return instance of the API
     */
    public static KitPvPPlusAPI getInstance() {
        return instance;
    }

    /**
     * @param uuid the uuid of the player your trying to get an instance of
     * @return that player's playerdata instance
     */
    public abstract PlayerData getPlayerData(UUID uuid);

    /**
     * @return all the playerdata on the server
     */
    public abstract List<PlayerData> getAllPlayerData();
    /**
     * @param player the name of the player your trying to get an instance of
     * @return that player's playerdata instance
     */
    public abstract PlayerData getPlayerData(OfflinePlayer player);
}
