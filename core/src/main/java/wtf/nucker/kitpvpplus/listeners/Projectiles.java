package wtf.nucker.kitpvpplus.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import wtf.nucker.kitpvpplus.KitPvPPlus;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class Projectiles implements Listener {

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent e) {
        if (e.getAmount() != KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer()).getExp()) {
            KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer()).updateExpBar();
        }
    }

    @EventHandler
    public void onLevelChange(PlayerLevelChangeEvent e) {
        if (e.getNewLevel() != KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer()).getLevel()) {
            KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer()).updateExpBar();
        }
    }

}
