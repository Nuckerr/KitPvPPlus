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

    SPAWN("spawn", Bukkit.getWorld("world").getSpawnLocation()),
    ARENA("arena", Bukkit.getWorld("world").getSpawnLocation().add(0, 5, 0));

    private Location location;
    private final String path;
    private final Location def;

    private static Config config;

    Locations(String path, Location def) {
        this.path = path;
        this.def = def;
        if(Locations.getConfig() == null || Locations.getConfig().getConfig().get(path) == null) {
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
    }

    public static Location deserialize(String path) {
        if(Locations.getConfig() == null) return new Location(Bukkit.getWorld("world"), 0, 65, 0);
        if(Locations.getConfig().getConfig().getConfigurationSection(path) == null) Locations.getConfig().getConfig().set(path, "");
        ConfigurationSection section = Locations.getConfig().getConfig().getConfigurationSection(path);
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
        ConfigurationSection section = Locations.getConfig().getConfig().getConfigurationSection(path);
        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", location.getYaw());
        section.set("pitch", location.getPitch());
    }

    public static void setup() {
        Locations.config = new Config("locations.yml");
        for (Locations location : Locations.values()) {
            location.location = Locations.deserialize(location.getPath());
        }
    }

    public static Config getConfig() {
        return config;
    }

    public static YamlConfiguration getConfigInstance() {
        return config.getConfig();
    }
}
