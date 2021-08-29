package wtf.nucker.kitpvpplus.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.utils.Logger;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class InventoryListeners implements Listener {

    @EventHandler
    public void onClickEvent(final InventoryClickEvent e) {
        if (KitPvPPlus.DEBUG) {
            Logger.debug(String.valueOf(e.getSlot()));
        }
    }
}
