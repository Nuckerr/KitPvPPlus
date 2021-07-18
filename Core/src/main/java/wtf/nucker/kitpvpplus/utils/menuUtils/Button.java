package wtf.nucker.kitpvpplus.utils.menuUtils;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import wtf.nucker.kitpvpplus.KitPvPPlus;

import java.util.UUID;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public abstract class Button {

    private final ItemStack item;
    private final UUID id;

    public Button(ItemStack item) {
        this.id = UUID.randomUUID();
        NBTItem nbt = new NBTItem(item);
        nbt.setString("button", id.toString());
        item = nbt.getItem();
        this.item = item;

        KitPvPPlus.getInstance().getMenuManager().registerButton(this);
    }

    public abstract void onClick(InventoryClickEvent event);

    public ItemStack getItem() {
        return item;
    }

    public UUID getId() {
        return id;
    }

    public static boolean isButton(ItemStack item) {
        if (item == null || item.getType().equals(XMaterial.AIR.parseMaterial())) return false;
        return new NBTItem(item).hasKey("button");
    }

    public static Button getButtonById(UUID id) {
        return KitPvPPlus.getInstance().getMenuManager().getButtons().get(id);
    }
}
