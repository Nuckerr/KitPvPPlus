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
public interface KitPvPPlusAPI {

    static KitPvPPlusAPI getInstance() {
        return null;
    }

    public void registerAbility(Ability ability);

    public KitManager getKitManager();

    public LocationsManager getLocationsManager();

    public ConfigManager getConfigManager();
}
