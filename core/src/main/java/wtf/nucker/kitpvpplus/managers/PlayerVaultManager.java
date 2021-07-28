package wtf.nucker.kitpvpplus.managers;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.utils.menuUtils.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 28/07/2021
 */
public class PlayerVaultManager {

    private final ConfigurationSection section;

    public PlayerVaultManager() {
        if(!KitPvPPlus.getInstance().getDataManager().getDataYaml().contains("playervaults")) {
            KitPvPPlus.getInstance().getDataManager().getDataYaml().createSection("playervaults");
            this.save();
        }
        this.section = KitPvPPlus.getInstance().getDataManager().getDataYaml().getConfigurationSection("playervaults");
    }

    public boolean openPV(int page, Player player) {
        if(!player.hasPermission("kitpvpplus.pv." + page)) return false;

        Menu menu = new Menu(4, "&0PV - " + page, false);
        if(section.contains(player.getUniqueId() + "." + page)) {
            ItemStack[] items = (ItemStack[]) section.get(player.getUniqueId() + "." + page + ".items");
            menu.setContents(items);
        }

        menu.onClose(e -> {
            List<ItemStack> contents = new ArrayList<>();

            for (int i = 0; i < menu.getInventory().getContents().length; i++) {
                if(menu.getInventory().getItem(i) == null) {
                    contents.add(XMaterial.AIR.parseItem());
                }else {
                    contents.add(menu.getInventory().getItem(i));
                }
            }

            section.set(player.getUniqueId() + "." + page + ".items", contents.toArray(new ItemStack[0]));

            this.save();
        });

        menu.open(player);

        return true;
    }


    private void save() {
        KitPvPPlus.getInstance().getDataManager().getDataConfig().save();
    }
}
