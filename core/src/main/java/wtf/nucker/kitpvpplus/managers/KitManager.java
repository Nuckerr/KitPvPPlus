package wtf.nucker.kitpvpplus.managers;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import wtf.nucker.kitpvpplus.exceptions.KitAlreadyExistException;
import wtf.nucker.kitpvpplus.exceptions.KitNotExistException;
import wtf.nucker.kitpvpplus.objects.Kit;
import wtf.nucker.kitpvpplus.utils.ClockUtils;
import wtf.nucker.kitpvpplus.utils.Config;
import wtf.nucker.kitpvpplus.utils.ItemUtils;
import wtf.nucker.kitpvpplus.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class KitManager {

    private static Config configInstance;
    private static YamlConfiguration config;
    private final HashMap<String, ClockUtils.CountingRunnable> runnables;

    private static ArrayList<Kit> kitCache;

    public KitManager() {
        this.runnables = new HashMap<>();
        if(kitCache == null) {
            kitCache = new ArrayList<>();

            for (String key : KitManager.getConfig().getConfigurationSection("").getKeys(false)) {
                kitCache.add(this.getKit(key));
            }
        }
    }

    public static void setup() {
        configInstance = new Config("kits.yml");
        config = configInstance.getConfig();
    }

    public Kit createKit(String id) {
        String nameId = id;
        id = id.toLowerCase();
        if (KitManager.getConfig().contains(id)) throw new KitAlreadyExistException("This kit already exists");
        KitManager.getConfig().set(id + ".displayname", "&b" + nameId);
        KitManager.getConfig().set(id+".id", nameId);
        KitManager.getConfigInstance().save();
        ItemStack icon = ItemUtils.buildItem(ChatColor.AQUA + id, XMaterial.PAPER.parseMaterial(), 1, "Edit to set the lore");
        Kit kit = this.getKit(id);
        kit.setIcon(icon.getType());
        kit.setLore(icon.getItemMeta().getLore());
        kit.setInventory(Bukkit.createInventory(null, 36));
        kit.setPermission("");
        kit.setPrice(1);
        kit.setCooldown(0);

        kitCache.add(kit);
        return kit;
    }

    public void deleteKit(String id) {
        kitCache.remove(this.getKit(id));
        id = id.toLowerCase();
        if (!KitManager.getConfig().contains(id)) throw new KitNotExistException("The kit does not exist");
        KitManager.getConfig().set(id, null);
        KitManager.getConfigInstance().save();
    }

    public Kit getKit(String id) {
        String configId = id.toLowerCase();
        if (!KitManager.getConfig().contains(configId)) {
            throw new KitNotExistException("Could not find kit");
        }

        ConfigurationSection section = KitManager.getConfig().getConfigurationSection(configId);
        Logger.debug(configId);
        return new Kit() {
            @Override
            public String getId() {
                return section.getString("id");
            }

            @Override
            public String getDisplayname() {
                return section.getString("displayname");
            }

            @Override
            public Material getIcon() {
                return Material.valueOf(section.getString("icon"));
            }

            @Override
            public List<String> getLore() {
                return section.getStringList("lore");
            }

            @Override
            public int getPrice() {
                return section.getInt("price");
            }

            @Override
            public String getPermission() {
                return section.getString("permission");
            }

            @Override
            public String setDisplayname(String newName) {
                section.set("displayname", newName);
                KitManager.getConfigInstance().save();
                return this.getDisplayname();
            }

            @Override
            public Material setIcon(Material newMat) {
                section.set("icon", newMat.name());
                KitManager.getConfigInstance().save();
                return this.getIcon();
            }

            @Override
            public List<String> addToLore(String item) {
                List<String> list = this.getLore();
                list.add(item);
                this.setLore(list);
                KitManager.getConfigInstance().save();
                return this.getLore();
            }

            @Override
            public List<String> removeFromLore(String item) {
                List<String> list = this.getLore();
                list.remove(item);
                this.setLore(list);
                KitManager.getConfigInstance().save();
                return this.getLore();
            }

            @Override
            public List<String> setLore(List<String> newLore) {
                section.set("lore", newLore);
                KitManager.getConfigInstance().save();
                return this.getLore();
            }

            @Override
            public int setPrice(int newPrice) {
                section.set("price", newPrice);
                configInstance.save();
                return this.getPrice();
            }

            @Override
            public String setPermission(String newPerm) {
                section.set("permission", newPerm);
                configInstance.save();
                return this.getPermission();
            }

            @Override
            public boolean isFree() {
                return this.getPrice() <= 0;
            }

            @Override
            public int getCooldown() {
                return section.getInt("cooldown");
            }

            @Override
            public int setCooldown(int cooldown) {
                section.set("cooldown", cooldown);
                KitManager.getConfigInstance().save();
                return this.getCooldown();
            }

            @Override
            public ClockUtils.CountingRunnable getCooldownRunnable() {
                return runnables.get(this.getId());
            }

            @Override
            public ClockUtils.CountingRunnable setCooldownRunnable(ClockUtils.CountingRunnable newRunnable) {
                runnables.remove(this.getId());
                runnables.put(this.getId(), newRunnable);
                return runnables.get(this.getId());
            }

            @Override
            public void setInventory(Inventory inv) {
                section.set("inventory", null);
                for (int i = 0; i < inv.getContents().length; i++) {
                    ItemUtils.serialize(KitManager.getConfig(), configId + ".inventory." + i, inv.getItem(i));
                }
                KitManager.getConfigInstance().save();
            }

            @Override
            public void fillInventory(Player player) {
                player.getInventory().clear();
                Inventory inv = player.getInventory();
                for (String key : section.getConfigurationSection("inventory").getKeys(false)) {
                    int index = Integer.parseInt(key);
                    inv.setItem(index, ItemUtils.serialize(KitManager.getConfig(), configId + ".inventory." + key));
                }
            }


            @Override
            public boolean equals(Object obj) {
                if(obj instanceof Kit) {
                    Kit kit = (Kit) obj;
                    return kit.getId().equals(this.getId());
                }

                return false;
            }
        };
    }

    public List<Kit> getKits() {
        return kitCache;
    }

    public static YamlConfiguration getConfig() {
        return config;
    }

    public static Config getConfigInstance() {
        return configInstance;
    }
}
