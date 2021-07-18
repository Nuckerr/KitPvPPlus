package wtf.nucker.kitpvpplus.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import wtf.nucker.kitpvpplus.api.objects.PlayerState;
import wtf.nucker.kitpvpplus.managers.DataManager;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 14/07/2021
 */
public class StateChangeEvent extends Event implements Cancellable {


    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean cancelled;

    private final Player player;
    private final PlayerState oldState;
    private final PlayerState newState;

    public StateChangeEvent(Player player, PlayerState oldState, PlayerState newState) {
        this.player = player;
        this.oldState = oldState;
        this.newState = newState;
        this.cancelled = false;
    }

    /**
     * See if the event is canceled
     * @see StateChangeEvent#setCancelled(boolean) StateChangeEvent#setCancelled(boolean) to cancel/uncancel the event
     * @return if the event is canceled
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Cancel/uncancel the event
     * <b>This sets the player's state back to the old state. It will scuff up locations</b>
     * @param cancel weather it should be cancelled or not
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    /**
     * @return the player whose state is being changed
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the new state of the player
     */
    public PlayerState getNewState() {
        return newState;
    }

    /**
     * @return the old state of the player
     */
    public PlayerState getOldState() {
        return oldState;
    }

    /**
     * Set a new state
     * @param state the new state
     */
    public void setNewState(PlayerState state) {
        DataManager.getPlayerStates().remove(this.player);
        DataManager.getPlayerStates().put(this.player, PlayerState.toInstanceState(state));
    }
}
