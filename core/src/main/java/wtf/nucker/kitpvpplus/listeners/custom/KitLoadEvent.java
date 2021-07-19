package wtf.nucker.kitpvpplus.listeners.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import wtf.nucker.kitpvpplus.objects.Kit;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 14/07/2021
 */
public class KitLoadEvent extends Event {

    public static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Kit kit;
    private final Player loader;

    public KitLoadEvent(Kit kit, Player player, Player loader) {
        this.kit = kit;
        this.player = player;
        this.loader = loader;
    }

    public Player getPlayer() {
        return player;
    }

    public Kit getKit() {
        return kit;
    }

    public Player getLoader() {
        return loader;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
