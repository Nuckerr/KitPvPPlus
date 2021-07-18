package wtf.nucker.kitpvpplus.listeners.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import wtf.nucker.kitpvpplus.managers.DataManager;
import wtf.nucker.kitpvpplus.player.PlayerState;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class PlayerStateChangeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean cancelled;

    private final Player player;
    private final PlayerState oldState;
    private final PlayerState newState;

    public PlayerStateChangeEvent(Player player, PlayerState oldState, PlayerState newState) {
        this.player = player;
        this.oldState = oldState;
        this.newState = newState;
        this.cancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

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

    public Player getPlayer() {
        return player;
    }

    public PlayerState getNewState() {
        return newState;
    }

    public PlayerState getOldState() {
        return oldState;
    }

    public void setNewState(PlayerState state) {
        DataManager.getPlayerStates().remove(this.player);
        DataManager.getPlayerStates().put(this.player, state);
    }
}
