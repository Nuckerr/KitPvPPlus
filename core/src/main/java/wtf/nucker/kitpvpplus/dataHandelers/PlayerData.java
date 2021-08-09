package wtf.nucker.kitpvpplus.dataHandelers;

import org.bukkit.OfflinePlayer;
import wtf.nucker.kitpvpplus.objects.Kit;

import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public interface PlayerData {

    void updateExp(int newAmount);

    void incrementDeaths();

    void incrementKills();

    void updateLevel();

    void incrementKillStreak();
    void resetKillStreak();

    void resetData();

    void deleteData();

    void setState(PlayerState newState);

    int getExp();

    int getDeaths();

    int getKills();

    int getLevel();

    int getKillStreak();

    int getTopKillStreak();

    double getKDR();

    PlayerState getState();

    void updateExpBar();

    List<Kit> getOwnedKits();

    boolean ownsKit(Kit kit);

    List<Kit> purchaseKit(Kit kit);



    OfflinePlayer getPlayer();
}
