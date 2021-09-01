package wtf.nucker.kitpvpplus.objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import wtf.nucker.kitpvpplus.utils.ClockUtils;

import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public interface Kit {

    String getId();

    String getDisplayname();

    Material getIcon();

    List<String> getLore();

    int getPrice();

    String getPermission();

    String setDisplayname(String newName);

    Material setIcon(Material material);

    List<String> addToLore(String item);

    List<String> removeFromLore(String item);

    List<String> setLore(List<String> newLore);

    int setPrice(int newPrice);

    String setPermission(String newPerm);

    boolean isFree();

    int getCooldown();

    int setCooldown(int cooldown);

    ClockUtils.CountingRunnable getCooldownRunnable();

    ClockUtils.CountingRunnable setCooldownRunnable(ClockUtils.CountingRunnable runnable);

    void setInventory(Inventory inv);

    void setInventory(PlayerInventory inv);

    void fillInventory(Player player);

}
