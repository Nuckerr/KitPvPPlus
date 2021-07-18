package wtf.nucker.kitpvpplus.api.objects;

import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.managers.DataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 14/07/2021
 */
public class PlayerData {

    private final Player player;
    private final wtf.nucker.kitpvpplus.player.PlayerData data;

    private PlayerData(Player player) {
        this.player = player;
        this.data = KitPvPPlus.getInstance().getDataManager().getPlayerData(this.player);
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the exp of the player
     * @param exp The exp it should be set to
     */
    public void setExp(int exp) {
        data.updateExp(-data.getExp());
        data.updateExp(exp);
    }

    /**
     * Adds an amount to the player's exp
     * @param exp The exp you want to be added
     */
    public void addExp(int exp) {
        data.updateExp(exp);
    }

    /**
     * Removes an amount from the players exp
     * @param exp The exp you want to be removed
     */
    public void minusExp(int exp) {
        data.updateExp(-exp);
    }

    /**
     * @return The player's exp
     */
    public int getExp() {
        return data.getExp();
    }

    /**
     * Increments the player's kills by 1
     */
    public void incrementKills() {
        data.incrementKills();;
    }

    /**
     * @return the player's kills
     */
    public int getKills() {
        return data.getKills();
    }

    /**
     * Increments deaths by 1
     */
    public void incrementDeaths() {
        data.incrementDeaths();
    }

    /**
     * @return the player's deaths
     */
    public int getDeaths() {
        return data.getDeaths();
    }

    /**
     * <b>You cannot set levels. It is automaticly calculated
     * by the exp. EG: 437 would be level 4</b>
     * @return the player's level
     */
    public int getLevel() {
        return data.getLevel();
    }

    /**
     * @return the state of the player
     */
    public PlayerState getState() {
        return PlayerState.fromInstanceState(data.getState());
    }

    /**
     * @return a list of all the kits owned by the player
     */
    public List<Kit> getOwnedKits() {
        List<Kit> res = new ArrayList<>();
        data.getOwnedKits().forEach(kit -> {
            res.add(Kit.fromInstanceKit(kit));
        });

        return res;
    }

    /**
     * @see PlayerData#getOwnedKits() Uses PlayerData#getOwnedKits() to see if the list contains the kit
     * @param kit the kit you want to check
     * @return weather the player owns it or not
     */
    public boolean ownsKit(Kit kit) {
        return this.getOwnedKits().contains(kit);
    }

    /**
     * Used to purchase a kit
     * @param kit the kit you want to purchase
     */
    public void purchaseKit(Kit kit) {
        data.purchaseKit(Kit.toInstanceKit(kit));
    }

}
