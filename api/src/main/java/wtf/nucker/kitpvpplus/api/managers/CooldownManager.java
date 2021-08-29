package wtf.nucker.kitpvpplus.api.managers;

import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.api.objects.Ability;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 21/07/2021
 */
public interface CooldownManager {

    /**
     * Adds a ability cooldown to a player
     * @param player The player you're adding the cooldown to
     * @param ability The ability you're putting them on cooldown for
     * @param time The amount of time they should be on cooldown for
     */
    static void addAbilityCooldown(Player player, Ability ability, int time) {};
}
