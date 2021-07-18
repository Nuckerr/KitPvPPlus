package wtf.nucker.kitpvpplus.utils;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class ItemUtils {

    public static ItemStack buildItem(String name, Material material, int amount, String... lore) {
        ItemStack res = new ItemStack(material, amount);
        ItemMeta meta = res.getItemMeta();
        meta.setDisplayName(ChatUtils.translate(name));
        meta.setLore(Arrays.asList(ChatUtils.translate(lore)));
        res.setItemMeta(meta);

        return res;
    }

    public static void serialize(YamlConfiguration config, String path, ItemStack item) {
        if (item == null || item.getType().equals(XMaterial.AIR.parseMaterial())) return;
        config.set(path + ".type", item.getType().name());
        config.set(path + ".amount", item.getAmount());
        config.set(path + ".durability", item.getDurability());
        config.set(path + ".max", item.getMaxStackSize());

        if (!item.getEnchantments().isEmpty()) {
            item.getEnchantments().forEach((ench, i) -> {
                config.set(path + ".enchantments." + ench.getName(), ench);
                config.set(path + ".enchantments." + ench.getName() + ".lvl", i);
            });
        }

        if (item.getItemMeta() != null) {
            if (item.getItemMeta().getDisplayName() != null) {
                config.set(path + ".meta.displayname", item.getItemMeta().getDisplayName());
            }
            if (item.getItemMeta().getLore() != null) {
                config.set(path + ".meta.lore", item.getItemMeta().getLore());
            }
            if (!item.getItemMeta().getEnchants().isEmpty()) {
                item.getItemMeta().getEnchants().forEach((ench, i) -> {
                    config.set(path + ".meta.enchants." + ench.getName() + ".value", ench.getName());
                    config.set(path + ".meta.enchants." + ench.getName() + ".lvl", i);
                });
            }
            if (!item.getItemMeta().getItemFlags().isEmpty()) {
                for (ItemFlag flag : item.getItemMeta().getItemFlags()) {
                    config.set(path + ".meta.flags." + flag.name(), flag);
                }
            }
        }
        NBTItem nbt = new NBTItem(item);
        nbt.removeKey("button");
        for (String key : nbt.getKeys()) {
            config.set(path + ".nbt."+key+".key", key);
            config.set(path + ".nbt."+key+".value", nbt.getString(key).replace("\"", ""));
        }
        if (item.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            meta.getStoredEnchants().forEach(((ench, lvl) -> {
                config.set(path + ".enchantmentmeta." + ench.getName() + ".value", ench.getName());
                config.set(path + ".enchantmentmeta." + ench.getName() + ".lvl", lvl);
            }));
        }
    }

    public static ItemStack serialize(YamlConfiguration config, String path) {
        ItemStack item = new ItemStack(Material.valueOf(config.getString(path + ".type")), config.getInt(path + ".amount"), Short.parseShort(String.valueOf(config.getInt(path + ".durability"))));
        if (config.contains(path + ".enchantments")) {
            for (String key : config.getConfigurationSection(path + ".enchantments").getKeys(false)) {
                Logger.debug(key);
                item.addUnsafeEnchantment(Enchantment.getByName(key), config.getInt(path + ".enchantments." + key + ".lvl"));
            }
        }
        if (config.contains(path + ".meta")) {
            ItemMeta meta = item.getItemMeta();
            if (config.contains(path + ".meta.displayname")) {
                meta.setDisplayName(config.getString(path + ".meta.displayname"));
            }
            if (config.contains(path + ".meta.lore")) {
                meta.setLore(config.getStringList(path + ".meta.lore"));
            }
            if (config.contains(path + ".meta.enchants")) {
                for (String key : config.getConfigurationSection(path + ".meta.enchants").getKeys(false)) {
                    meta.addEnchant(Enchantment.getByName(key), config.getInt(path + ".meta.enchants." + key + ".lvl"), true);
                }
            }
            if (config.contains(path + ".meta.flags")) {
                for (String key : config.getConfigurationSection(path + ".meta.flags").getKeys(false)) {
                    meta.addItemFlags((ItemFlag) config.get(path + ".meta.flags." + key));
                }
            }
            item.setItemMeta(meta);
        }
        if (config.contains(path + ".nbt")) {
            NBTItem nbt = new NBTItem(item);
            for (String key : config.getConfigurationSection(path + ".nbt").getKeys(false)) {
                if(!((key.equals("Enchantments") || key.equals("ench")) && config.getString(path + ".nbt." + key + ".value").equals(""))) {
                    nbt.setString(config.getString(path + ".nbt." + key + ".key"), config.getString(path + ".nbt." + key + ".value"));
                }
            }
            item = nbt.getItem();
        }
        if (config.contains(path + ".enchantmentmeta")) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            for (String key : config.getConfigurationSection(path + ".enchantmentmeta").getKeys(false)) {
                if (!item.getEnchantments().containsKey(Enchantment.getByName(key))) {
                    meta.addStoredEnchant(Enchantment.getByName(key), config.getInt(path + ".enchantmentmeta." + key + ".lvl"), true);
                }
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack copyItem(ItemStack item) {
        ItemStack res = new ItemStack(item);

        ItemMeta meta = res.getItemMeta();
        meta.setDisplayName(item.getItemMeta().getDisplayName());
        meta.setLore(item.getItemMeta().getLore());
        meta.setAttributeModifiers(item.getItemMeta().getAttributeModifiers());
        meta.setUnbreakable(item.getItemMeta().isUnbreakable());
        if(item.getItemMeta().hasCustomModelData()) {
            meta.setCustomModelData(item.getItemMeta().getCustomModelData());
        }
        meta.setLocalizedName(item.getItemMeta().getLocalizedName());
        res.setItemMeta(meta);

        NBTItem nbt = new NBTItem(item);
        NBTItem resNBT = new NBTItem(res);
        for (String key : nbt.getKeys()) {
            resNBT.setObject(key, nbt.getObject(key, Object.class));
        }
        res = resNBT.getItem();

        Logger.debug(item.getItemMeta().getDisplayName());
        return res;
    }
}
