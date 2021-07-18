package wtf.nucker.kitpvpplus.listeners.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import wtf.nucker.kitpvpplus.objects.Ability;

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

    public AbilityActivateEvent(Ability ability, ItemStack item, PlayerInteractEvent interactEvent, Player player) {
        this.ability = ability;
        this.item = item;
        this.interactEvent = interactEvent;
        this.player = player;
    }

    public ItemStack getItem() {
        return item;
    }

    public Ability getAbility() {
        return ability;
    }

    public PlayerInteractEvent getInteractEvent() {
        return interactEvent;
    }

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
