package wtf.nucker.kitpvpplus.objects;

import org.bukkit.OfflinePlayer;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.dataHandlers.PlayerData;

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
        List<PlayerData> data = KitPvPPlus.getInstance().getDataManager().getAllPlayerData();

        for (PlayerData datum : data) {
            this.addValue(datum.getPlayer());
        }

        /*
        KitPvPPlus.getInstance().getDataManager().getAllPlayerData().forEach(data -> {
            this.addValue(data.getPlayer());
        });
         */
    }

    public void addValue(OfflinePlayer player) {
        this.leaderboard.add(new LeaderboardValue(player, this.getValue(player)));
    }

    private void updateValues() {
        for(int i = 0; i<leaderboard.size(); i++){
            LeaderboardValue value = leaderboard.get(i);
            leaderboard.remove(i);
            leaderboard.add(new LeaderboardValue(value.getPlayer(), getValue(value.getPlayer())));
        }
    }

    public void sort() {
        this.updateValues();
        this.leaderboard.sort(Comparator.comparingDouble(LeaderboardValue::getValue));
    }

    public List<LeaderboardValue> getTop(int amount) {
        List<LeaderboardValue> res = new ArrayList<>();

        this.sort();
        for (int i = 0; i < amount; i++) {
            if((this.leaderboard.size() - 1) < i) continue;
            res.add(this.leaderboard.get(i));
        }

        return res;
    }

    public int getPlace(OfflinePlayer player) {
        for (int i = 0; i < this.leaderboard.size(); i++) {
            if(this.leaderboard.get(i).equals(new LeaderboardValue(player, 0 /* Value dosent matter */))) return i + 1;
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

    public abstract double getValue(OfflinePlayer player);

}
