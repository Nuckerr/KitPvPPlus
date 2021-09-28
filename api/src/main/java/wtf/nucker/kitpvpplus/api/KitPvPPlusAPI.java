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
 * @project KitPvPPlus
 * @date 20/09/2021
 */
public abstract class KitPvPPlusAPI {

    private static KitPvPPlusAPI instance;

    protected void setInstance(KitPvPPlusAPI instance) {
        KitPvPPlusAPI.instance = instance;
    }

    /**
     * @return instance of the API
     */
    public static KitPvPPlusAPI getInstance() { return  instance; }

    /**
     * Registers a custom ability to the server
     * @param ability API instance of the ability you are registering
     * **MAKE SURE YOUR ID DOES NOT CONFLICT WITH ANY OTHER ABILITIES
     */
    public abstract void registerAbility(Ability ability);

    /**
     * @return the servers kit manager
     */
    public abstract KitManager getKitManager();

    /**
     * @return the servers locations manager
     */
    public abstract LocationsManager getLocationsManager();

    /**
     * @return the server's leaderboard manager
     */
    public abstract LeaderboardManager getLeaderboardManager();

    /**
     * @return the servers config manager
     */
    public abstract ConfigManager getConfigManager();

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

    /**
     * @return the dump debug information (Same as what you get when you run /kitpvpplus dump)
     */
    public abstract String getDumpInformation();
}
