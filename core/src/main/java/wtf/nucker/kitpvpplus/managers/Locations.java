package wtf.nucker.kitpvpplus.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import wtf.nucker.kitpvpplus.utils.Config;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public enum Locations {

    SPAWN("spawn", new Location(Bukkit.getWorld("world"), 0, 0, 0)),
    ARENA("arena", new Location(Bukkit.getWorld("world"), 0, 0, 0));

    private Location location;
    private final String path;
    private final Location def;

    private static Config configInstance;
    private static YamlConfiguration config;

    Locations(String path, Location def) {
        this.path = path;
        this.def = def;
        if(Locations.getConfig() == null || Locations.getConfig().get(path) == null) {
            Locations.serialize(def, path);
        }
        this.location = Locations.deserialize(path);
    }

    public Location get() {
        return this.location;
    }

    public String getPath() {
        return path;
    }

    public Location getDef() {
        return def;
    }

    public void set(Location newLocation) {
        this.location = newLocation;
        Locations.serialize(newLocation, this.path);
        Locations.getConfigInstance().save();
    }

    public static Location deserialize(String path) {
        if(Locations.getConfig() == null) return new Location(Bukkit.getWorld("world"), 0, 65, 0);
        if(Locations.getConfig().getConfigurationSection(path) == null) Locations.getConfig().set(path, "");
        Locations.getConfigInstance().save();
        ConfigurationSection section = Locations.getConfig().getConfigurationSection(path);
        return new Location(
                Bukkit.getWorld(section.getString("world")),
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"),
                Float.parseFloat(section.getString("yaw")),
                Float.parseFloat(section.getString("pitch"))
        );
    }

    public static void serialize(Location location, String path) {
        if(Locations.getConfig() == null) return;
        ConfigurationSection section = Locations.getConfig().getConfigurationSection(path);
        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", location.getYaw());
        section.set("pitch", location.getPitch());
        Locations.getConfigInstance().save();
    }

    public static void setup() {
        Locations.configInstance = new Config("locations.yml");
        Locations.config = Locations.configInstance.getConfig();
        for (Locations location : Locations.values()) {
            location.location = Locations.deserialize(location.getPath());
        }
    }

    public static YamlConfiguration getConfig() {
        return config;
    }

    public static Config getConfigInstance() {
        return configInstance;
    }
}
