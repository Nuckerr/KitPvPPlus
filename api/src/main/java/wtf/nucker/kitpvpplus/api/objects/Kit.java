package wtf.nucker.kitpvpplus.api.objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.api.events.KitLoadEvent;
import wtf.nucker.kitpvpplus.api.exceptions.InitException;
import wtf.nucker.kitpvpplus.managers.CooldownManager;

import java.util.List;

/**
 * @author Nucker
 * Used for getting information about a kit
 */
public class Kit {

    private final wtf.nucker.kitpvpplus.objects.Kit kit;

    private Kit(wtf.nucker.kitpvpplus.objects.Kit kit) {
        this.kit = kit;
    }

    /**
     * @deprecated <b>DO NOT USE THIS</b>
     * @see Kit#fromInstanceKit(wtf.nucker.kitpvpplus.objects.Kit)
     */
    public Kit() {
        throw new InitException("You cannot create kits through a constructor. See Kit#fromInstanceKit");
    }

    /**
     * @return the id of the kit
     */
    public String getId() {
        return kit.getId();
    }

    /**
     * If it is free it will return 0
     * @see Kit#isFree() Kit#isFree() which
     * returns weather the kit is free as a boolean object
     * @return the price of the kit
     */
    public int getPrice() {
        return kit.getPrice();
    }

    /**
     * @see Kit#getPrice() Kit#getPrice() to get the price
     * @return weather the price of the kit is 0 as a boolean
     */
    public boolean isFree() {
        return kit.getPrice() == 0;
    }

    /**
     * If there is no cooldown, it will return 0
     * @return the kits cooldown
     */
    public int getCooldown() {
        return kit.getCooldown();
    }

    /**
     * @return the kits icon
     */
    public Material getIcon() {
        return kit.getIcon();
    }

    /**
     * @return the kits description (lore)
     */
    public List<String> getDescription() {
        return kit.getLore();
    }

    /**
     * @return the kits displayname
     */
    public String getDisplayname() {
        return kit.getDisplayname();
    }

    /**
     * If there is no permission, it will return null
     * @return the permission of the kit
     */
    public String getPermission() {
        if(kit.getPermission().equals("")) return null;
        return kit.getPermission();
    }

    /**
     * Used for loading the kit into a player's inventory
     * @see Kit#loadKit(Player) Kit#loadKit(Player) to load a kit for a singular player
     * @param receiver the person receiving the items in the kit
     * @param loader the person giving the kit
     */
    public void loadKit(Player receiver, Player loader) {
        kit.fillInventory(receiver);
        if (kit.getCooldown() > 0) CooldownManager.addKitCooldown(loader, kit, kit.getCooldown());
        Bukkit.getPluginManager().callEvent(new KitLoadEvent(this, receiver, loader));
    }

    /**
     * Used for loading a kit into a player's inventory
     * This only uses one player in the params.
     * @see Kit#loadKit(Player, Player) Kit#loadKit(Player, Player) is used in this method with the <i>Player</i> param being used
     * as the receiver
     * @param player the player who is getting the kit
     */
    public void loadKit(Player player) {
        this.loadKit(player, player);
    }

    /**
     * Used for getting api kit instance from plugin instance kit
     * @param kit plugin instance kit
     * @return api instance kit
     */
    public static Kit fromInstanceKit(wtf.nucker.kitpvpplus.objects.Kit kit) {
        return new Kit(kit);
    }

    /**
     * Used for getting a plugin instance of a kit from an api instance
     * @param kit the api instance of the kit
     * @return The plugin instance of the kit
     */
    public static wtf.nucker.kitpvpplus.objects.Kit toInstanceKit(Kit kit) {
        return KitPvPPlus.getInstance().getKitManager().getKit(kit.getId());
    }
}
