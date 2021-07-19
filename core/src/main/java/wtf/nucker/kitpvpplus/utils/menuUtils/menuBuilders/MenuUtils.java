package wtf.nucker.kitpvpplus.utils.menuUtils.menuBuilders;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import wtf.nucker.kitpvpplus.utils.menuUtils.Button;
import wtf.nucker.kitpvpplus.utils.menuUtils.PaginatedMenu;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class MenuUtils {

    protected static PaginatedMenu buildPaginatedGUI(String title, List<ItemStack> items, Consumer<InventoryClickEvent> onClick) {
        return new PaginatedMenu(2, title, items, 9) {

            @Override
            public void onClick(InventoryClickEvent e) {
                onClick.accept(e);
            }

            @Override
            public void addFiller(Button previousPageButton, Button nextPageButton, Button closeInvButton, ItemStack filler) {
                for (int i = 10; i < 17; i++) {
                    super.setItem(i, filler);
                }
                super.setButton(previousPageButton, 9);
                super.setButton(nextPageButton, 17);
                super.setButton(closeInvButton, 13);
            }
        };
    }
}
