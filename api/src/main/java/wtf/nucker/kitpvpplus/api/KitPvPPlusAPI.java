package wtf.nucker.kitpvpplus.api;

import wtf.nucker.kitpvpplus.api.managers.ConfigManager;
import wtf.nucker.kitpvpplus.api.managers.KitManager;
import wtf.nucker.kitpvpplus.api.managers.LocationsManager;
import wtf.nucker.kitpvpplus.api.objects.Ability;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 21/07/2021
 */
public abstract class KitPvPPlusAPI {

    private static KitPvPPlusAPI instance;

    private final KitManager kitManager;
    private final LocationsManager locationsManager;
    private final ConfigManager configManager;

    public KitPvPPlusAPI(KitManager kitManager, LocationsManager locationsManager, ConfigManager configManager) {
        instance = this;

        this.kitManager = kitManager;
        this.locationsManager = locationsManager;
        this.configManager = configManager;
    }

    public abstract void registerAbility(Ability ability);

    public KitManager getKitManager() {
        return this.kitManager;
    }

    public LocationsManager getLocationsManager() {
        return this.locationsManager;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public static KitPvPPlusAPI getInstance() {
        return instance;
    }
}
