package wtf.nucker.kitpvpplus.api.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import wtf.nucker.kitpvpplus.api.objects.PlayerData;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 28/08/2021
 */
public class PlayerDataCreationEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final OfflinePlayer player;
    private final PlayerData playerData;

    public PlayerDataCreationEvent(PlayerData playerData) {
        this.player = playerData.getPlayer();
        this.playerData = playerData;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
