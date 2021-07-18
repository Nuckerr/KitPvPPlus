package wtf.nucker.kitpvpplus.utils.menuUtils;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.utils.ChatUtils;
import wtf.nucker.kitpvpplus.utils.ItemUtils;
import wtf.nucker.kitpvpplus.utils.Logger;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class Menu {

    private final int rows;
    private final String name;
    private final Inventory inventory;

    protected boolean canceledClicks;

    protected ArrayList<Consumer<InventoryClickEvent>> listeners;

    public Menu(int rows, String name) {
        this(rows, name, true);
    }

    public Menu(int rows, String name, boolean canceledClicks) {
        this.listeners = new ArrayList<>();
        this.rows = rows;
        this.name = name;
        this.inventory = Bukkit.createInventory(null, rows * 9, ChatUtils.translate(name));
        this.canceledClicks = canceledClicks;
    }

    public void open(Player p) {
        p.openInventory(this.inventory);
        KitPvPPlus.getInstance().getMenuManager().getOpenMenus().put(p, this);
    }

    public void fillMenu() {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType().equals(XMaterial.AIR.parseMaterial())) {
                inventory.setItem(i, ItemUtils.buildItem("", Material.valueOf(KitPvPPlus.getInstance().getConfig().getString("filler-item")), 1));
            }
        }
    }

    public void fillMenu(Material material) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType().equals(XMaterial.AIR.parseMaterial())) {
                inventory.setItem(i, ItemUtils.buildItem("", material, 1));
            }
        }
    }

    public void addListener(Consumer<InventoryClickEvent> event) {
        this.listeners.add(event);
    }

    public void setItem(int index, ItemStack item) {
        this.inventory.setItem(index, item);
    }

    public void addItem(ItemStack item) {
        this.inventory.addItem(item);
    }

    public void setButton(Button button, int index) {
        this.inventory.setItem(index, button.getItem());
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getRows() {
        return rows;
    }

    public String getName() {
        return name;
    }

    public void setClicksCanceled(boolean canceled) {
        this.canceledClicks = canceled;
    }
}
