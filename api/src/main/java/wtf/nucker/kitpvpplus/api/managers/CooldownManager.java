package wtf.nucker.kitpvpplus.api.managers;

import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.api.objects.Ability;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 21/07/2021
 */
public interface CooldownManager {

    static void addAbilityCooldown(Player player, Ability ability, int time) {};
}
