package wtf.nucker.kitpvpplus.objects;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public abstract class Ability {

    private final String id;
    private ItemStack item;

    public Ability(String id, ItemStack item) {
        this.id = id;
        NBTItem nbt = new NBTItem(item);
        nbt.setString("ability", id);
        this.item = nbt.getItem();
    }

    public abstract void onActivate(Ability ability, ItemStack item, PlayerInteractEvent listener);

    public ItemStack getItem() {
        NBTItem nbt = new NBTItem(item);
        nbt.setString("ability", this.id);
        return nbt.getItem(); // Dont question it. It wont work otherwise
    }

    public void setItem(ItemStack newItem) {
        this.item = newItem;
    }

    public String getId() {
        return id;
    }
}
