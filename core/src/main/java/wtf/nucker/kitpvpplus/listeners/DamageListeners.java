package wtf.nucker.kitpvpplus.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.dataHandelers.PlayerState;
import wtf.nucker.kitpvpplus.managers.Locations;
import wtf.nucker.kitpvpplus.utils.Language;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class DamageListeners implements Listener {

    @EventHandler
    public void onDeath(final PlayerDeathEvent e) {
        KitPvPPlus instance = KitPvPPlus.getInstance();

        instance.getDataManager().getPlayerData(e.getEntity()).incrementDeaths();
        instance.getDataManager().getPlayerData(e.getEntity()).updateExp(instance.getConfig().getInt("exp.deaths"));
        instance.getDataManager().getPlayerData(e.getEntity()).resetKillStreak();

        if (e.getEntity().getKiller() == null) return;
        if(KitPvPPlus.getInstance().getConfig().contains("kill-commands")) {
            KitPvPPlus.getInstance().getConfig().getStringList("kill-commands").forEach(cmd -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd
                    .replace("%player%", e.getEntity().getName()).replace("%killer%", e.getEntity().getKiller().getName())));
        }
        instance.getDataManager().getPlayerData(e.getEntity().getKiller()).incrementKills();
        instance.getDataManager().getPlayerData(e.getEntity().getKiller()).incrementKillStreak();
        instance.getDataManager().getPlayerData(e.getEntity().getKiller()).updateExp(instance.getConfig().getInt("exp.kills"));
        e.getEntity().getKiller().sendMessage(Language.EXP_MESSAGE.get(e.getEntity().getKiller()).replace("%amount%", String.valueOf(instance.getConfig().getDouble("exp.kills"))));

        e.getEntity().sendMessage(Language.DEATH_MESSAGE.get(e.getEntity()).replace("%killer%", e.getEntity().getKiller().getName()));
        e.getEntity().getKiller().sendMessage(Language.KILLED_MESSAGE.get(e.getEntity().getKiller()).replace("%victim%", e.getEntity().getName()));

        e.getEntity().playSound(e.getEntity().getLocation(), Sound.valueOf(KitPvPPlus.getInstance().getConfig().getString("death-sound")), 1f, 1f);

        Bukkit.getServer().broadcastMessage(Language.DEATH_BROADCAST.get().replace("%victim%", e.getEntity().getName()).replace("%killer%", e.getEntity().getKiller().getName()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(final PlayerRespawnEvent e) {
        e.setRespawnLocation(Locations.SPAWN.get());
        e.getPlayer().teleport(Locations.SPAWN.get());
        KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer()).setState(PlayerState.SPAWN);
    }
}
