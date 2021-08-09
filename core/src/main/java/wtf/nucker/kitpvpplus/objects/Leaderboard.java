package wtf.nucker.kitpvpplus.objects;

import org.bukkit.OfflinePlayer;
import wtf.nucker.kitpvpplus.KitPvPPlus;

import java.util.ArrayList;
import java.util.Comparator;
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

        this.addValues();
        this.sort();
    }

    private void addValues() {
        KitPvPPlus.getInstance().getDataManager().getAllPlayerData().forEach(data -> {
            this.addValue(data.getPlayer());
        });
    }

    private void addValue(OfflinePlayer player) {
        this.leaderboard.add(new LeaderboardValue(player.getPlayer(), this.getValue(player)));
    }

    private void updateValues() {
        this.leaderboard.forEach(sub -> {
            this.leaderboard.remove(sub);
            this.leaderboard.add(new LeaderboardValue(sub.getPlayer(), this.getValue(sub.getPlayer())));
        });
    }

    public void sort() {
        this.updateValues();
        this.leaderboard.sort(Comparator.comparingInt(LeaderboardValue::getValue));
    }

    public List<LeaderboardValue> getTop(int amount) {
        List<LeaderboardValue> res = new ArrayList<>();

        this.sort();
        for (int i = 0; i < amount; i++) {
            if(this.leaderboard.get(i) == null) continue;
            res.add(this.leaderboard.get(i));
        }

        return res;
    }

    public int getPlace(OfflinePlayer player) {
        for (int i = 0; i < this.leaderboard.size(); i++) {
            if(this.leaderboard.get(i).getPlayer().equals(player.getPlayer())) return i;
        }

        return -1;
    }


    public List<LeaderboardValue> getList() {
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
