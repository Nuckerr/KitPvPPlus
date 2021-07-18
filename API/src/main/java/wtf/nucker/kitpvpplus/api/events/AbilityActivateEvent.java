package wtf.nucker.kitpvpplus.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import wtf.nucker.kitpvpplus.api.objects.Ability;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 14/07/2021
 */
public class AbilityActivateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Ability ability;
    private final ItemStack item;
    private final PlayerInteractEvent interactEvent;
    private final Player player;

    public AbilityActivateEvent(Ability ability, ItemStack item, PlayerInteractEvent interactEvent) {
        this.ability = ability;
        this.item = item;
        this.interactEvent = interactEvent;
        this.player = this.interactEvent.getPlayer();
    }

    /**
     * @return instance of the item the used for the ability
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * @return instance of the ability
     */
    public Ability getAbility() {
        return ability;
    }

    /**
     * @return the interact event that initially called the event
     */
    public PlayerInteractEvent getInteractEvent() {
        return interactEvent;
    }

    /**
     * @return the player who activated the event
     */
    public Player getPlayer() {
        return player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
