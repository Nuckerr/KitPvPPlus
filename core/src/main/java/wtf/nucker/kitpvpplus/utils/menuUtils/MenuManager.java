package wtf.nucker.kitpvpplus.utils.menuUtils;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class MenuManager {

    private final Map<UUID, Button> buttons;
    private final Map<Player, Menu> openMenus;

    public MenuManager(Plugin plugin) {
        this.buttons = new HashMap<>();
        this.openMenus = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this.getListener(), plugin);
    }

    public Listener getListener() {
        return new Listener() {

            @EventHandler
            public void onClick(InventoryClickEvent e) {
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(XMaterial.AIR.parseMaterial())) return;
                openMenus.forEach(((player, menu) -> {
                    if (e.getWhoClicked() == player && e.getInventory().equals(menu.getInventory())) {
                        menu.listeners.forEach(event -> event.accept(e));
                        e.setCancelled(menu.canceledClicks);
                    }
                }));
                if (Button.isButton(e.getCurrentItem())) {
                    e.setCancelled(true);
                    Button.getButtonById(UUID.fromString(new NBTItem(e.getCurrentItem()).getString( "button").replace("\"", ""))).onClick(e);
                }
            }

            @EventHandler
            public void onClose(InventoryCloseEvent e) {
                openMenus.remove(e.getPlayer());
            }
        };
    }

    public void registerButton(Button button) {
        this.buttons.put(button.getId(), button);
    }

    public Map<UUID, Button> getButtons() {
        return buttons;
    }

    public Map<Player, Menu> getOpenMenus() {
        return openMenus;
    }
}
