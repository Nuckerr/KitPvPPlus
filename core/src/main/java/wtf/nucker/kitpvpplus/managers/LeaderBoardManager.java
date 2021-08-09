package wtf.nucker.kitpvpplus.managers;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.objects.Leaderboard;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 28/07/2021
 */
public class LeaderBoardManager {

    private final Leaderboard deathsLeaderboard;

    public LeaderBoardManager() {
        this.deathsLeaderboard = new Leaderboard("deaths", "&e&lDeaths") {
            @Override
            public int getValue(OfflinePlayer player) {
                return KitPvPPlus.getInstance().getDataManager().getPlayerData(player.getPlayer()).getDeaths();
            }
        };


        Bukkit.getScheduler().runTaskTimerAsynchronously(KitPvPPlus.getInstance(), task -> {
           this.getDeathsLeaderboard().sort();
        }, 0L, 120L);
    }


    public Leaderboard getDeathsLeaderboard() {
        return this.deathsLeaderboard;
    }
}
