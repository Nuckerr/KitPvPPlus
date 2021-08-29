package wtf.nucker.kitpvpplus.api.objects;

import org.bukkit.OfflinePlayer;

import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 28/08/2021
 */
public interface Leaderboard {

    /**
     * @param amount the amount of places it should attempt to get
     * @return A list of LeaderboardValues up to the specified amount.
     */
    List<LeaderboardValue> getTop(int amount);

    /**
     * @param player The player your trying to get the place for
     * @return The player's place in the leaderboard
     */
    int getPlace(OfflinePlayer player);

    /**
     * @return The raw list of the leaderboard
     */
    List<LeaderboardValue> getList();

    /**
     * @return the displayname of the leaderboard
     */
    String getDisplayname();

    /**
     * @return the id of the leaderboard
     */
    String getId();
}
