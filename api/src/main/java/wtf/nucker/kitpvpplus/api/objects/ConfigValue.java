package wtf.nucker.kitpvpplus.api.objects;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

/**
 * @author Nucker
 * Used for getting config values as X object
 */
public class ConfigValue {

    private final FileConfiguration config;
    private final String path;

    /**
     * Used for initiating instance of ConfigValueClass
     * @param config The file config that the value is in
     * @param path the path to the value
     */
    public ConfigValue(FileConfiguration config, String path) {
        this.config = config;
        this.path = path;
    }

    /**
     * @return the value as an object
     */
    public Object getAsObject() {
        return config.get(path);
    }

    /**
     * @return the value as an string
     */
    public String getAsString() {
        return config.getString(path);
    }

    /**
     * @return the value as an integer
     */
    public int getAsInt() {
        return config.getInt(path);
    }

    /**
     * @return the value as a boolean
     */
    public boolean getAsBool() {
        return config.getBoolean(path);
    }

    /**
     * @return the value as a string list
     */
    public List<String> getAsStringList() {
        return config.getStringList(path);
    }

    /**
     * @return the value as a double
     */
    public double getAsDouble() {
        return config.getDouble(path);
    }

    /**
     * @param def the class def you want to be returned
     * @return the value as specified object
     */
    public Object getAs(Class def) {
        return config.get(path, def);
    }
}
