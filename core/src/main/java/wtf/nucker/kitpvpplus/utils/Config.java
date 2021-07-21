package wtf.nucker.kitpvpplus.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import wtf.nucker.kitpvpplus.KitPvPPlus;

import java.io.File;
import java.io.IOException;

/**
 * A class for easily creating custom config files
 *
 * @author Nucker
 */
public class Config {
    private File file;
    private final YamlConfiguration yaml;
    private final Plugin plugin = KitPvPPlus.getInstance();

    private final String name;

    /**
     * The constructor. Pass your params and the file is created and initiated.
     *
     * @param name The name of the file. EG data.yml
     *
     *             <b>Must include the extension (.yml)</b>
     */
    public Config(String name) {
        if (!name.endsWith(".yml")) name = name + ".yml";
        this.name = name;
        File dir = plugin.getDataFolder();
        if (!dir.exists()) {
            Logger.debug(name + " does not exist. Creating file");
            dir.mkdirs();
        } else Logger.debug(name + " exists. Loading it.");
        file = new File(dir, name);

        if (!file.exists()) {
            plugin.saveResource(name, false);
        }

        yaml = new YamlConfiguration();

        try {
            yaml.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        yaml.options().copyDefaults(true);
    }

    /**
     * Loads the config file again updating the values that you can get in your code
     * to those that have been updated in the file
     */
    public void reload() {
        File dir = plugin.getDataFolder();
        if (!dir.exists()) {
            Logger.debug(this.name + " does not exist. Creating file");
            dir.mkdirs();
        } else Logger.debug(this.name + " exists. Loading it.");
        file = new File(dir, this.name);

        if (!file.exists()) {
            plugin.saveResource(name, false);
        }
        try {
            yaml.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves any changes made by the code to the file
     * EG: You set a value in your config. Call this method to save it into your file
     */
    public void save() {
        try {
            yaml.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Returns the {@link YamlConfiguration}
     */
    public YamlConfiguration getConfig() {
        return yaml;
    }
}
