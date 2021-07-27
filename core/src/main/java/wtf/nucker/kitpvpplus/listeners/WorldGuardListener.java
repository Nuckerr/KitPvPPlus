package wtf.nucker.kitpvpplus.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.player.PlayerData;
import wtf.nucker.kitpvpplus.player.PlayerState;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 18/07/2021
 */
public class WorldGuardListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(!KitPvPPlus.getInstance().isWGEnabled()) return;

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getPlayer().getLocation()));
        if(set.getRegions().size() <= 0) return;
        ProtectedRegion region = set.getRegions().toArray(new ProtectedRegion[set.getRegions().size()])[0];
            region.getFlags().forEach((flag, o) -> {
                PlayerData data = KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer());
                switch (flag.getName()) {
                    case "kpvp-spawn":
                        data.setState(PlayerState.SPAWN);
                        break;
                    case "kpvp-arena":
                        data.setState(PlayerState.ARENA);
                    case "kpvp-protected":
                        data.setState(PlayerState.PROTECTED);
                }
        });
    }
}
