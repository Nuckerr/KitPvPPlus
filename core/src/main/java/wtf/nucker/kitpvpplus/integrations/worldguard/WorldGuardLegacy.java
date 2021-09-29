package wtf.nucker.kitpvpplus.integrations.worldguard;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
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

import java.lang.reflect.InvocationTargetException;

/**
 * @author Nucker
 * @project KitPvPPlus
 * @date 29/09/2021
 */
public class WorldGuardLegacy implements BaseWorldGuard {


    @Override
    public void subscribeListeners() {
        Bukkit.getPluginManager().registerEvents(new Listener() {

            @EventHandler
            public void onMove(final PlayerMoveEvent event) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
                /*
                Object container = WorldGuardPlugin.class.getMethod("getRegionContainer").invoke(WorldGuardPlugin.inst());
                Object query = container.getClass().getMethod("createQuery").invoke(container);

                Class<?> debug = Class.forName("com.sk89q.worldguard.bukkit.RegionQuery");
                for (Method method : debug.getMethods()) {
                    System.out.println(method.getName()+"(" + Arrays.toString(method.getParameters()) + ") (Accessible: " + method.isAccessible() + ")");
                }
                ApplicableRegionSet set = (ApplicableRegionSet) Class.forName("com.sk89q.worldguard.bukkit.RegionQuery").getMethod("getApplicableRegions").invoke(query, event.getPlayer().getLocation());
                */
                RegionContainer container = (RegionContainer) WorldGuardPlugin.class.getMethod("getRegionContainer").invoke(WorldGuardPlugin.inst());
                RegionQuery query = container.createQuery();

                ApplicableRegionSet set = query.getApplicableRegions(event.getPlayer().getLocation());
                if(set.getRegions().size() <= 0) return;
                ProtectedRegion region = set.getRegions().toArray(new ProtectedRegion[0])[0];

                region.getFlags().forEach((flag, o) -> {
                    PlayerData data = KitPvPPlus.getInstance().getDataManager().getPlayerData(event.getPlayer());
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

    @Override
    public void registerFlags() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        FlagRegistry registry = (FlagRegistry) WorldGuardPlugin.class.getMethod("getFlagRegistry").invoke(WorldGuardPlugin.inst());
        registry.register(new SpawnFlag());
        registry.register(new ArenaFlag());
        registry.register(new ProtectedFlag());
    }
}
