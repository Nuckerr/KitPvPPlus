package wtf.nucker.kitpvpplus.api.objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 21/07/2021
 */
public interface Kit {

    /**
     * @return the id of the kit
     */
    String getId();

    /**
     * If it is free it will return 0
     * @see Kit#isFree() Kit#isFree() which
     * returns weather the kit is free as a boolean object
     * @return the price of the kit
     */
    int getPrice();

    /**
     * @see Kit#getPrice() Kit#getPrice() to get the price
     * @return weather the price of the kit is 0 as a boolean
     */
    default boolean isFree() {
        return this.getPrice() == 0;
    }

    /**
     * If there is no cooldown, it will return 0
     * @return the kits cooldown
     */
    int getCooldown();

    /**
     * @return the kits icon
     */
    Material getIcon();

    /**
     * @return the kits description (lore)
     */
    List<String> getLore();

    /**
     * @return the kits displayname
     */
    String getDisplayname();

    /**
     * If there is no permission, it will return null
     * @return the permission of the kit
     */
    String getPermission();

    /**
     * Used for loading the kit into a player's inventory
     * @see Kit#loadKit(Player) Kit#loadKit(Player) to load a kit for a singular player
     * @param receiver the person receiving the items in the kit
     * @param loader the person giving the kit
     */
    void loadKit(Player receiver, Player loader);

    /**
     * Used for loading a kit into a player's inventory
     * This only uses one player in the params.
     * @see Kit#loadKit(Player, Player) Kit#loadKit(Player, Player) is used in this method with the <i>Player</i> param being used
     * as the receiver
     * @param player the player who is getting the kit (The player doesn't have to have permission)
     */
    void loadKit(Player player);


    /**
     * Sets the displayname
     * @param name the new displayname
     */
    void setDisplayname(String name);

    /**
     * Sets the lore
     * @param lore the new lore
     */
    void setLore(List<String> lore);

    /**
     * Sets the permission
     * @param permission the new permission
     */
    void setPermission(String permission);

    /**
     * Sets the cooldown
     * @param cooldown the new cooldown
     */
    void setCooldown(int cooldown);

    /**
     * Sets the price
     * @param price the new price
     */
    void setPrice(int price);

    /**
     * Sets the material
     * @param material the material
     */
    void setIcon(Material material);

    /**
     * Sets the contents of the kit
     * @param inventory the inventory with the new items
     * @see Kit#setContents(PlayerInventory) to set a kit with armor contents or from a player inventory
     */
    void setContents(Inventory inventory);

    /**
     * Sets the contents of the kit (with armor)
     * @param inventory the inventory with the new items
     * @see Kit#setContents(Inventory) to set a kit without armor contents/not a player inventory
     */
    void setContents(PlayerInventory inventory);
}
