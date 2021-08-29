package wtf.nucker.kitpvpplus.api.objects;

import org.bukkit.OfflinePlayer;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 28/08/2021
 */
public class LeaderboardValue {
    private final OfflinePlayer player;
    private final double value;

    public LeaderboardValue(OfflinePlayer player, double value) {
        this.player = player;
        this.value = value;
    }

    /**
     * @return the player's value
     */
    public double getValue() {
        return value;
    }

    /**
     * @return the player
     */
    public OfflinePlayer getPlayer() {
        return player;
    }

    /**
     * @param o the object your comparing
     * @return weather the object specified is an instance of this class and weather the player's uuids match
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof LeaderboardValue)) return false;
        LeaderboardValue compare = (LeaderboardValue) o;
        return compare.getPlayer().getUniqueId().equals(this.getPlayer().getUniqueId());
    }
}
