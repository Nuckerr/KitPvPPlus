package wtf.nucker.kitpvpplus.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import wtf.nucker.kitpvpplus.api.objects.Kit;

/**
 * @author Nucker
 * Called when a player loads a kit
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

    /**
     * @return the player who received the kit
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the kit that was loaded
     */
    public Kit getKit() {
        return kit;
    }

    /**
     * <b>If the kit was loaded by the receiver the loader is the receiver</b>
     * @return the person who loaded the kit
     */
    public Player getLoader() {
        return loader;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
