package wtf.nucker.kitpvpplus.objects;

import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 28/07/2021
 */
public abstract class Leaderboard {

    private final String id;
    private final String collumName;

    private final List<LeaderboardValue> leaderboard;

    public Leaderboard(String id, String collumName) {
        this.id = id;
        this.collumName = collumName;

        this.leaderboard = new ArrayList<>();
    }

    public void updateValues() {
        this.leaderboard.forEach(sub -> {
            this.leaderboard.remove(sub);
            this.leaderboard.add(new LeaderboardValue(sub.getPlayer(), this.getValue(sub.getPlayer())));
        });
    }

    public void sort() {
        //TODO: sort the list with highest value at 0 and lowest at array.size()
    }

    public String getTop(int amount) {
        String res = "&e&lLeaderboard - " + this.collumName + "\n";


        return res;
    }


    public List<LeaderboardValue> getMap() {
        return leaderboard;
    }

    public String getCollumName() {
        return collumName;
    }

    public String getId() {
        return id;
    }

    public abstract int getValue(OfflinePlayer player);
}
