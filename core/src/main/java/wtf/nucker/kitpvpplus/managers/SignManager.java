package wtf.nucker.kitpvpplus.managers;

import org.bukkit.Location;
import org.bukkit.block.Block;
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
            Block block = config.getLocation("signs."+key+".block").getBlock();
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
        config.set("signs."+i+".block", block.getLocation());

        configInstance.save();
    }

    public void removeSign(Block block) {
        if(config.getConfigurationSection("signs") == null) return;
        for (String key : config.getConfigurationSection("signs").getKeys(false)) {
            if(config.getLocation("signs."+key+".block").getBlock().equals(block)) {
                config.set("signs."+key, null);
            }
        }
        block.setMetadata("kpvp", null);

        configInstance.save();
    }

    public Config getConfigInstance() {
        return configInstance;
    }

    public YamlConfiguration getConfig() {
        return config;
    }
}
