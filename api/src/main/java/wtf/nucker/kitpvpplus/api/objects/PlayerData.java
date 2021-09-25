package wtf.nucker.kitpvpplus.api.objects;

import org.bukkit.OfflinePlayer;

import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 28/07/2021
 */
public interface PlayerData {

    /**
     * @return the player's exp
     */
    int getExp();

    /**
     * @return the player's kills
     */
    int getKills();

    /**
     * @return the player's deaths
     */
    int getDeaths();

    /**
     * @return the player's level
     */
    int getLevel();

    /**
     * @return the player's current kill streak
     */
    int getKillStreak();

    /**
     * @return the player's top kill streak
     */
    int getTopKillStreak();

    /**
     * @return the player's kill:death ratio
     */
    double getKDR();

    /**
     * @return the player's balance
     */
    double getBal();

    /**
     * @return the player's current playerstate
     * @see PlayerState
     */
    PlayerState getState();

    /**
     * @return a list with all of the player's owned kits
     * @see Kit
     */
    List<Kit> getOwnedKits();

    /**
     * @param kit the kit that is chcked
     * @return weather the provided kit is owned by the player
     */
    boolean ownsKit(Kit kit);

    /**
     * @param kit the kit to purchase
     * @return The updated list of owned kits
     */
    List<Kit> purchaseKit(Kit kit);

    /**
     * Used to update a player's state
     * @param state The new playerstate
     * @see PlayerState
     */
    void  setState(PlayerState state);

    /**
     * Re-calculates the player's level
     */
    void updateLevel();

    /**
     * Adds to the player's current exp
     * @param newAmount The exp to be added to the player's current exp
     */
    void addExp(int newAmount);

    /**
     * @return the owner of the playerdata
     */
    OfflinePlayer getPlayer();
}
