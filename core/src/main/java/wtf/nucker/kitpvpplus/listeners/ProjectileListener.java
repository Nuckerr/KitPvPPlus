package wtf.nucker.kitpvpplus.listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.utils.Language;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class ProjectileListener implements Listener {

    @EventHandler
    public void onExpChange(final PlayerExpChangeEvent e) {
        if (e.getAmount() != KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer()).getExp()) {
            KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer()).updateExpBar();
        }
    }

    @EventHandler
    public void onLevelChange(final PlayerLevelChangeEvent e) {
        if (e.getNewLevel() != KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer()).getLevel()) {
            KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer()).updateExpBar();
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent e) {
        if(e.getEntity().getType().name().contains("ARROW")) {
            if(e.getEntity().getShooter() instanceof Player) {
                Player player = (Player) e.getEntity().getShooter();
                player.playSound(player.getLocation(), Sound.valueOf(KitPvPPlus.getInstance().getConfig().getString("arrow-hit-sound")), 1f, 1f);
                player.sendMessage(Language.ARROW_HIT.get(player).replace("%victim%", e.getHitEntity().getName()));
            }
        }
    }

}
