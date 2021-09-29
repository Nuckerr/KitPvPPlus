package wtf.nucker.kitpvpplus.integrations.worldguard;

import org.bukkit.Bukkit;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.utils.Logger;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Nucker
 * @project KitPvPPlus
 * @date 29/09/2021
 */
public class WorldGuardManager {

    private final KitPvPPlus instance;
    private final BaseWorldGuard worldGuard;

    public WorldGuardManager(KitPvPPlus instance) {
        this.instance = instance;

        if(Bukkit.getPluginManager().getPlugin("WorldGuard") == null) {
            Logger.error("WorldGuard is being initialized when there is no WorldGuard plugin installed.");
            this.worldGuard = null;
            return;
        }

        if(instance.getSubVersion() >= 12) {
            this.worldGuard = new WorldGuard_v7_0();
        }else {
            // Load legacy world guard
            this.worldGuard = new WorldGuardLegacy();
        }
    }

    public BaseWorldGuard getWorldGuard() {
        return worldGuard;
    }
}
