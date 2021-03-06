package wtf.nucker.kitpvpplus.objects;

import org.bukkit.OfflinePlayer;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 28/07/2021
 */
public class LeaderboardValue {

    private final OfflinePlayer player;
    private final double value;

    public LeaderboardValue(OfflinePlayer player, double value) {
        this.player = player;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof LeaderboardValue)) return false;
        LeaderboardValue compare = (LeaderboardValue) o;
        return compare.getPlayer().getUniqueId().equals(this.getPlayer().getUniqueId());
    }
}
