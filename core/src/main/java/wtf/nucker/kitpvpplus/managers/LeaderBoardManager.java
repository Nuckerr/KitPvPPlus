package wtf.nucker.kitpvpplus.managers;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.objects.Leaderboard;
import wtf.nucker.kitpvpplus.utils.ClockUtils;
import wtf.nucker.kitpvpplus.utils.Language;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 28/07/2021
 */
public class LeaderBoardManager {

    private Leaderboard deathsLeaderboard;
    private Leaderboard killsLeaderboard;
    private Leaderboard expLeaderboard;
    private Leaderboard balLeaderboard;
    private Leaderboard ksLeaderboard;
    private Leaderboard kdrLeaderboard;
    private Leaderboard levelLeaderboard;

    public LeaderBoardManager() {
        this.registerLeaderboards();
        ClockUtils.runIntervalAsync(120, runnable -> {
            this.getDeathsLeaderboard().sort();
            this.getKdrLeaderboard().sort();
            this.getKillsLeaderboard().sort();
            this.getExpLeaderboard().sort();
            this.getBalLeaderboard().sort();
            this.getKsLeaderboard().sort();
            this.getLevelLeaderboard().sort();
        });
    }

    private void registerLeaderboards() {
        DataManager manager = KitPvPPlus.getInstance().getDataManager();
        this.deathsLeaderboard = new Leaderboard("deaths", Language.DEATH_LEADERBOARD.get()) {
            @Override
            public double getValue(OfflinePlayer player) {
                return manager.getPlayerData(player).getDeaths();
            }
        };
        this.killsLeaderboard = new Leaderboard("kills", Language.KILLS_LEADERBOARD.get()) {
            @Override
            public double getValue(OfflinePlayer player) {
                return manager.getPlayerData(player).getKills();
            }
        };
        this.balLeaderboard = new Leaderboard("bal", Language.BAL_LEADERBOARD.get()) {
            @Override
            public double getValue(OfflinePlayer player) {
                return new PlayerBank(player).getBal();
            }
        };
        this.expLeaderboard = new Leaderboard("exp", Language.EXP_LEADERBOARD.get()) {
            @Override
            public double getValue(OfflinePlayer player) {
                return manager.getPlayerData(player).getExp();
            }
        };
        this.ksLeaderboard = new Leaderboard("killstreak", Language.KILLSTREAK_LEADERBOARD.get()) {
            @Override
            public double getValue(OfflinePlayer player) {
                return manager.getPlayerData(player).getKillStreak();
            }
        };
        this.kdrLeaderboard = new Leaderboard("kdr", Language.KDR_LEADERBOARD.get()) {
            @Override
            public double getValue(OfflinePlayer player) {
                return manager.getPlayerData(player).getKDR();
            }
        };
        this.levelLeaderboard = new Leaderboard("level", Language.LEVEL_LEADERBOARD.get()) {
            @Override
            public double getValue(OfflinePlayer player) {
                return manager.getPlayerData(player).getLevel();
            }
        };
    }


    public Leaderboard getDeathsLeaderboard() {
        return this.deathsLeaderboard;
    }
    public Leaderboard getBalLeaderboard() {
        return balLeaderboard;
    }
    public Leaderboard getExpLeaderboard() {
        return expLeaderboard;
    }
    public Leaderboard getKdrLeaderboard() {
        return kdrLeaderboard;
    }
    public Leaderboard getKillsLeaderboard() {
        return killsLeaderboard;
    }
    public Leaderboard getKsLeaderboard() {
        return ksLeaderboard;
    }
    public Leaderboard getLevelLeaderboard() {
        return levelLeaderboard;
    }
}
