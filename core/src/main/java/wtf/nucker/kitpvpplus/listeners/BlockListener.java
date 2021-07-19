package wtf.nucker.kitpvpplus.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import wtf.nucker.kitpvpplus.managers.AbilityManager;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class BlockListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (e.getItemInHand() == null) return;
        if (AbilityManager.isAbilityItem(e.getItemInHand())) e.setCancelled(true);
    }

}
