package wtf.nucker.kitpvpplus.api;

import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.api.exceptions.InitException;
import wtf.nucker.kitpvpplus.api.objects.ConfigValue;
import wtf.nucker.kitpvpplus.managers.KitManager;

/**
 * @author Nucker
 * Used for getting information from the config files
 */
public class Configs {

    private final KitPvPPlus instance;

    protected Configs(KitPvPPlus instance) {
        this.instance = instance;
    }

    /**
     * @deprecated <b>DO NOT USE THIS</b>
     * @see KitPvPPlusAPI#getConfigs() KitPvPPlus#getConfigs() to get `Configs` class instance
     */
    public Configs() {
        throw new InitException("You cannot initiate this class. Use KitPvPPlusAPI#getConfigs()");
    }

    /**
     * Used for getting raw values from KitPvPPlus config.yml file
     * @param path the path of the value you want to access
     * @return the value in `ConfigValue` class
     * @see ConfigValue ConfigValue class for
     */
    public ConfigValue getFromConfig(String path) {
        return new ConfigValue(instance.getConfig(), path);
    }

    /**
     * Used for getting raw values from KitPvPPlus messages.yml file
     * @param path the path of the value you want to access
     * @return the value in `ConfigValue` class
     * @see ConfigValue ConfigValue class for
     */
    public ConfigValue getMessage(String path) {
        return new ConfigValue(instance.getMessages(), path);
    }

    /**
     * Used for getting raw values from KitPvPPlus data.yml file
     * @param path the path of the value you want to access
     * @return the value in `ConfigValue` class
     * @see ConfigValue ConfigValue class for
     */
    public ConfigValue getDataRaw(String path) {
        return new ConfigValue(instance.getDataManager().getDataYaml(), path);
    }

    /**
     * Used for getting raw values from KitPvPPlus signs.yml file
     * @param path the path of the value you want to access
     * @return the value in `ConfigValue` class
     * @see ConfigValue ConfigValue class for
     */
    public ConfigValue getSignDataRaw(String path) {
        return new ConfigValue(instance.getSignManager().getConfig(), path);
    }

    /**
     * Used for getting raw values from KitPvPPlus kits.yml file
     * @param path the path of the value you want to access
     * @return the value in `ConfigValue` class
     * @see ConfigValue ConfigValue class for
     */
    public ConfigValue getKitDataRaw(String path) {
        return new ConfigValue(KitManager.getConfig(), path);
    }
}
