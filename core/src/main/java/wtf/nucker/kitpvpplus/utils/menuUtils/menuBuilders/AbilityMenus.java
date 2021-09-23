package wtf.nucker.kitpvpplus.utils.menuUtils.menuBuilders;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import wtf.nucker.kitpvpplus.managers.AbilityManager;
import wtf.nucker.kitpvpplus.utils.ChatUtils;
import wtf.nucker.kitpvpplus.utils.menuUtils.PaginatedMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class AbilityMenus {

    public static PaginatedMenu buildAbilitiesMenu(Player player) {
        List<ItemStack> items = new ArrayList<>();
        AbilityManager.getAbilities().forEach((id, ability) -> items.add(ability.getItem()));

        if(items.size() <= 0) {
            if(items.size() <= 0) {
                ItemStack item = XMaterial.RED_STAINED_GLASS.parseItem();
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatUtils.translate("&cThis server doesent have any abilities"));
                meta.setLore(ChatUtils.translate(Arrays.asList("&7Use the &7&oabilities.yml &r&7file", "&7to add some")));
                item.setItemMeta(meta);
                for (int i = 0; i < 9; i++) {
                    items.add(item);
                }
            }
        }

        PaginatedMenu menu = MenuUtils.buildPaginatedGUI("&0Abilities", items, event -> event.setCancelled(false));

        menu.open(player);
        return menu;
    }
}
