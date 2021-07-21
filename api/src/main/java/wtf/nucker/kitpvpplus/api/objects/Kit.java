package wtf.nucker.kitpvpplus.api.objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;

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
    public String getId();

    /**
     * If it is free it will return 0
     * @see Kit#isFree() Kit#isFree() which
     * returns weather the kit is free as a boolean object
     * @return the price of the kit
     */
    public int getPrice();

    /**
     * @see Kit#getPrice() Kit#getPrice() to get the price
     * @return weather the price of the kit is 0 as a boolean
     */
    public boolean isFree();

    /**
     * If there is no cooldown, it will return 0
     * @return the kits cooldown
     */
    public int getCooldown();

    /**
     * @return the kits icon
     */
    public Material getIcon();

    /**
     * @return the kits description (lore)
     */
    public List<String> getLore();

    /**
     * @return the kits displayname
     */
    public String getDisplayname();

    /**
     * If there is no permission, it will return null
     * @return the permission of the kit
     */
    public String getPermission();

    /**
     * Used for loading the kit into a player's inventory
     * @see Kit#loadKit(Player) Kit#loadKit(Player) to load a kit for a singular player
     * @param receiver the person receiving the items in the kit
     * @param loader the person giving the kit
     */
    public void loadKit(Player receiver, Player loader);

    /**
     * Used for loading a kit into a player's inventory
     * This only uses one player in the params.
     * @see Kit#loadKit(Player, Player) Kit#loadKit(Player, Player) is used in this method with the <i>Player</i> param being used
     * as the receiver
     * @param player the player who is getting the kit
     */
    public void loadKit(Player player);
}
