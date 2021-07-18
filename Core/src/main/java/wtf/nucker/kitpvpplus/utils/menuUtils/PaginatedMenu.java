package wtf.nucker.kitpvpplus.utils.menuUtils;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.utils.ItemUtils;
import wtf.nucker.kitpvpplus.utils.Language;

import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public abstract class PaginatedMenu extends Menu {

    private final int spaces;
    private List<ItemStack> items;
    private final int totalPages;
    private int page = 0;

    public PaginatedMenu(int rows, String title, List<ItemStack> items, int spaces) {
        super(rows, title, true);
        this.spaces = spaces;
        this.items = items;
        this.totalPages = (int) Math.ceil((double) items.size() / spaces);
    }

    public void openPage(Player player, int page) {
        if(page <= 0) {
            player.sendMessage(Language.NO_LAST_PAGE.get(player));
            return;
        }
        if(page > this.totalPages) {
            player.sendMessage(Language.NO_NEXT_PAGE.get(player));
            return;
        }
        this.page = page;
        for (int i = 0; i < this.getInventory().getSize(); i++) {
            this.getInventory().setItem(i, null);
        }
        int upperIndex = page * this.spaces;
        int lowerIndex = upperIndex - spaces;
        this.addFiller(new Button(ItemUtils.buildItem("&9Previous page", XMaterial.PAPER.parseMaterial(),
                1, "&7Click to go to the last page")) {
            @Override
            public void onClick(InventoryClickEvent event) {
                PaginatedMenu.this.openPage(player, PaginatedMenu.this.page - 1);
            }
        }, new Button(ItemUtils.buildItem("&9Next page", XMaterial.PAPER.parseMaterial(),
                1, "&7Click to go to the next page")) {
            @Override
            public void onClick(InventoryClickEvent event) {
                PaginatedMenu.this.openPage(player, PaginatedMenu.this.page +1);
            }
        }, new Button(ItemUtils.buildItem("&cClose menu", XMaterial.BARRIER.parseMaterial(),
                1, "&7Click to close the menu")) {
            @Override
            public void onClick(InventoryClickEvent event) {
                event.getWhoClicked().closeInventory();
            }
        }, ItemUtils.buildItem("", Material.valueOf(KitPvPPlus.getInstance().getConfig().getString("filler-item")), 1));

        if(items.size() > spaces) {
            for (int i = lowerIndex; i < upperIndex; i++) {
                if(i > (items.size()-1)) return;
                this.setButton(new Button(items.get(i)) {
                    @Override
                    public void onClick(InventoryClickEvent event) {
                        PaginatedMenu.this.onClick(event);
                    }
                }, this.getInventory().firstEmpty());
            }
        }else {
            for (ItemStack item : items) {
                this.setButton(new Button(item) {
                    @Override
                    public void onClick(InventoryClickEvent event) {
                        PaginatedMenu.this.onClick(event);
                    }
                }, this.getInventory().firstEmpty());
            }
        }

        player.openInventory(this.getInventory());
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    @Override
    public void open(Player p) {
        this.openPage(p, 1);
    }

    public abstract void onClick(InventoryClickEvent e);
    public abstract void addFiller(Button previousPageButton, Button nextPageButton, Button closeInvButton, ItemStack filler);


    public List<ItemStack> getItems() {
        return items;
    }

    public int getSpaces() {
        return spaces;
    }
}
