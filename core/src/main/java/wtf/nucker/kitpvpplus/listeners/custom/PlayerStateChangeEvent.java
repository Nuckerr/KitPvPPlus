package wtf.nucker.kitpvpplus.listeners.custom;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import wtf.nucker.kitpvpplus.api.events.StateChangeEvent;
import wtf.nucker.kitpvpplus.dataHandlers.PlayerState;
import wtf.nucker.kitpvpplus.managers.DataManager;
import wtf.nucker.kitpvpplus.utils.APIConversion;

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

    public PlayerStateChangeEvent(final Player player, final PlayerState oldState, final PlayerState newState) {
        this.player = player;
        this.oldState = oldState;
        this.newState = newState;
        this.cancelled = false;
//        for (int i = 0; i < ; i++) {
//
//        }

        if(oldState != null) {
            Bukkit.getServer().getPluginManager().callEvent(new StateChangeEvent(player, APIConversion.fromInstanceState(oldState), APIConversion.fromInstanceState(newState)) {
                @Override
                public void setNewState(wtf.nucker.kitpvpplus.api.objects.PlayerState state) {
                    PlayerStateChangeEvent.this.setNewState(APIConversion.toInstanceState(state));
                }
            });
        }
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
