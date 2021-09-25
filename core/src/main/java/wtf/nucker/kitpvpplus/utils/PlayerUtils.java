package wtf.nucker.kitpvpplus.utils;

import org.bukkit.OfflinePlayer;

/**
 * @author Nucker
 * @project KitPvPPlus
 * @date 25/09/2021
 */
public class PlayerUtils {

    public static boolean checkOfflinePermission(OfflinePlayer player, String permission) {
        if(player.isOnline()) return player.getPlayer().hasPermission(permission);
        return false;
    }
}
