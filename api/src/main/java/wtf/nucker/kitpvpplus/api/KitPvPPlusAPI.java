package wtf.nucker.kitpvpplus.api;

import org.bukkit.Bukkit;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.api.events.Listener;
import wtf.nucker.kitpvpplus.api.exceptions.InitException;
import wtf.nucker.kitpvpplus.api.managers.KitManager;
import wtf.nucker.kitpvpplus.api.objects.Ability;
import wtf.nucker.kitpvpplus.api.objects.Kit;

/**
 * @author Nucker
 * The main class for the KitPvPPlus API
 */
public final class KitPvPPlusAPI {

    //TODO: The API should rely on interfaces for better compatibility, which the plugin itself have implemented.

    private static KitPvPPlusAPI instance = null;

    private final Locations locations;
    private final Configs configs;

    public KitPvPPlusAPI() {
        if(instance != null) throw new InitException("An instance of the KitPvP Plus API already exists");
        instance = this;

        Bukkit.getServer().getPluginManager().registerEvents(new Listener(), KitPvPPlus.getInstance());

        this.configs = new Configs(KitPvPPlus.getInstance());
        this.locations = new Locations(this);
    }

    /**
     * Used for registering an ability
     * @param ability The ability you want to register
     *
     */
    public void registerAbility(Ability ability) {
        KitPvPPlus.getInstance().getAbilityManager().registerAbility(Ability.toInstanceAbility(ability));
    }

    /**
     * Used for getting instances of kits
     * @param id the id of the kit you want to get
     * @throws wtf.nucker.kitpvpplus.exceptions.KitNotExistException throws KitNotExistException if kit isnt found
     * @return the kit
     */
    public Kit getKit(String id) {
        return Kit.fromInstanceKit(KitPvPPlus.getInstance().getKitManager().getKit(id));
    }

    /**
     * @return instance of the Configs class
     */
    public Configs getConfigs() {
        return this.configs;
    }

    /**
     * @return instance of the Locations class
     */
    public Locations getLocations() {
        return locations;
    }

    public KitManager getKitManager() {
        return KitManager.getInstance();
    }

    /**
     * Used for getting the instance of the api.
     * @return instance variable.
     */
    public static KitPvPPlusAPI getInstance() {
        if(instance == null) {
            new KitPvPPlusAPI();
        }
        return instance;
    }
}
