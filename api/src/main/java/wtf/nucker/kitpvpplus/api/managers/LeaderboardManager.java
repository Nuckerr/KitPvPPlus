package wtf.nucker.kitpvpplus.api.managers;

import wtf.nucker.kitpvpplus.api.objects.Leaderboard;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 28/08/2021
 */
public interface LeaderboardManager {

    Leaderboard getDeathsLeaderboard();
    Leaderboard getBalLeaderboard();
    Leaderboard getExpLeaderboard();
    Leaderboard getKdrLeaderboard();
    Leaderboard getKillsLeaderboard();
    Leaderboard getKillStreakLeaderboard();
    Leaderboard getLevelLeaderboard();

}
