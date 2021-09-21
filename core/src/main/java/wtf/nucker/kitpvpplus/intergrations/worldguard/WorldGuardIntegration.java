package wtf.nucker.kitpvpplus.intergrations.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.dataHandelers.PlayerData;
import wtf.nucker.kitpvpplus.dataHandelers.PlayerState;
import wtf.nucker.kitpvpplus.flags.ArenaFlag;
import wtf.nucker.kitpvpplus.flags.ProtectedFlag;
import wtf.nucker.kitpvpplus.flags.SpawnFlag;

/**
 * @author Nucker
 * @project KitPvPPlus
 * @date 20/09/2021
 */
public class WorldGuardIntegration implements WorldGuardBase {
    private FlagRegistry registry = null;

    public WorldGuardIntegration() {
        if(KitPvPPlus.getInstance().getSubVersion() <= 12) return;
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") == null) return;
        this.registry = WorldGuard.getInstance().getFlagRegistry();
        this.registerEvents();
    }

    public void registerFlags() {
        registry.register(new SpawnFlag());
        registry.register(new ArenaFlag());
        registry.register(new ProtectedFlag());
    }

    @Override
    public void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new Listener() {

            @EventHandler
            public void onMove(final PlayerMoveEvent e) {
                if(!KitPvPPlus.getInstance().getIntegrationsManager().isWorldGuardEnabled()) return;

                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionQuery query = container.createQuery();
                ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getPlayer().getLocation()));
                if(set.getRegions().size() <= 0) return;
                ProtectedRegion region = set.getRegions().toArray(new ProtectedRegion[0])[0];
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

        }, KitPvPPlus.getInstance());
    }
}
