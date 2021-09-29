package wtf.nucker.kitpvpplus.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.metadata.FixedMetadataValue;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.utils.Config;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class SignManager {

    private final Config configInstance;
    private final YamlConfiguration config;

    public SignManager() {
        this.configInstance = new Config( "signs.yml");
        this.config = this.configInstance.getConfig();
        if(config.getConfigurationSection("signs") == null) return;
        for (String key : config.getConfigurationSection("signs").getKeys(false)) {
            Block block = this.deserialize("signs."+key+".block").getBlock();
            if(block.getType().name().contains("SIGN")) {
                block.setMetadata("kpvp", new FixedMetadataValue(KitPvPPlus.getInstance(), config.getString("signs."+key+".type")));
            }else {
                config.set("signs."+key, null);
                configInstance.save();
            }
        }
    }

    public void addSign(Block block, String type) {
        int i;
        if(config.getConfigurationSection("signs") == null) i = 0; else i = config.getConfigurationSection("signs").getKeys(false).size() +1;
        config.set("signs."+i+".type", type);
        this.serialize(block.getLocation(), "signs."+i+".block");

        configInstance.save();
    }

    public void removeSign(Block block) {
        if(config.getConfigurationSection("signs") == null) return;
        for (String key : config.getConfigurationSection("signs").getKeys(false)) {
            if(this.deserialize("signs." + key + ".block").getBlock().equals(block)) {
                config.set("signs."+key, null);
            }
        }
        block.removeMetadata("kpvp", KitPvPPlus.getInstance());

        configInstance.save();
    }

    public Config getConfigInstance() {
        return configInstance;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    private Location deserialize(String path) {
        if(this.config == null) return new Location(Bukkit.getWorld("world"), 0, 65, 0);
        if(Locations.getConfig().getConfig().getConfigurationSection(path) == null) Locations.getConfig().getConfig().set(path, "");
        ConfigurationSection section = this.config.contains(path) ? config.getConfigurationSection(path) : config.createSection(path);
        return new Location(
                Bukkit.getWorld(section.getString("world")),
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"),
                Float.parseFloat(section.getString("yaw")),
                Float.parseFloat(section.getString("pitch"))
        );
    }

    private void serialize(Location location, String path) {
        if(this.config == null) return;
        ConfigurationSection section = this.config.contains(path) ? config.getConfigurationSection(path) : config.createSection(path);
        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", location.getYaw());
        section.set("pitch", location.getPitch());
        this.configInstance.save();
    }
}
