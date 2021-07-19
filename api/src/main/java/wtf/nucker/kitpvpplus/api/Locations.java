package wtf.nucker.kitpvpplus.api;

import org.bukkit.Location;
import wtf.nucker.kitpvpplus.api.exceptions.InitException;

/**
 * @author Nucker
 * Class used to get locations such as spawn and arena
 */
public class Locations {

    protected Locations(KitPvPPlusAPI api) {} // Used to make sure only KitPvPPlusAPI class can init instance

    /**
     * @deprecated <b>DO NOT USE THIS</b>
     * @see KitPvPPlusAPI#getLocations() KitPvPPlus#getLocations() to get `Locations` class instance
     */
    public Locations() {
        throw new InitException("You cannot initiate this class. Use KitPvPPlusAPI#getLocations()");
    }

    /**
     * @return the spawn location
     */
    public Location getSpawn() {
        return wtf.nucker.kitpvpplus.managers.Locations.SPAWN.get();
    }

    /**
     * @return the arena location
     */
    public Location getArena() {
        return wtf.nucker.kitpvpplus.managers.Locations.ARENA.get();
    }
}
