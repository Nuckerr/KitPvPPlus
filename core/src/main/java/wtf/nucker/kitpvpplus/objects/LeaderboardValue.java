package wtf.nucker.kitpvpplus.objects;

import org.bukkit.entity.Player;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 28/07/2021
 */
public class LeaderboardValue {

    private final Player player;
    private final int value;

    public LeaderboardValue(Player player, int value) {
        this.player = player;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Player getPlayer() {
        return player;
    }
}
