package wtf.nucker.kitpvpplus.api.managers;

import wtf.nucker.kitpvpplus.api.objects.ConfigValue;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 21/07/2021
 */
public interface ConfigManager {

    /**
     * Used for getting raw values from KitPvPPlus messages.yml file
     * @param path the path of the value you want to access
     * @return the value in `ConfigValue` class
     * @see ConfigValue ConfigValue class for
     */
    public ConfigValue getMessage(String path);

    /**
     * Used for getting raw values from KitPvPPlus config.yml file
     * @param path the path of the value you want to access
     * @return the value in `ConfigValue` class
     * @see ConfigValue ConfigValue class for
     */
    public ConfigValue getFromConfig(String path);

    /**
     * Used for getting raw values from KitPvPPlus data.yml file
     * @param path the path of the value you want to access
     * @return the value in `ConfigValue` class
     * @see ConfigValue ConfigValue class for
     */
    public ConfigValue getDataRaw(String path);

    /**
     * Used for getting raw values from KitPvPPlus signs.yml file
     * @param path the path of the value you want to access
     * @return the value in `ConfigValue` class
     * @see ConfigValue ConfigValue class for
     */
    public ConfigValue getSignDataRaw(String path);

    /**
     * Used for getting raw values from KitPvPPlus kits.yml file
     * @param path the path of the value you want to access
     * @return the value in `ConfigValue` class
     * @see ConfigValue ConfigValue class for
     */
    public ConfigValue getKitDataRaw(String path);
}
