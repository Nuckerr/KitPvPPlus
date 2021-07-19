package wtf.nucker.kitpvpplus.objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import wtf.nucker.kitpvpplus.utils.ClockUtils;

import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public interface Kit {

    public String getId();

    public String getDisplayname();

    public Material getIcon();

    public List<String> getLore();

    public int getPrice();

    public String getPermission();

    public String setDisplayname(String newName);

    public Material setIcon(Material material);

    public List<String> addToLore(String item);

    public List<String> removeFromLore(String item);

    public List<String> setLore(List<String> newLore);

    public int setPrice(int newPrice);

    public String setPermission(String newPerm);

    public boolean isFree();

    public int getCooldown();

    public int setCooldown(int cooldown);

    public ClockUtils.CountingRunnable getCooldownRunnable();

    public ClockUtils.CountingRunnable setCooldownRunnable(ClockUtils.CountingRunnable runnable);

    public void setInventory(Inventory inv);

    public void fillInventory(Player player);
}
