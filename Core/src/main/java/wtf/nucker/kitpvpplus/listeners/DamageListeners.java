package wtf.nucker.kitpvpplus.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.managers.Locations;
import wtf.nucker.kitpvpplus.player.PlayerData;
import wtf.nucker.kitpvpplus.player.PlayerState;
import wtf.nucker.kitpvpplus.utils.Language;
import wtf.nucker.kitpvpplus.utils.Logger;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class DamageListeners implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        KitPvPPlus instance = KitPvPPlus.getInstance();

        instance.getDataManager().getPlayerData(e.getEntity()).incrementDeaths();

        if (e.getEntity().getKiller() == null) return;
        instance.getDataManager().getPlayerData(e.getEntity().getKiller()).incrementKills();
        instance.getDataManager().getPlayerData(e.getEntity().getKiller()).updateExp(instance.getConfig().getInt("exp.kills"));
        e.getEntity().getKiller().sendMessage(Language.EXP_MESSAGE.get(e.getEntity().getKiller()).replace("%amount%", String.valueOf(instance.getConfig().getDouble("exp.kills"))));
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(Locations.SPAWN.get());
        KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer()).setState(PlayerState.SPAWN);
    }
}
