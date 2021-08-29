package wtf.nucker.kitpvpplus.api.managers;

import org.bukkit.Location;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 21/07/2021
 */
public interface LocationsManager {

    /**
     * @return The bukkit location of spawn
     */
    Location getSpawn();

    /**
     * @return The bukkit location of the arena
     */
    Location getArena();
}
