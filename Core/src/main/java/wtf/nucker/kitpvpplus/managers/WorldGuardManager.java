package wtf.nucker.kitpvpplus.managers;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.Bukkit;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.flags.ArenaFlag;
import wtf.nucker.kitpvpplus.flags.ProtectedFlag;
import wtf.nucker.kitpvpplus.flags.SpawnFlag;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 15/07/2021
 */
public class WorldGuardManager {

    private FlagRegistry registry = null;

    public WorldGuardManager() {
        if(KitPvPPlus.getInstance().getSubVersion() <= 12) return;
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") == null) return;
        this.registry = WorldGuard.getInstance().getFlagRegistry();

    }

    public void registerFlags() {
        registry.register(new SpawnFlag());
        registry.register(new ArenaFlag());
        registry.register(new ProtectedFlag());
    }
}
