package wtf.nucker.kitpvpplus.player;

import wtf.nucker.kitpvpplus.objects.Kit;

import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public interface PlayerData {

    public void updateExp(int newAmount);

    public void incrementDeaths();

    public void incrementKills();

    public void updateLevel();

    public void resetData();

    public void deleteData();

    public void setState(PlayerState newState);

    public int getExp();

    public int getDeaths();

    public int getKills();

    public int getLevel();

    public PlayerState getState();

    public void updateExpBar();

    public List<Kit> getOwnedKits();

    public boolean ownsKit(Kit kit);

    public List<Kit> purchaseKit(Kit kit);
}
